package com.necroworld.systems

import com.necroworld.GameConfig
import com.necroworld.blocks.GameBlock
import com.necroworld.builders.GameTileRepository
import com.necroworld.commands.CastSpell
import com.necroworld.commands.SelectTarget
import com.necroworld.extensions.*
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.GameComponents
import org.hexworks.zircon.api.Layers
import org.hexworks.zircon.api.builder.component.ModalBuilder
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.extensions.onClosed
import org.hexworks.zircon.api.extensions.onKeyboardEvent
import org.hexworks.zircon.api.extensions.onMouseEvent
import org.hexworks.zircon.api.game.ProjectionMode
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.MouseEventType
import org.hexworks.zircon.api.uievent.Processed

object TargetPicker : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>) = command.
        responseWhenCommandIs(SelectTarget::class) { (context, source, spell) ->
            val (world, screen, uiEvent) = context

            val selectionLayer = Layers.newBuilder()
                .withSize(world.actualSize().to2DSize())
                .build()
            world.pushOverlayAt(selectionLayer, source.position.z)

            val rangeAOE = source.position.to2DPosition().buildAOEWithRadius(spell.baseRange)

            rangeAOE.forEach {
                selectionLayer.setTileAt(it, GameTileRepository.IN_RANGE)
            }

            val range = spell.baseRange
            val aoe = spell.areaOfEffect

            var mousePosition = Position.defaultPosition()

            val listOfTargetPositions = mutableListOf<Position3D>()

            val panel = Components.panel()
                .withSize(screen.size.minus(Size.create(1,1)))
                .build()

            val sidebar = Components.panel()
                .withSize(GameConfig.SIDEBAR_WIDTH, GameConfig.WINDOW_HEIGHT)
                .withTitle("Casting ${spell.name}")
                .wrapWithBox()
                .build()
            panel.addComponent(sidebar)

            val gameComponent = GameComponents.newGameComponentBuilder<Tile, GameBlock>()
                .withGameArea(world)
                .withVisibleSize(world.visibleSize())
                .withProjectionMode(ProjectionMode.TOP_DOWN)
                .withAlignmentWithin(screen, ComponentAlignment.TOP_RIGHT)
                .build()
            panel.addComponent(gameComponent)

            val modal = ModalBuilder.newBuilder<TargetPickerModalResult>()
                .withParentSize(screen.size)
                .withComponent(panel)
                .withAlignmentWithin(screen, ComponentAlignment.TOP_RIGHT)
                .build()

            gameComponent.onKeyboardEvent(KeyboardEventType.KEY_PRESSED) { event, _ ->
                when(event.code) {
                    KeyCode.ESCAPE -> {
                        modal.close(TargetPickerModalResult(false, listOf()))
                        Processed
                    }
                    KeyCode.ENTER -> {
                        modal.close(TargetPickerModalResult(true, listOfTargetPositions))
                        Processed
                    }
                    else -> Processed
                }
            }

            gameComponent.onMouseEvent(MouseEventType.MOUSE_MOVED) { event, _ ->
                val worldOffset = world.visibleOffset().to2DPosition()
                val componentOffset = gameComponent.absolutePosition

                mousePosition = event.position.plus(worldOffset).minus(componentOffset)
                val distance = mousePosition.getDistanceFrom(source.position.to2DPosition())
                selectionLayer.clear()
                rangeAOE.forEach {
                    selectionLayer.setTileAt(it, GameTileRepository.IN_RANGE)
                }

                if (distance <= range) {
                    aoe.forEach {
                        selectionLayer.setTileAt(mousePosition.plus(it), GameTileRepository.SELECTED_VALID)
                    }
                } else {
                    selectionLayer.setTileAt(mousePosition, GameTileRepository.SELECTED_INVALID)
                }

                Processed
            }

            gameComponent.onMouseEvent(MouseEventType.MOUSE_RELEASED) { event, _ ->
                when(event.button) {
                    1 -> {
                        aoe.forEach {
                            listOfTargetPositions.add(mousePosition.plus(it).toPosition3D(source.position.z))
                        }
                        modal.close(TargetPickerModalResult(true, listOfTargetPositions))
                        Processed
                    }
                    3 -> {
                        modal.close(TargetPickerModalResult(false, listOf()))
                        Processed
                    }
                }
                Processed
            }

            modal.applyColorTheme(GameConfig.THEME)
            modal.onClosed {
                world.removeOverlay(selectionLayer, source.position.z)
                if (it.hasSelectedTarget) {
                    source.executeCommand(
                        CastSpell(
                            context,
                            source,
                            it.targets,
                            spell
                        )
                    )
                }
            }

            screen.openModal(modal)

            Consumed
        }
}
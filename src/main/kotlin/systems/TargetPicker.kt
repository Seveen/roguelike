package com.necroworld.systems

import com.necroworld.GameConfig
import com.necroworld.attributes.types.Spellcaster
import com.necroworld.blocks.GameBlock
import com.necroworld.builders.GameTileRepository
import com.necroworld.commands.CastSpell
import com.necroworld.commands.SelectTarget
import com.necroworld.extensions.*
import com.necroworld.spells.Spell
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.Consumed
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
import org.hexworks.zircon.api.extensions.*
import org.hexworks.zircon.api.game.ProjectionMode
import org.hexworks.zircon.api.graphics.Layer
import org.hexworks.zircon.api.uievent.*

object TargetPicker : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>) = command.
        responseWhenCommandIs(SelectTarget::class) { (context, source, spell) ->
            val (world, screen, uiEvent) = context
            val listOfTargetPositions = mutableListOf<Position3D>()

            //TODO(In the future range and aoe are influenced by caster skills)
            val range = spell.baseRange
            val aoe = spell.areaOfEffect
            val rangeAOE = source.position.to2DPosition().buildAOEWithRadius(range)

            val selectionLayer = Layers.newBuilder()
                .withSize(world.actualSize().to2DSize())
                .build()
            world.pushOverlayAt(selectionLayer, source.position.z)

            rangeAOE.forEach {
                selectionLayer.setTileAt(it, GameTileRepository.IN_RANGE)
            }

            val targetSelectPanel = Components.panel()
                .withSize(screen.size.minus(Size.create(1,1)))
                .build()

            val sidebar = Components.panel()
                .withSize(GameConfig.SIDEBAR_WIDTH, GameConfig.WINDOW_HEIGHT)
                .withDecorations(box(title = "Casting ${spell.name}"))
                .build()
            targetSelectPanel.addComponent(sidebar)

            val gameComponent = GameComponents.newGameComponentBuilder<Tile, GameBlock>()
                .withGameArea(world)
                .withVisibleSize(world.visibleSize())
                .withProjectionMode(ProjectionMode.TOP_DOWN)
                .withAlignmentWithin(screen, ComponentAlignment.TOP_RIGHT)
                .build()
            targetSelectPanel.addComponent(gameComponent)

            val modal = ModalBuilder.newBuilder<TargetPickerModalResult>()
                .withParentSize(screen.size)
                .withComponent(targetSelectPanel)
                .withAlignmentWithin(screen, ComponentAlignment.TOP_RIGHT)
                .build()

            gameComponent.processMouseEvents(MouseEventType.MOUSE_MOVED) { event, _ ->
                val mousePosition = world.screenToWorldPosition(event.position, gameComponent.absolutePosition)
                drawAoE(mousePosition, selectionLayer, source, spell)

                Processed
            }

            gameComponent.processMouseEvents(MouseEventType.MOUSE_RELEASED) { event, _ ->
                val mousePosition = world.screenToWorldPosition(event.position, gameComponent.absolutePosition)
                when(event.button) {
                    1 -> {
                        aoe.forEach {
                            val actualPosition = mousePosition.plus(it).toPosition3D(source.position.z)
                            val outsideLimits = actualPosition.x < 0 || actualPosition.x >= world.actualSize().xLength || actualPosition.y < 0 || actualPosition.y >= world.actualSize().yLength
                            if (outsideLimits.not()) {
                                listOfTargetPositions.add(actualPosition)
                            }
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

            gameComponent.processKeyboardEvents(KeyboardEventType.KEY_PRESSED) { event, _ ->
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

    fun drawAoE(mousePosition: Position, selectionLayer: Layer, source: GameEntity<Spellcaster>, spell: Spell) {
        val rangeAOE = source.position.to2DPosition().buildAOEWithRadius(spell.baseRange)
        val distance = mousePosition.getDistanceFrom(source.position.to2DPosition())

        selectionLayer.clear()

        rangeAOE.forEach {
            selectionLayer.setTileAt(it, GameTileRepository.IN_RANGE)
        }

        if (distance <= spell.baseRange) {
            spell.areaOfEffect.forEach {
                selectionLayer.setTileAt(mousePosition.plus(it), GameTileRepository.SELECTED_VALID)
            }
        } else {
            selectionLayer.setTileAt(mousePosition, GameTileRepository.SELECTED_INVALID)
        }
    }
}
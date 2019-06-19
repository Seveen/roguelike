package com.necroworld.systems

import com.necroworld.attributes.types.ItemHolder
import com.necroworld.attributes.types.Spellcaster
import com.necroworld.commands.*
import com.necroworld.extensions.GameEntity
import com.necroworld.extensions.GameItemHolder
import com.necroworld.extensions.position
import com.necroworld.extensions.whenTypeIs
import com.necroworld.spells.Blink
import com.necroworld.spells.Fireball
import com.necroworld.spells.Spell
import com.necroworld.spells.Swap
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEvent

object InputReceiver : BaseBehavior<GameContext>() {

    private val logger = LoggerFactory.getLogger(this::class)

    override fun update(entity: GameEntity<out EntityType>, context: GameContext): Boolean {
        val (_, _, uiEvent) = context
        val currentPos = entity.position
        if (uiEvent is KeyboardEvent) {
            when (uiEvent.code) {
                KeyCode.KEY_W -> entity.moveTo(currentPos.withRelativeY(-1), context)
                KeyCode.KEY_A -> entity.moveTo(currentPos.withRelativeX(-1), context)
                KeyCode.KEY_S -> entity.moveTo(currentPos.withRelativeY(1), context)
                KeyCode.KEY_D -> entity.moveTo(currentPos.withRelativeX(1), context)

                KeyCode.KEY_Q -> entity.moveTo(currentPos.withRelativeX(-1).withRelativeY(-1), context)
                KeyCode.KEY_E -> entity.moveTo(currentPos.withRelativeX(1).withRelativeY(-1), context)
                KeyCode.KEY_Z -> entity.moveTo(currentPos.withRelativeX(-1).withRelativeY(1), context)
                KeyCode.KEY_C -> entity.moveTo(currentPos.withRelativeX(1).withRelativeY(1), context)

                KeyCode.KEY_R -> entity.moveUp(context)
                KeyCode.KEY_F -> entity.moveDown(context)
                KeyCode.KEY_P -> entity.whenTypeIs<ItemHolder> {
                    it.pickItemUp(currentPos, context)
                }
                KeyCode.KEY_I -> entity.whenTypeIs<ItemHolder> {
                    it.inspectInventory(currentPos, context)
                }
                KeyCode.KEY_G -> entity.whenTypeIs<Spellcaster> {
                    it.selectTargets(context, Fireball())
                }
                KeyCode.KEY_H -> entity.whenTypeIs<Spellcaster> {
                    it.selectTargets(context, Swap())
                }
                KeyCode.KEY_X -> entity.whenTypeIs<Spellcaster> {
                    it.selectTargets(context, Blink())
                }
                else -> {
                    logger.debug("UI Event ($uiEvent) does not have a corresponding command, it is ignored")
                }
            }
        }
        return true
    }

    private fun GameEntity<EntityType>.moveTo(position: Position3D, context: GameContext) {
        val oldPos = this.position.copy()

        executeCommand(MoveTo(context, this, position))
        executeCommand(MoveCamera(context, this, oldPos))
    }

    private fun GameEntity<EntityType>.moveDown(context: GameContext) {
        val oldPos = this.position.copy()
        executeCommand(MoveDown(context, this))
        executeCommand(MoveCamera(context, this, oldPos))
    }

    private fun GameEntity<EntityType>.moveUp(context: GameContext) {
        val oldPos = this.position.copy()
        executeCommand(MoveUp(context, this))
        executeCommand(MoveCamera(context, this, oldPos))
    }

    private fun GameItemHolder.pickItemUp(position: Position3D, context: GameContext) {
        executeCommand(PickItemUp(context, this, position))
    }

    private fun GameItemHolder.inspectInventory(position: Position3D, context: GameContext) {
        executeCommand(InspectInventory(context, this, position))
    }

    private fun GameEntity<Spellcaster>.selectTargets(context: GameContext, spell: Spell) {
        executeCommand(SelectTarget(context, this, spell ))
    }

}

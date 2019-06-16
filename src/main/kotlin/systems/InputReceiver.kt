package com.necroworld.systems

import com.necroworld.attributes.types.Player
import com.necroworld.commands.*
import com.necroworld.extensions.GameEntity
import com.necroworld.extensions.position
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
        val (_, _, uiEvent, player) = context
        val currentPos = player.position
        if (uiEvent is KeyboardEvent) {
            val newPosition = when (uiEvent.code) {
                KeyCode.KEY_W -> player.moveTo(currentPos.withRelativeY(-1), context)
                KeyCode.KEY_A -> player.moveTo(currentPos.withRelativeX(-1), context)
                KeyCode.KEY_S -> player.moveTo(currentPos.withRelativeY(1), context)
                KeyCode.KEY_D -> player.moveTo(currentPos.withRelativeX(1), context)

                KeyCode.KEY_Q -> player.moveTo(currentPos.withRelativeX(-1).withRelativeY(-1), context)
                KeyCode.KEY_E -> player.moveTo(currentPos.withRelativeX(1).withRelativeY(-1), context)
                KeyCode.KEY_Z -> player.moveTo(currentPos.withRelativeX(-1).withRelativeY(1), context)
                KeyCode.KEY_C -> player.moveTo(currentPos.withRelativeX(1).withRelativeY(1), context)

                KeyCode.KEY_R -> player.moveUp(context)
                KeyCode.KEY_F -> player.moveDown(context)
                KeyCode.KEY_P -> player.pickItemUp(currentPos, context)
                KeyCode.KEY_I -> player.inspectInventory(currentPos, context)
                else -> {
                    logger.debug("UI Event ($uiEvent) does not have a corresponding command, it is ignored")
                }
            }
        }
        return true
    }

    private fun GameEntity<Player>.moveTo(position: Position3D, context: GameContext) {
        val oldPos = this.position.copy()

        executeCommand(MoveTo(context, this, position))
        executeCommand(MoveCamera(context, this, oldPos))
    }

    private fun GameEntity<Player>.moveDown(context: GameContext) {
        val oldPos = this.position.copy()
        executeCommand(MoveDown(context, this))
        executeCommand(MoveCamera(context, this, oldPos))
    }

    private fun GameEntity<Player>.moveUp(context: GameContext) {
        val oldPos = this.position.copy()
        executeCommand(MoveUp(context, this))
        executeCommand(MoveCamera(context, this, oldPos))
    }

    private fun GameEntity<Player>.pickItemUp(position: Position3D, context: GameContext) {
        executeCommand(PickItemUp(context, this, position))
    }

    private fun GameEntity<Player>.inspectInventory(position: Position3D, context: GameContext) {
        executeCommand(InspectInventory(context, this, position))
    }

}

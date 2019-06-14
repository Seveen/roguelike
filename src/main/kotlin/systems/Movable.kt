package com.necroworld.systems

import com.necroworld.attributes.types.Player
import com.necroworld.commands.MoveCamera
import com.necroworld.commands.MoveTo
import com.necroworld.commands.UpdateDecisionMap
import com.necroworld.extensions.GameCommand
import com.necroworld.extensions.isDecisionMapTarget
import com.necroworld.extensions.position
import com.necroworld.extensions.tryActionsOn
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.*
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.extensions.map

object Movable : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>) =
        command.responseWhenCommandIs(MoveTo::class) {(context, entity, position) ->
            val world = context.world
            val previousPosition = entity.position
            var result: Response = Pass
            world.fetchBlockAt(position).map { block ->
                if (block.isOccupied) {
                    result = entity.tryActionsOn(context, block.occupier.get())
                } else {
                    if (world.moveEntity(entity, position)) {
                        result = if (entity.isDecisionMapTarget) {
                            CommandResponse(UpdateDecisionMap(
                                context = context,
                                source = entity,
                                oldPosition = previousPosition,
                                newPosition = position))
                        } else Consumed
                    }
                }
            }
            result
        }
}
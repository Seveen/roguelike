package com.necroworld.systems

import com.necroworld.commands.Destroy
import com.necroworld.commands.UpdateDecisionMap
import com.necroworld.extensions.GameCommand
import com.necroworld.extensions.isDecisionMapTarget
import com.necroworld.extensions.position
import com.necroworld.functions.logGameEvent
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.CommandResponse
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object Destructible : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>) =
        command.responseWhenCommandIs(Destroy::class) { (context, attacker, target, cause) ->
            var result: Response = if (target.isDecisionMapTarget) {
                CommandResponse(
                    UpdateDecisionMap(
                    context = context,
                    source = target,
                    oldPosition = target.position,
                    newPosition = null)
                )
            } else Consumed
            context.world.removeEntity(target)

            logGameEvent("$target dies after receiving $cause.")
            result
        }
}
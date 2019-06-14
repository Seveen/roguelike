package com.necroworld.systems

import com.necroworld.attributes.DecisionMapTarget
import com.necroworld.commands.MoveTo
import com.necroworld.commands.UpdateDecisionMap
import com.necroworld.extensions.GameCommand
import com.necroworld.extensions.GameEntity
import com.necroworld.extensions.isDecisionMapTarget
import com.necroworld.extensions.position
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseActor
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.amethyst.api.extensions.responseWhenCommandIs
import kotlin.system.measureTimeMillis

object DecisionMapUpdater : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>) =
            command.responseWhenCommandIs(UpdateDecisionMap::class) { (context, entity, oldPosition, newPosition) ->
                val world = context.world
                if (entity.isDecisionMapTarget) {
                    val tag = entity.findAttribute(DecisionMapTarget::class).get().tag
                    val decisionMap = world.getDecisionMapByTag(tag)
                    decisionMap?.removeTargetPosition(oldPosition)
                    if (newPosition != null) {
                        decisionMap?.addTargetPosition(newPosition)
                    }
                    decisionMap?.process(entity.position.z)

                    Consumed
                } else Pass
            }
}
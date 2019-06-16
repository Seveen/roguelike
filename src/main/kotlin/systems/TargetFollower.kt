package com.necroworld.systems

import com.necroworld.attributes.DecisionMapUser
import com.necroworld.commands.MoveTo
import com.necroworld.extensions.GameEntity
import com.necroworld.extensions.isDecisionMapUser
import com.necroworld.extensions.position
import com.necroworld.functions.logGameEvent
import com.necroworld.world.DecisionMap
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.EntityType

object TargetFollower : BaseBehavior<GameContext>() {

    override fun update(entity: GameEntity<EntityType>, context: GameContext): Boolean {
        val pos = entity.position
        val world = context.world
        val decisionMaps = mutableListOf<DecisionMap>()
        var finalDecisionMap: DecisionMap

        //TODO(Must be able to chain dm behaviors and have followers and fleers THEN add them all)
        if (entity.isDecisionMapUser && pos.isUnknown().not()) {
            entity.findAttribute(DecisionMapUser::class).get().weightedTags.forEach {
                var dm = world.getDecisionMapByTag(it.key)
                if (dm != null) {
                    dm *= it.value
                    decisionMaps.add(dm)
                }
            }
            finalDecisionMap = decisionMaps.reduce { acc, it ->
                acc + it
            }
            entity.executeCommand(
                MoveTo(
                    context = context,
                    source = entity,
                    position = finalDecisionMap.findLowestAround(entity.position)
                )
            )
            return true
        }
        return false
    }
}
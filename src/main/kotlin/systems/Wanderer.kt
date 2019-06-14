package com.necroworld.systems

import com.necroworld.commands.MoveTo
import com.necroworld.extensions.GameEntity
import com.necroworld.extensions.position
import com.necroworld.extensions.sameLevelNeighborsShuffled
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.EntityType

object Wanderer : BaseBehavior<GameContext>() {

    override fun update(entity: GameEntity<EntityType>, context: GameContext): Boolean {
        val pos = entity.position
        if (pos.isUnknown().not()) {
            entity.executeCommand(
                MoveTo(
                context = context,
                source = entity,
                position = pos.sameLevelNeighborsShuffled().first())
            )
            return true
        }
        return false
    }
}
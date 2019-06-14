package com.necroworld.systems

import com.necroworld.attributes.FungusSpread
import com.necroworld.builders.EntityFactory
import com.necroworld.extensions.GameEntity
import com.necroworld.extensions.position
import com.necroworld.extensions.tryToFindAttribute
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.zircon.api.Sizes

object FungusGrowth : BaseBehavior<GameContext>(FungusSpread::class) {

    override fun update(entity: GameEntity<out EntityType>, context: GameContext): Boolean {
        val world = context.world
        val fungusSpread = entity.tryToFindAttribute(FungusSpread::class)
        val (spreadCount, maxSpread) = fungusSpread
        return if (spreadCount < maxSpread && Math.random() < 0.015) {
            world.findEmptyLocationWithin(
                offset = entity.position
                    .withRelativeX(-1)
                    .withRelativeY(-1),
                size = Sizes.create3DSize(3, 3, 0)).map { emptyLocation ->
                    world.addEntity(EntityFactory.newFungus(fungusSpread), emptyLocation)
                    fungusSpread.spreadCount++
                }
            true
        } else false
    }
}
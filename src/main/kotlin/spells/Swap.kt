package com.necroworld.spells

import com.necroworld.attributes.types.Spellcaster
import com.necroworld.extensions.GameEntity
import com.necroworld.extensions.isPlayer
import com.necroworld.extensions.position
import com.necroworld.functions.logGameEvent
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Position

class Swap : EntitySpell {
    override val name = "Swap"
    override val baseManaCost = 1
    override val baseRange = 15
    override val areaOfEffect = listOf(Position.defaultPosition())

    override val effects = listOf<(context: GameContext, caster: GameEntity<Spellcaster>, target: GameEntity<EntityType>) -> Unit>
    { context, caster, target ->
        val world = context.world
        val targetOldPosition = target.position
        val casterOldPosition = caster.position

        val targetBlock = world.fetchBlockAt(targetOldPosition)
        val casterBlock = world.fetchBlockAt(casterOldPosition)

        targetBlock.get().removeEntity(target)
        casterBlock.get().removeEntity(caster)

        target.position = casterOldPosition
        caster.position = targetOldPosition

        targetBlock.get().addEntity(caster)
        casterBlock.get().addEntity(target)

        val subject = if (caster.isPlayer) {
            "You"
        } else {
            "The ${caster.name}"
        }
        val verb = if (caster.isPlayer) {
            "swap places"
        } else {
            "swaps places"
        }
        logGameEvent("$subject $verb with the ${target.name}!")
    }
}
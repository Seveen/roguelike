package com.necroworld.spells

import com.necroworld.attributes.types.Spellcaster
import com.necroworld.attributes.types.spellcastStats
import com.necroworld.extensions.GameEntity
import com.necroworld.extensions.isPlayer
import com.necroworld.extensions.position
import com.necroworld.functions.logGameEvent
import com.necroworld.world.GameContext
import org.hexworks.zircon.api.data.Position

class Blink : GroundSpell {
    override val name = "Blink"
    override val baseManaCost = 1
    override val baseRange = 15
    override val areaOfEffect = listOf(Position.defaultPosition())

    override val effects = listOf<(context: GameContext, caster: GameEntity<Spellcaster>, target: Position) -> Unit>
    { context, caster, target ->
        val world = context.world
        val casterOldPosition = caster.position

        val casterBlock = world.fetchBlockAt(casterOldPosition)
        val targetBlock = world.fetchBlockAt(target.toPosition3D(caster.position.z))

        caster.spellcastStats.mana -= baseManaCost

        casterBlock.get().removeEntity(caster)
        caster.position = target.toPosition3D(caster.position.z)
        targetBlock.get().addEntity(caster)
        //TODO(nothing is updated. Consider emiting a move command instead)

        val subject = if (caster.isPlayer) {
            "You"
        } else {
            "The ${caster.name}"
        }
        val verb = if (caster.isPlayer) {
            "blink"
        } else {
            "blinks"
        }
        logGameEvent("$subject $verb!")
    }
}
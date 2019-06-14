package com.necroworld.systems

import com.necroworld.attributes.Faction
import com.necroworld.attributes.types.combatStats
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import com.necroworld.commands.Attack
import com.necroworld.commands.Destroy
import com.necroworld.extensions.*
import com.necroworld.functions.logGameEvent
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.extensions.responseWhenCommandIs

object Attackable : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>) = command.responseWhenCommandIs(Attack::class) { (context, attacker, target) ->
        var allied = false
        if (attacker.hasFaction && target.hasFaction) {
            allied = attacker.faction == target.faction
        }
        if (allied.not()) {
            val damage = Math.max(0, attacker.combatStats.attackValue - target.combatStats.defenseValue)
            val finalDamage = (Math.random() * damage).toInt() + 1
            target.combatStats.hp -= finalDamage

            logGameEvent("The $attacker hits the $target for $finalDamage!")

            target.whenHasNoHealthLeft {
                target.executeCommand(Destroy(
                    context = context,
                    source = attacker,
                    target = target,
                    cause = "a blow to the head"
                ))
            }
            Consumed
        } else Pass
    }
}
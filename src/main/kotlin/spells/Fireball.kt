package com.necroworld.spells

import com.necroworld.attributes.types.Combatant
import com.necroworld.attributes.types.Spellcaster
import com.necroworld.attributes.types.combatStats
import com.necroworld.attributes.types.spellcastStats
import com.necroworld.commands.Destroy
import com.necroworld.extensions.GameEntity
import com.necroworld.extensions.buildAOEWithRadius
import com.necroworld.extensions.whenHasNoHealthLeft
import com.necroworld.extensions.whenTypeIs
import com.necroworld.functions.logGameEvent
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Position

class Fireball : EntitySpell {
    override val name = "Fireball"
    override val baseManaCost = 5
    override val baseRange = 15
    override val areaOfEffect = Position.defaultPosition().buildAOEWithRadius(3)

    private val damage = 10
    override val effects = listOf<(context: GameContext, caster: GameEntity<Spellcaster>, target: GameEntity<EntityType>) -> Unit>
    { context, caster, target ->
        caster.spellcastStats.mana -= baseManaCost
        target.whenTypeIs<Combatant> {
            val finalDamage = (Math.random() * damage).toInt() + 1
            it.combatStats.hp -= finalDamage

            logGameEvent("The fireball burns the $it for $finalDamage!")

            it.whenHasNoHealthLeft {
                target.executeCommand(
                    Destroy(
                        context = context,
                        source = caster,
                        target = target,
                        cause = "burned to death"
                    )
                )
            }
        }
    }
}
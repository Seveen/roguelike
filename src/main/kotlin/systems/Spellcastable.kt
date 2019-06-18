package com.necroworld.systems

import com.necroworld.attributes.types.spellcastStats
import com.necroworld.commands.CastSpell
import com.necroworld.extensions.GameCommand
import com.necroworld.extensions.getDistanceFrom
import com.necroworld.extensions.position
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.amethyst.api.extensions.responseWhenCommandIs

object Spellcastable : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>) =
        command.responseWhenCommandIs(CastSpell::class) {(context, caster, targets, spell ) ->
            val world = context.world
            var response : Response = Pass
            targets.forEach { target ->
                world.fetchBlockAt(target).get().entities.forEach { entity ->
                    val distance = entity.position.getDistanceFrom(caster.position)
                    val actualRange = spell.baseRange
                    //TODO("Do I need to check range here?")
                    var canCast = distance <= actualRange && caster.spellcastStats.mana >= spell.baseManaCost
                    if (canCast) {
                        spell.effects.forEach {
                            it(context, caster, entity)
                        }
                        response = Consumed
                    }
                }

            }
            response
        }
}
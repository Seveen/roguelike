package com.necroworld.systems

import com.necroworld.attributes.types.spellcastStats
import com.necroworld.commands.CastSpell
import com.necroworld.extensions.GameCommand
import com.necroworld.extensions.getDistanceFrom
import com.necroworld.extensions.position
import com.necroworld.extensions.whenTypeIs
import com.necroworld.spells.EntitySpell
import com.necroworld.spells.GroundSpell
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.amethyst.api.extensions.responseWhenCommandIs
import org.hexworks.cobalt.datatypes.Maybe

object Spellcastable : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>) =
        command.responseWhenCommandIs(CastSpell::class) {(context, caster, targets, spell ) ->
            val world = context.world
            var response : Response = Pass

            spell.whenTypeIs<GroundSpell> { groundSpell ->
                targets.forEach { target ->
                    val distance = target.getDistanceFrom(caster.position)
                    val actualRange = spell.baseRange
                    //TODO("Do I need to check range here?")
                    var canCast = distance <= actualRange && caster.spellcastStats.mana >= spell.baseManaCost
                    if (canCast) {
                        groundSpell.effects.forEach {
                            it(context, caster, target.to2DPosition())
                        }
                        response = Consumed
                    }
                }
            }

            spell.whenTypeIs<EntitySpell> { entitySpell ->
                targets.forEach { target ->
                    world.fetchBlockAt(target).get().entities.forEach { entity ->
                        val distance = entity.position.getDistanceFrom(caster.position)
                        val actualRange = spell.baseRange
                        //TODO("Do I need to check range here?")
                        var canCast = distance <= actualRange && caster.spellcastStats.mana >= spell.baseManaCost
                        if (canCast) {
                            entitySpell.effects.forEach {
                                it(context, caster, entity)
                            }
                            response = Consumed
                        }
                    }
                }
            }

            response
        }
}
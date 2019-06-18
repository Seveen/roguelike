package com.necroworld.commands

import com.necroworld.attributes.types.Spellcaster
import com.necroworld.extensions.GameEntity
import com.necroworld.spells.Spell
import com.necroworld.world.GameContext
import org.hexworks.zircon.api.data.impl.Position3D

data class CastSpell(override val context: GameContext,
                     override val source: GameEntity<Spellcaster>,
                     override val targets: List<Position3D>,
                     val spell: Spell
) : GroundAction<Spellcaster>
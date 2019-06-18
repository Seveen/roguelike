package com.necroworld.spells

import com.necroworld.attributes.types.Spellcaster
import com.necroworld.extensions.GameEntity
import com.necroworld.world.GameContext
import org.hexworks.zircon.api.data.Position

interface GroundSpell : Spell {
    val effects: List<(context: GameContext, caster: GameEntity<Spellcaster>, target: Position) -> Unit>
}
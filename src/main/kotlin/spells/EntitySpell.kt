package com.necroworld.spells

import com.necroworld.attributes.types.Spellcaster
import com.necroworld.extensions.GameEntity
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.entity.EntityType

interface EntitySpell : Spell {
    val effects: List<(context: GameContext, caster: GameEntity<Spellcaster>, target: GameEntity<EntityType>) -> Unit>
}

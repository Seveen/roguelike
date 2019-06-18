package com.necroworld.spells

import com.necroworld.attributes.types.Spellcaster
import com.necroworld.extensions.GameEntity
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Position

interface Spell {
    val name: String
    val baseManaCost: Int
    val baseRange: Int
    val areaOfEffect: List<Position>
    val effects: List<(context: GameContext, caster: GameEntity<Spellcaster>, target: GameEntity<EntityType>) -> Unit>
}


package com.necroworld.attributes.types

import com.necroworld.attributes.SpellcastStats
import com.necroworld.extensions.GameEntity
import org.hexworks.amethyst.api.entity.EntityType

interface Spellcaster : EntityType

val GameEntity<Spellcaster>.spellcastStats
    get() = findAttribute(SpellcastStats::class).get()
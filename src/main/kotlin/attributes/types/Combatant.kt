package com.necroworld.attributes.types

import com.necroworld.attributes.CombatStats
import com.necroworld.extensions.GameEntity
import org.hexworks.amethyst.api.entity.EntityType

interface Combatant : EntityType

val GameEntity<Combatant>.combatStats: CombatStats
    get() = findAttribute(CombatStats::class).get()
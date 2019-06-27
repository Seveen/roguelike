package com.necroworld.attributes.types

import com.necroworld.attributes.ItemCombatStats
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.entity.Entity

interface Armor: CombatItem

val Entity<Armor, GameContext>.attackValue: Int
    get() = findAttribute(ItemCombatStats::class).get().attackValue

val Entity<Armor, GameContext>.defenseValue: Int
    get() = findAttribute(ItemCombatStats::class).get().defenseValue
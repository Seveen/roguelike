package com.necroworld.spells

import org.hexworks.zircon.api.data.Position

interface Spell {
    val name: String
    val baseManaCost: Int
    val baseRange: Int
    val areaOfEffect: List<Position>
}


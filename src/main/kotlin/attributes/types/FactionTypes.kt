package com.necroworld.attributes.types

import org.hexworks.amethyst.api.Attribute

interface FactionType : Attribute {
    val name: String
}

abstract class BaseFactionType(override val name: String = "unknown") : FactionType

object PlayerFaction : BaseFactionType(
    name = "player faction")

object CaveDwellersFaction : BaseFactionType(
    name = "cave dwellers faction")
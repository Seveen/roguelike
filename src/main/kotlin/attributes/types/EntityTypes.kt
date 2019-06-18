package com.necroworld.attributes.types

import org.hexworks.amethyst.api.base.BaseEntityType

object FogOfWarType : BaseEntityType()

object Wall : BaseEntityType(
    name = "wall")

object StairsDown : BaseEntityType(
    name = "stairs down")

object StairsUp : BaseEntityType(
    name = "stairs up")

object Player : BaseEntityType(
    name = "player"), Combatant, ItemHolder, Spellcaster

object Fungus : BaseEntityType(
    name = "fungus"), Combatant

object Bat : BaseEntityType(
    name = "bat"), Combatant, ItemHolder

object Stalker : BaseEntityType(
    name = "stalker"), Combatant, ItemHolder

object Kobold : BaseEntityType(
    name = "kobold"), Combatant, ItemHolder

object Zircon : BaseEntityType(
    name = "Zircon",
    description = "A small piece of Zircon. Its value is unfathomable."), Item
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
    name = "player"), Combatant, ItemHolder, EquipmentHolder, Spellcaster

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

object Dagger : BaseEntityType(
    name = "Rusty Dagger",
    description = "A small, rusty dagger made of some metal alloy."), Weapon

object Sword : BaseEntityType(
    name = "Iron Sword",
    description = "A shiny sword made of iron. It is a two-hand weapon"), Weapon

object Staff : BaseEntityType(
    name = "Wooden Staff",
    description = "A wooden staff made of birch. It has seen some use"), Weapon

object LightArmor : BaseEntityType(
    name = "Leather Tunic",
    description = "A tunic made of rugged leather. It is very comfortable."), Armor

object MediumArmor : BaseEntityType(
    name = "Chainmail",
    description = "A sturdy chainmail armor made of interlocking iron chains."), Armor

object HeavyArmor : BaseEntityType(
    name = "Platemail",
    description = "A heavy and shiny platemail armor made of bronze."), Armor

object Club : BaseEntityType(
    name = "Club",
    description = "A wooden club. It doesn't give you an edge over your opponent (haha)."), Weapon

object Jacket : BaseEntityType(
    name = "Leather jacket",
    description = "Dirty and rugged jacket made of leather."), Armor
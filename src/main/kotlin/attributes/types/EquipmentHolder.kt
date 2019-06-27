package com.necroworld.attributes.types

import com.necroworld.attributes.Equipment
import com.necroworld.attributes.Inventory
import com.necroworld.extensions.GameCombatItem
import com.necroworld.extensions.GameEquipmentHolder
import org.hexworks.amethyst.api.entity.EntityType

interface EquipmentHolder : EntityType

fun GameEquipmentHolder.equip(inventory: Inventory, item: GameCombatItem): GameCombatItem {
    return equipment.equip(inventory, item)
}

val GameEquipmentHolder.equipment: Equipment
    get() = findAttribute(Equipment::class).get()
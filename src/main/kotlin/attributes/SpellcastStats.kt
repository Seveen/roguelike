package com.necroworld.attributes

import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property

data class SpellcastStats(val maxManaProperty: Property<Int>,
                          val manaProperty: Property<Int> = createPropertyFrom(maxManaProperty.value)
) : Attribute {
    val maxMana: Int by maxManaProperty.asDelegate()
    var mana: Int by manaProperty.asDelegate()

    companion object {

        fun create(maxMana: Int, mana: Int = maxMana) =
            SpellcastStats(
                maxManaProperty = createPropertyFrom(maxMana),
                manaProperty = createPropertyFrom(mana)
            )
    }
}
package com.necroworld.attributes

import com.necroworld.extensions.toStringProperty
import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.expression.concat
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.zircon.api.Components

data class SpellcastStats(val maxManaProperty: Property<Int>,
                          val manaProperty: Property<Int> = createPropertyFrom(maxManaProperty.value)
) : DisplayableAttribute {
    val maxMana: Int by maxManaProperty.asDelegate()
    var mana: Int by manaProperty.asDelegate()

    override fun toComponent(width: Int) = Components.vbox()
        .withSize(width, 5)
        .build().apply {
            val manaLabel = Components.label()
                .withSize(width, 1)
                .build()

            manaLabel.textProperty.updateFrom(manaProperty.toStringProperty()
                .concat("/").concat(maxMana))

            addComponent(Components.textBox(width)
                .addHeader("Mana"))
            addComponent(manaLabel)
        }

    companion object {

        fun create(maxMana: Int, mana: Int = maxMana) =
            SpellcastStats(
                maxManaProperty = createPropertyFrom(maxMana),
                manaProperty = createPropertyFrom(mana)
            )
    }
}
package com.necroworld.view.fragment

import com.necroworld.attributes.types.CombatItem
import com.necroworld.attributes.types.iconTile
import com.necroworld.extensions.GameItem
import com.necroworld.extensions.whenTypeIs
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.graphics.Symbols

class InventoryRowFragment(width: Int, item: GameItem) : Fragment {

    val dropButton = Components.button()
        .withText("${Symbols.ARROW_DOWN}")
        .build()

    val equipButton = Components.button()
        .withText("Equip")
        .build()

    override val root = Components.hbox()
        .withSpacing(1)
        .withSize(width, 1)
        .build().apply {
            addComponent(Components.icon()
                .withIcon(item.iconTile))
            addComponent(Components.label()
                .withSize(InventoryFragment.NAME_COLUMN_WIDTH, 1)
                .withText(item.name))
            addComponent(dropButton)
            item.whenTypeIs<CombatItem> {
                addComponent(equipButton)
            }
        }
}
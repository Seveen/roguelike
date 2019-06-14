package com.necroworld.view.fragment

import com.necroworld.attributes.types.iconTile
import com.necroworld.extensions.GameItem
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.graphics.Symbols

class InventoryRowFragment(width: Int, item: GameItem) : Fragment {

    val dropButton = Components.button()
        .wrapSides(false)
        .withText("${Symbols.ARROW_DOWN}")
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
        }
}

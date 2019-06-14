package com.necroworld.systems

import com.necroworld.GameConfig
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import com.necroworld.attributes.types.ItemHolder
import com.necroworld.attributes.types.inventory
import com.necroworld.commands.DropItem
import com.necroworld.commands.InspectInventory
import com.necroworld.extensions.GameCommand
import com.necroworld.view.fragment.InventoryFragment
import com.necroworld.world.GameContext
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.builder.component.ModalBuilder
import org.hexworks.zircon.api.component.ComponentAlignment.BOTTOM_LEFT
import org.hexworks.zircon.api.extensions.onComponentEvent
import org.hexworks.zircon.api.uievent.ComponentEventType.ACTIVATED
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.internal.component.modal.EmptyModalResult

object InventoryInspector : BaseFacet<GameContext>() {

    val DIALOG_SIZE = Sizes.create(33, 14)

    override fun executeCommand(command: GameCommand<out EntityType>) = command
        .responseWhenCommandIs(InspectInventory::class) { (context, itemHolder, position) ->

            val screen = context.screen

            val fragment = InventoryFragment(
                inventory = itemHolder.inventory,
                width = DIALOG_SIZE.width - 3,
                onDrop = { item ->
                    itemHolder.executeCommand(DropItem(context, itemHolder, item, position))
                })

            val panel = Components.panel()
                .withSize(DIALOG_SIZE)
                .wrapWithBox(true)
                .wrapWithShadow(true)
                .withTitle("Inventory")
                .build()

            panel.addFragment(fragment)

            val modal = ModalBuilder.newBuilder<EmptyModalResult>()
                .withParentSize(screen.size)
                .withComponent(panel)
                .build()

            panel.addComponent(Components.button()
                .withText("Close")
                .withAlignmentWithin(panel, BOTTOM_LEFT)
                .build()
                .apply {
                    onComponentEvent(ACTIVATED) {
                        modal.close(EmptyModalResult)
                        Processed
                    }
                })

            modal.applyColorTheme(GameConfig.THEME)
            screen.openModal(modal)
            Consumed
        }

}
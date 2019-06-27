package com.necroworld.systems

import com.necroworld.GameConfig
import com.necroworld.attributes.types.CombatItem
import com.necroworld.attributes.types.EquipmentHolder
import com.necroworld.attributes.types.equip
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import com.necroworld.attributes.types.inventory
import com.necroworld.commands.DropItem
import com.necroworld.commands.InspectInventory
import com.necroworld.extensions.GameCommand
import com.necroworld.extensions.GameItem
import com.necroworld.extensions.whenTypeIs
import com.necroworld.view.fragment.InventoryFragment
import com.necroworld.world.GameContext
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.builder.component.ModalBuilder
import org.hexworks.zircon.api.component.ComponentAlignment.BOTTOM_LEFT
import org.hexworks.zircon.api.component.ComponentAlignment.TOP_CENTER
import org.hexworks.zircon.api.extensions.processComponentEvents
import org.hexworks.zircon.api.extensions.shadow
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
                },
                onEquip = { item ->
                    var result = Maybe.empty<GameItem>()
                    itemHolder.whenTypeIs<EquipmentHolder> { equipmentHolder ->
                        item.whenTypeIs<CombatItem> { combatItem ->
                            result = Maybe.of(equipmentHolder.equip(itemHolder.inventory, combatItem))
                        }
                    }
                    result
                }
            )

            val panel = Components.panel()
                .withSize(DIALOG_SIZE)
                .withDecorations(box(title = "Inventory"))
                .build()

            panel.addFragment(fragment)

            val modal = ModalBuilder.newBuilder<EmptyModalResult>()
                .withParentSize(screen.size)
                .withComponent(panel)
                .build()

            panel.addComponent(Components.button()
                .withText("Close")
                .withAlignmentWithin(panel, TOP_CENTER)
                .build()
                .apply {
                    processComponentEvents(ACTIVATED) {
                        modal.close(EmptyModalResult)
                        Processed
                    }
                })

            modal.applyColorTheme(GameConfig.THEME)
            screen.openModal(modal)
            Consumed
        }

}
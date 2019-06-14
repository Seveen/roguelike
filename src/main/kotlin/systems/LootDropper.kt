package com.necroworld.systems

import com.necroworld.attributes.types.ItemHolder
import com.necroworld.attributes.types.inventory
import com.necroworld.commands.Destroy
import com.necroworld.commands.DropItem
import com.necroworld.extensions.GameCommand
import com.necroworld.extensions.position
import com.necroworld.extensions.whenTypeIs
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object LootDropper : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>) = command
        .responseWhenCommandIs(Destroy::class) { (context, _, target) ->
            target.whenTypeIs<ItemHolder> { entity ->
                entity.inventory.items.forEach { item ->
                    entity.executeCommand(DropItem(context, entity, item, entity.position))
                }
            }
            Pass
        }
}
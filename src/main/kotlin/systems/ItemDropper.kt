package com.necroworld.systems

import com.necroworld.attributes.types.removeItem
import com.necroworld.commands.DropItem
import com.necroworld.extensions.GameCommand
import com.necroworld.extensions.isPlayer
import com.necroworld.functions.logGameEvent
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.amethyst.api.extensions.responseWhenCommandIs

object ItemDropper : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>) = command
        .responseWhenCommandIs(DropItem::class) {(context, itemHolder, item, position) ->
            if (itemHolder.removeItem(item)) {
                context.world.addEntity(item, position)
                val subject = if (itemHolder.isPlayer) "You" else "The $itemHolder"
                val verb = if (itemHolder.isPlayer) "drop" else "drops"
                logGameEvent("$subject $verb the $item")
            }
            Consumed
        }
}

package com.necroworld.view.fragment

import com.necroworld.attributes.DisplayableAttribute
import com.necroworld.attributes.types.Player
import com.necroworld.extensions.GameEntity
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment

class PlayerStatsFragment(
    width: Int,
    player: GameEntity<Player>) : Fragment {

    override val root = Components.vbox()
        .withSize(width, 30)
        .withSpacing(1)
        .build().apply {
            addComponent(Components.header().withText("Player"))
            player.attributes.toList().filterIsInstance<DisplayableAttribute>()
                .forEach {
                    addComponent(it.toComponent(width))
                }
        }

}
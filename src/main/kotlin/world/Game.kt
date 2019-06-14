package com.necroworld.world

import com.necroworld.attributes.types.Player
import com.necroworld.extensions.GameEntity

class Game(val world: World,
           val player: GameEntity<Player>) {

    companion object {

        fun create(player: GameEntity<Player>,
                   world: World) = Game(
            world = world,
            player = player)
    }
}

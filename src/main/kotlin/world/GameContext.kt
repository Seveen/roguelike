package com.necroworld.world

import org.hexworks.amethyst.api.Context
import com.necroworld.attributes.types.Player
import com.necroworld.extensions.GameEntity
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.UIEvent

data class GameContext(val world: World,
                       val screen: Screen,
                       val uiEvent: UIEvent) : Context
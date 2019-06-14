package com.necroworld.commands

import com.necroworld.extensions.GameCommand
import com.necroworld.extensions.GameEntity
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.entity.EntityType

data class Destroy(override val context: GameContext,
                   override val source: GameEntity<EntityType>,
                   val target: GameEntity<EntityType>,
                   val cause: String = "natural cause") : GameCommand<EntityType>
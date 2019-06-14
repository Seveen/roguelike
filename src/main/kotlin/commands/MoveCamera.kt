package com.necroworld.commands

import com.necroworld.extensions.GameCommand
import com.necroworld.extensions.GameEntity
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.impl.Position3D

data class MoveCamera(override val context: GameContext,
                      override val source: GameEntity<EntityType>,
                      val previousPosition: Position3D
) : GameCommand<EntityType>
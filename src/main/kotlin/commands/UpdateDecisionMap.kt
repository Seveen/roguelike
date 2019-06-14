package com.necroworld.commands

import com.necroworld.extensions.GameCommand
import com.necroworld.extensions.GameEntity
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.impl.Position3D

data class UpdateDecisionMap(override val context: GameContext,
                             override val source: GameEntity<EntityType>,
                             val oldPosition: Position3D,
                             val newPosition: Position3D?
) : GameCommand<EntityType>
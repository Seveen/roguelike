package com.necroworld.commands

import org.hexworks.amethyst.api.entity.EntityType
import com.necroworld.extensions.GameCommand
import com.necroworld.extensions.GameEntity
import com.necroworld.world.GameContext
import org.hexworks.zircon.api.data.impl.Position3D

data class MoveTo(override val context: GameContext,
                  override val source: GameEntity<EntityType>,
                  val position: Position3D) : GameCommand<EntityType>
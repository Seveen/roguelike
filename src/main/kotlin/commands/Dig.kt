package com.necroworld.commands

import org.hexworks.amethyst.api.entity.EntityType
import com.necroworld.extensions.GameEntity
import com.necroworld.world.GameContext

data class Dig(override val context: GameContext,
               override val source: GameEntity<EntityType>,
               override val target: GameEntity<EntityType>) : EntityAction<EntityType, EntityType>
package com.necroworld.commands

import com.necroworld.extensions.GameCommand
import com.necroworld.extensions.GameItemHolder
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.impl.Position3D

data class InspectInventory(override val context: GameContext,
                            override val source: GameItemHolder,
                            val position: Position3D) : GameCommand<EntityType>
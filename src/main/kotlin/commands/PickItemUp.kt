package com.necroworld.commands

import com.necroworld.attributes.types.ItemHolder
import com.necroworld.extensions.GameCommand
import com.necroworld.extensions.GameItemHolder
import com.necroworld.world.GameContext
import org.hexworks.zircon.api.data.impl.Position3D

data class PickItemUp(override val context: GameContext,
                      override val source: GameItemHolder,
                      val position: Position3D) : GameCommand<ItemHolder>
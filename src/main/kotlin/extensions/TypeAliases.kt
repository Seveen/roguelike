package com.necroworld.extensions

import com.necroworld.attributes.types.Item
import com.necroworld.attributes.types.ItemHolder
import org.hexworks.amethyst.api.entity.Entity
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.entity.EntityType

typealias AnyGameEntity = Entity<EntityType, GameContext>

typealias GameEntity<T> = Entity<T, GameContext>

typealias GameCommand<T> = Command<T, GameContext>

typealias GameItem = GameEntity<Item>

typealias GameItemHolder = GameEntity<ItemHolder>
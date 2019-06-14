package com.necroworld.commands

import org.hexworks.amethyst.api.entity.EntityType
import com.necroworld.extensions.GameCommand
import com.necroworld.extensions.GameEntity

interface EntityAction<S : EntityType, T : EntityType> : GameCommand<S> {

    val target: GameEntity<T>

    operator fun component1() = context
    operator fun component2() = source
    operator fun component3() = target
}
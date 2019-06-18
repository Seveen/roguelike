package com.necroworld.commands

import com.necroworld.extensions.GameCommand
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.impl.Position3D

interface GroundAction<S : EntityType> : GameCommand<S> {

    val targets: List<Position3D>

    operator fun component1() = context
    operator fun component2() = source
    operator fun component3() = targets
}
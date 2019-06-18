package com.necroworld.extensions

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.impl.Position3D

fun Position3D.sameLevelNeighborsShuffled(): List<Position3D> {
    return (-1..1).flatMap { x ->
        (-1..1).map { y ->
            this.withRelativeX(x).withRelativeY(y)
        }
    }.minus(this).shuffled()
}

fun Position3D.getDistanceFrom(other: Position3D): Int {
    val delta = this.minus(other)
    return delta.x + delta.y + delta.z
}

fun Position3D.isSameLevelAs(other: Position3D): Boolean {
    return this.z == other.z
}

fun Position.getDistanceFrom(other: Position): Int {
    val delta = this.minus(other)
    return Math.abs(delta.x) + Math.abs(delta.y)
}

fun Position.buildAOEWithRadius(radius: Int): List<Position> {
    return (-radius..radius).flatMap { x ->
        (-radius..radius).map { y ->
            this.withRelativeX(x).withRelativeY(y)
        }
    }.filter {
        it.getDistanceFrom(this) <= radius
    }
}




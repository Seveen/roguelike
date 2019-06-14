package com.necroworld.extensions

import org.hexworks.zircon.api.data.impl.Position3D

fun Position3D.sameLevelNeighborsShuffled(): List<Position3D> {
    return (-1..1).flatMap { x ->
        (-1..1).map { y ->
            this.withRelativeX(x).withRelativeY(y)
        }
    }.minus(this).shuffled()
}

/*fun Position3D.sameLevelCardinalNeighbors(): List<Position3D> {
    return (-1..1).map { x ->
        this.withRelativeX(x)
    }.minus(this)
        .plus((-1..1).map{y ->
            this.withRelativeY(y)
    }).minus(this)
}*/

fun Position3D.sameLevelCardinalNeighbors(): List<Position3D> {
    return listOf(this.withRelativeX(-1), this.withRelativeX(1), this.withRelativeY(-1), this.withRelativeY(1))
}

package com.necroworld.world

import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D

class DecisionMap(private val size: Size3D, private val callback: (position: Position3D) -> Boolean) {
    private var valueArray = DoubleArray(size.xLength * size.yLength)
    private var invertedValueArray = DoubleArray(size.xLength * size.yLength)
    private var listOfTargetPositions = mutableListOf<Position3D>()

    fun addTargetPosition(position: Position3D) {
        listOfTargetPositions.add(position)
    }

    fun removeTargetPosition(position: Position3D) {
        listOfTargetPositions.remove(position)
    }

    fun process(level: Int) {
        resetValues(level)
        compute(valueArray, level)
        computeInverted(level)
    }

    fun findLowestCardinalAround(position: Position3D): Position3D {
        var lowest = Double.MAX_VALUE
        var lowestPosition: Position3D = position

        position.sameLevelCardinalNeighbors().forEach {
            val outsideLimits = it.x < 0 || it.x >= size.xLength || it.y < 0 || it.y >= size.yLength
            if(outsideLimits.not()) {
                val actualValue = getValueAtPosition(it)
                if ( actualValue < lowest) {
                    lowest = actualValue
                    lowestPosition = it
                }
            }
        }

        return lowestPosition
    }

    fun findLowestAround(position: Position3D): Position3D {
        var lowest = Double.MAX_VALUE
        var lowestPosition: Position3D = position

        position.sameLevelNeighbors().forEach {
            val actualValue = getValueAtPosition(it)
            if ( actualValue < lowest) {
                lowest = actualValue
                lowestPosition = it
            }
        }

        return lowestPosition
    }

    fun findLowestCardinalAroundOnInverted(position: Position3D): Position3D {
        var lowest = Double.MAX_VALUE
        var lowestPosition: Position3D = position

        position.sameLevelCardinalNeighbors().forEach {
            val outsideLimits = it.x < 0 || it.x >= size.xLength || it.y < 0 || it.y >= size.yLength
            if(outsideLimits.not()) {
                val actualValue = getInvertedValueAtPosition(it)
                if ( actualValue < lowest) {
                    lowest = actualValue
                    lowestPosition = it
                }
            }
        }

        return lowestPosition
    }

    fun findLowestAroundOnInverted(position: Position3D): Position3D {
        var lowest = Double.MAX_VALUE
        var lowestPosition: Position3D = position

        position.sameLevelNeighbors().forEach {
            val actualValue = getInvertedValueAtPosition(it)
            if ( actualValue < lowest) {
                lowest = actualValue
                lowestPosition = it
            }
        }

        return lowestPosition
    }

    fun getValueAtPosition(position: Position3D): Double {
        val outsideLimits = position.x < 0 || position.x >= size.xLength || position.y < 0 || position.y >= size.yLength
        return if (outsideLimits.not()) {
            valueArray[(position.y * size.xLength) + position.x]
        } else {
            Double.MAX_VALUE
        }
    }

    fun getInvertedValueAtPosition(position: Position3D): Double {
        val outsideLimits = position.x < 0 || position.x >= size.xLength || position.y < 0 || position.y >= size.yLength
        return if (outsideLimits.not()) {
            invertedValueArray[(position.y * size.xLength) + position.x]
        } else {
            Double.MAX_VALUE
        }
    }

    operator fun plus(other: DecisionMap): DecisionMap {
        val new = DecisionMap(this.size, this.callback)
        new.setValueArray(
            this.valueArray.zip(other.valueArray) { a, b ->
                a + b
            }.toDoubleArray()
        )
        this.listOfTargetPositions.forEach {
            new.addTargetPosition(it)
        }
        other.listOfTargetPositions.forEach {
            new.addTargetPosition(it)
        }
        return new
    }

    operator fun minus(other: DecisionMap): DecisionMap {
        val new = DecisionMap(this.size, this.callback)
        new.setInvertedValueArray(
            this.invertedValueArray.zip(other.invertedValueArray) { a, b ->
                a + b
            }.toDoubleArray()
        )
        this.listOfTargetPositions.forEach {
            new.addTargetPosition(it)
        }
        other.listOfTargetPositions.forEach {
            new.addTargetPosition(it)
        }
        return new
    }

    operator fun times(weight: Double): DecisionMap {
        val new = DecisionMap(this.size, this.callback)
        new.setValueArray(
            valueArray.map {value ->
                value * weight
            }.toDoubleArray()
        )
        new.setInvertedValueArray(
            invertedValueArray.map {value ->
                value * weight
            }.toDoubleArray()
        )
        new.listOfTargetPositions = listOfTargetPositions
        return new
    }

    private fun compute(array: DoubleArray, level: Int) {
        var changesMade: Boolean
        do {
            changesMade = false

            for (y in 0 until size.yLength) {
                for (x in 0 until size.xLength) {
                    if (callback(Position3D.create(x, y, level))) {
                        val currentValue = getValueAtCoords(x, y)
                        val lowestValue = getLowestNeighborAtCoords(x, y)

                        if (currentValue - lowestValue > 1) {
                            array[y * size.xLength + x] = lowestValue + 1
                            changesMade = true
                        }
                    }
                }
            }
        } while (changesMade)
    }

    private fun computeInverted(level: Int) {
        invertArray()
        compute(invertedValueArray, level)
    }

    private fun invertArray() {
        for (y in 0 until size.yLength) {
            for (x in 0 until size.xLength) {
                if (valueArray[y * size.xLength + x] != Double.MAX_VALUE) {
                    invertedValueArray[y * size.xLength + x] = valueArray[y * size.xLength + x] * -1.4
                } else {
                    invertedValueArray[y * size.xLength + x] = Double.MAX_VALUE
                }
            }
        }
    }

    private fun getLowestNeighborAtCoords(x: Int, y: Int): Double {
        var lowest = Double.MAX_VALUE

        Position3D.create(x, y, 0).sameLevelNeighbors().forEach {
            val value = getValueAtCoords(it.x, it.y)
            if (value < lowest) {
                lowest = value
            }
        }

        return lowest
    }

    private fun getValueAtCoords(x: Int, y: Int): Double {
        val outsideLimits = x < 0 || x >= size.xLength || y < 0 || y >= size.yLength
        return if (outsideLimits.not()) {
            valueArray[(y * size.xLength) + x]
        } else {
            Double.MAX_VALUE
        }
    }

    private fun setValueArray(value: DoubleArray) {
        this.valueArray = value.copyOf()
    }

    private fun setInvertedValueArray(value: DoubleArray) {
        this.invertedValueArray = value.copyOf()
    }

    private fun resetValues(level: Int) {
        valueArray.fill(Double.MAX_VALUE)
        listOfTargetPositions
            .filter { it.z == level }
            .forEach {
                valueArray[(it.y * size.xLength) + it.x] = 0.0
        }
    }

    private fun Position3D.sameLevelCardinalNeighbors(): List<Position3D> {
        return listOf(this.withRelativeX(-1), this.withRelativeX(1), this.withRelativeY(-1), this.withRelativeY(1))
    }

    private fun Position3D.sameLevelNeighbors(): List<Position3D> {
        return (-1..1).flatMap { x ->
            (-1..1).map { y ->
                this.withRelativeX(x).withRelativeY(y)
            }
        }.minus(this)
    }
}
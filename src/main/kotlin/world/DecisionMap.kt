package com.necroworld.world

import com.necroworld.extensions.sameLevelCardinalNeighbors
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D

class DecisionMap(private val size: Size3D, callback: (position: Position3D) -> Boolean) {
    private var valueArray = IntArray(size.xLength * size.yLength)
    private var processPositionArray = BooleanArray(size.xLength * size.yLength)
    private var listOfTargetPositions = mutableListOf<Position3D>()

    private val processPositionCallback: (position: Position3D) -> Boolean = callback

    fun addTargetPosition(position: Position3D) {
        listOfTargetPositions.add(position)
    }

    fun removeTargetPosition(position: Position3D) {
        listOfTargetPositions.remove(position)
    }

    fun processIterator(level: Int) {
        var changesMade: Boolean
        val newValueArray = IntArray(size.xLength * size.yLength) { Int.MAX_VALUE }
        resetValues(level)

        val validPositions = size.fetchPositions().filter{ it.z == level && processPositionCallback(it) }

        do {
            changesMade = false

            validPositions.forEach {
                val currentValue = getValueAtPosition(it)
                val lowestValue = getLowestNeighbor(it)

                if (currentValue - lowestValue > 1) {
                    newValueArray[it.y * size.xLength + it.x] = lowestValue + 1
                    changesMade = true
                } else {
                    newValueArray[it.y * size.xLength + it.x] = currentValue
                }
            }

            listOfTargetPositions.forEach{
                newValueArray[it.y * size.xLength + it.x] = 0
            }
            setValueArray(newValueArray)

        } while(changesMade)
    }

    fun processForLoops(level: Int) {
        var changesMade: Boolean
        val newValueArray = IntArray(size.xLength * size.yLength) { Int.MAX_VALUE }
        resetValues(level)

        do {
            changesMade = false

            for (y in 0 until size.yLength) {
                for (x in 0 until size.xLength) {
                    if (processPositionCallback(Position3D.create(x, y, level))) {
                        val currentValue = getValueAtCoords(x, y)
                        val lowestValue = getLowestNeighborAtCoords(x, y)

                        if (currentValue - lowestValue > 1) {
                            newValueArray[y * size.xLength + x] = lowestValue + 1
                            changesMade = true
                        } else {
                            newValueArray[y * size.xLength + x] = currentValue
                        }
                    }
                }
                listOfTargetPositions.forEach {
                    newValueArray[it.y * size.xLength + it.x] = 0
                }
                setValueArray(newValueArray)
            }
        } while (changesMade)
    }

    fun processPrecalculateCallback(level: Int) {
        var changesMade: Boolean
        val newValueArray = IntArray(size.xLength * size.yLength) { Int.MAX_VALUE }
        resetValues(level)

        for (y in 0 until size.yLength) {
            for (x in 0 until size.xLength) {
                processPositionArray[y * size.xLength + x] = processPositionCallback(Position3D.create(x, y, level))
            }
        }

        do {
            changesMade = false

            for (y in 0 until size.yLength) {
                for (x in 0 until size.xLength) {
                    if (processPositionArray[y * size.xLength + x]) {
                        val currentValue = getValueAtCoords(x, y)
                        val lowestValue = getLowestNeighborAtCoords(x, y)

                        if (currentValue - lowestValue > 1) {
                            newValueArray[y * size.xLength + x] = lowestValue + 1
                            changesMade = true
                        } else {
                            newValueArray[y * size.xLength + x] = currentValue
                        }
                    }
                }
                listOfTargetPositions.forEach {
                    newValueArray[it.y * size.xLength + it.x] = 0
                }
                setValueArray(newValueArray)
            }
        } while (changesMade)
    }

    fun process(level: Int) {
        var changesMade: Boolean
        resetValues(level)

        do {
            changesMade = false

            for (y in 0 until size.yLength) {
                for (x in 0 until size.xLength) {
                    if (processPositionCallback(Position3D.create(x, y, level))) {
                        val currentValue = getValueAtCoords(x, y)
                        val lowestValue = getLowestNeighborAtCoords(x, y)

                        if (currentValue - lowestValue > 1) {
                            valueArray[y * size.xLength + x] = lowestValue + 1
                            changesMade = true
                        } else {
                            valueArray[y * size.xLength + x] = currentValue
                        }
                    }
                }
            }
        } while (changesMade)
    }

    fun findLowestCardinalAround(position: Position3D): Position3D {
        var lowest = Int.MAX_VALUE
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

    fun getLowestNeighbor(position: Position3D): Int {
        var lowest = Int.MAX_VALUE

        position.sameLevelCardinalNeighbors()
            .forEach {
                val actualValue = getValueAtPosition(it)
                if ( actualValue < lowest) {
                    lowest = actualValue
                }
            }

        return lowest
    }

    fun getLowestNeighborAtCoords(x: Int, y: Int): Int {
        var lowest = Int.MAX_VALUE

        var value = getValueAtCoords(x , y -1)
        if ( value < lowest) {
            lowest = value
        }
        value = getValueAtCoords(x , y + 1)
        if ( value < lowest) {
            lowest = value
        }
        value = getValueAtCoords(x - 1 , y)
        if ( value < lowest) {
            lowest = value
        }
        value = getValueAtCoords(x + 1 , y)
        if ( value < lowest) {
            lowest = value
        }

        return lowest
    }

    operator fun plus(other: DecisionMap): DecisionMap {
        val new = DecisionMap(this.size, this.processPositionCallback)
        new.setValueArray(
            this.valueArray.zip(other.valueArray) { a, b ->
                a + b
            }.toIntArray()
        )
        this.listOfTargetPositions.forEach {
            new.addTargetPosition(it)
        }
        other.listOfTargetPositions.forEach {
            new.addTargetPosition(it)
        }
        return new
    }

    operator fun times(weight: Int): DecisionMap {
        val new = DecisionMap(this.size, this.processPositionCallback)
        new.setValueArray(
            valueArray.map {value ->
                value * weight
            }.toIntArray()
        )
        new.listOfTargetPositions = listOfTargetPositions
        return new
    }

    private fun setValueAtPosition(position: Position3D, value: Int) {
        val outsideLimits = position.x < 0 || position.x >= size.xLength || position.y < 0 || position.y >= size.yLength
        if (outsideLimits.not()) {
            this.valueArray[(position.y * size.xLength) + position.x] = value
        }
    }

    fun getValueAtPosition(position: Position3D): Int {
        val outsideLimits = position.x < 0 || position.x >= size.xLength || position.y < 0 || position.y >= size.yLength
        return if (outsideLimits.not()) {
            valueArray[(position.y * size.xLength) + position.x]
        } else {
            Int.MAX_VALUE
        }
    }

    fun getValueAtCoords(x: Int, y: Int): Int {
        val outsideLimits = x < 0 || x >= size.xLength || y < 0 || y >= size.yLength
        return if (outsideLimits.not()) {
            valueArray[(y * size.xLength) + x]
        } else {
            Int.MAX_VALUE
        }
    }

    fun setValueArray(value: IntArray) {
        this.valueArray = value.copyOf()
    }

    fun getArray() : IntArray {
        return this.valueArray
    }

    fun resetValues(level: Int) {
        valueArray.fill(Int.MAX_VALUE)
        listOfTargetPositions
            .filter { it.z == level }
            .forEach {
                valueArray[(it.y * size.xLength) + it.x] = 0
        }
    }


}
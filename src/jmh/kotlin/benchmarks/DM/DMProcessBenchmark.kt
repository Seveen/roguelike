package benchmarks.DM

import com.necroworld.world.DecisionMap
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.openjdk.jmh.annotations.*
import java.util.concurrent.*

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
open class DMProcessBenchmark {
    lateinit var dm: DecisionMap

    val position3D = Position3D.create(10,20,1)
    val size3D = Size3D.create(200,100,2)
    val sizeArray = size3D.xLength * size3D.yLength

    val intArray = IntArray(sizeArray) {Int.MAX_VALUE}
    val level = 1

    @Setup
    fun init() {
        dm = DecisionMap(size3D) {true}
        dm.addTargetPosition(position3D)
    }

    @Benchmark
    fun processDM() {
        val process = dm.process(1)
    }

    /*
    fun benchFindLowestCardinal() {
        val process = dm.findLowestCardinalAround(position3D)
    }

    fun benchGetLowestNeighbor() {
        val process = dm.getLowestNeighbor(position3D)
    }

    fun benchGetValueAt() {
        val process = dm.getValueAtPosition(position3D)
    }

    fun benchResetValue() {
        val process = dm.resetValues(level)
    }

    fun benchSetValueArray() {
        val process = dm.setValueArray(intArray)
    }

    fun benchCreateArray() {
        IntArray(sizeArray) {Int.MAX_VALUE}
    }

    fun filterListOfPositions() {
        size3D.fetchPositions().filter { it.z == level }.forEach {
            val v = dm.getValueAtPosition(it)
            dm.getLowestNeighbor(it)

            intArray[it.y * size3D.xLength + it.x] = v
        }
    }

    fun filterArrayOfPositions() {
        for (y in 0 until size3D.yLength) {
            for (x in 0 until size3D.xLength) {
                val v = dm.getValueAtCoords(x, y)
                dm.getLowestNeighborAtCoords(x, y)

                intArray[y * size3D.xLength + x] = v
            }
        }
    }*/
}

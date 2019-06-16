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
        dm.process(1)
    }
}

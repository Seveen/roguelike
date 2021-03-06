package com.necroworld.world

import com.necroworld.attributes.Vision
import org.hexworks.amethyst.api.Engine
import org.hexworks.amethyst.api.Engines
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import com.necroworld.blocks.GameBlock
import com.necroworld.builders.GameBlockFactory
import com.necroworld.extensions.GameEntity
import com.necroworld.extensions.blocksVision
import com.necroworld.extensions.position
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.fold
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.game.GameArea
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.shape.EllipseFactory
import org.hexworks.zircon.api.shape.LineFactory
import org.hexworks.zircon.api.uievent.UIEvent

class World(startingBlocks: Map<Position3D, GameBlock>,
            visibleSize: Size3D,
            actualSize: Size3D) : GameArea<Tile, GameBlock> by GameAreaBuilder.newBuilder<Tile, GameBlock>()
                .withVisibleSize(visibleSize)
                .withActualSize(actualSize)
                .withDefaultBlock(DEFAULT_BLOCK)
                .withLayersPerBlock(1)
                .build() {

    private val engine: Engine<GameContext> = Engines.newEngine()
    //TODO: dissociate world + engine from area
    //The world should be an overmap + geopolitics
    //The areas should be what is rn the world
    //One engine for each area, or use the same engine and unload and load it ?

    private val decisionMaps: HashMap<String, DecisionMap> = hashMapOf()

    init {
        startingBlocks.forEach { (pos, block) ->
            setBlockAt(pos, block)
            block.entities.forEach { entity ->
                engine.addEntity(entity)
                entity.position = pos
            }
        }
        addDecisionMapWithTag("player",
            DecisionMap(actualSize) {
                val result = this.fetchBlockAt(it).map {block ->
                    block.isEmptyFloor
                }
                if (result.isPresent) {
                    result.get()
                } else false
            }
        )
    }

    /**
     * Adds the given [Entity] at the given [Position3D].
     * Has no effect if this world already contains the
     * given [Entity].
     */
    fun addEntity(entity: Entity<EntityType, GameContext>, position: Position3D) {
        entity.position = position
        engine.addEntity(entity)
        fetchBlockAt(position).map {
            it.addEntity(entity)
        }
    }

    fun removeEntity(entity: GameEntity<EntityType>) {
        fetchBlockAt(entity.position).map {
            it.removeEntity(entity)
        }
        engine.removeEntity(entity)
        entity.position = Position3D.unknown()
    }

    fun addAtEmptyPosition(entity: GameEntity<EntityType>,
                           offset: Position3D = Positions.default3DPosition(),
                           size: Size3D = actualSize()): Boolean {
        return findEmptyLocationWithin(offset, size).fold(
            whenEmpty = {
                false
            },
            whenPresent = { location ->
                addEntity(entity, location)
                true
            })

    }

    fun addWorldEntity(entity: Entity<EntityType, GameContext>) {
        engine.addEntity(entity)
    }

    fun addDecisionMapWithTag(tag: String, map: DecisionMap) {
        decisionMaps.put(tag, map)
    }

    fun getDecisionMapByTag(tag: String): DecisionMap? {
        return decisionMaps.get(tag)
    }

    /**
     * Finds an empty location within the given area (offset and size) on this [World].
     */
    fun findEmptyLocationWithin(offset: Position3D, size: Size3D): Maybe<Position3D> {
        var position = Maybe.empty<Position3D>()
        val maxTries = 10
        var currentTry = 0
        while (position.isPresent.not() && currentTry < maxTries) {
            val pos = Positions.create3DPosition(
                x = (Math.random() * size.xLength).toInt() + offset.x,
                y = (Math.random() * size.yLength).toInt() + offset.y,
                z = (Math.random() * size.zLength).toInt() + offset.z)
            fetchBlockAt(pos).map {
                if (it.isEmptyFloor) {
                    position = Maybe.of(pos)
                }
            }
            currentTry++
        }
        return position
    }

    fun update(screen: Screen, uiEvent: UIEvent, game: Game) {
        engine.update(GameContext(
            world = this,
            screen = screen,
            uiEvent = uiEvent
        ))
    }

    fun moveEntity(entity: GameEntity<EntityType>, position: Position3D): Boolean {
        var success = false
        val oldBlock = fetchBlockAt(entity.position)
        val newBlock = fetchBlockAt(position)

        if (bothBlocksPresent(oldBlock, newBlock)) {
            success = true
            oldBlock.get().removeEntity(entity)
            entity.position = position
            newBlock.get().addEntity(entity)
        }
        return success
    }

    fun isVisionBlockedAt(pos: Position3D): Boolean {
        return fetchBlockAt(pos).fold(whenEmpty = { false }, whenPresent = {
            it.entities.any(GameEntity<EntityType>::blocksVision)
        })
    }

    fun findVisiblePositionsFor(entity: GameEntity<EntityType>): Iterable<Position> {
        val centerPos = entity.position.to2DPosition()
        return entity.findAttribute(Vision::class).map { (radius) ->
            EllipseFactory.buildEllipse(
                fromPosition = centerPos,
                toPosition = centerPos.withRelativeX(radius).withRelativeY(radius))
                    .positions()
                    .flatMap { ringPos ->
                        val result = mutableListOf<Position>()
                        val iter = LineFactory.buildLine(centerPos, ringPos).iterator()
                        do {
                            val next = iter.next()
                            result.add(next)
                        } while (iter.hasNext() &&
                                isVisionBlockedAt(Position3D.from2DPosition(next, entity.position.z)).not())
                        result
                    }
        }.orElse(listOf())
    }

    private fun bothBlocksPresent(oldBlock: Maybe<GameBlock>, newBlock: Maybe<GameBlock>) =
        oldBlock.isPresent && newBlock.isPresent

    companion object {
        private val DEFAULT_BLOCK = GameBlockFactory.floor()
    }

    fun screenToWorldPosition(screenPosition:Position, screenOffset: Position) : Position {
        val worldOffset = this.visibleOffset().to2DPosition()
        return screenPosition.plus(worldOffset).minus(screenOffset)
    }
}

package com.necroworld.world

import com.necroworld.GameConfig
import com.necroworld.GameConfig.ARMOR_PER_LEVEL
import com.necroworld.GameConfig.BATS_PER_LEVEL
import com.necroworld.GameConfig.FUNGI_PER_LEVEL
import com.necroworld.GameConfig.WEAPONS_PER_LEVEL
import com.necroworld.GameConfig.WORLD_SIZE
import com.necroworld.GameConfig.ZIRCONS_PER_LEVEL
import com.necroworld.attributes.types.Player
import com.necroworld.builders.EntityFactory
import com.necroworld.extensions.GameEntity
import com.necroworld.extensions.position
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.impl.Size3D

class GameBuilder(val worldSize: Size3D) {

    private val visibleSize = Sizes.create3DSize(
        xLength = GameConfig.WINDOW_WIDTH - GameConfig.SIDEBAR_WIDTH,
        yLength = GameConfig.WINDOW_HEIGHT - GameConfig.LOG_AREA_HEIGHT,
        zLength = 1)

    val world = WorldBuilder(worldSize)
        .makeCaves()
        .build(visibleSize = visibleSize)

    fun buildGame(): Game {

        prepareWorld()

        val player = addPlayer()
        addFungi()
        addBats()
        addStalker()
        addKobold()
        addZircons()
        addWeapons()
        addArmor()

        val game = Game.create(
            player = player,
            world = world)

        world.addWorldEntity(EntityFactory.newFogOfWar(game))

        val firstDecisionMap = world.getDecisionMapByTag("player")
        firstDecisionMap?.addTargetPosition(player.position)
        firstDecisionMap?.process(player.position.z)

        return game
    }

    private fun prepareWorld() = also {
        world.scrollUpBy(world.actualSize().zLength)
    }

    private fun <T : EntityType> GameEntity<T>.addToWorld(
        atLevel: Int,
        atArea: Size = world.actualSize().to2DSize()): GameEntity<T> {
            world.addAtEmptyPosition(this,
                offset = Positions.default3DPosition().withZ(atLevel),
                size = Size3D.from2DSize(atArea))
        return this
    }

    private fun addPlayer(): GameEntity<Player> {
        return EntityFactory.newPlayer().addToWorld(
            atLevel = GameConfig.DUNGEON_LEVELS - 1,
            atArea = world.visibleSize().to2DSize())
    }

    private fun addFungi() = also {
        repeat(world.actualSize().zLength) { level ->
            repeat(FUNGI_PER_LEVEL) {
                EntityFactory.newFungus().addToWorld(level)
            }
        }
    }

    private fun addBats() = also {
        repeat(world.actualSize().zLength) { level ->
            repeat(BATS_PER_LEVEL) {
                EntityFactory.newBat().addToWorld(level)
            }
        }
    }

    private fun addStalker() = also {
        repeat(world.actualSize().zLength) { level ->
            EntityFactory.newStalker().addToWorld(level)
        }
    }

    private fun addKobold() = also {
        repeat(world.actualSize().zLength) { level ->
            repeat(10) {
                EntityFactory.newKobold().addToWorld(level)
            }
        }
    }

    private fun addZircons() = also {
        repeat(world.actualSize().zLength) { level ->
            repeat(ZIRCONS_PER_LEVEL) {
                EntityFactory.newZircon().addToWorld(level)
            }
        }
    }

    private fun addWeapons() = also {
        repeat(world.actualSize().zLength) { level ->
            repeat(WEAPONS_PER_LEVEL) {
                EntityFactory.newRandomWeapon().addToWorld(level)
            }
        }
    }

    private fun addArmor() = also {
        repeat(world.actualSize().zLength) { level ->
            repeat(ARMOR_PER_LEVEL) {
                EntityFactory.newRandomArmor().addToWorld(level)
            }
        }
    }

    companion object {

        fun defaultGame() = GameBuilder(
            worldSize = WORLD_SIZE).buildGame()
    }
}
package com.necroworld.entities

import com.necroworld.attributes.types.FogOfWarType
import com.necroworld.builders.GameColors
import com.necroworld.builders.GameTileRepository
import com.necroworld.extensions.position
import com.necroworld.functions.logGameEvent
import com.necroworld.world.Game
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.base.BaseEntity
import org.hexworks.zircon.api.Layers
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.graphics.Layer
import java.util.concurrent.ConcurrentHashMap

class FogOfWar(game: Game) : BaseEntity<FogOfWarType, GameContext>(FogOfWarType) {

    private val world = game.world
    private val player = game.player
    private val size = game.world.actualSize()

    private val DMPerLevel = ConcurrentHashMap<Int, Layer>().also { fows ->
        repeat(size.zLength) { level ->
            val fow = Layers.newBuilder()
                .withSize(size.to2DSize())
                .build()
                .fill(GameTileRepository.DM)
            fows[level] = fow
//            world.pushOverlayAt(fow, level)
        }
    }

    private val fowPerLevel = ConcurrentHashMap<Int, Layer>().also { fows ->
        repeat(size.zLength) { level ->
            val fow = Layers.newBuilder()
                .withSize(size.to2DSize())
                .build()
                .fill(GameTileRepository.UNREVEALED)
            fows[level] = fow
            world.pushOverlayAt(fow, level)
        }
    }

    init {
        updateFOW()
//        updateDM()
    }

    override fun update(context: GameContext): Boolean {
        updateFOW()
//        updateDM()
        return true
    }

    private fun updateFOW() {
        world.findVisiblePositionsFor(player).forEach {
            fowPerLevel[player.position.z]?.setTileAt(it, GameTileRepository.EMPTY)
        }

    }

       private fun updateDM() {
        DMPerLevel[player.position.z]?.fill(GameTileRepository.DM)
        world.actualSize().fetchPositions().filter{ it.z == player.position.z}.forEach {
            var nber = world.getDecisionMapByTag("player")?.getInvertedValueAtPosition(it) ?: Double.MAX_VALUE
            var char = 'F'
            var color: TileColor
            var fgcolor: TileColor = TileColor.fromString("#FFFFFF")
            when {
                nber == 0.0 -> {
                    color = TileColor.create(0,0,0,63)
                    //char = ' '
                }
                nber < 5.0 && nber > -5.0 -> {
                    color = TileColor.create(255,0,0,63)
                    char = (Math.round(Math.abs(nber)) + '0'.toInt()).toChar()
                }
                nber < 10.0 && nber > -10.0 -> {
                    color = TileColor.create(127,0,127,63)
                    char = (Math.round(Math.abs(nber)) + '0'.toInt()).toChar()
                }
                nber < 16.0 && nber > -16.0 -> {
                    color = TileColor.create(0,0,255,63)
                    char = (Math.round(Math.abs(nber)) - 10.0 + 'A'.toInt()).toChar()
                }
                nber == Double.MAX_VALUE -> {
                    color = TileColor.create(0,0,0,63)
                    char = '#'
                }
                else -> {
                    color = TileColor.create(255,0,255,63)
                    char = 'F'
                }
            }
            when {
                nber < 0 -> {
                    fgcolor = TileColor.fromString("#000000")
                }
            }

            val newTile = Tiles.newBuilder()
                .withCharacter(char)
                .withForegroundColor(fgcolor)
                .withBackgroundColor(color)
                .buildCharacterTile()
            DMPerLevel[player.position.z]?.setTileAt(it.to2DPosition(), newTile)
        }
    }


}
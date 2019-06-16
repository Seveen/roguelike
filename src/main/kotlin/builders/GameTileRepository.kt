package com.necroworld.builders

import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.graphics.Symbols

object GameTileRepository {

    val EMPTY: CharacterTile = Tiles.empty()

    val UNREVEALED = Tiles.newBuilder()
        .withCharacter(' ')
        .withBackgroundColor(GameColors.UNREVEALED_COLOR)
        .buildCharacterTile()

    val DM = Tiles.newBuilder()
        .withCharacter(' ')
        .withBackgroundColor(TileColor.transparent())
        .buildCharacterTile()

    /*val NOT_IN_SIGHT = Tiles.newBuilder()
        .withCharacter(' ')
        .withBackgroundColor(GameColors.NOT_IN_SIGHT_COLOR)
        .buildCharacterTile()*/

    val FLOOR: CharacterTile = Tiles.newBuilder()
        .withCharacter(Symbols.INTERPUNCT)
        .withForegroundColor(GameColors.FLOOR_FOREGROUND)
        .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
        .buildCharacterTile()

    val WALL: CharacterTile = Tiles.newBuilder()
        .withCharacter('#')
        .withForegroundColor(GameColors.WALL_FOREGROUND)
        .withBackgroundColor(GameColors.WALL_BACKGROUND)
        .buildCharacterTile()

    val STAIRS_UP: CharacterTile = Tiles.newBuilder()
        .withCharacter('<')
        .withForegroundColor(GameColors.ACCENT_COLOR)
        .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
        .buildCharacterTile()

    val STAIRS_DOWN: CharacterTile = Tiles.newBuilder()
        .withCharacter('>')
        .withForegroundColor(GameColors.ACCENT_COLOR)
        .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
        .buildCharacterTile()

    val PLAYER: CharacterTile = Tiles.newBuilder()
        .withCharacter('@')
        .withForegroundColor(GameColors.ACCENT_COLOR)
        .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
        .buildCharacterTile()

    val FUNGUS: CharacterTile = Tiles.newBuilder()
        .withCharacter('f')
        .withForegroundColor(GameColors.FUNGUS_COLOR)
        .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
        .buildCharacterTile()

    val BAT: CharacterTile = Tiles.newBuilder()
        .withCharacter('b')
        .withForegroundColor(GameColors.BAT_COLOR)
        .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
        .buildCharacterTile()

    val STALKER: CharacterTile = Tiles.newBuilder()
        .withCharacter('S')
        .withForegroundColor(GameColors.STALKER_COLOR)
        .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
        .buildCharacterTile()

    val KOBOLD: CharacterTile = Tiles.newBuilder()
        .withCharacter('k')
        .withForegroundColor(GameColors.KOBOLD_COLOR)
        .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
        .buildCharacterTile()

    val ZIRCON: CharacterTile = Tiles.newBuilder()
        .withCharacter(',')
        .withForegroundColor(GameColors.ZIRCON_COLOR)
        .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
        .buildCharacterTile()
}

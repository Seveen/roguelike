package com.necroworld.builders

import org.hexworks.zircon.api.TileColors
import org.hexworks.zircon.api.color.TileColor

object GameColors {
    val WALL_FOREGROUND = TileColors.fromString("#75715E")
    val WALL_BACKGROUND = TileColors.fromString("#3E3D32")

    val FLOOR_FOREGROUND = TileColors.fromString("#75715E")
    val FLOOR_BACKGROUND = TileColors.fromString("#1e2320")

    val ACCENT_COLOR = TileColors.fromString("#FFCD22")
    val UNREVEALED_COLOR = TileColors.fromString("#000000")
    //val NOT_IN_SIGHT_COLOR = TileColor.create(127,127,127,64)

    val FUNGUS_COLOR = TileColors.fromString("#85DD1B")
    val BAT_COLOR = TileColors.fromString("#2348b2")
    val STALKER_COLOR = TileColors.fromString("#FF2222")

    val ZIRCON_COLOR = TileColors.fromString("#dddddd")

    val COLD = TileColor.create(0,0,255,63)
}

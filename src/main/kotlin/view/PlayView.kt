package com.necroworld.view

import com.necroworld.GameConfig
import com.necroworld.blocks.GameBlock
import com.necroworld.events.GameLogEvent
import com.necroworld.view.fragment.PlayerStatsFragment
import com.necroworld.world.Game
import com.necroworld.world.GameBuilder
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.GameComponents
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.extensions.box
import org.hexworks.zircon.api.extensions.handleKeyboardEvents
import org.hexworks.zircon.api.game.ProjectionMode
import org.hexworks.zircon.api.mvc.base.BaseView
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.internal.Zircon

class PlayView(private val game: Game = GameBuilder.defaultGame()) : BaseView() {

    override val theme = GameConfig.THEME

    override fun onDock() {
        screen.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { event, _ ->
            game.world.update(screen, event, game)
            Processed
        }

        val sidebar = Components.panel()
            .withSize(GameConfig.SIDEBAR_WIDTH, GameConfig.WINDOW_HEIGHT)
            .withDecorations(box())
            .build()
        sidebar.addFragment(PlayerStatsFragment(
            width = sidebar.contentSize.width,
            player = game.player
        ))

        screen.addComponent(sidebar)

        val logArea = Components.logArea()
            .withDecorations(box(title = "Log"))
            .withSize(GameConfig.WINDOW_WIDTH - GameConfig.SIDEBAR_WIDTH, GameConfig.LOG_AREA_HEIGHT)
            .withAlignmentWithin(screen, ComponentAlignment.BOTTOM_RIGHT)
            .build()
        screen.addComponent(logArea)

        Zircon.eventBus.subscribe<GameLogEvent> { (text) ->
            logArea.addParagraph(
                paragraph = text,
                withNewLine = false,
                withTypingEffectSpeedInMs = 10
            )
        }

        val gameComponent = GameComponents.newGameComponentBuilder<Tile, GameBlock>()
            .withGameArea(game.world)
            .withVisibleSize(game.world.visibleSize())
            .withProjectionMode(ProjectionMode.TOP_DOWN)
            .withAlignmentWithin(screen, ComponentAlignment.TOP_RIGHT)
            .build()
        screen.addComponent(gameComponent)
    }
}

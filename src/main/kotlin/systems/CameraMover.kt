package com.necroworld.systems

import com.necroworld.commands.MoveCamera
import com.necroworld.extensions.GameCommand
import com.necroworld.extensions.position
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object CameraMover : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>) =
            command.responseWhenCommandIs(MoveCamera::class) { cmd ->
                val (context, source, previousPosition) = cmd
                val world = context.world
                val screenPos = source.position - world.visibleOffset()
                val halfHeight = world.visibleSize().yLength / 2
                val halfWidth = world.visibleSize().xLength / 2
                val currentPosition = source.position
                when {
                    previousPosition.y > currentPosition.y && screenPos.y < halfHeight -> {
                        world.scrollOneBackward()
                    }
                    previousPosition.y < currentPosition.y && screenPos.y > halfHeight -> {
                        world.scrollOneForward()
                    }
                    previousPosition.x > currentPosition.x && screenPos.x < halfWidth -> {
                        world.scrollOneLeft()
                    }
                    previousPosition.x < currentPosition.x && screenPos.x > halfWidth -> {
                        world.scrollOneRight()
                    }
                }
                Consumed
            }
}
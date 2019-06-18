package com.necroworld.commands

import com.necroworld.attributes.types.Spellcaster
import com.necroworld.extensions.GameCommand
import com.necroworld.extensions.GameEntity
import com.necroworld.spells.Spell
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.entity.EntityType

data class SelectTarget(override val context: GameContext,
                        override val source: GameEntity<Spellcaster>,
                        val spell: Spell
) : GameCommand<EntityType>
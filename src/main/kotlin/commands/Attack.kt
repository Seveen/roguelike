package com.necroworld.commands

import com.necroworld.attributes.types.Combatant
import com.necroworld.extensions.GameEntity
import com.necroworld.world.GameContext
import org.hexworks.amethyst.api.entity.EntityType

data class Attack(override val context: GameContext,
                  override val source: GameEntity<Combatant>,
                  override val target: GameEntity<Combatant>) : EntityAction<Combatant, Combatant>
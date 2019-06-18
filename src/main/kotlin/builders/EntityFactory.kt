package com.necroworld.builders

import com.necroworld.attributes.*
import org.hexworks.amethyst.api.Entities
import org.hexworks.amethyst.api.builder.EntityBuilder
import org.hexworks.amethyst.api.entity.EntityType
import com.necroworld.attributes.flags.BlockOccupier
import com.necroworld.attributes.flags.VisionBlocker
import com.necroworld.attributes.types.*
import com.necroworld.commands.Attack
import com.necroworld.commands.Dig
import com.necroworld.commands.EntityAction
import com.necroworld.entities.FogOfWar
import com.necroworld.systems.*
import com.necroworld.world.Game
import com.necroworld.world.GameContext
import org.hexworks.zircon.api.GraphicalTilesetResources
import org.hexworks.zircon.api.Tiles

fun <T : EntityType> newGameEntityOfType(type: T, init: EntityBuilder<T, GameContext>.() -> Unit) =
    Entities.newEntityOfType(type, init)

object EntityFactory {

    fun newPlayer() = newGameEntityOfType(Player) {
        attributes(
            EntityPosition(),
            BlockOccupier,
            EntityTile(GameTileRepository.PLAYER),
            EntityActions(Dig::class, Attack::class),
            CombatStats.create(
                maxHp = 100,
                attackValue = 10,
                defenseValue = 5
            ),
            SpellcastStats.create(
                maxMana = 1000
            ),
            Vision(9),
            Faction(PlayerFaction),
            Inventory(10),
            DecisionMapTarget("player")
        )
        behaviors(InputReceiver)
        facets(TargetPicker, Spellcastable, Movable, CameraMover, StairClimber, StairDescender, Attackable, Destructible, ItemPicker, ItemDropper, InventoryInspector, DecisionMapUpdater)
    }

    fun newFogOfWar(game: Game) = FogOfWar(game)

    fun newWall() = newGameEntityOfType(Wall) {
        attributes(
            EntityPosition(),
            BlockOccupier,
            EntityTile(GameTileRepository.WALL),
            VisionBlocker
        )
        facets(Diggable)
    }

    fun newStairsUp() = newGameEntityOfType(StairsUp) {
        attributes(EntityTile(GameTileRepository.STAIRS_UP),
            EntityPosition())
    }

    fun newStairsDown() = newGameEntityOfType(StairsDown) {
        attributes(EntityTile(GameTileRepository.STAIRS_DOWN),
            EntityPosition())
    }

    fun newFungus(fungusSpread: FungusSpread = FungusSpread()) = newGameEntityOfType(Fungus) {
        attributes(BlockOccupier,
            EntityPosition(),
            EntityTile(GameTileRepository.FUNGUS),
            fungusSpread,
            CombatStats.create(
                maxHp = 10,
                attackValue = 0,
                defenseValue = 0
            ),
            Faction(CaveDwellersFaction)
        )
        facets(Attackable, Destructible)
        behaviors(FungusGrowth)
    }

    fun newBat() = newGameEntityOfType(Bat) {
        attributes(BlockOccupier,
            EntityPosition(),
            EntityTile(GameTileRepository.BAT),
            CombatStats.create(
                maxHp = 5,
                attackValue = 2,
                defenseValue = 1),
            EntityActions(Attack::class),
            Faction(CaveDwellersFaction),
            Inventory(1).apply {
                addItem(newZircon())
            }
        )
        facets(Movable, Attackable, ItemDropper, LootDropper, Destructible)
        behaviors(Wanderer)
    }

    fun newStalker() = newGameEntityOfType(Stalker) {
        attributes(BlockOccupier,
            EntityPosition(),
            EntityTile(GameTileRepository.STALKER),
            CombatStats.create(
                maxHp = 150,
                attackValue = 4,
                defenseValue = 1),
            EntityActions(Attack::class),
            Faction(CaveDwellersFaction),
            Inventory(1).apply {
                addItem(newZircon())
            },
            DecisionMapUser(hashMapOf("player" to 1.0))
        )
        facets(Movable, Attackable, ItemDropper, LootDropper, Destructible)
        behaviors(TargetFollower)
    }

    fun newKobold() = newGameEntityOfType(Kobold) {
        attributes(BlockOccupier,
            EntityPosition(),
            EntityTile(GameTileRepository.KOBOLD),
            CombatStats.create(
                maxHp = 10,
                attackValue = 4,
                defenseValue = 1),
            EntityActions(Attack::class),
            Faction(CaveDwellersFaction),
            Inventory(1).apply {
                addItem(newZircon())
            },
            DecisionMapUser(hashMapOf("player" to 1.0))
        )
        facets(Movable, Attackable, ItemDropper, LootDropper, Destructible)
        behaviors(TargetFleer)
    }

    fun newZircon() = newGameEntityOfType(Zircon) {
        attributes(ItemIcon(Tiles.newBuilder()
            .withName("white gem")
            .withTileset(GraphicalTilesetResources.nethack16x16())
            .buildGraphicTile()),
            EntityPosition(),
            EntityTile(GameTileRepository.ZIRCON))
    }
}
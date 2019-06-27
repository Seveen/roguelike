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
import com.necroworld.extensions.GameEntity
import com.necroworld.systems.*
import com.necroworld.world.Game
import com.necroworld.world.GameContext
import org.hexworks.zircon.api.GraphicalTilesetResources
import org.hexworks.zircon.api.Tiles
import kotlin.random.Random


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
            DecisionMapTarget("player"),
            Equipment(
                initialWeapon = newClub(),
                initialArmor = newJacket())
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

    fun newDagger() = newGameEntityOfType(Dagger) {
        attributes(ItemIcon(Tiles.newBuilder()
            .withName("Dagger")
            .withTileset(GraphicalTilesetResources.nethack16x16())
            .buildGraphicTile()),
            EntityPosition(),
            ItemCombatStats(
                attackValue = 4,
                combatItemType = "Weapon"),
            EntityTile(GameTileRepository.DAGGER))
    }

    fun newSword() = newGameEntityOfType(Sword) {
        attributes(ItemIcon(Tiles.newBuilder()
            .withName("Short sword")
            .withTileset(GraphicalTilesetResources.nethack16x16())
            .buildGraphicTile()),
            EntityPosition(),
            ItemCombatStats(
                attackValue = 6,
                combatItemType = "Weapon"),
            EntityTile(GameTileRepository.SWORD))
    }

    fun newStaff() = newGameEntityOfType(Staff) {
        attributes(ItemIcon(Tiles.newBuilder()
            .withName("staff")
            .withTileset(GraphicalTilesetResources.nethack16x16())
            .buildGraphicTile()),
            EntityPosition(),
            ItemCombatStats(
                attackValue = 4,
                defenseValue = 2,
                combatItemType = "Weapon"),
            EntityTile(GameTileRepository.STAFF))
    }

    fun newLightArmor() = newGameEntityOfType(LightArmor) {
        attributes(ItemIcon(Tiles.newBuilder()
            .withName("Leather armor")
            .withTileset(GraphicalTilesetResources.nethack16x16())
            .buildGraphicTile()),
            EntityPosition(),
            ItemCombatStats(
                defenseValue = 2,
                combatItemType = "Armor"),
            EntityTile(GameTileRepository.LIGHT_ARMOR))
    }

    fun newMediumArmor() = newGameEntityOfType(MediumArmor) {
        attributes(ItemIcon(Tiles.newBuilder()
            .withName("Chain mail")
            .withTileset(GraphicalTilesetResources.nethack16x16())
            .buildGraphicTile()),
            EntityPosition(),
            ItemCombatStats(
                defenseValue = 3,
                combatItemType = "Armor"),
            EntityTile(GameTileRepository.MEDIUM_ARMOR))
    }

    fun newHeavyArmor() = newGameEntityOfType(HeavyArmor) {
        attributes(ItemIcon(Tiles.newBuilder()
            .withName("Plate mail")
            .withTileset(GraphicalTilesetResources.nethack16x16())
            .buildGraphicTile()),
            EntityPosition(),
            ItemCombatStats(
                defenseValue = 4,
                combatItemType = "Armor"),
            EntityTile(GameTileRepository.HEAVY_ARMOR))
    }

    fun newClub() = newGameEntityOfType(Club) {
        attributes(ItemCombatStats(combatItemType = "Weapon"),
            EntityTile(GameTileRepository.CLUB),
            EntityPosition(),
            ItemIcon(Tiles.newBuilder()
                .withName("Club")
                .withTileset(GraphicalTilesetResources.nethack16x16())
                .buildGraphicTile()))
    }

    fun newJacket() = newGameEntityOfType(Jacket) {
        attributes(ItemCombatStats(combatItemType = "Armor"),
            EntityTile(GameTileRepository.JACKET),
            EntityPosition(),
            ItemIcon(Tiles.newBuilder()
                .withName("Leather jacket")
                .withTileset(GraphicalTilesetResources.nethack16x16())
                .buildGraphicTile()))
    }

    fun newRandomWeapon(): GameEntity<Weapon> = when (Random.nextInt(3)) {
        0 -> newDagger()
        1 -> newSword()
        else -> newStaff()
    }

    fun newRandomArmor(): GameEntity<Armor> = when (Random.nextInt(3)) {
        0 -> newLightArmor()
        1 -> newMediumArmor()
        else -> newHeavyArmor()
    }
}
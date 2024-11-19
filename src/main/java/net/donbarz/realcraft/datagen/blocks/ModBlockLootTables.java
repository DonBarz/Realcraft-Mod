package net.donbarz.realcraft.datagen.blocks;

import net.donbarz.realcraft.block.ModBlocks;
import net.donbarz.realcraft.item.ModItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModBlockLootTables extends BlockLootSubProvider {

    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.add(ModBlocks.IRON_ORE_LOW.get(),
                block -> createAdvancedOreBuilder(ModBlocks.IRON_ORE_LOW.get(), ModItems.RAW_IRON_DUST.get(), 0.1f));
        this.add(ModBlocks.IRON_ORE_MEDIUM.get(),
                block -> createAdvancedOreBuilder(ModBlocks.IRON_ORE_MEDIUM.get(), ModItems.RAW_IRON_DUST.get(), 0.25f));
        //this.add(Blocks.IRON_ORE,block -> createAdvancedOreBuilder(Blocks.IRON_ORE, ModItems.RAW_IRON_DUST.get(), 0.5f));;

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Stream.of(
                ModBlocks.SIMPLE_BLOCKS.getEntries().stream().map(RegistryObject::get),
                ModBlocks.ADVANCED_ORE_BLOCKS.getEntries().stream().map(RegistryObject::get),
                ModBlocks.ADVANCED_BLOCKS.getEntries().stream().map(RegistryObject::get)
        ).flatMap(stream -> stream).collect(Collectors.toList());
    }

    protected LootTable.Builder createAdvancedOreBuilder(Block block, Item item, float chance) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1)) // Ensures only one item drops
                        .add(LootItem.lootTableItem(item)
                                .when(LootItemRandomChanceCondition.randomChance(chance))
                        )
                        .add(LootItem.lootTableItem(ModItems.LOOSE_STONE.get()))
                        .add(LootItem.lootTableItem(ForgeRegistries.ITEMS.getValue(ForgeRegistries.BLOCKS.getKey(ModBlocks.GRAVEL_LAYERS.get()))))
                );
    }


}

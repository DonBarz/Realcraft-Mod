package net.donbarz.realcraft.datagen.loottables;

import net.donbarz.realcraft.RealCraft;
import net.donbarz.realcraft.block.ModBlocks;
import net.donbarz.realcraft.item.ModItems;
import net.donbarz.realcraft.loot.AddLootModifier;
import net.donbarz.realcraft.loot.OverrideLootModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.registries.ForgeRegistries;

public class ModGlobalLootModifiersProv extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProv(PackOutput output) {
        super(output, RealCraft.MOD_ID);
    }

    @Override
    protected void start() {
        blockRandomAmountOverride(Blocks.COBBLESTONE,4,9,0.5f,ModItems.LOOSE_STONE.get());
        blockRandomAmountOverride(Blocks.GRAVEL,4,8,0.3f, ForgeRegistries.ITEMS.getValue(ForgeRegistries.BLOCKS.getKey(ModBlocks.GRAVEL_LAYERS.get())));
        blockRandomAmountAdd(Blocks.GRAVEL,0,1,0.1f, Items.FLINT);
    }

    void blockRandomAmountOverride(Block block, int min, int max, float chance, Item item) {
        add(ForgeRegistries.ITEMS.getKey(item).getPath() + "_from_" + ForgeRegistries.BLOCKS.getKey(block).getPath() + "_replace", new OverrideLootModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).build(),
        }, item, min));
        for (int i = min; i < max; i++) {
            add(ForgeRegistries.ITEMS.getKey(item).getPath() + "_from_" + ForgeRegistries.BLOCKS.getKey(block).getPath() + "_" + (i-min+1), new AddLootModifier(new LootItemCondition[]{
                    LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).build(),
                    LootItemRandomChanceCondition.randomChance(chance).build()
            }, item, 1));
        }
    }
    void blockRandomAmountAdd(Block block, int min, int max, float chance, Item item) {
        if (min > 0){
            add(ForgeRegistries.ITEMS.getKey(item).getPath() + "_from_" + ForgeRegistries.BLOCKS.getKey(block).getPath() + "_0", new AddLootModifier(new LootItemCondition[]{
                    LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).build(),
            }, item, min));
        }
        for (int i = min; i < max; i++) {
            add(ForgeRegistries.ITEMS.getKey(item).getPath() + "_from_" + ForgeRegistries.BLOCKS.getKey(block).getPath() + "_" + (i-min+1), new AddLootModifier(new LootItemCondition[]{
                    LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).build(),
                    LootItemRandomChanceCondition.randomChance(chance).build()
            }, item, 1));
        }
    }
}

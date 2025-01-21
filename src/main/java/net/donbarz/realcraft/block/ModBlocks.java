package net.donbarz.realcraft.block;

import net.donbarz.realcraft.item.ModItems;
import net.donbarz.realcraft.RealCraft;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> ADVANCED_ORE_BLOCKS
            = DeferredRegister.create(ForgeRegistries.BLOCKS, RealCraft.MOD_ID);
    public static final DeferredRegister<Block> SIMPLE_BLOCKS
            = DeferredRegister.create(ForgeRegistries.BLOCKS, RealCraft.MOD_ID);
    public static final DeferredRegister<Block> ADVANCED_BLOCKS
            = DeferredRegister.create(ForgeRegistries.BLOCKS, RealCraft.MOD_ID);
    public static final DeferredRegister<Block> LAYERED_BLOCKS
            = DeferredRegister.create(ForgeRegistries.BLOCKS, RealCraft.MOD_ID);

    public static final RegistryObject<Block> IRON_ORE_LOW
            = registerAdvancedOreBlock("iron_ore_low",() ->
            new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .sound(SoundType.STONE)
            ));

    public static final RegistryObject<Block> IRON_ORE_MEDIUM
            = registerAdvancedOreBlock("iron_ore_medium",() ->
            new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .sound(SoundType.STONE)
            ));

    public static final RegistryObject<Block> IRON_ORE_HIGH
            = registerAdvancedOreBlock("iron_ore_high",() ->
            new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .sound(SoundType.STONE)
                    .noLootTable()
            ));

    public static final RegistryObject<Block> GRAVEL_LAYERS
            = registerLayeredBlock("gravel_layers", () ->
            new GravityLayersBlock(BlockBehaviour.Properties.copy(Blocks.GRAVEL)
            ),"gravel");

    //public static final RegistryObject<Block> DIRT_LAYERS
            //= registerLayeredBlock("dirt_layers", () ->
            //new GravelLayersBlock(BlockBehaviour.Properties.copy(Blocks.DIRT)
                    //.noLootTable()
            //),"dirt");

    private static <T extends Block>RegistryObject<T> registerSimpleBlock(String name, Supplier<T> block){
        RegistryObject<T> blockObj = SIMPLE_BLOCKS.register(name, block);
        registerBlockItem(name, blockObj);
        return blockObj;
    }

    private static <T extends Block>RegistryObject<T> registerAdvancedOreBlock(String name, Supplier<T> block){
        RegistryObject<T> blockObj = ADVANCED_ORE_BLOCKS.register(name, block);
        registerAdvancedItem(name, blockObj);
        return blockObj;
    }

    private static <T extends Block>RegistryObject<T> registerAdvancedBlock(String name, Supplier<T> block, String itemName){
        RegistryObject<T> blockObj = ADVANCED_BLOCKS.register(name, block);
        registerSimpleItem(itemName, blockObj);
        return blockObj;
    }

    private static <T extends Block>RegistryObject<T> registerLayeredBlock(String name, Supplier<T> block, String itemName){
        RegistryObject<T> blockObj = LAYERED_BLOCKS.register(name, block);
        registerSimpleItem(itemName, blockObj);
        return blockObj;
    }

    private static <T extends Block>RegistryObject<Item> registerSimpleItem(String name, RegistryObject<T> block){
        return ModItems.SIMPLE_ITEMS.register(name, () -> new BlockItem(block.get(),new Item.Properties()));
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block){
        return ModItems.BLOCK_ITEMS.register(name, () -> new BlockItem(block.get(),new Item.Properties()));
    }

    private static <T extends Block>RegistryObject<Item> registerAdvancedItem(String name, RegistryObject<T> block){
        return ModItems.BLOCK_ITEMS.register(name, () -> new BlockItem(block.get(),new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        ADVANCED_ORE_BLOCKS.register(eventBus);
        SIMPLE_BLOCKS.register(eventBus);
        ADVANCED_BLOCKS.register(eventBus);
        LAYERED_BLOCKS.register(eventBus);
    }
}

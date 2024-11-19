package net.donbarz.realcraft.item;

import net.donbarz.realcraft.RealCraft;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> SIMPLE_ITEMS
            = DeferredRegister.create(ForgeRegistries.ITEMS, RealCraft.MOD_ID);

    public static final DeferredRegister<Item> BLOCK_ITEMS
            = DeferredRegister.create(ForgeRegistries.ITEMS, RealCraft.MOD_ID);

    public static final DeferredRegister<Item> ADVANCED_ITEMS
            = DeferredRegister.create(ForgeRegistries.ITEMS, RealCraft.MOD_ID);

    public static final RegistryObject<LooseIronOreItem> LOOSE_IRON_ORE = SIMPLE_ITEMS.register("loose_iron_ore",
            () -> new LooseIronOreItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> RAW_IRON_DUST = SIMPLE_ITEMS.register("raw_iron_dust",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LOOSE_STONE = SIMPLE_ITEMS.register("loose_stone",
            () -> new Item(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> BAR_MOLD = SIMPLE_ITEMS.register("bar_mold",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CLAY_BAR_MOLD = SIMPLE_ITEMS.register("clay_bar_mold",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> IRON_BAR_MOLD = SIMPLE_ITEMS.register("iron_bar_mold",
            () -> new Item(new Item.Properties().stacksTo(1)));


    public static void register(IEventBus bus) {
        SIMPLE_ITEMS.register(bus);
        BLOCK_ITEMS.register(bus);
        ADVANCED_ITEMS.register(bus);
    }

}
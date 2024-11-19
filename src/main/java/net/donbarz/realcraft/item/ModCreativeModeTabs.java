package net.donbarz.realcraft.item;

import net.donbarz.realcraft.RealCraft;
import net.donbarz.realcraft.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS
            = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RealCraft.MOD_ID);

    public static final RegistryObject<CreativeModeTab> EXAMPLE_CREATIVE_MODE_TAB
            = CREATIVE_MODE_TABS.register("example_creative_tab",() -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.LOOSE_IRON_ORE.get()))
            .title(Component.translatable("creativetab.example_creative_tab"))
            .displayItems((pParameters, pOutout) -> {
                pOutout.accept(ModBlocks.IRON_ORE_LOW.get());
                pOutout.accept(ModBlocks.IRON_ORE_MEDIUM.get());
                pOutout.accept(ModBlocks.GRAVEL_LAYERS.get());
                pOutout.accept(ModItems.LOOSE_IRON_ORE.get());
                pOutout.accept(ModItems.RAW_IRON_DUST.get());
                pOutout.accept(ModItems.LOOSE_STONE.get());
                pOutout.accept(ModItems.CLAY_BAR_MOLD.get());
                pOutout.accept(ModItems.BAR_MOLD.get());
                pOutout.accept(ModItems.IRON_BAR_MOLD.get());
            })
            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}

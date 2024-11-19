package net.donbarz.realcraft.datagen;

import net.donbarz.realcraft.RealCraft;
import net.donbarz.realcraft.datagen.blocks.ModBlockStateProv;
import net.donbarz.realcraft.datagen.blocks.ModBlockTagGen;
import net.donbarz.realcraft.datagen.items.ModItemModelProv;
import net.donbarz.realcraft.datagen.items.ModItemTagGen;
import net.donbarz.realcraft.datagen.loottables.ModGlobalLootModifiersProv;
import net.donbarz.realcraft.datagen.loottables.ModLootTableProv;
import net.donbarz.realcraft.datagen.recipes.ModRecipeProv;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = RealCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGens {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput packOutput = gen.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupPrivider = event.getLookupProvider();

        gen.addProvider(event.includeServer(), new ModRecipeProv(packOutput));
        gen.addProvider(event.includeServer(), ModLootTableProv.create(packOutput));

        gen.addProvider(event.includeClient(), new ModBlockStateProv(packOutput, existingFileHelper));
        gen.addProvider(event.includeClient(), new ModItemModelProv(packOutput, existingFileHelper));

        ModBlockTagGen blockTagGen = gen.addProvider(event.includeServer(), new ModBlockTagGen(packOutput, lookupPrivider, existingFileHelper));
        gen.addProvider(event.includeServer(), new ModItemTagGen(packOutput, lookupPrivider, blockTagGen.contentsGetter(), existingFileHelper));

        gen.addProvider(event.includeServer(), new ModGlobalLootModifiersProv(packOutput));
    }
}

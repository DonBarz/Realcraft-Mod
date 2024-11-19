package net.donbarz.realcraft;

import com.mojang.logging.LogUtils;
import net.donbarz.realcraft.block.ModBlocks;
import net.donbarz.realcraft.datagen.recipes.ModRecipes;
import net.donbarz.realcraft.item.ModCreativeModeTabs;
import net.donbarz.realcraft.item.ModItems;
import net.donbarz.realcraft.loot.ModLootModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.io.FileWriter;
import java.io.IOException;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(RealCraft.MOD_ID)
public class RealCraft {

    public static final String MOD_ID = "realcraft";
    private static final Logger LOGGER = LogUtils.getLogger();

    public RealCraft (FMLJavaModLoadingContext context){

        ModCreativeModeTabs.register(context.getModEventBus());
        ModItems.register(context.getModEventBus());
        ModBlocks.register(context.getModEventBus());
        ModLootModifier.register(context.getModEventBus());

        MinecraftForge.EVENT_BUS.register(this);

        context.getModEventBus().addListener(this::commonSetup);
        context.getModEventBus().addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event){
        if (event.getTabKey()== CreativeModeTabs.INGREDIENTS){
            event.accept(ModItems.LOOSE_IRON_ORE);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event){
    MinecraftServer server = event.getServer();

        if (server != null) {

            //GPT be cookin'
            //only use before new datagen
            //returns all vanilla recipes in an array-ly fashion when starting the server...
            //server.getRecipeManager().getRecipes().forEach((Recipe<?> recipe) -> {
                //ResourceLocation recipeId = recipe.getId();
                //logRecipeId(recipeId.toString());
            //});
            //for (String id : ModRecipes.allVanillaRecipeIds) System.out.println(id);
        }
    }

    // |
    // V
    //...and puts them onto a random .txt file inside the run folder
    private static void logRecipeId(String recipeId) {
        try (FileWriter writer = new FileWriter("vanilla_recipe_ids.txt", true)) {
            writer.write("\"" + recipeId + "\",\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void onRecipesUpdated(RecipesUpdatedEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}

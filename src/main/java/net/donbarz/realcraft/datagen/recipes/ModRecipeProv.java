package net.donbarz.realcraft.datagen.recipes;

import net.donbarz.realcraft.RealCraft;
import net.donbarz.realcraft.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProv extends RecipeProvider implements IConditionBuilder {
    public static final int advancedOreBlastingTime = 1000;

    public ModRecipeProv(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        for (String recipeId : ModRecipes.allVanillaRecipeIds) {
            // Generate an empty recipe only if the ID is not in the exclusion list
            if (!ModRecipes.recipesToExclude.contains(recipeId)) {
                ResourceLocation recipeLoc = new ResourceLocation(recipeId);
                consumer.accept(new EmptyRecipeResult(recipeLoc));
            }
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////

        oreBlasting(consumer, ModItems.IRON_BAR_MOLD.get(), Items.IRON_INGOT, "iron_ingot");

        oreSmelting(consumer, ModItems.CLAY_BAR_MOLD.get(), ModItems.BAR_MOLD.get(), "ingot_mold");

        //////////////////////////////////////////////////////////////////////////////////////////////////////////

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,ModItems.IRON_BAR_MOLD.get(),1)
                .requires(ModItems.BAR_MOLD.get())
                .requires(ModItems.RAW_IRON_DUST.get())
                .requires(ModItems.RAW_IRON_DUST.get())
                .requires(ModItems.RAW_IRON_DUST.get())
                .requires(ModItems.RAW_IRON_DUST.get())
                .requires(ModItems.RAW_IRON_DUST.get())
                .requires(ModItems.RAW_IRON_DUST.get())
                .requires(ModItems.RAW_IRON_DUST.get())
                .requires(ModItems.RAW_IRON_DUST.get())
                .unlockedBy("has_bar_mold", has(ModItems.BAR_MOLD.get()))
                .save(consumer);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.CLAY_BAR_MOLD.get(),4)
                .pattern("# #")
                .pattern("###")
                .define('#', Items.CLAY_BALL)
                .unlockedBy("has_clay_ball", has(Items.CLAY_BALL)) // Condition to unlock the recipe
                .save(consumer);

    }

    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pIngredient, ItemLike pResult, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, List.of(pIngredient), RecipeCategory.MISC, pResult, 0.35f, advancedOreBlastingTime, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pIngredient, ItemLike pResult, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, List.of(pIngredient), RecipeCategory.MISC, pResult, 0.35f, advancedOreBlastingTime, pGroup, "_from_blasting");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, ItemLike pIngredient, ItemLike pResult, String pGroup, int cookingTime, String pRecipeName) {
        List<ItemLike> pIngredients = List.of(pIngredient);
        Iterator var9 = pIngredients.iterator();

        while(var9.hasNext()) {
            ItemLike itemlike = (ItemLike)var9.next();
            SimpleCookingRecipeBuilder.generic(Ingredient.of(new ItemLike[]{itemlike}), RecipeCategory.MISC, pResult,
                    0.25f, cookingTime, pCookingSerializer)
                    .group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pFinishedRecipeConsumer, RealCraft.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }

    }


}

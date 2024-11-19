package net.donbarz.realcraft.datagen.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

// Inner class to define the empty recipe JSON structure
public class EmptyRecipeResult implements FinishedRecipe {
    private final ResourceLocation id;

    public EmptyRecipeResult(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        // Define an empty recipe JSON structure to invalidate the recipe
        json.addProperty("type", "minecraft:crafting_shaped");
        json.add("pattern", new JsonArray()); // Empty pattern
        json.add("key", new JsonObject());
        JsonObject result = new JsonObject();
        result.addProperty("item", "minecraft:air"); // Result is air, so it has no effect
        json.add("result", result);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getType() {
        return RecipeSerializer.SHAPED_RECIPE;
    }

    @Override
    public JsonObject serializeAdvancement() {
        return null; // No advancement for an empty recipe
    }

    @Override
    public ResourceLocation getAdvancementId() {
        return null;
    }
}

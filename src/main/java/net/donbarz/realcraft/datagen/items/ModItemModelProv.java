package net.donbarz.realcraft.datagen.items;

import net.donbarz.realcraft.RealCraft;
import net.donbarz.realcraft.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProv extends ItemModelProvider {
    public ModItemModelProv(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, RealCraft.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (RegistryObject<Item>  item: ModItems.SIMPLE_ITEMS.getEntries()) {
            simpleItem(item);
        }
        for (RegistryObject<Item>  item: ModItems.BLOCK_ITEMS.getEntries()) {
            blockItem(item);
        }
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(RealCraft.MOD_ID, "item/" + item.getId().getPath())
        );
    }

    private ItemModelBuilder advancedItem(RegistryObject<Item> item, String path) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(RealCraft.MOD_ID, "item/" + item.getId().getPath())
        );
    }

    private ItemModelBuilder blockItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(), RealCraft.MOD_ID + ":" + "block/" + item.getId().getPath());
    }
}

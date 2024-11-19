package net.donbarz.realcraft.datagen.blocks;

import net.donbarz.realcraft.RealCraft;
import net.donbarz.realcraft.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGen extends BlockTagsProvider {
    public ModBlockTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, RealCraft.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for(RegistryObject<Block> block : ModBlocks.ADVANCED_ORE_BLOCKS.getEntries()){
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block.get());
            this.tag(BlockTags.NEEDS_IRON_TOOL).add(block.get());
        }
    }
}

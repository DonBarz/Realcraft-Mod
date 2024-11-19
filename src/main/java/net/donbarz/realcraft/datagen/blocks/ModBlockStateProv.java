package net.donbarz.realcraft.datagen.blocks;

import net.donbarz.realcraft.RealCraft;
import net.donbarz.realcraft.block.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProv extends BlockStateProvider {
    public ModBlockStateProv(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, RealCraft.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (RegistryObject<Block>  block: ModBlocks.SIMPLE_BLOCKS.getEntries()){
            blockWithItem(block);
        }
        for (RegistryObject<Block>  block: ModBlocks.ADVANCED_ORE_BLOCKS.getEntries()){
            blockWithItem(block);
        }
        for (RegistryObject<Block>  block: ModBlocks.LAYERED_BLOCKS.getEntries()){
            generateLayeredBlockModels(block.get());
        }
    }

    private void blockWithItem(RegistryObject<Block> block) {
        simpleBlockWithItem(block.get(),cubeAll(block.get()));
    }

    protected void generateLayeredBlockModels(Block block) {
        // Generate models for each layer programmatically
        for (int layer = 2; layer <= 16; layer+=2) {  // Assuming 8 layers, adjust as necessary
            ModelFile model = models()
                    .withExistingParent(ForgeRegistries.BLOCKS.getKey(block).getPath() + "_" + layer, "block/thin_block")
                    .texture("particle", "block/" + ForgeRegistries.BLOCKS.getKey(block).getPath())
                    .texture("texture", "block/" + ForgeRegistries.BLOCKS.getKey(block).getPath())
                    .element()
                    .from(0, 0, 0)
                    .to(16, layer, 16)  // Adjust height according to layer count
                    .face(Direction.DOWN).uvs(0,0,16,16).texture("#texture").cullface(Direction.DOWN).end()
                    .face(Direction.UP).uvs(0,0,16,16).texture("#texture").end()
                    .face(Direction.NORTH).uvs(0,16-layer,16,16).texture("#texture").cullface(Direction.NORTH).end()
                    .face(Direction.SOUTH).uvs(0,16-layer,16,16).texture("#texture").cullface(Direction.SOUTH).end()
                    .face(Direction.WEST).uvs(0,16-layer,16,16).texture("#texture").cullface(Direction.WEST).end()
                    .face(Direction.EAST).uvs(0,16-layer,16,16).texture("#texture").cullface(Direction.EAST).end()
                    .end();
        }

        getVariantBuilder(block).forAllStates(state -> {
            int layers = state.getValue(SnowLayerBlock.LAYERS);  // Assuming it uses the LAYERS property like snow
            return ConfiguredModel.builder()
                    .modelFile(models().getExistingFile(modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block).getPath() + "_" + (layers*2))))
                    .build();
        });
    }

}

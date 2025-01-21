package net.donbarz.realcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SnowLayerBlock;

import javax.annotation.Nullable;
import java.util.Random;

public class GravityLayersBlock extends SnowLayerBlock {

    public GravityLayersBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos){
        BlockState belowState = pLevel.getBlockState(pPos.below());
        if (belowState.isSolidRender(pLevel, pPos)) {
            return true;
        }
        dropItems(pState, (Level) pLevel, pPos, pState.getValue(SnowLayerBlock.LAYERS));
        return false;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        checkForFalling(state, level, pos);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, neighborPos, isMoving);
        checkForFalling(state, level, pos);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);

        // Get the number of layers for this block
        dropItems(state, level, pos, state.getValue(SnowLayerBlock.LAYERS));

    }

    /**
     * Check if the block should start falling. Called on placement or neighbor changes.
     */
    private void checkForFalling(BlockState state, Level level, BlockPos pos) {
        if (!level.isClientSide) {
            BlockPos belowPos = pos.below();
            BlockState belowState = level.getBlockState(belowPos);

            // If the block below is air or another unstable block, start falling.
            if (belowState.isAir() || !belowState.isSolidRender(level, belowPos)) {
                FallingBlockEntity.fall(level, pos, state);
            }
        }
    }

    private void dropItems(BlockState state, Level level, BlockPos pos, int layers) {
        Random random = new Random();  // Get the number of layers

        // Drop one gravel for each layer
        for (int i = 0; i < layers; i++) {
            // 5% chance to drop flint
            if (random.nextInt(100) < 5) {
                level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.FLINT)));
            } else level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModBlocks.GRAVEL_LAYERS.get())));
        }
    }
}

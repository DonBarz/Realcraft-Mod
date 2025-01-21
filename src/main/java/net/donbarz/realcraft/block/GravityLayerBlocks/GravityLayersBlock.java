package net.donbarz.realcraft.block.GravityLayerBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GravityLayersBlock extends Block implements LiquidBlockContainer {
    public static final int MAX_HEIGHT = 8;
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[]{
            Shapes.empty(), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    };

    public GravityLayersBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LAYERS, 1).setValue(WATERLOGGED, false));
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        level.setBlock(pos, updateShape(state, null, null, level, pos, null), 3);
        checkForFalling(state, level, pos);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, neighborPos, isMoving);
        level.setBlock(pos, updateShape(state, null, null, level, pos, null), 3);
        checkForFalling(state, level, pos);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
    }

    private void checkForFalling(BlockState state, Level level, BlockPos pos) {
        if (!level.isClientSide) {
            BlockPos belowPos = pos.below();
            BlockState belowState = level.getBlockState(belowPos);

            if (belowState.isAir() || (belowState.is(this)&&belowState.getValue(LAYERS) < 8) || belowState.is(Blocks.WATER) || belowState.is(Blocks.LAVA) || belowState.is(Blocks.GRASS)) {
                FallingLayersBlockEntity.fall(level, pos, state);
            }
        }
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        switch (type) {
            case LAND:
                return state.getValue(LAYERS) < 5;
            case WATER:
            case AIR:
            default:
                return false;
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_LAYER[state.getValue(LAYERS)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_LAYER[state.getValue(LAYERS)];
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter reader, BlockPos pos) {
        return SHAPE_BY_LAYER[state.getValue(LAYERS)];
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_LAYER[state.getValue(LAYERS)];
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(LAYERS) == 8 ? 0.2F : 1.0F;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        int currLayer = state.getValue(LAYERS);

        if (currLayer >= 3) {
            List<Direction> directions = Arrays.asList(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
            Collections.shuffle(directions);

            for (Direction dir : directions) {
                BlockPos targetPos = currentPos.relative(dir);
                int targetLayers = getLayers(level, targetPos, state);

                if (currLayer - targetLayers > 1) {
                    BlockState targetState = level.getBlockState(targetPos);
                    boolean waterlogged = level.getFluidState(targetPos).getType() == Fluids.WATER;

                    if (targetState.isAir()) {
                        level.setBlock(targetPos, state.setValue(LAYERS, 1).setValue(WATERLOGGED, waterlogged), 3);
                        currLayer--;
                    } else if (targetState.getBlock() == state.getBlock()) {
                        int mergeHeight = targetState.getValue(LAYERS);
                        level.setBlock(targetPos, state.setValue(LAYERS, mergeHeight + 1).setValue(WATERLOGGED, waterlogged), 3);
                        currLayer--;
                    }

                    if (currLayer <= 1) {
                        break; // Ensure we don't decrement below 1
                    }
                }
            }
        }

        state = state.setValue(LAYERS, currLayer);

        if (state.getValue(LAYERS) >= 7) {
            state = state.setValue(WATERLOGGED, false);
        }
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return !state.canSurvive(level, currentPos) ?
                Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    private int getLayers(LevelAccessor level, BlockPos pos, BlockState state) {
        BlockState targetState = level.getBlockState(pos);
        if (targetState.getBlock() == state.getBlock()) {
            return targetState.getValue(LAYERS);
        } else if (targetState.isAir()) {
            return 0;
        } else {
            return 8;
        }
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        int i = state.getValue(LAYERS);
        if (useContext.getItemInHand().is(this.asItem()) && i < 8) {
            if (useContext.replacingClickedOnBlock()) {
                return useContext.getClickedFace() == Direction.UP;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos());
        if (blockstate.is(this)) {
            int i = blockstate.getValue(LAYERS);
            return blockstate.setValue(LAYERS, Math.min(8, i + 1)).setValue(WATERLOGGED, i < 7 && context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
        } else {
            return this.defaultBlockState().setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYERS, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!state.getValue(WATERLOGGED) && state.getValue(LAYERS) < 8 && fluidState.getType() == Fluids.WATER) {
            level.setBlock(pos, state.setValue(WATERLOGGED, true), 3);
            level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
        return !state.getValue(WATERLOGGED) && state.getValue(LAYERS) < 8 && fluid == Fluids.WATER;
    }
}
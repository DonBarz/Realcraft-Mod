package net.donbarz.realcraft.block.GravityLayerBlocks;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import static net.donbarz.realcraft.block.GravityLayerBlocks.GravityLayersBlock.LAYERS;
import static net.donbarz.realcraft.block.GravityLayerBlocks.GravityLayersBlock.WATERLOGGED;

public class FallingLayersBlockEntity extends FallingBlockEntity {

    private static final Logger LOGGER = LogUtils.getLogger();
    private BlockState blockState;
    private boolean cancelDrop = false;

    public FallingLayersBlockEntity(EntityType<? extends FallingBlockEntity> entityType, Level level) {
        super(entityType, level);
    }

    public FallingLayersBlockEntity(Level level, double x, double y, double z, BlockState state) {
        super(EntityType.FALLING_BLOCK, level);
        this.blockState = state; // Set the blockState to the provided state
        this.blocksBuilding = true;
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.setStartPos(this.blockPosition());
    }

    public static FallingLayersBlockEntity fall(Level level, BlockPos pos, BlockState state) {
        FallingLayersBlockEntity fallingEntity = new FallingLayersBlockEntity(level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, state);
        level.setBlock(pos, state.getFluidState().createLegacyBlock(), 3);
        level.addFreshEntity(fallingEntity);
        return fallingEntity;
    }

    @Override
    public void tick() {
        if (this.blockState.isAir()) {
            this.discard();
        } else {
            Block block = this.blockState.getBlock();
            ++this.time;
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
            }

            this.move(MoverType.SELF, this.getDeltaMovement());
            if (!this.level().isClientSide) {
                BlockPos blockpos = this.blockPosition();
                boolean flag = this.blockState.getBlock() instanceof ConcretePowderBlock;
                boolean flag1 = flag && this.blockState.canBeHydrated(this.level(), blockpos, this.level().getFluidState(blockpos), blockpos);
                double d0 = this.getDeltaMovement().lengthSqr();
                if (flag && d0 > 1.0D) {
                    BlockHitResult blockhitresult = this.level().clip(new ClipContext(new Vec3(this.xo, this.yo, this.zo), this.position(), ClipContext.Block.COLLIDER, ClipContext.Fluid.SOURCE_ONLY, this));
                    if (blockhitresult.getType() != HitResult.Type.MISS && this.blockState.canBeHydrated(this.level(), blockpos, this.level().getFluidState(blockhitresult.getBlockPos()), blockhitresult.getBlockPos())) {
                        blockpos = blockhitresult.getBlockPos();
                        flag1 = true;
                    }
                }

                if (!this.onGround() && !flag1) {
                    if (!this.level().isClientSide && (this.time > 100 && (blockpos.getY() <= this.level().getMinBuildHeight() || blockpos.getY() > this.level().getMaxBuildHeight()) || this.time > 600)) {
                        if (this.dropItem && this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            this.spawnAtLocation(block);
                        }

                        this.discard();
                    }
                } else {
                    BlockState blockstate = this.level().getBlockState(blockpos);
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
                    if (!blockstate.is(Blocks.MOVING_PISTON)) {
                        if (!this.cancelDrop) {
                            //for waterlogging
                            this.blockState.setValue(WATERLOGGED, false);
                            if (this.blockState.hasProperty(BlockStateProperties.WATERLOGGED) && this.level().getFluidState(blockpos).getType() == Fluids.WATER) {
                                this.blockState = this.blockState.setValue(BlockStateProperties.WATERLOGGED, true);
                            }

                            if (level().getBlockState(blockpos).getBlock() == this.blockState.getBlock() & blockState.getValue(LAYERS) < 8) {
                                int mergeHeight = level().getBlockState(blockpos).getValue(LAYERS);

                                level().removeBlock(blockpos, false);
                                level().setBlock(blockpos, this.blockState.setValue(LAYERS, Math.min(mergeHeight + this.blockState.getValue(LAYERS),8)), 3);

                                //for waterlogging above
                                this.blockState.setValue(WATERLOGGED, false);
                                if (this.blockState.hasProperty(BlockStateProperties.WATERLOGGED) && this.level().getFluidState(blockpos.above()).getType() == Fluids.WATER) {
                                    this.blockState = this.blockState.setValue(BlockStateProperties.WATERLOGGED, true);
                                }

                                if (mergeHeight + this.blockState.getValue(LAYERS) > 8) level().setBlock(blockpos.above(), this.blockState.setValue(LAYERS, Math.max(mergeHeight + this.blockState.getValue(LAYERS)-8,1)), 3);

                                this.discard();
                                return;

                            } else if (level().setBlock(blockpos, this.blockState, 3)) {

                                this.discard();
                                return;
                            } else if (this.dropItem && this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                                this.discard();
                                this.callOnBrokenAfterFall(block, blockpos);
                            }
                        } else {
                            this.discard();
                            if (this.dropItem && this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                                this.callOnBrokenAfterFall(block, blockpos);
                                this.spawnAtLocation(block);
                            }
                        }
                    }
                }
            }

            this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, Block.getId(this.blockState));
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.blockState = Block.stateById(packet.getData());
        this.blocksBuilding = true;
        double d0 = packet.getX();
        double d1 = packet.getY();
        double d2 = packet.getZ();
        this.setPos(d0, d1, d2);
        this.setStartPos(this.blockPosition());
    }

    // Add any other custom methods or overrides as needed
}
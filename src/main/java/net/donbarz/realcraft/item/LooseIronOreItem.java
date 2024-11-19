package net.donbarz.realcraft.item;

import net.donbarz.realcraft.block.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class LooseIronOreItem extends Item {
    public LooseIronOreItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (!pContext.getLevel().isClientSide()) {
            BlockPos pos = pContext.getClickedPos();
            Player player = pContext.getPlayer();
            Level level = pContext.getLevel();
            ItemStack stack = pContext.getItemInHand();

            BlockState state0 = pContext.getLevel().getBlockState(pos);
            BlockState state1 = pContext.getLevel().getBlockState(pos.above(1));

            if(state0.is(Blocks.WATER) || state1.is(Blocks.WATER)) {

                pContext.getLevel().playSeededSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.AXOLOTL_SPLASH, SoundSource.PLAYERS, 1f, 1f ,0);

                stack.shrink(1);

                if(Math.random() > 0.5){
                    level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(),
                            new ItemStack(ModItems.RAW_IRON_DUST.get())));
                }else if(Math.random() > 0.5){
                    level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(),
                            new ItemStack(ModItems.LOOSE_STONE.get())));
                }else
                level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(),
                            new ItemStack(ForgeRegistries.ITEMS.getValue(ForgeRegistries.BLOCKS.getKey(ModBlocks.GRAVEL_LAYERS.get())))));


            }
        }

        return InteractionResult.PASS;
    }
}

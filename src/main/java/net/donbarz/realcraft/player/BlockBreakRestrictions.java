package net.donbarz.realcraft.player;
import net.donbarz.realcraft.RealCraft;
import net.minecraft.nbt.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = RealCraft.MOD_ID)
public class BlockBreakRestrictions {

    @SubscribeEvent
    public void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        event.setNewSpeed(event.getNewSpeed()*0.25f); //set miningspeed to 25% cus realism n shit
        Player player = event.getEntity();
        BlockState state = event.getState();
        Block block = state.getBlock();
        ItemStack tool = player.getMainHandItem();

        // List of exceptions where blocks can be mined without the correct tool
        List<Block> exceptions = List.of(
                Blocks.GRASS_BLOCK,
                Blocks.DIRT,
                Blocks.SAND,
                Blocks.GRAVEL
        );
        List<TagKey> tagExceptions = List.of(
                BlockTags.LEAVES,
                BlockTags.ENDERMAN_HOLDABLE,
                BlockTags.CAVE_VINES,
                BlockTags.FALL_DAMAGE_RESETTING,
                BlockTags.WOOL,
                BlockTags.BAMBOO_PLANTABLE_ON
        );

        boolean hasCorrectTool = false;

        // Check if the block requires a specific tool type and verify the tool action

        // Check the appropriate tool action based on block requirements
        if (state.is(BlockTags.MINEABLE_WITH_PICKAXE) && tool.canPerformAction(ToolActions.PICKAXE_DIG)) {
            hasCorrectTool = true;
        }
        if (state.is(BlockTags.MINEABLE_WITH_SHOVEL) && tool.canPerformAction(ToolActions.SHOVEL_DIG)) {
            hasCorrectTool = true;
        }
        if (state.is(BlockTags.MINEABLE_WITH_AXE) && tool.canPerformAction(ToolActions.AXE_DIG)) {
            hasCorrectTool = true;
        }
        if (state.is(BlockTags.MINEABLE_WITH_HOE) && tool.canPerformAction(ToolActions.HOE_DIG)) {
            hasCorrectTool = true;
        }


        // If the player does not have the correct tool, prevent breaking the block
        if (!hasCorrectTool) {
            event.setNewSpeed(0.0F); // Stops the block from being broken

            // If the block is in the exception list, allow normal break speed
            if (exceptions.contains(block)) {
                event.setNewSpeed(0.25F);
            }
            for (TagKey tag : tagExceptions) {
                if (state.is(tag)) return;
                event.setNewSpeed(0.25F);
            }
        }
    }
}

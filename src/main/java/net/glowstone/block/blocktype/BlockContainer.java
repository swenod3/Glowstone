package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.entity.TEContainer;
import net.glowstone.block.entity.TileEntity;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.inventory.MaterialMatcher;
import org.bukkit.Statistic;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Base BlockType for containers.
 */
public class BlockContainer extends BlockType {

    @Override
    public boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc) {
        TileEntity te = block.getTileEntity();
        if (te instanceof TEContainer) {
            switch (((TEContainer) te).getInventory().getType()) {
                case CHEST:
                    player.incrementStatistic(Statistic.CHEST_OPENED);
                    break;
                case CRAFTING:
                    player.incrementStatistic(Statistic.CRAFTING_TABLE_INTERACTION);
                    break;
                case DROPPER:
                    player.incrementStatistic(Statistic.DROPPER_INSPECTED);
                    break;
                case HOPPER:
                    player.incrementStatistic(Statistic.HOPPER_INSPECTED);
                    break;
                case DISPENSER:
                    player.incrementStatistic(Statistic.DISPENSER_INSPECTED);
                    break;
                case FURNACE:
                    player.incrementStatistic(Statistic.FURNACE_INTERACTION);
                    break;
                case ENDER_CHEST:
                    player.incrementStatistic(Statistic.ENDERCHEST_OPENED);
                    break;
            }
            // todo: animation?
            player.openInventory(((TEContainer) te).getInventory());
            return true;
        }
        return false;
    }

    @Override
    public Collection<ItemStack> getDrops(GlowBlock block, ItemStack tool) {
        Collection<ItemStack> drops = getContentDrops(block);

        MaterialMatcher neededTool = getNeededMiningTool(block);
        if (neededTool == null ||
                tool != null && neededTool.matches(tool.getType())) {
            drops.addAll(getBlockDrops(block));
        }

        return drops;
    }

    @Override
    public Collection<ItemStack> getMinedDrops(GlowBlock block) {
        Collection<ItemStack> drops = getContentDrops(block);
        drops.addAll(getBlockDrops(block));
        return drops;
    }

    public Collection<ItemStack> getContentDrops(GlowBlock block) {
        LinkedList<ItemStack> drops = new LinkedList<>();
        for (ItemStack i : ((TEContainer) block.getTileEntity()).getInventory().getContents()) {
            if (i != null) {
                drops.add(i);
            }
        }
        return drops;
    }

    /**
     * Returns the drops for block itself, WITHOUT it's contents.
     *
     * @param block The block the drops should be calculated for
     * @return the drops
     */
    protected Collection<ItemStack> getBlockDrops(GlowBlock block) {
        if (drops == null) {
            return Arrays.asList(new ItemStack(block.getType()));
        } else {
            return drops;
        }
    }

    protected MaterialMatcher getNeededMiningTool(GlowBlock block) {
        return null; //default any
    }
}

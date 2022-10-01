package net.zytorx.library.registry.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ToolTipBlockItem extends BlockItem {
    private final String modid;
    private final String name;

    public ToolTipBlockItem(Block block, String modid, String name, Properties properties) {
        super(block, properties);
        this.modid = modid;
        this.name = name;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        if (Screen.hasShiftDown()) {
            pTooltip.add(new TranslatableComponent("tooltip." + modid + "." + name + ".tooltip.shift"));
        } else {
            pTooltip.add(new TranslatableComponent("tooltip." + modid + "." + name + ".tooltip"));
        }
    }
}

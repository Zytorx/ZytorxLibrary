package net.zytorx.library.registry;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class RegisteredBlock implements ItemLike {

    private final RegistryObject<Block> block;

    RegisteredBlock(Registrar registrar, String name, Supplier<Block> block, CreativeModeTab tab, boolean hasToolTip, boolean hasItem) {
        this.block = registerBlock(registrar, name, block, tab, hasToolTip, hasItem);
    }

    private static RegistryObject<Block> registerBlock(Registrar registrar, String name, Supplier<Block> block,
                                                       CreativeModeTab tab, boolean hasToolTip, boolean hasItem) {
        var registeredBlock = registrar.registerBlock(name, block);
        if (hasItem) {
            registerBlockItem(registrar, name, registeredBlock, tab, hasToolTip);
        }

        return registeredBlock;
    }

    private static RegistryObject<Item> registerBlockItem(Registrar registrar, String name, RegistryObject<Block> block,
                                                          CreativeModeTab tab, boolean hasToolTip) {
        return registrar.registerItem(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)) {
            @Override
            public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
                if (!hasToolTip) return;
                if (Screen.hasShiftDown()) {
                    pTooltip.add(new TranslatableComponent("tooltip." + registrar.getModId() + "." + name + ".tooltip.shift"));
                } else {
                    pTooltip.add(new TranslatableComponent("tooltip." + registrar.getModId() + "." + name + ".tooltip"));
                }
            }
        });
    }

    public Block getBlock() {
        return block.get();
    }

    @Override
    public Item asItem() {
        return getBlock().asItem();
    }
}

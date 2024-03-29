package net.zytorx.library.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import net.zytorx.library.registry.item.ToolTipBlockItem;

import java.util.function.Supplier;

public class RegisteredBlock implements ItemLike {

    private final RegistryObject<Block> block;

    RegisteredBlock(Registrar registrar, String name, Supplier<Block> block, RegisteredTab tab, int tabPos, boolean hasToolTip, boolean hasItem) {
        this.block = registerBlock(registrar, name, block, tab, tabPos, hasToolTip, hasItem);
    }

    private static RegistryObject<Block> registerBlock(Registrar registrar, String name, Supplier<Block> block,
                                                       RegisteredTab tab,int tabPos, boolean hasToolTip, boolean hasItem) {
        var registeredBlock = registrar.registerBlock(name, block);
        if (hasItem) {
            registerBlockItem(registrar, name, registeredBlock, tab,tabPos, hasToolTip);
        }

        return registeredBlock;
    }

    private static RegisteredItem registerBlockItem(Registrar registrar, String name, RegistryObject<Block> block,
                                                    RegisteredTab tab,int tabPos, boolean hasToolTip) {
        if (!hasToolTip) {
            return registrar.createItem(name, () -> new BlockItem(block.get(), new Item.Properties()),tab,tabPos);
        }

        return registrar.createItem(name, () -> new ToolTipBlockItem(block.get(), registrar.getModId(), name, new Item.Properties()),tab,tabPos);
    }

    public Block getBlock() {
        return block.get();
    }

    @Override
    public Item asItem() {
        return getBlock().asItem();
    }
}

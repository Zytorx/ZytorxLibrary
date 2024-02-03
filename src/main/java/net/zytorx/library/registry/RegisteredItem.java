package net.zytorx.library.registry;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class RegisteredItem implements ItemLike {

    private final RegistryObject<Item> item;
    private final String name;

    RegisteredItem(Registrar registrar, String name, Supplier<? extends Item> supplier) {
        this.item = registrar.registerItem(name, supplier);
        this.name = name;
    }


    public Item getItem() {
        return item.get();
    }

    public String getName() {
        return name;
    }

    @Override
    public Item asItem() {
        return getItem();
    }
}

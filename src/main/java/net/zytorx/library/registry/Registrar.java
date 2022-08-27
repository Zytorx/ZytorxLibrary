package net.zytorx.library.registry;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.moddiscovery.ClasspathLocator;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.zytorx.library.block.WoodBlockCollection;
import net.zytorx.library.datagen.reflection.FieldCollector;

import java.util.Arrays;
import java.util.function.Supplier;

public class Registrar {

    private final DeferredRegister<Block> blocks;
    private final DeferredRegister<Item> items;
    private final String modid;

    public Registrar(String modid) {
        this.modid = modid;
        this.blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, modid);
        this.items = DeferredRegister.create(ForgeRegistries.ITEMS, modid);
    }

    public RegisteredItem createItem(String name, Supplier<? extends Item> sup) {
        return new RegisteredItem(this, name, sup);
    }

    public RegisteredBlock createBlock(String name, Supplier<Block> block) {
        return createBlock(name, block, null, false, false);
    }

    public RegisteredBlock createBlock(String name, Supplier<Block> block, CreativeModeTab tab) {
        return createBlock(name, block, tab, false);
    }

    public RegisteredBlock createBlock(String name, Supplier<Block> block, CreativeModeTab tab, boolean hasToolTip) {
        return createBlock(name, block, tab, hasToolTip, true);
    }

    public RegisteredBlock createBlock(String name, Supplier<Block> block, CreativeModeTab tab, boolean hasToolTip, boolean hasItem) {
        return new RegisteredBlock(this, name, block, tab, hasToolTip, hasItem);
    }

    public String getModId() {
        return modid;
    }

    public void registerBlocks(IEventBus eventBus) {
        blocks.register(eventBus);
    }
    public void registerItems(IEventBus eventBus) {
        items.register(eventBus);
    }

    public void clientSetup(final FMLClientSetupEvent event, Class<?> classes) {
        FieldCollector.getCollections(WoodBlockCollection.class, classes).forEach(woodBlockCollection -> {
            ItemBlockRenderTypes.setRenderLayer(woodBlockCollection.getDoorBlock().getBlock(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(woodBlockCollection.getTrapdoorBlock().getBlock(), RenderType.translucent());
        });
    }

    public Iterable<Block> getKnownBlocks() {
        return blocks.getEntries().stream().map(RegistryObject::get)::iterator;
    }

    public Iterable<Item> getKnownItems() {
        return items.getEntries().stream().map(RegistryObject::get)::iterator;
    }

    RegistryObject<Block> registerBlock(String name, Supplier<? extends Block> sup) {
        return blocks.register(name, sup);
    }

    RegistryObject<Item> registerItem(String name, Supplier<? extends Item> sup) {
        return items.register(name, sup);
    }

}

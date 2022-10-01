package net.zytorx.library.registry;

import joptsimple.internal.Reflection;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.zytorx.library.block.WoodBlockCollection;
import net.zytorx.library.datagen.reflection.FieldCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Registrar {

    private static final Map<String, Registrar> instances = new HashMap<>();
    private final List<Class<?>> blockDeclarations = new ArrayList<>();
    private final List<Class<?>> itemDeclarations = new ArrayList<>();
    private final DeferredRegister<Block> blocks;
    private final DeferredRegister<Item> items;
    private final String modid;

    private Registrar(String modid) {
        this.modid = modid;
        this.blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, modid);
        this.items = DeferredRegister.create(ForgeRegistries.ITEMS, modid);
    }

    public static Registrar getInstance(String modid) {
        if (!instances.containsKey(modid)) {
            instances.put(modid, new Registrar(modid));
        }
        return instances.get(modid);
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

    public void addBlockDeclaration(Class<?> blockDeclarationClass) {
        blockDeclarations.add(blockDeclarationClass);
        try {
            Reflection.instantiate(blockDeclarationClass.getConstructor());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void addItemDeclaration(Class<?> itemDeclarationClass) {
        itemDeclarations.add(itemDeclarationClass);
        try {
            Reflection.instantiate(itemDeclarationClass.getConstructor());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Class<?>> getBlockDeclaration() {
        return new ArrayList<>(blockDeclarations);
    }

    public List<Class<?>> getItemDeclaration() {
        return new ArrayList<>(itemDeclarations);
    }

    public void register(IEventBus eventBus) {
        blocks.register(eventBus);
        items.register(eventBus);
    }

    public void clientSetup(final FMLClientSetupEvent event) {
        FieldCollector.getCollections(WoodBlockCollection.class, blockDeclarations).forEach(woodBlockCollection -> {
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

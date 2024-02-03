package net.zytorx.library.registry;

import joptsimple.internal.Reflection;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;
import java.util.function.Supplier;

public class Registrar {
    private static final Map<String, Registrar> instances = new HashMap<>();
    private final List<Class<?>> blockDeclarations = new ArrayList<>();
    private final List<Class<?>> itemDeclarations = new ArrayList<>();
    private final List<Class<?>> tabDeclarations = new ArrayList<>();
    private final DeferredRegister<Block> blocks;
    private final DeferredRegister<Item> items;
    private final DeferredRegister<CreativeModeTab> tabs;
    private final String modid;

    private Registrar(String modid) {
        this.modid = modid;
        this.blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, modid);
        this.items = DeferredRegister.create(ForgeRegistries.ITEMS, modid);
        this.tabs = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, modid);
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

    public RegisteredItem createItem(String name, Supplier<? extends Item> sup, RegisteredTab tab) {
        return createItem(name,sup,tab,-1);
    }

    public RegisteredItem createItem(String name, Supplier<? extends Item> sup, RegisteredTab tab,int tabPos) {
        var item = new RegisteredItem(this, name, sup);
        if(tab != null) {
            tab.addItem(() -> item.asItem(), tabPos);
        }
        return item;
    }

    public RegisteredTab createTab(String name, Supplier<ItemLike> icon){
        return new RegisteredTab(this,name,icon);
    }

    public RegisteredBlock createBlock(String name, Supplier<Block> block) {
        return createBlock(name, block, null, -1,false, false);
    }

    public RegisteredBlock createBlock(String name, Supplier<Block> block, RegisteredTab tab) {
        return createBlock(name, block, tab,-1, false);
    }

    public RegisteredBlock createBlock(String name, Supplier<Block> block, RegisteredTab tab, int tabPos) {
        return createBlock(name, block, tab,tabPos, false);
    }

    public RegisteredBlock createBlock(String name, Supplier<Block> block, RegisteredTab tab, boolean hasToolTip) {
        return createBlock(name, block, tab,-1, hasToolTip, true);
    }

    public RegisteredBlock createBlock(String name, Supplier<Block> block, RegisteredTab tab,int tabPos, boolean hasToolTip) {
        return createBlock(name, block, tab,tabPos, hasToolTip, true);
    }

    public RegisteredBlock createBlock(String name, Supplier<Block> block, RegisteredTab tab, boolean hasToolTip, boolean hasItem) {
        return new RegisteredBlock(this, name, block, tab, -1, hasToolTip, hasItem);
    }

    public RegisteredBlock createBlock(String name, Supplier<Block> block, RegisteredTab tab,int tabPos, boolean hasToolTip, boolean hasItem) {
        return new RegisteredBlock(this, name, block, tab, tabPos, hasToolTip, hasItem);
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

    public void addTabDeclaration(Class<?> tabDeclarationClass){
        tabDeclarations.add(tabDeclarationClass);
        try {
            Reflection.instantiate(tabDeclarationClass.getConstructor());
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

    public List<Class<?>> getTabDeclaration() {
        return new ArrayList<>(itemDeclarations);
    }

    public void register(IEventBus eventBus) {
        blocks.register(eventBus);
        items.register(eventBus);
        tabs.register(eventBus);
    }

    public Iterable<Block> getKnownBlocks() {
        return blocks.getEntries().stream().map(RegistryObject::get)::iterator;
    }

    public Iterable<Item> getKnownItems() {
        return items.getEntries().stream().map(RegistryObject::get)::iterator;
    }

    public Iterable<CreativeModeTab> getKnownTabs() {
        return tabs.getEntries().stream().map(RegistryObject::get)::iterator;
    }

    RegistryObject<Block> registerBlock(String name, Supplier<? extends Block> sup) {
        return blocks.register(name, sup);
    }

    RegistryObject<Item> registerItem(String name, Supplier<? extends Item> sup) {
        return items.register(name, sup);
    }
    RegistryObject<CreativeModeTab> registerTab(String name, Supplier<? extends CreativeModeTab> sup) {
        return tabs.register(name, sup);
    }
}

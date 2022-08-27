package net.zytorx.library.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.zytorx.library.registry.RegisteredItem;
import net.zytorx.library.registry.Registrar;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.ParameterizedType;

public class ItemArmorCollection implements ItemCollection {
    protected final RegisteredItem material;
    protected final RegisteredItem helmet;
    protected final RegisteredItem chestplate;
    protected final RegisteredItem leggings;
    protected final RegisteredItem boots;


    public ItemArmorCollection(Registrar registrar, RegisteredItem material, ArmorMaterial tier, CreativeModeTab tab) {
        this(ArmorItem.class,registrar, material, tier, tab);
    }

    public ItemArmorCollection(Registrar registrar, String customName, RegisteredItem material, ArmorMaterial tier, CreativeModeTab tab) {
        this(ArmorItem.class,registrar,customName, material, tier, tab);
    }

    public ItemArmorCollection(Class<? extends ArmorItem> type,Registrar registrar, RegisteredItem material, ArmorMaterial tier, CreativeModeTab tab) {
        this(type,registrar, material.getName(), material, tier, tab);
    }

    public ItemArmorCollection(Class<? extends ArmorItem> type,Registrar registrar, String customName, RegisteredItem material, ArmorMaterial tier,
                               CreativeModeTab tab) {
        this.material = material;

        try {
            var constructor = type.getConstructor(ArmorMaterial.class, EquipmentSlot.class, Item.Properties.class);

            this.helmet = registrar.createItem(customName + "_helmet",
                    () -> createArmorItem(constructor, tier, EquipmentSlot.HEAD, tab));

            this.chestplate = registrar.createItem(customName + "_chestplate",
                    () -> createArmorItem(constructor, tier, EquipmentSlot.CHEST, tab));

            this.leggings = registrar.createItem(customName + "_leggings",
                    () -> createArmorItem(constructor, tier, EquipmentSlot.LEGS, tab));

            this.boots = registrar.createItem(customName + "_boots",
                    () -> createArmorItem(constructor, tier, EquipmentSlot.FEET, tab));
        } catch (NoSuchMethodException | MalformedParameterizedTypeException | TypeNotPresentException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T extends ArmorItem> T createArmorItem(Constructor<T> constructor, ArmorMaterial tier, EquipmentSlot slot, CreativeModeTab tab) {
        try {
            return constructor.newInstance(tier, slot, new Item.Properties().tab(tab));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public RegisteredItem getHelmet() {
        return helmet;
    }

    public RegisteredItem getChestplate() {
        return chestplate;
    }

    public RegisteredItem getLeggings() {
        return leggings;
    }

    public RegisteredItem getBoots() {
        return boots;
    }

    public RegisteredItem getMaterial() {
        return material;
    }
}

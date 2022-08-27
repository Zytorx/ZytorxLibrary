package net.zytorx.library.datagen.model;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.zytorx.library.block.WoodBlockCollection;
import net.zytorx.library.datagen.reflection.FieldCollector;
import net.zytorx.library.datagen.reflection.annotations.ItemDefinition;
import net.zytorx.library.item.ItemArmorCollection;
import net.zytorx.library.item.ItemToolCollection;
import net.zytorx.library.registry.RegisteredItem;

public abstract class ZytorxItemModelProvider extends ItemModelProvider {

    protected final String modid;
    protected final Class<?>[] classes;

    public ZytorxItemModelProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper, Class<?>... classes) {
        super(gen, modid, exFileHelper);
        this.modid = modid;
        this.classes = classes;
    }

    protected abstract void addItemModels();

    @Override
    protected final void registerModels() {
        registerSimpleItems();
        registerWoodCollectionDoors();
        addItemModels();
    }

    private void registerWoodCollectionDoors() {

        FieldCollector.getCollections(WoodBlockCollection.class, classes)
                .forEach(woodCollection -> simpleItem(woodCollection.getDoorBlock()));
    }

    private void registerSimpleItems() {
        FieldCollector.getFieldsWithAnnotation(ItemDefinition.class, classes).forEach(pair -> registerUnknown(pair.getFirst(), pair.getSecond()));
    }

    private void registerUnknown(Object object, ItemDefinition data) {
        if (data.hasCustomModel()) return;

        if (object instanceof RegisteredItem registered) {
            registerSimpleItem(registered, data);
            return;
        }

        if (object instanceof ItemToolCollection toolCollection) {
            registerToolCollection(toolCollection, data);
            return;
        }

        if (object instanceof ItemArmorCollection armorCollection) {
            registerArmorCollection(armorCollection, data);
            return;
        }
    }

    private void registerSimpleItem(ItemLike item, ItemDefinition data) {
        if (data.isTool()) {
            handheldItem(item);
        } else {
            simpleItem(item);
        }
    }

    private void registerToolCollection(ItemToolCollection toolCollection, ItemDefinition data) {
        handheldItem(toolCollection.getSword());
        handheldItem(toolCollection.getPickaxe());
        handheldItem(toolCollection.getAxe());
        handheldItem(toolCollection.getShovel());
        handheldItem(toolCollection.getHoe());
    }

    private void registerArmorCollection(ItemArmorCollection armorCollection, ItemDefinition data) {
        registerSimpleItem(armorCollection.getHelmet(), data);
        registerSimpleItem(armorCollection.getChestplate(), data);
        registerSimpleItem(armorCollection.getLeggings(), data);
        registerSimpleItem(armorCollection.getBoots(), data);
    }

    protected ItemModelBuilder simpleItem(ItemLike item) {
        return withExistingParent(item.asItem().getRegistryName().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(modid, "item/" + item.asItem().getRegistryName().getPath()));
    }

    protected ItemModelBuilder handheldItem(ItemLike item) {
        return withExistingParent(item.asItem().getRegistryName().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(modid, "item/" + item.asItem().getRegistryName().getPath()));
    }
}

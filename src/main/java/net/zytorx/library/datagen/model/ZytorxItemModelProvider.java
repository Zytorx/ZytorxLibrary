package net.zytorx.library.datagen.model;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.zytorx.library.block.WoodBlockCollection;
import net.zytorx.library.datagen.reflection.FieldCollector;
import net.zytorx.library.datagen.reflection.annotations.ItemDefinition;
import net.zytorx.library.item.ItemArmorCollection;
import net.zytorx.library.item.ItemToolCollection;
import net.zytorx.library.registry.RegisteredItem;
import net.zytorx.library.registry.Registrar;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public abstract class ZytorxItemModelProvider extends ItemModelProvider {

    protected final String modid;
    private final ZytorxTextureEnsurer textureEnsurer;
    private final Collection<Class<?>> classes;

    public ZytorxItemModelProvider(PackOutput output, String modid, ExistingFileHelper exFileHelper) {
        this(output, modid, exFileHelper, new ZytorxTextureEnsurer(output, exFileHelper));
    }

    public ZytorxItemModelProvider(PackOutput output, String modid, ExistingFileHelper exFileHelper, ZytorxTextureEnsurer textureEnsurer) {
        super(output, modid, exFileHelper);
        this.modid = modid;
        this.classes = Registrar.getInstance(modid).getItemDeclaration();
        this.textureEnsurer = textureEnsurer;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        textureEnsurer.setCache(cache);
       return super.run(cache);
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
        var key = ForgeRegistries.ITEMS.getKey(item.asItem());
        if(key == null){
            return null;
        }
        return withExistingParent(key.getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                itemTexture(item));
    }

    protected ItemModelBuilder handheldItem(ItemLike item) {
        var key = ForgeRegistries.ITEMS.getKey(item.asItem());
        if(key == null){
            return null;
        }
        return withExistingParent(key.getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                itemTexture(item));
    }

    protected final ResourceLocation itemTexture(ItemLike item) {
        var key = ForgeRegistries.ITEMS.getKey(item.asItem());
        if(key == null){
            return null;
        }
        return itemTexture(key.getPath());
    }

    protected final ResourceLocation itemTexture(String item) {
        var texture = itemTexture(modid, item);
        try {
            textureEnsurer.ensureItemTexture(texture);
        } catch (IOException e) {
            return null;
        }
        return texture;
    }

    public static ResourceLocation itemTexture(String modid, String item) {
        return new ResourceLocation(modid, "item/" + item);
    }
}

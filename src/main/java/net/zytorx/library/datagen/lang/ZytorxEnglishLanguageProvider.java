package net.zytorx.library.datagen.lang;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import net.zytorx.library.block.NormalBlockCollection;
import net.zytorx.library.block.SimpleBlockCollection;
import net.zytorx.library.block.WoodBlockCollection;
import net.zytorx.library.datagen.reflection.FieldCollector;
import net.zytorx.library.datagen.reflection.annotations.EnglishName;
import net.zytorx.library.item.ItemArmorCollection;
import net.zytorx.library.item.ItemToolCollection;
import net.zytorx.library.registry.RegisteredBlock;
import net.zytorx.library.registry.RegisteredItem;
import org.jetbrains.annotations.NotNull;

public abstract class ZytorxEnglishLanguageProvider extends LanguageProvider {

    private final Class<?>[] classes;

    public ZytorxEnglishLanguageProvider(DataGenerator gen, String modid, Class<?>... classes) {
        super(gen, modid, "en_us");
        this.classes = classes;
    }

    @Override
    protected final void addTranslations() {
        registerAnnotated();
        addManualTranslations();
    }

    protected abstract void addManualTranslations();

    private void registerAnnotated() {
        var fields = FieldCollector.getFieldsWithAnnotation(EnglishName.class, classes);
        fields.forEach(pair -> registerUnknown(pair.getFirst(), pair.getSecond()));
    }

    private void registerUnknown(@NotNull Object object, EnglishName name) {

        if (object instanceof WoodBlockCollection woodBlocks) {
            registerWoodBlockCollection(woodBlocks, name);
            return;
        }

        if (object instanceof NormalBlockCollection normalBlocks) {
            registerNormalBlockCollection(normalBlocks, name);
            return;
        }

        if (object instanceof SimpleBlockCollection simpleBlocks) {
            registerSimpleBlockCollection(simpleBlocks, name);
            return;
        }

        if (object instanceof RegisteredBlock block) {
            this.addBlock(block::getBlock, name.name());
            return;
        }

        if (object instanceof ItemToolCollection toolCollection) {
            registerToolCollection(toolCollection, name);
            return;
        }

        if (object instanceof ItemArmorCollection armorCollection) {
            registerArmorCollection(armorCollection, name);
            return;
        }

        if (object instanceof RegisteredItem item) {
            this.add(item.getItem(), name.name());
        }
    }

    private void registerSimpleBlockCollection(SimpleBlockCollection simpleBlocks, EnglishName name) {
        this.addBlock(simpleBlocks.getStandardBlock()::getBlock, name.name());
        this.addBlock(simpleBlocks.getSlabBlock()::getBlock, name.name() + " Slab");
        this.addBlock(simpleBlocks.getStairsBlock()::getBlock, name.name() + " Stairs");
    }

    private void registerWoodBlockCollection(WoodBlockCollection woodBlocks, EnglishName name) {
        registerSimpleBlockCollection(woodBlocks, name);
        this.addBlock(woodBlocks.getFenceBlock()::getBlock, name.name() + " Fence");
        this.addBlock(woodBlocks.getFenceGateBlock()::getBlock, name.name() + " Fence Gate");
        this.addBlock(woodBlocks.getDoorBlock()::getBlock, name.name() + " Door");
        this.addBlock(woodBlocks.getTrapdoorBlock()::getBlock, name.name() + " Trap Door");
        this.addBlock(woodBlocks.getButtonBlock()::getBlock, name.name() + " Button");
        this.addBlock(woodBlocks.getPressurePlateBlock()::getBlock, name.name() + " Pressure Plate");
    }

    private void registerNormalBlockCollection(NormalBlockCollection normalBlocks, EnglishName name) {
        registerSimpleBlockCollection(normalBlocks, name);
        this.addBlock(normalBlocks.getWallBlock()::getBlock, name.name() + " Wall");
    }

    private void registerToolCollection(ItemToolCollection toolCollection, EnglishName name) {
        this.add(toolCollection.getAxe().getItem(), name.name() + " Axe");
        this.add(toolCollection.getPickaxe().getItem(), name.name() + " Pickaxe");
        this.add(toolCollection.getShovel().getItem(), name.name() + " Shovel");
        this.add(toolCollection.getSword().getItem(), name.name() + " Sword");
        this.add(toolCollection.getHoe().getItem(), name.name() + " Hoe");
    }

    private void registerArmorCollection(ItemArmorCollection armorCollection, EnglishName name) {
        this.add(armorCollection.getHelmet().getItem(), name.name() + " Helmet");
        this.add(armorCollection.getChestplate().getItem(), name.name() + " Chestplate");
        this.add(armorCollection.getLeggings().getItem(), name.name() + " Leggings");
        this.add(armorCollection.getBoots().getItem(), name.name() + " Boots");
    }
}

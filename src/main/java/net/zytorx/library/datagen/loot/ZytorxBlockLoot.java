package net.zytorx.library.datagen.loot;

import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.zytorx.library.block.NormalBlockCollection;
import net.zytorx.library.block.SimpleBlockCollection;
import net.zytorx.library.block.WoodBlockCollection;
import net.zytorx.library.datagen.reflection.FieldCollector;
import net.zytorx.library.datagen.reflection.annotations.BlockDefinition;
import net.zytorx.library.registry.RegisteredBlock;
import net.zytorx.library.registry.Registrar;

import java.util.Collection;

public abstract class ZytorxBlockLoot extends BlockLoot {

    private final String modid;
    private final Collection<Class<?>> classes;

    public ZytorxBlockLoot(String modid) {
        this.modid = modid;
        this.classes = Registrar.getInstance(modid).getBlockDeclaration();
    }

    @Override
    protected final void addTables() {
        registerSimpleBlocks();
        addBlockTables();
    }

    protected abstract void addBlockTables();

    private void registerSimpleBlocks() {
        FieldCollector.getFieldsWithAnnotation(BlockDefinition.class, classes)
                .forEach(pair -> registerUnknown(pair.getFirst(), pair.getSecond()));
    }

    private void registerUnknown(Object object, BlockDefinition data) {
        if (object instanceof RegisteredBlock block) {
            registerBlock(block.getBlock(), data);
            return;
        }

        if (object instanceof NormalBlockCollection normalBlocks) {
            registerNormalBlockCollection(normalBlocks, data);
            return;
        }

        if (object instanceof WoodBlockCollection woodBlocks) {
            registerWoodBlockCollection(woodBlocks, data);
            return;
        }

        if (object instanceof SimpleBlockCollection simpleBlocks) {
            registerSimpleBlockCollection(simpleBlocks, data);
        }
    }

    private void registerSimpleBlockCollection(SimpleBlockCollection simpleBlocks, BlockDefinition data) {
        registerBlock(simpleBlocks.getStandardBlock().getBlock(), data);
        registerSlabBlock(simpleBlocks.getSlabBlock().getBlock(), data);
        registerBlock(simpleBlocks.getStairsBlock().getBlock(), data);
    }

    private void registerNormalBlockCollection(NormalBlockCollection normalBlocks, BlockDefinition data) {
        registerSimpleBlockCollection(normalBlocks, data);
        registerBlock(normalBlocks.getWallBlock().getBlock(), data);

    }

    private void registerWoodBlockCollection(WoodBlockCollection woodBlocks, BlockDefinition data) {
        registerSimpleBlockCollection(woodBlocks, data);
        registerBlock(woodBlocks.getFenceBlock().getBlock(), data);
        registerBlock(woodBlocks.getFenceGateBlock().getBlock(), data);
        registerDoorBlock(woodBlocks.getDoorBlock().getBlock(), data);
        registerBlock(woodBlocks.getTrapdoorBlock().getBlock(), data);
        registerBlock(woodBlocks.getPressurePlateBlock().getBlock(), data);
        registerBlock(woodBlocks.getButtonBlock().getBlock(), data);
    }

    private void registerBlock(Block block, BlockDefinition data) {
        if (!data.hasDrops() || !data.hasItem() || data.hasSpecialDrop()) {
            dropsNothing(block);
            return;
        }
        if (!data.needsSilktouch()) {
            this.dropSelf(block);
            return;
        }
        dropWhenSilkTouch(block);
    }

    private void registerSlabBlock(Block block, BlockDefinition data) {
        if (!data.hasDrops() || !data.hasItem() || data.hasSpecialDrop()) {
            dropsNothing(block);
            return;
        }
        this.add(block, createSlabItemTable(block));
    }

    private void registerDoorBlock(Block block, BlockDefinition data) {
        if (!data.hasDrops() || !data.hasItem() || data.hasSpecialDrop()) {
            dropsNothing(block);
            return;
        }
        this.add(block, createDoorTable(block));
    }


    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Registrar.getInstance(modid).getKnownBlocks();
    }

    protected void createOreDrop(Block block, Item drop, float minDrops, float maxDrops) {
        createOreDrop(block, drop, minDrops, maxDrops, true);
    }

    protected void createOreDrop(Block block, Item drop, float minDrops, float maxDrops, boolean affectedByFortune) {

        var lootItem = LootItem.lootTableItem(drop)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)));

        if (affectedByFortune) {
            lootItem.apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE));
        }

        var lootTable = createSilkTouchDispatchTable(block, applyExplosionDecay(block, lootItem));

        this.add(block, lootTable);
    }

    protected void dropsNothing(Block block) {
        this.add(block, noDrop());
    }
}

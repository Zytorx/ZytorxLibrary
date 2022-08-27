package net.zytorx.library.datagen.tags;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.zytorx.library.block.NormalBlockCollection;
import net.zytorx.library.block.SimpleBlockCollection;
import net.zytorx.library.block.WoodBlockCollection;
import net.zytorx.library.datagen.reflection.FieldCollector;
import net.zytorx.library.datagen.reflection.annotations.BlockDefinition;
import net.zytorx.library.registry.RegisteredBlock;

public abstract class ZytorxBlockTagsProvider extends BlockTagsProvider {

    private final Class<?>[] classes;

    public ZytorxBlockTagsProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper, Class<?>... classes) {
        super(gen, modid, exFileHelper);
        this.classes = classes;
    }

    protected abstract void addCustomTags();

    @Override
    protected final void addTags() {

        registerSimpleBlocks();

    }

    private void registerSimpleBlocks() {
        FieldCollector.getFieldsWithAnnotation(BlockDefinition.class, classes)
                .forEach(pair -> registerUnknown(pair.getFirst(), pair.getSecond()));
    }

    private void registerUnknown(Object object, BlockDefinition data) {
        if (object instanceof RegisteredBlock block) {
            registerSimpleBlock(block.getBlock(), data);
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
        registerSimpleBlock(simpleBlocks.getStandardBlock().getBlock(), data);
        registerSimpleBlock(simpleBlocks.getSlabBlock().getBlock(), data);
        registerSimpleBlock(simpleBlocks.getStairsBlock().getBlock(), data);
    }

    private void registerNormalBlockCollection(NormalBlockCollection normalBlocks, BlockDefinition data) {
        registerSimpleBlockCollection(normalBlocks, data);
        registerSimpleBlock(normalBlocks.getWallBlock().getBlock(), data);
        this.tag(BlockTags.WALLS).add(normalBlocks.getWallBlock().getBlock());
    }

    private void registerWoodBlockCollection(WoodBlockCollection woodBlocks, BlockDefinition data) {
        registerSimpleBlockCollection(woodBlocks, data);
        registerSimpleBlock(woodBlocks.getFenceBlock().getBlock(), data);
        registerSimpleBlock(woodBlocks.getFenceGateBlock().getBlock(), data);
        registerSimpleBlock(woodBlocks.getDoorBlock().getBlock(), data);
        registerSimpleBlock(woodBlocks.getTrapdoorBlock().getBlock(), data);
        registerSimpleBlock(woodBlocks.getPressurePlateBlock().getBlock(), data);
        registerSimpleBlock(woodBlocks.getButtonBlock().getBlock(), data);
        this.tag(BlockTags.WOODEN_FENCES).add(woodBlocks.getFenceBlock().getBlock());
        this.tag(BlockTags.FENCE_GATES).add(woodBlocks.getFenceGateBlock().getBlock());
    }

    private void registerSimpleBlock(Block block, BlockDefinition data) {
        for (var tool : data.toolTypes()) {
            var key = switch (tool) {
                case PICKAXE -> BlockTags.MINEABLE_WITH_PICKAXE;
                case AXE -> BlockTags.MINEABLE_WITH_AXE;
                case SHOVEL -> BlockTags.MINEABLE_WITH_SHOVEL;
                case HOE -> BlockTags.MINEABLE_WITH_HOE;
                default -> null;
            };

            if (key != null) {
                this.tag(key).add(block);
            }
        }

        if (data.isOre()) {
            this.tag(Tags.Blocks.ORES).add(block);
        }
    }
}

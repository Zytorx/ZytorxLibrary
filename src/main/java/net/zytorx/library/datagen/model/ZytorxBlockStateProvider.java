package net.zytorx.library.datagen.model;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.zytorx.library.block.NormalBlockCollection;
import net.zytorx.library.block.SimpleBlockCollection;
import net.zytorx.library.block.WoodBlockCollection;
import net.zytorx.library.datagen.reflection.FieldCollector;
import net.zytorx.library.datagen.reflection.annotations.BlockDefinition;
import net.zytorx.library.registry.RegisteredBlock;

public abstract class ZytorxBlockStateProvider extends BlockStateProvider {

    protected final String modid;
    private final Class<?>[] classes;

    public ZytorxBlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper, Class<?>... classes) {
        super(gen, modid, exFileHelper);
        this.modid = modid;
        this.classes = classes;
    }

    protected abstract void addStatesAndModels();

    @Override
    protected final void registerStatesAndModels() {
        registerSimpleBlocks();
        addStatesAndModels();
    }

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
        var base = simpleBlocks.getStandardBlock().getBlock();
        registerBlock(base, data);
        simpleSlabBlock((SlabBlock) simpleBlocks.getSlabBlock().getBlock(), base);
        simpleStairsBlock((StairBlock) simpleBlocks.getStairsBlock().getBlock(), base);
    }

    private void registerNormalBlockCollection(NormalBlockCollection normalBlocks, BlockDefinition data) {
        registerSimpleBlockCollection(normalBlocks, data);
        var base = normalBlocks.getStandardBlock().getBlock();
        simpleWallBlock((WallBlock) normalBlocks.getWallBlock().getBlock(), base);
    }

    private void registerWoodBlockCollection(WoodBlockCollection woodBlocks, BlockDefinition data) {
        registerSimpleBlockCollection(woodBlocks, data);
        var base = woodBlocks.getStandardBlock().getBlock();
        simpleFenceBlock((FenceBlock) woodBlocks.getFenceBlock().getBlock(), base);
        simpleFenceGateBlock((FenceGateBlock) woodBlocks.getFenceGateBlock().getBlock(), base);
        simpleButtonBlock((ButtonBlock) woodBlocks.getButtonBlock().getBlock(), base);
        simplePressurePlateBlock((PressurePlateBlock) woodBlocks.getPressurePlateBlock().getBlock(), base);
        simpleDoorBlock((DoorBlock) woodBlocks.getDoorBlock().getBlock());
        simpleTrapDoorBlock((TrapDoorBlock) woodBlocks.getTrapdoorBlock().getBlock());
    }

    private void registerBlock(Block block, BlockDefinition data) {
        if (data.hasCustomModel()) {
            if (data.hasItem()) {
                simpleBlockItem(block, cubeAll(block));
            }
            return;
        }

        if (!data.hasItem()) {
            simpleBlock(block);
            return;
        }
        simpleBlockAndItem(block);
    }

    protected void simpleBlockAndItem(Block block) {
        var model = cubeAll(block);
        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }

    protected void simpleStairsBlock(StairBlock stairs, Block base) {
        var texture = blockTexture(base);
        complexStairsBlock(stairs, texture, texture, texture);
    }

    protected void simpleSlabBlock(SlabBlock slab, Block base) {
        var texture = blockTexture(base);
        complexSlabBlock(slab, base, texture, texture, texture);
    }

    protected void complexSlabBlock(SlabBlock slab, Block base, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        var name = name(slab);
        var bottomModel = models().slab(name, side, bottom, top);
        var topModel = models().slabTop(name + "_top", side, bottom, top);
        var doubleModel = models().getExistingFile(base.getRegistryName());
        slabBlock(slab, bottomModel, topModel, doubleModel);
        simpleBlockItem(slab, bottomModel);
    }

    protected void complexStairsBlock(StairBlock stairs, ResourceLocation side, ResourceLocation bottom,
                                      ResourceLocation top) {
        var baseName = name(stairs);

        var stairsModel = models().stairs(baseName, side, bottom, top);
        var stairsInner = models().stairsInner(baseName + "_inner", side, bottom, top);
        var stairsOuter = models().stairsOuter(baseName + "_outer", side, bottom, top);
        stairsBlock(stairs, stairsModel, stairsInner, stairsOuter);
        simpleBlockItem(stairs, stairsModel);
    }

    protected void simpleFenceBlock(FenceBlock fence, Block base) {
        var texture = blockTexture(base);
        complexFenceBlock(fence, texture);
    }

    protected void complexFenceBlock(FenceBlock fence, ResourceLocation texture) {
        var name = name(fence);
        var model = models().fenceInventory(name, texture);

        fenceBlock(fence, texture);
        simpleBlockItem(fence, model);
    }

    protected void simpleFenceGateBlock(FenceGateBlock fence, Block base) {
        var texture = blockTexture(base);
        complexFenceGateBlock(fence, texture);
    }

    protected void complexFenceGateBlock(FenceGateBlock fence, ResourceLocation texture) {
        var name = name(fence);
        var model = models().fenceGate(name, texture);

        fenceGateBlock(fence, texture);
        simpleBlockItem(fence, model);
    }

    protected void simpleWallBlock(WallBlock wall, Block base) {
        var texture = blockTexture(base);
        complexWallBlock(wall, texture);
    }

    protected void complexWallBlock(WallBlock wall, ResourceLocation texture) {
        var name = name(wall);
        var model = models().wallInventory(name, texture);

        wallBlock(wall, texture);
        simpleBlockItem(wall, model);
    }

    protected void simpleButtonBlock(ButtonBlock wall, Block base) {
        var texture = blockTexture(base);
        complexButtonBlock(wall, texture);
    }

    protected void complexButtonBlock(ButtonBlock button, ResourceLocation texture) {
        var name = name(button);
        var model = models().buttonInventory(name + "_inventory", texture);

        buttonBlock(button, texture);
        simpleBlockItem(button, model);
    }

    protected void simplePressurePlateBlock(PressurePlateBlock pressurePlate, Block base) {
        var texture = blockTexture(base);
        complexPressurePlateBlock(pressurePlate, texture);
    }

    protected void complexPressurePlateBlock(PressurePlateBlock pressurePlate, ResourceLocation texture) {
        var name = name(pressurePlate);
        var model = models().pressurePlate(name, texture);

        pressurePlateBlock(pressurePlate, texture);
        simpleBlockItem(pressurePlate, model);
    }

    protected void simpleDoorBlock(DoorBlock door) {

        var location = location(door);

        var bottom = blockTexture(location + "_bottom");
        var top = blockTexture(location + "_top");

        doorBlock(door, bottom, top);
    }

    protected void simpleTrapDoorBlock(TrapDoorBlock trapDoor) {

        var texture = blockTexture(trapDoor);
        var model = models().trapdoorBottom(name(trapDoor) + "_bottom", texture);

        trapdoorBlock(trapDoor, texture, true);
        simpleBlockItem(trapDoor, model);
    }

    protected String name(Block block) {
        return block.getRegistryName().toString();
    }

    protected String location(Block block) {
        return block.getRegistryName().getPath();
    }

    protected ResourceLocation blockTexture(String blockName) {
        return new ResourceLocation(modid, ModelProvider.BLOCK_FOLDER + "/" + blockName);
    }
}

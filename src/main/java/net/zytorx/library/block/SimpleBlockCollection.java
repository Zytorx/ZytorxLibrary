package net.zytorx.library.block;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.zytorx.library.registry.RegisteredBlock;
import net.zytorx.library.registry.RegisteredTab;
import net.zytorx.library.registry.Registrar;

import java.util.function.Supplier;

public class SimpleBlockCollection implements BlockCollection {

    protected static final String STANDARD_SUFFIX = "_block";
    protected static final String STAIRS_SUFFIX = "_stairs";
    protected static final String SLAB_SUFFIX = "_slab";

    protected final RegisteredBlock standardBlock;
    protected final RegisteredBlock stairsBlock;


    protected final RegisteredBlock slabBlock;

    public SimpleBlockCollection(Registrar registrar, String name, Supplier<Block> block, RegisteredTab tab) {
        this(registrar,name,block,tab,-1);

    }

    public SimpleBlockCollection(Registrar registrar, String name, Supplier<Block> block, RegisteredTab tab, int tabPos) {
        var nameWithoutSuffix = getNameWithoutSuffix(name);


        standardBlock = registrar.createBlock(nameWithoutSuffix + STANDARD_SUFFIX, block, tab,tabPos);
        stairsBlock = registrar.createBlock(nameWithoutSuffix + STAIRS_SUFFIX,
                () -> new StairBlock(
                        () -> standardBlock.getBlock().defaultBlockState(),
                        BlockBehaviour.Properties.copy(standardBlock.getBlock())),
                tab,tabPos == -1?tabPos:tabPos+1);
        slabBlock = registrar.createBlock(nameWithoutSuffix + SLAB_SUFFIX,
                () -> new SlabBlock(BlockBehaviour.Properties.copy(standardBlock.getBlock())),
                tab,tabPos == -1?tabPos:tabPos+2);
    }

    protected String getNameWithoutSuffix(String name) {
        if (name.endsWith(STANDARD_SUFFIX)) {
            return name.substring(0, name.length() - STANDARD_SUFFIX.length());
        }
        return name;
    }

    public RegisteredBlock getStandardBlock() {
        return standardBlock;
    }

    public RegisteredBlock getStairsBlock() {
        return stairsBlock;
    }

    public RegisteredBlock getSlabBlock() {
        return slabBlock;
    }
}

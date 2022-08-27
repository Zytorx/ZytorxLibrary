package net.zytorx.library.block;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.zytorx.library.registry.RegisteredBlock;
import net.zytorx.library.registry.Registrar;

import java.util.function.Supplier;

public class NormalBlockCollection extends SimpleBlockCollection {

    protected static final String WALL_SUFFIX = "_wall";
    protected final RegisteredBlock wallBlock;
    
    public NormalBlockCollection(Registrar registrar, String name, Supplier<Block> block, CreativeModeTab tab) {
        super(registrar, name, block, tab);
        var nameWithoutSuffix = getNameWithoutSuffix(name);
        wallBlock = registrar.createBlock(nameWithoutSuffix + WALL_SUFFIX,
                () -> new WallBlock(
                        BlockBehaviour.Properties.copy(standardBlock.getBlock())),
                tab);
    }

    public RegisteredBlock getWallBlock() {
        return wallBlock;
    }
}

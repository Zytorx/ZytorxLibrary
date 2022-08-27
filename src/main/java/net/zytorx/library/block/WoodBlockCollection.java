package net.zytorx.library.block;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.zytorx.library.registry.RegisteredBlock;
import net.zytorx.library.registry.Registrar;

import java.util.function.Supplier;

public class WoodBlockCollection extends SimpleBlockCollection {
    protected static final String FENCE_SUFFIX = "_fence";
    protected static final String FENCE_GATE_SUFFIX = "_fence_gate";
    protected static final String DOOR_SUFFIX = "_door";
    protected static final String TRAPDOOR_SUFFIX = "_trapdoor";
    protected static final String PRESSURE_PLATE_SUFFIX = "_pressure_plate";
    protected static final String BUTTON_SUFFIX = "_button";
    protected final RegisteredBlock fenceBlock;
    protected final RegisteredBlock fenceGateBlock;
    protected final RegisteredBlock doorBlock;
    protected final RegisteredBlock trapdoorBlock;
    protected final RegisteredBlock buttonBlock;
    protected final RegisteredBlock pressurePlateBlock;

    public WoodBlockCollection(Registrar registrar, String name, Supplier<Block> block, CreativeModeTab tab) {
        super(registrar, name, block, tab);
        var nameWithoutSuffix = getNameWithoutSuffix(name);
        fenceBlock = registrar.createBlock(nameWithoutSuffix + FENCE_SUFFIX,
                () -> new FenceBlock(
                        BlockBehaviour.Properties.copy(standardBlock.getBlock())),
                tab);
        fenceGateBlock = registrar.createBlock(nameWithoutSuffix + FENCE_GATE_SUFFIX,
                () -> new FenceGateBlock(
                        BlockBehaviour.Properties.copy(standardBlock.getBlock())),
                tab);
        doorBlock = registrar.createBlock(nameWithoutSuffix + DOOR_SUFFIX,
                () -> new DoorBlock(
                        BlockBehaviour.Properties.copy(standardBlock.getBlock()).noOcclusion()),
                tab);
        trapdoorBlock = registrar.createBlock(nameWithoutSuffix + TRAPDOOR_SUFFIX,
                () -> new TrapDoorBlock(
                        BlockBehaviour.Properties.copy(standardBlock.getBlock()).noOcclusion()),
                tab);
        pressurePlateBlock = registrar.createBlock(nameWithoutSuffix + PRESSURE_PLATE_SUFFIX,
                () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,
                        BlockBehaviour.Properties.copy(standardBlock.getBlock())),
                tab);
        buttonBlock = registrar.createBlock(nameWithoutSuffix + BUTTON_SUFFIX,
                () -> new WoodButtonBlock(
                        BlockBehaviour.Properties.copy(standardBlock.getBlock())),
                tab);
    }

    public RegisteredBlock getFenceBlock() {
        return fenceBlock;
    }

    public RegisteredBlock getFenceGateBlock() {
        return fenceGateBlock;
    }

    public RegisteredBlock getDoorBlock() {
        return doorBlock;
    }

    public RegisteredBlock getTrapdoorBlock() {
        return trapdoorBlock;
    }

    public RegisteredBlock getButtonBlock() {
        return buttonBlock;
    }

    public RegisteredBlock getPressurePlateBlock() {
        return pressurePlateBlock;
    }
}

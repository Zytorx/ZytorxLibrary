package net.zytorx.library.block;

import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.zytorx.library.registry.RegisteredBlock;
import net.zytorx.library.registry.RegisteredTab;
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

    public WoodBlockCollection(Registrar registrar, String name, Supplier<Block> block,WoodType wood, RegisteredTab tab) {
        this(registrar,name,block,wood,tab,-1);
    }
    public WoodBlockCollection(Registrar registrar, String name, Supplier<Block> block,WoodType wood, RegisteredTab tab, int tabPos) {
        super(registrar, name, block, tab, tabPos);
        var nameWithoutSuffix = getNameWithoutSuffix(name);
        fenceBlock = registrar.createBlock(nameWithoutSuffix + FENCE_SUFFIX,
                () -> new FenceBlock(
                        BlockBehaviour.Properties.copy(standardBlock.getBlock())),
                tab,tabPos == -1?tabPos:tabPos+3);
        fenceGateBlock = registrar.createBlock(nameWithoutSuffix + FENCE_GATE_SUFFIX,
                () -> new FenceGateBlock(
                        BlockBehaviour.Properties.copy(standardBlock.getBlock()), wood),
                tab,tabPos == -1?tabPos:tabPos+4);
        doorBlock = registrar.createBlock(nameWithoutSuffix + DOOR_SUFFIX,
                () -> new DoorBlock(
                        BlockBehaviour.Properties.copy(standardBlock.getBlock()).noOcclusion(), wood.setType()),
                tab,tabPos == -1?tabPos:tabPos+5);
        trapdoorBlock = registrar.createBlock(nameWithoutSuffix + TRAPDOOR_SUFFIX,
                () -> new TrapDoorBlock(
                        BlockBehaviour.Properties.copy(standardBlock.getBlock()).noOcclusion(), wood.setType()),
                tab,tabPos == -1?tabPos:tabPos+6);
        pressurePlateBlock = registrar.createBlock(nameWithoutSuffix + PRESSURE_PLATE_SUFFIX,
                () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,
                        BlockBehaviour.Properties.copy(standardBlock.getBlock()),wood.setType()),
                tab);
        buttonBlock = registrar.createBlock(nameWithoutSuffix + BUTTON_SUFFIX,
                () -> new ButtonBlock(
                        BlockBehaviour.Properties.copy(standardBlock.getBlock()),wood.setType(),30, true),
                tab,tabPos == -1?tabPos:tabPos+7);
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

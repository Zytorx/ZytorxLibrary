package net.zytorx.library;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ZytorxLibrary.MOD_ID)
public class ZytorxLibrary {
    public static final String MOD_ID = "zytorxlibrary";

    private static final Logger LOGGER = LogUtils.getLogger();


    public ZytorxLibrary() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::setup);

    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM ZytorxLibrary PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}

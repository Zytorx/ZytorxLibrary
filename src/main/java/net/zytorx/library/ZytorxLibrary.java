package net.zytorx.library;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.zytorx.library.registry.Registrar;
import org.slf4j.Logger;

@Mod(ZytorxLibrary.MOD_ID)
public class ZytorxLibrary {
    public static final String MOD_ID = "zytorxlibrary";

    public ZytorxLibrary() {


    }
}

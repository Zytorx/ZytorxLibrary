package net.zytorx.library.datagen.model;

import com.google.common.hash.HashCode;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.HashCache;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.FallbackResourceManager;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.resource.ResourcePackLoader;
import net.zytorx.library.ZytorxLibrary;

import javax.annotation.Nullable;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class ZytorxTextureEnsurer {

    protected static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");
    private final PackOutput output;
    private final ExistingFileHelper exFileHelper;
    private final byte[] defaultBlockTexture;
    private final byte[] defaultItemTexture;

    private CachedOutput cache = null;

    public ZytorxTextureEnsurer(PackOutput output, ExistingFileHelper exFileHelper) {
        this(output, exFileHelper, null, null);
    }

    public ZytorxTextureEnsurer(PackOutput output, ExistingFileHelper exFileHelper, @Nullable ResourceLocation defaultBlockTexture, @Nullable ResourceLocation defaultItemTexture) {
        this.output = output;
        this.exFileHelper = exFileHelper;
        byte[] itemTexture;
        byte[] blockTexture;

        var modFileInfo = ModList.get().getModFileById(ZytorxLibrary.MOD_ID);
        if (modFileInfo == null) {
            throw new RuntimeException("ZytorxLibrary has no Modfile, which is weird, because this class is from said mod");
        }
        var pack = ResourcePackLoader.createPackForMod(modFileInfo);
        var resourceManager = new FallbackResourceManager(TEXTURE.getPackType(), ZytorxLibrary.MOD_ID);
        resourceManager.push(pack);
        try {
            var blockResource = defaultBlockTexture != null ? exFileHelper.getResource(defaultBlockTexture, TEXTURE.getPackType()) : resourceManager.getResource(getTextureLocation(ZytorxBlockStateProvider.blockTexture(ZytorxLibrary.MOD_ID, "default")));
            var itemResource = defaultItemTexture != null ? exFileHelper.getResource(defaultItemTexture, TEXTURE.getPackType()) : resourceManager.getResource(getTextureLocation(ZytorxItemModelProvider.itemTexture(ZytorxLibrary.MOD_ID, "default")));
            blockTexture = blockResource.getInputStream().readAllBytes();
            itemTexture = itemResource.getInputStream().readAllBytes();
            blockResource.close();
            itemResource.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.defaultBlockTexture = blockTexture;
        this.defaultItemTexture = itemTexture;
    }

    public void ensureItemTexture(ResourceLocation textureToEnsure) throws IOException {
        ensureTexture(textureToEnsure, defaultItemTexture);
    }

    public void ensureBlockTexture(ResourceLocation textureToEnsure) throws IOException {
        ensureTexture(textureToEnsure, defaultBlockTexture);
    }

    private void ensureTexture(ResourceLocation texture, byte[] defaultImage) throws IOException {

        if (exFileHelper.exists(texture, TEXTURE)) {
            return;
        }

        Path path = this.output.getOutputFolder().resolve(
                Paths.get(TEXTURE.getPackType().getDirectory(),
                        texture.getNamespace(), TEXTURE.getPrefix(), texture.getPath() + TEXTURE.getSuffix()));

        var hash = HashCode.fromBytes(defaultImage);

        cache.writeIfNeeded(path, defaultImage ,hash);
        exFileHelper.trackGenerated(texture, TEXTURE);

        if (Objects.equals(cache., hash) && Files.exists(path)) {
            return;
        }

        try {
            Files.createDirectories(path.getParent());
            var writer = new BufferedOutputStream(Files.newOutputStream(path));
            writer.write(defaultImage);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ResourceLocation getTextureLocation(ResourceLocation location) {
        if (!location.getPath().endsWith(TEXTURE.getSuffix())) {
            location = new ResourceLocation(location.getNamespace(), location.getPath() + TEXTURE.getSuffix());
        }

        if (!location.getPath().startsWith(TEXTURE.getPrefix())) {
            location = new ResourceLocation(location.getNamespace(), TEXTURE.getPrefix() + "/" + location.getPath());
        }

        return location;
    }

    void setCache(CachedOutput cache) {
        this.cache = cache;
    }
}

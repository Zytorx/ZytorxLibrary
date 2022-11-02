package net.zytorx.library.datagen.model;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
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

import static net.minecraft.data.DataProvider.SHA1;

public class ZytorxTextureEnsurer {

    protected static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");
    private final DataGenerator generator;
    private final ExistingFileHelper exFileHelper;
    private final byte[] defaultBlockTexture;
    private final byte[] defaultItemTexture;

    private HashCache cache = null;

    public ZytorxTextureEnsurer(DataGenerator generator, ExistingFileHelper exFileHelper) {
        this(generator, exFileHelper, null, null);
    }

    public ZytorxTextureEnsurer(DataGenerator generator, ExistingFileHelper exFileHelper, @Nullable ResourceLocation defaultBlockTexture, @Nullable ResourceLocation defaultItemTexture) {
        this.generator = generator;
        this.exFileHelper = exFileHelper;
        byte[] itemTexture;
        byte[] blockTexture;

        var modFileInfo = ModList.get().getModFileById(ZytorxLibrary.MOD_ID);
        if (modFileInfo == null) {
            throw new RuntimeException("ZytorxLibrary has no Modfile, which is weird, because this class is from said mod");
        }
        var pack = ResourcePackLoader.createPackForMod(modFileInfo);
        var resourceManager = new FallbackResourceManager(TEXTURE.getPackType(), ZytorxLibrary.MOD_ID);
        resourceManager.add(pack);
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

    public void ensureItemTexture(ResourceLocation textureToEnsure) {
        ensureTexture(textureToEnsure, defaultItemTexture);
    }

    public void ensureBlockTexture(ResourceLocation textureToEnsure) {
        ensureTexture(textureToEnsure, defaultBlockTexture);
    }

    private void ensureTexture(ResourceLocation texture, byte[] defaultImage) {

        if (exFileHelper.exists(texture, TEXTURE)) {
            return;
        }

        Path path = this.generator.getOutputFolder().resolve(
                Paths.get(TEXTURE.getPackType().getDirectory(),
                        texture.getNamespace(), TEXTURE.getPrefix(), texture.getPath() + TEXTURE.getSuffix()));

        String hash = SHA1.hashBytes(defaultImage).toString();

        cache.putNew(path, hash);
        exFileHelper.trackGenerated(texture, TEXTURE);

        if (Objects.equals(cache.getHash(path), hash) && Files.exists(path)) {
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

    void setCache(HashCache cache) {
        this.cache = cache;
    }
}

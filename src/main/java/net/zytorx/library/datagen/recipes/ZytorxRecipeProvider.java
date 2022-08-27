package net.zytorx.library.datagen.recipes;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.zytorx.library.block.NormalBlockCollection;
import net.zytorx.library.block.SimpleBlockCollection;
import net.zytorx.library.block.WoodBlockCollection;
import net.zytorx.library.datagen.reflection.FieldCollector;
import net.zytorx.library.item.ItemArmorCollection;
import net.zytorx.library.item.ItemToolCollection;

import java.util.List;
import java.util.function.Consumer;

public abstract class ZytorxRecipeProvider extends RecipeProvider implements IConditionBuilder {

    protected final String modid;
    private final Class<?>[] classes;

    public ZytorxRecipeProvider(DataGenerator pGenerator, String modid, Class<?>... classes) {
        super(pGenerator);
        this.modid = modid;
        this.classes = classes;
    }

    protected abstract void createCraftingRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer);

    @Override
    protected final void buildCraftingRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        registerCollections(pFinishedRecipeConsumer);
        createCraftingRecipes(pFinishedRecipeConsumer);
    }

    private void registerCollections(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        FieldCollector.getCollectionsForRecipe(classes)
                .forEach(collection -> registerCollection(pFinishedRecipeConsumer, collection));
    }

    private void registerCollection(Consumer<FinishedRecipe> pFinishedRecipeConsumer, Object collection) {
        if (collection instanceof WoodBlockCollection woodCollection) {
            registerWoodCollection(pFinishedRecipeConsumer, woodCollection);
            return;
        }
        if (collection instanceof NormalBlockCollection normalCollection) {
            registerNormalCollection(pFinishedRecipeConsumer, normalCollection);
            return;
        }
        if (collection instanceof SimpleBlockCollection simpleCollection) {
            registerSimpleCollection(pFinishedRecipeConsumer, simpleCollection);
            return;
        }

        if (collection instanceof ItemToolCollection toolCollection) {
            registerToolCollection(pFinishedRecipeConsumer, toolCollection);
            return;
        }

        if (collection instanceof ItemArmorCollection armorCollection) {
            registerArmorCollection(pFinishedRecipeConsumer, armorCollection);
            return;
        }
    }

    private void registerSimpleCollection(Consumer<FinishedRecipe> pFinishedRecipeConsumer, SimpleBlockCollection collection) {
        slabRecipe(pFinishedRecipeConsumer, collection.getStandardBlock(), collection.getSlabBlock());
        stairsRecipe(pFinishedRecipeConsumer, collection.getStandardBlock(), collection.getStairsBlock());
    }

    private void registerNormalCollection(Consumer<FinishedRecipe> pFinishedRecipeConsumer, NormalBlockCollection collection) {
        registerSimpleCollection(pFinishedRecipeConsumer, collection);
        wallRecipe(pFinishedRecipeConsumer, collection.getStandardBlock(), collection.getWallBlock());
    }

    private void registerWoodCollection(Consumer<FinishedRecipe> pFinishedRecipeConsumer, WoodBlockCollection collection) {
        registerSimpleCollection(pFinishedRecipeConsumer, collection);
        fenceRecipe(pFinishedRecipeConsumer, collection.getStandardBlock(), collection.getFenceBlock());
        fenceGateRecipe(pFinishedRecipeConsumer, collection.getStandardBlock(), collection.getFenceGateBlock());
        doorRecipe(pFinishedRecipeConsumer, collection.getStandardBlock(), collection.getDoorBlock());
        trapdoorRecipe(pFinishedRecipeConsumer, collection.getStandardBlock(), collection.getTrapdoorBlock());
        buttonRecipe(pFinishedRecipeConsumer, collection.getStandardBlock(), collection.getButtonBlock());
    }

    private void registerToolCollection(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemToolCollection collection) {
        swordRecipe(pFinishedRecipeConsumer, collection.getMaterial(), collection.getSword());
        axeRecipe(pFinishedRecipeConsumer, collection.getMaterial(), collection.getAxe());
        pickaxeRecipe(pFinishedRecipeConsumer, collection.getMaterial(), collection.getPickaxe());
        shovelRecipe(pFinishedRecipeConsumer, collection.getMaterial(), collection.getShovel());
        hoeRecipe(pFinishedRecipeConsumer, collection.getMaterial(), collection.getHoe());
    }

    private void registerArmorCollection(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemArmorCollection collection) {
        helmetRecipe(pFinishedRecipeConsumer, collection.getMaterial(), collection.getHelmet());
        chestplateRecipe(pFinishedRecipeConsumer, collection.getMaterial(), collection.getChestplate());
        leggingsRecipe(pFinishedRecipeConsumer, collection.getMaterial(), collection.getLeggings());
        bootsRecipe(pFinishedRecipeConsumer, collection.getMaterial(), collection.getBoots());
    }

    protected void slabRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike material, ItemLike slab) {
        slabBuilder(slab, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(pFinishedRecipeConsumer, new ResourceLocation(modid, getSimpleRecipeName(slab)));
    }

    protected void stairsRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike material, ItemLike stairs) {
        stairBuilder(stairs, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(pFinishedRecipeConsumer, new ResourceLocation(modid, getSimpleRecipeName(stairs)));
    }

    protected void wallRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike material, ItemLike wall) {
        wallBuilder(wall, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(pFinishedRecipeConsumer, new ResourceLocation(modid, getSimpleRecipeName(wall)));
    }

    protected void fenceRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike material, ItemLike fence) {
        fenceBuilder(fence, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(pFinishedRecipeConsumer, new ResourceLocation(modid, getSimpleRecipeName(fence)));
    }

    protected void fenceGateRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike material, ItemLike fenceGate) {
        fenceGateBuilder(fenceGate, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(pFinishedRecipeConsumer, new ResourceLocation(modid, getSimpleRecipeName(fenceGate)));
    }

    protected void doorRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike material, ItemLike door) {
        doorBuilder(door, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(pFinishedRecipeConsumer, new ResourceLocation(modid, getSimpleRecipeName(door)));
    }

    protected void trapdoorRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike material, ItemLike trapDoor) {
        trapdoorBuilder(trapDoor, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(pFinishedRecipeConsumer, new ResourceLocation(modid, getSimpleRecipeName(trapDoor)));
    }

    protected void buttonRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike material, ItemLike button) {
        buttonBuilder(button, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(pFinishedRecipeConsumer, new ResourceLocation(modid, getSimpleRecipeName(button)));
    }

    protected void swordRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike material, ItemLike sword) {
        ShapedRecipeBuilder.shaped(sword).define('#', Ingredient.of(material)).define('s', Ingredient.of(Items.STICK)).pattern("#").pattern("#").pattern("s").unlockedBy(getHasName(material), has(material)).save(pFinishedRecipeConsumer, new ResourceLocation(modid, getSimpleRecipeName(sword)));
    }

    protected void axeRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike material, ItemLike axe) {
        ShapedRecipeBuilder.shaped(axe).define('#', Ingredient.of(material)).define('s', Ingredient.of(Items.STICK)).pattern("##").pattern("#s").pattern(" s").unlockedBy(getHasName(material), has(material)).save(pFinishedRecipeConsumer, new ResourceLocation(modid, getSimpleRecipeName(axe)));
    }

    protected void pickaxeRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike material, ItemLike pickaxe) {
        ShapedRecipeBuilder.shaped(pickaxe).define('#', Ingredient.of(material)).define('s', Ingredient.of(Items.STICK)).pattern("###").pattern(" s ").pattern(" s ").unlockedBy(getHasName(material), has(material)).save(pFinishedRecipeConsumer, new ResourceLocation(modid, getSimpleRecipeName(pickaxe)));
    }

    protected void shovelRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike material, ItemLike shovel) {
        ShapedRecipeBuilder.shaped(shovel).define('#', Ingredient.of(material)).define('s', Ingredient.of(Items.STICK)).pattern("#").pattern("s").pattern("s").unlockedBy(getHasName(material), has(material)).save(pFinishedRecipeConsumer, new ResourceLocation(modid, getSimpleRecipeName(shovel)));
    }

    protected void hoeRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike material, ItemLike hoe) {
        ShapedRecipeBuilder.shaped(hoe).define('#', Ingredient.of(material)).define('s', Ingredient.of(Items.STICK)).pattern("##").pattern(" s").pattern(" s").unlockedBy(getHasName(material), has(material)).save(pFinishedRecipeConsumer, new ResourceLocation(modid, getSimpleRecipeName(hoe)));
    }

    protected void helmetRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike material, ItemLike helmet) {
        ShapedRecipeBuilder.shaped(helmet).define('#', Ingredient.of(material)).pattern("###").pattern("# #").unlockedBy(getHasName(material), has(material)).save(pFinishedRecipeConsumer, new ResourceLocation(modid, getSimpleRecipeName(helmet)));
    }

    protected void chestplateRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike material, ItemLike chestplate) {
        ShapedRecipeBuilder.shaped(chestplate).define('#', Ingredient.of(material)).pattern("# #").pattern("###").pattern("###").unlockedBy(getHasName(material), has(material)).save(pFinishedRecipeConsumer, new ResourceLocation(modid, getSimpleRecipeName(chestplate)));
    }

    protected void leggingsRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike material, ItemLike leggings) {
        ShapedRecipeBuilder.shaped(leggings).define('#', Ingredient.of(material)).pattern("###").pattern("# #").pattern("# #").unlockedBy(getHasName(material), has(material)).save(pFinishedRecipeConsumer, new ResourceLocation(modid, getSimpleRecipeName(leggings)));
    }

    protected void bootsRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike material, ItemLike boots) {
        ShapedRecipeBuilder.shaped(boots).define('#', Ingredient.of(material)).pattern("# #").pattern("# #").unlockedBy(getHasName(material), has(material)).save(pFinishedRecipeConsumer, new ResourceLocation(modid, getSimpleRecipeName(boots)));
    }

    protected void nineBlockStorageRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pUnpacked, ItemLike pPacked) {
        ShapelessRecipeBuilder.shapeless(pUnpacked, 9).requires(pPacked).unlockedBy(getHasName(pPacked), has(pPacked)).save(pFinishedRecipeConsumer, new ResourceLocation(modid, getSimpleRecipeName(pUnpacked)));
        ShapedRecipeBuilder.shaped(pPacked).define('#', pUnpacked).pattern("###").pattern("###").pattern("###").unlockedBy(getHasName(pUnpacked), has(pUnpacked)).save(pFinishedRecipeConsumer, new ResourceLocation(modid, getSimpleRecipeName(pPacked)));
    }

    protected void oreSmeltingAndBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pItems, ItemLike pOutput, float pExperience, int pCookingTime, String pName) {
        oreSmelting(pFinishedRecipeConsumer, pItems, pOutput, 1.0f, pCookingTime, pName, modid);
        oreBlasting(pFinishedRecipeConsumer, pItems, pOutput, 1.0f, pCookingTime / 2, pName, modid);
    }

    protected void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String modid) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pResult, pExperience, pCookingTime, pGroup, "_from_smelting", modid);
    }

    protected void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String modid) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pResult, pExperience, pCookingTime, pGroup, "_from_blasting", modid);
    }

    protected void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, SimpleCookingSerializer<?> pCookingSerializer, List<ItemLike> pIngredients, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName, String modid) {
        for (ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.cooking(Ingredient.of(itemlike), pResult, pExperience, pCookingTime, pCookingSerializer).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike)).save(pFinishedRecipeConsumer, new ResourceLocation(modid, getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike)));
        }
    }

    protected void oreSmeltingAndBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, TagKey<Item> ingredient, ItemLike pOutput, float pExperience, int pCookingTime, String pName) {
        oreSmelting(pFinishedRecipeConsumer, ingredient, pOutput, 1.0f, pCookingTime, pName);
        oreBlasting(pFinishedRecipeConsumer, ingredient, pOutput, 1.0f, pCookingTime / 2, pName);
    }

    protected void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, TagKey<Item> ingredient, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, ingredient, pResult, pExperience, pCookingTime, pGroup, "_from_smelting");
    }

    protected void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, TagKey<Item> ingredient, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, ingredient, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, SimpleCookingSerializer<?> pCookingSerializer, TagKey<Item> ingredient, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        SimpleCookingRecipeBuilder.cooking(Ingredient.of(ingredient), pResult, pExperience, pCookingTime, pCookingSerializer).group(pGroup).unlockedBy(getHasName(ingredient), has(ingredient)).save(pFinishedRecipeConsumer, new ResourceLocation(modid, getItemName(pResult) + pRecipeName + "_" + getTagName(ingredient)));
    }

    protected String getTagName(TagKey<Item> tag) {
        return tag.location().getPath();
    }

    protected String getHasName(TagKey<Item> tag) {
        return "has_" + getTagName(tag);
    }

}


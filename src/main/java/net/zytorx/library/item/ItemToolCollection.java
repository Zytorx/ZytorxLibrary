package net.zytorx.library.item;

import net.minecraft.world.item.*;
import net.zytorx.library.registry.RegisteredItem;
import net.zytorx.library.registry.Registrar;

public class ItemToolCollection implements ItemCollection {

    protected final RegisteredItem material;
    protected final RegisteredItem sword;
    protected final RegisteredItem pickaxe;
    protected final RegisteredItem axe;
    protected final RegisteredItem shovel;
    protected final RegisteredItem hoe;

    public ItemToolCollection(Registrar registrar, RegisteredItem material, Tier tier, CreativeModeTab tab) {
        this(registrar, material.getName(), material, tier, tab);
    }

    public ItemToolCollection(Registrar registrar, String customName, RegisteredItem material, Tier tier, CreativeModeTab tab) {
        this.material = material;
        this.sword = registrar.createItem(customName + "_sword",
                () -> new SwordItem(tier, 3, -2.4F,
                        new Item.Properties()));

        this.pickaxe = registrar.createItem(customName + "_pickaxe",
                () -> new PickaxeItem(tier, 1, -2.8F,
                        new Item.Properties()),tab);

        this.axe = registrar.createItem(customName + "_axe",
                () -> new AxeItem(tier, 6.0F, -3.2F,
                        new Item.Properties()), tab);

        this.shovel = registrar.createItem(customName + "_shovel",
                () -> new ShovelItem(tier, 1.5F, -3.0F,
                        new Item.Properties()), tab);

        this.hoe = registrar.createItem(customName + "_hoe",
                () -> new HoeItem(tier, (int) -tier.getAttackDamageBonus(), -Tiers.NETHERITE.getLevel() + tier.getLevel(),
                        new Item.Properties()), tab);
    }

    public RegisteredItem getMaterial() {
        return material;
    }

    public RegisteredItem getSword() {
        return sword;
    }

    public RegisteredItem getPickaxe() {
        return pickaxe;
    }

    public RegisteredItem getAxe() {
        return axe;
    }

    public RegisteredItem getShovel() {
        return shovel;
    }

    public RegisteredItem getHoe() {
        return hoe;
    }
}

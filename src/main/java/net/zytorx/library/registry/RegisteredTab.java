package net.zytorx.library.registry;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.function.Supplier;

public class RegisteredTab {

    private final RegistryObject<CreativeModeTab> tab;
    private final ArrayList<Supplier<ItemLike>> items = new ArrayList<>();

    public RegisteredTab(Registrar registrar, String name, Supplier<ItemLike> icon){
        this.tab = registerTab(registrar, name, icon);
    }

    private RegistryObject<CreativeModeTab> registerTab(Registrar registrar,String name, Supplier<ItemLike> icon){
        return registrar.registerTab(name, () ->
                CreativeModeTab.builder().icon(() -> new ItemStack(icon.get())).title(Component.translatable("creativetab."+name))
                        .displayItems((parameters, output)-> {
            output.acceptAll(items.stream().map(item -> new ItemStack(item.get() == null ? Items.AIR:item.get())).toList());
                        }
        ).build());
    }

    void addItem(Supplier<ItemLike> item, int pos){

        if(pos < 0){
            items.add(item);
            return;
        }

        items.ensureCapacity(pos+1);
        items.set(pos,item);
    }
}

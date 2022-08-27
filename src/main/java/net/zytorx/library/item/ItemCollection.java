package net.zytorx.library.item;

import net.zytorx.library.registry.RegisteredItem;
import net.zytorx.library.registry.RegistryCollection;

public interface ItemCollection extends RegistryCollection {

    RegisteredItem getMaterial();
}

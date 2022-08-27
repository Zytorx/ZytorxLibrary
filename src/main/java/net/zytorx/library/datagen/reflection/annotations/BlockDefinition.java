package net.zytorx.library.datagen.reflection.annotations;

import net.zytorx.library.datagen.reflection.ToolType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BlockDefinition {

    ToolType[] toolTypes();

    boolean hasItem() default true;

    boolean hasDrops() default true;

    boolean needsSilktouch() default false;

    boolean hasSpecialDrop() default false;

    boolean isOre() default false;

    boolean hasCustomModel() default false;

}

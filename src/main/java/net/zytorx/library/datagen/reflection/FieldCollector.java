package net.zytorx.library.datagen.reflection;

import com.mojang.datafixers.util.Pair;
import net.zytorx.library.datagen.reflection.annotations.CustomRecipe;
import net.zytorx.library.datagen.reflection.annotations.NoDataGen;
import net.zytorx.library.registry.RegistryCollection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

public class FieldCollector {

    public static <T extends Annotation> Stream<Pair<Object, T>> getFieldsWithAnnotation(Class<T> annotation, Collection<Class<?>> classes) {
        var fieldPairs = new ArrayList<Pair<Object, T>>();

        for (Class<?> aClass : classes) {
            Arrays.stream(aClass.getFields())
                    .filter(field -> isFieldValid(field) && field.isAnnotationPresent(annotation))
                    .forEach(field -> {
                        try {
                            fieldPairs.add(Pair.of(field.get(null), field.getAnnotation(annotation)));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        return fieldPairs.stream();

    }

    public static <T extends RegistryCollection> Stream<T> getCollections(Class<T> returnType, Collection<Class<?>> classes) {
        var collections = new ArrayList<T>();
        for (Class<?> aClass : classes) {
            Arrays.stream(aClass.getFields())
                    .filter(field -> isFieldValid(field) && returnType.isAssignableFrom(field.getType()))
                    .forEach(field -> {
                        try {
                            collections.add((T) field.get(null));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        return collections.stream();
    }

    public static Stream<RegistryCollection> getCollections(Collection<Class<?>> classes) {

        var fieldPairs = new ArrayList<RegistryCollection>();
        for (Class<?> aClass : classes) {
            Arrays.stream(aClass.getFields())
                    .filter(field -> isFieldValid(field)
                            && !field.isAnnotationPresent(CustomRecipe.class)
                            && isCollection(field.getType()))
                    .forEach(field -> {
                        try {
                            fieldPairs.add((RegistryCollection) field.get(null));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        return fieldPairs.stream();
    }

    private static boolean isFieldValid(Field field) {
        return field.canAccess(null)
                && !field.isAnnotationPresent(NoDataGen.class);
    }

    private static boolean isCollection(Class<?> type) {
        return RegistryCollection.class.isAssignableFrom(type);
    }
}

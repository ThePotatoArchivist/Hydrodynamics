package archives.tater.hydrodynamics.registry;

import archives.tater.hydrodynamics.Hydrodynamics;
import archives.tater.hydrodynamics.component.TidalCubeFood;

import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;

import java.util.function.Consumer;

public interface HydrodynamicsModComponents {
    private static <T> DataComponentType<T> register(String path, Consumer<DataComponentType.Builder<T>> init) {
        var builder = DataComponentType.<T>builder();
        init.accept(builder);
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Hydrodynamics.id(path), builder.build());
    }

    DataComponentType<TidalCubeFood> TIDAL_CUBE_FOOD = register("tidal_cube_food", builder -> builder
            .persistent(TidalCubeFood.CODEC)
            .networkSynchronized(TidalCubeFood.STREAM_CODEC)
            .cacheEncoding()
    );

    DataComponentType<Integer> TIDAL_CUBE_SIZE = register("tidal_cube/size", builder -> builder
            .persistent(ExtraCodecs.NON_NEGATIVE_INT)
            .networkSynchronized(ByteBufCodecs.VAR_INT)
    );

    static void init() {
        DefaultItemComponentEvents.MODIFY.register(context -> {
            tidalCubeFood(context, HydrodynamicsModItemTags.DEFAULT_TIDAL_CUBE_FOOD_LOW, HydrodynamicsModIntProviders.TIDAL_CUBE_FOOD_LOW);
            tidalCubeFood(context, HydrodynamicsModItemTags.DEFAULT_TIDAL_CUBE_FOOD_MEDIUM, HydrodynamicsModIntProviders.TIDAL_CUBE_FOOD_MEDIUM);
            tidalCubeFood(context, HydrodynamicsModItemTags.DEFAULT_TIDAL_CUBE_FOOD_HIGH, HydrodynamicsModIntProviders.TIDAL_CUBE_FOOD_HIGH);
            tidalCubeFood(context, HydrodynamicsModItemTags.DEFAULT_TIDAL_CUBE_FOOD_ALWAYS_REUSABLE, new TidalCubeFood(ResolvableInt.fromKey(HydrodynamicsModIntProviders.TIDAL_CUBE_FOOD_ALWAYS), false));
        });
    }

    private static void tidalCubeFood(DefaultItemComponentEvents.ModifyContext context, TagKey<Item> tag, ResourceKey<ContextIntProvider> provider) {
        tidalCubeFood(context, tag, new TidalCubeFood(provider));
    }

    @SuppressWarnings("deprecation")
    private static void tidalCubeFood(DefaultItemComponentEvents.ModifyContext context, TagKey<Item> tag, TidalCubeFood food) {
        context.modify(
                item -> item.builtInRegistryHolder().is(tag), (builder, _) ->
                        builder.set(HydrodynamicsModComponents.TIDAL_CUBE_FOOD, food)
        );
    }
}

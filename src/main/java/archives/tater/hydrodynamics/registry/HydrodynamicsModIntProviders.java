package archives.tater.hydrodynamics.registry;

import archives.tater.hydrodynamics.Hydrodynamics;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ints.ConditionalValue;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;

import static net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders.exactly;
import static net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders.weighted;

public interface HydrodynamicsModIntProviders {
    private static ResourceKey<ContextIntProvider> create(String path) {
        return ResourceKey.create(Registries.CONTEXT_INT_PROVIDER, Hydrodynamics.id(path));
    }

    ResourceKey<ContextIntProvider> TIDAL_CUBE_FOOD_LOW = create("tidal_cube_food/low");
    ResourceKey<ContextIntProvider> TIDAL_CUBE_FOOD_MEDIUM = create("tidal_cube_food/medium");
    ResourceKey<ContextIntProvider> TIDAL_CUBE_FOOD_HIGH = create("tidal_cube_food/high");
    ResourceKey<ContextIntProvider> TIDAL_CUBE_FOOD_ALWAYS = create("tidal_cube_food/always");

    static void bootstrap(BootstrapContext<ContextIntProvider> registry) {
        var predicates = registry.lookup(Registries.PREDICATE);

        registry.register(TIDAL_CUBE_FOOD_LOW, tidalCubeFood(predicates, 10));
        registry.register(TIDAL_CUBE_FOOD_MEDIUM, tidalCubeFood(predicates, 30));
        registry.register(TIDAL_CUBE_FOOD_HIGH, tidalCubeFood(predicates, 50));
        registry.register(TIDAL_CUBE_FOOD_ALWAYS, tidalCubeFood(predicates, 100));
    }

    private static ContextIntProvider tidalCubeFood(HolderGetter<LootItemCondition> predicates, int layerIncreaseChance) {
        return new ConditionalValue(
                predicates.getOrThrow(HydrodynamicsModPredicates.CAN_FEED_TIDAL_CUBE),
                layerIncreaseChance >= 100
                        ? exactly(1)
                        : weighted(WeightedList.<Holder<ContextIntProvider>>builder()
                                .add(exactly(1), layerIncreaseChance)
                                .add(exactly(0), 100 - layerIncreaseChance)
                                .build()),
                exactly(0)
        );
    }
}

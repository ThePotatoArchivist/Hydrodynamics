package archives.tater.hydrodynamics.registry;

import archives.tater.hydrodynamics.Hydrodynamics;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import static net.minecraft.advancements.predicates.MinMaxBounds.Ints.atLeast;
import static net.minecraft.advancements.predicates.entity.CubeMobPredicate.sized;
import static net.minecraft.advancements.predicates.entity.EntityPredicate.Builder.entity;
import static net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition.hasProperties;

public interface HydrodyamicsModPredicates {

    private static ResourceKey<LootItemCondition> create(String path) {
        return ResourceKey.create(Registries.PREDICATE, Hydrodynamics.id(path));
    }

    ResourceKey<LootItemCondition> CAN_FEED_TIDAL_CUBE = create("can_feed_tidal_cube");

    static void bootstrap(BootstrapContext<LootItemCondition> registry) {
        registry.register(CAN_FEED_TIDAL_CUBE, hasProperties(LootContext.EntityTarget.THIS, entity().cubeMob(sized(atLeast(5)))).build());
    }
}

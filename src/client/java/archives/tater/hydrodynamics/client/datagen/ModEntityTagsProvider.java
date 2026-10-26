package archives.tater.hydrodynamics.client.datagen;

import archives.tater.hydrodynamics.registry.HydrodynamicsModEntityIds;
import archives.tater.hydrodynamics.registry.HydrodynamicsModEntityTags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityTypeIds;

import java.util.concurrent.CompletableFuture;

public class ModEntityTagsProvider extends FabricTagsProvider.EntityTypeTagsProvider {
    public ModEntityTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(EntityTypeTags.FALL_DAMAGE_IMMUNE)
                .add(HydrodynamicsModEntityIds.TIDAL_CUBE);

        builder(EntityTypeTags.AQUATIC)
                .add(HydrodynamicsModEntityIds.TIDAL_CUBE);

        builder(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
                .add(HydrodynamicsModEntityIds.TIDAL_CUBE);

        builder(EntityTypeTags.NOT_SCARY_FOR_PUFFERFISH)
                .add(HydrodynamicsModEntityIds.TIDAL_CUBE);

        builder(HydrodynamicsModEntityTags.TIDAL_CUBE_CAN_HOLD)
                .forceAddTag(EntityTypeTags.AQUATIC)
                .add(EntityTypeIds.DROWNED)
                .add(EntityTypeIds.FROG);

        builder(HydrodynamicsModEntityTags.REQUIRES_LARGER_TIDAL_CUBE)
                .add(EntityTypeIds.GUARDIAN)
                .add(EntityTypeIds.ELDER_GUARDIAN)
                .add(EntityTypeIds.DOLPHIN)
                .add(EntityTypeIds.SQUID)
                .add(EntityTypeIds.GLOW_SQUID)
                .add(EntityTypeIds.NAUTILUS)
                .add(EntityTypeIds.ZOMBIE_NAUTILUS);

        builder(HydrodynamicsModEntityTags.NON_SITTING_MOUNT)
                .add(HydrodynamicsModEntityIds.TIDAL_CUBE);

        builder(HydrodynamicsModEntityTags.SUBMERGES_PASSENGERS)
                .add(HydrodynamicsModEntityIds.TIDAL_CUBE);

        builder(HydrodynamicsModEntityTags.WATER_BREATHING_SUBMERGED_PASSENGER)
                .addTag(HydrodynamicsModEntityTags.TIDAL_CUBE_CAN_HOLD);
    }
}

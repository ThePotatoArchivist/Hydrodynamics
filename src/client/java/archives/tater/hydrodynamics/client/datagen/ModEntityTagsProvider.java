package archives.tater.hydrodynamics.client.datagen;

import archives.tater.hydrodynamics.registry.HydrodynamicsModEntityIds;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

public class ModEntityTagsProvider extends FabricTagsProvider.EntityTypeTagsProvider {
    public ModEntityTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(EntityTypeTags.FALL_DAMAGE_IMMUNE)
                .add(HydrodynamicsModEntityIds.TIDAL_CUBE);
    }
}

package archives.tater.hydrodynamics.client.datagen;

import archives.tater.hydrodynamics.registry.HydrodynamicsModBlockTags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.references.BlockItemIds;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {
    public ModBlockTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(HydrodynamicsModBlockTags.DEAD_CORAL_BLOCKS).add(
                BlockItemIds.DEAD_TUBE_CORAL_BLOCK,
                BlockItemIds.DEAD_BRAIN_CORAL_BLOCK,
                BlockItemIds.DEAD_BUBBLE_CORAL_BLOCK,
                BlockItemIds.DEAD_FIRE_CORAL_BLOCK,
                BlockItemIds.DEAD_HORN_CORAL_BLOCK
        );

        builder(HydrodynamicsModBlockTags.TIDE_POOLS_AIR_VEGETATION_BASE)
                .addTag(HydrodynamicsModBlockTags.DEAD_CORAL_BLOCKS)
                .add(BlockItemIds.DRIPSTONE_BLOCK);
    }
}

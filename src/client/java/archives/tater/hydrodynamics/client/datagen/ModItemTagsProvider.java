package archives.tater.hydrodynamics.client.datagen;

import archives.tater.hydrodynamics.registry.HydrodynamicsModItemIds;
import archives.tater.hydrodynamics.registry.HydrodynamicsModItemTags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.core.HolderLookup;
import net.minecraft.references.BlockItemIds;
import net.minecraft.references.ItemIds;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends FabricTagsProvider.ItemTagsProvider {
    public ModItemTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(ConventionalItemTags.ENTITY_DRY_BUCKETS)
                .add(HydrodynamicsModItemIds.TIDAL_CUBE_BUCKET);

        builder(HydrodynamicsModItemTags.DEFAULT_TIDAL_CUBE_FOOD_LOW)
                .add(BlockItemIds.SEAGRASS)
                .add(BlockItemIds.KELP);

        builder(HydrodynamicsModItemTags.DEFAULT_TIDAL_CUBE_FOOD_MEDIUM)
                .add(ItemIds.DRIED_KELP)
                .add(BlockItemIds.SEA_PICKLE)
                .add(BlockItemIds.TUBE_CORAL)
                .add(BlockItemIds.BRAIN_CORAL)
                .add(BlockItemIds.BUBBLE_CORAL)
                .add(BlockItemIds.FIRE_CORAL)
                .add(BlockItemIds.HORN_CORAL)
                .add(BlockItemIds.TUBE_CORAL_FAN)
                .add(BlockItemIds.BRAIN_CORAL_FAN)
                .add(BlockItemIds.BUBBLE_CORAL_FAN)
                .add(BlockItemIds.FIRE_CORAL_FAN)
                .add(BlockItemIds.HORN_CORAL_FAN);

        builder(HydrodynamicsModItemTags.DEFAULT_TIDAL_CUBE_FOOD_HIGH)
                .add(BlockItemIds.DRIED_KELP_BLOCK)
                .add(BlockItemIds.TUBE_CORAL_BLOCK)
                .add(BlockItemIds.BRAIN_CORAL_BLOCK)
                .add(BlockItemIds.BUBBLE_CORAL_BLOCK)
                .add(BlockItemIds.FIRE_CORAL_BLOCK)
                .add(BlockItemIds.HORN_CORAL_BLOCK)
                .forceAddTag(ConventionalItemTags.RAW_FISH_FOODS);

        builder(HydrodynamicsModItemTags.DEFAULT_TIDAL_CUBE_FOOD_ALWAYS)
                .add(ItemIds.HEART_OF_THE_SEA);
    }
}

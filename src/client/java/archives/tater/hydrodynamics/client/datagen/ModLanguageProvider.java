package archives.tater.hydrodynamics.client.datagen;

import archives.tater.hydrodynamics.registry.HydrodynamicsModEntities;
import archives.tater.hydrodynamics.registry.HydrodynamicsModItems;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ModLanguageProvider extends FabricLanguageProvider {
    public ModLanguageProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(packOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(HydrodynamicsModEntities.TIDAL_CUBE, "Tidal Cube");
        translationBuilder.add(HydrodynamicsModItems.TIDAL_CUBE_SPAWN_EGG, "Tidal Cube Spawn Egg");
        translationBuilder.add(HydrodynamicsModItems.TIDAL_CUBE_BUCKET, "Bucket of Tidal Cube");
    }
}

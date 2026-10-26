package archives.tater.hydrodynamics.client;

import archives.tater.hydrodynamics.client.datagen.*;
import archives.tater.hydrodynamics.registry.worldgen.HydrodynamicsModBiomes;
import archives.tater.hydrodynamics.registry.HydrodynamicsModIntProviders;
import archives.tater.hydrodynamics.registry.worldgen.HydrodynamicsModFeatures;
import archives.tater.hydrodynamics.registry.worldgen.HydrodynamicsModPlacements;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

import java.util.List;

public class HydrodynamicsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void buildReloadableRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.CONTEXT_INT_PROVIDER, HydrodynamicsModIntProviders::bootstrap);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.BIOME, HydrodynamicsModBiomes::bootstrap);
		registryBuilder.add(Registries.FEATURE, HydrodynamicsModFeatures::bootstrap);
		registryBuilder.add(Registries.PLACED_FEATURE, HydrodynamicsModPlacements::bootstrap);
	}

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		var pack = fabricDataGenerator.createPack();

		pack.addProvider(ModLanguageProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModSoundsProvider::new);

		pack.addProvider(BuiltRegistryProvider.provider(List.of(
				Registries.CONTEXT_INT_PROVIDER,
				Registries.BIOME,
				Registries.FEATURE,
				Registries.PLACED_FEATURE
		)));
		pack.addProvider(ModLithostitchedProvider::new);
		pack.addProvider(ModItemTagsProvider::new);
		pack.addProvider(ModBlockTagsProvider::new);
		pack.addProvider(ModEntityLootProvider::new);
		pack.addProvider(ModEntityTagsProvider::new);
		pack.addProvider(ModBiomeTagsProvider::new);
	}
}

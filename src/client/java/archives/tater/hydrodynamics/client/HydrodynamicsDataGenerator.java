package archives.tater.hydrodynamics.client;

import archives.tater.hydrodynamics.client.datagen.ModEntityLootProvider;
import archives.tater.hydrodynamics.client.datagen.ModItemTagsProvider;
import archives.tater.hydrodynamics.client.datagen.ModLanguageProvider;
import archives.tater.hydrodynamics.client.datagen.BuiltRegistryProvider;
import archives.tater.hydrodynamics.registry.HydrodyamicsModPredicates;
import archives.tater.hydrodynamics.registry.HydrodynamicsModIntProviders;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

import java.util.List;

public class HydrodynamicsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void buildReloadableRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.CONTEXT_INT_PROVIDER, HydrodynamicsModIntProviders::bootstrap);
		registryBuilder.add(Registries.PREDICATE, HydrodyamicsModPredicates::bootstrap);
	}

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		var pack = fabricDataGenerator.createPack();

		pack.addProvider(ModLanguageProvider::new);

		pack.addProvider(BuiltRegistryProvider.provider(List.of(
				Registries.CONTEXT_INT_PROVIDER,
				Registries.PREDICATE
		)));
		pack.addProvider(ModItemTagsProvider::new);
		pack.addProvider(ModEntityLootProvider::new);
	}
}

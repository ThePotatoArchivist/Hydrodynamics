package archives.tater.hydrodynamics.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class BuiltRegistryProvider extends FabricDynamicRegistryProvider {
    private final Collection<ResourceKey<? extends Registry<?>>> registryKeys;

    public BuiltRegistryProvider(Collection<ResourceKey<? extends Registry<?>>> registryKeys, FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
        this.registryKeys = registryKeys;
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        for (var key : registryKeys)
            entries.addAll(registries.lookupOrThrow(key));
    }

    @Override
    public String getName() {
        return "Dynamic Registries";
    }

    public static FabricDataGenerator.Pack.RegistryDependentFactory<FabricDynamicRegistryProvider> provider(Collection<ResourceKey<? extends Registry<?>>> registryKeys) {
        return (output, registriesFuture) -> new BuiltRegistryProvider(registryKeys, output, registriesFuture);
    }
}

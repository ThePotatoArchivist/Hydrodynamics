package archives.tater.hydrodynamics.client.datagen;

import archives.tater.hydrodynamics.registry.HydrodynamicsModEntities;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricEntityLootSubProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.world.level.storage.loot.LootPool.lootPool;
import static net.minecraft.world.level.storage.loot.LootTable.lootTable;
import static net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem;
import static net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction.lootingMultiplier;
import static net.minecraft.world.level.storage.loot.functions.SetItemCountFunction.setCount;
import static net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders.between;
import static net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders.between;

public class ModEntityLootProvider extends FabricEntityLootSubProvider {
    public ModEntityLootProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate() {
        add(HydrodynamicsModEntities.TIDAL_CUBE, lootTable()
                .withPool(lootPool()
                        .add(lootTableItem(Items.KELP)
                                .apply(setCount(between(0, 2)))
                                .apply(lootingMultiplier(enchantments, between(0f, 1f)))
                        )
                )
        );
    }
}

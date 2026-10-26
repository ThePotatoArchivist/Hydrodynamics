package archives.tater.hydrodynamics.registry;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Function;

public interface HydrodynamicsModItems {
    private static Item register(ResourceKey<Item> key, Function<Item.Properties, Item> item, Item.Properties properties) {
        return Registry.register(BuiltInRegistries.ITEM, key, item.apply(properties.setId(key)));
    }

    Item TIDAL_CUBE_SPAWN_EGG = register(HydrodynamicsModItemIds.TIDAL_CUBE_SPAWN_EGG, SpawnEggItem::new, new Item.Properties().spawnEgg(HydrodynamicsModEntities.TIDAL_CUBE));

    Item TIDAL_CUBE_BUCKET = register(HydrodynamicsModItemIds.TIDAL_CUBE_BUCKET, properties -> new MobBucketItem(
            HydrodynamicsModEntities.TIDAL_CUBE,
            Fluids.EMPTY,
            HydrodynamicsModSounds.BUCKET_EMPTY_TIDAL_CUBE,
            properties
    ), new Item.Properties()
            .stacksTo(1)
            .component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)
    );

    static void init() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS).register(output -> {
            output.insertAfter(Items.SULFUR_CUBE_SPAWN_EGG, TIDAL_CUBE_SPAWN_EGG);
        });
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(output -> {
            output.insertAfter(Items.SULFUR_CUBE_BUCKET, TIDAL_CUBE_BUCKET);
        });
    }
}

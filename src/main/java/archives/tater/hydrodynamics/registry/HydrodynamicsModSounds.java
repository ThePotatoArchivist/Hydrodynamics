package archives.tater.hydrodynamics.registry;

import archives.tater.hydrodynamics.Hydrodynamics;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public interface HydrodynamicsModSounds {
    private static SoundEvent register(String path) {
        return register(Hydrodynamics.id(path));
    }

    private static SoundEvent register(Identifier id) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    SoundEvent TIDAL_CUBE_JUMP = register("entity.tidal_cube.jump");
    SoundEvent TIDAL_CUBE_HURT = register("entity.tidal_cube.hurt");
    SoundEvent TIDAL_CUBE_DEATH = register("entity.tidal_cube.death");
    SoundEvent TIDAL_CUBE_SQUISH = register("entity.tidal_cube.squish");
    SoundEvent TIDAL_CUBE_EAT = register("entity.tidal_cube.eat");
    SoundEvent BUCKET_EMPTY_TIDAL_CUBE = register("item.bucket.empty_tidal_cube");
    SoundEvent BUCKET_FILL_TIDAL_CUBE = register("item.bucket.fill_tidal_cube");

    static void init() {

    }
}

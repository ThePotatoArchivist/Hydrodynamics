package archives.tater.hydrodynamics.client.datagen;

import archives.tater.hydrodynamics.registry.HydrodynamicsModSounds;

import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvents;

import java.util.concurrent.CompletableFuture;

import static net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder.RegistrationBuilder.ofEvent;

public class ModSoundsProvider extends FabricSoundsProvider {
    public ModSoundsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    public static final String TIDAL_CUBE_SPLASH = "subtitles.hydrodynamics.entity.tidal_cube.splash";

    @Override
    protected void configure(HolderLookup.Provider registryLookup, SoundExporter exporter) {
        exporter.add(HydrodynamicsModSounds.TIDAL_CUBE_JUMP, SoundTypeBuilder.of()
                .sound(ofEvent(SoundEvents.SLIME_JUMP))
                .subtitle(TIDAL_CUBE_SPLASH)
        );
        exporter.add(HydrodynamicsModSounds.TIDAL_CUBE_HURT, SoundTypeBuilder.of(HydrodynamicsModSounds.TIDAL_CUBE_HURT)
                .sound(ofEvent(SoundEvents.SLIME_HURT))
        );
        exporter.add(HydrodynamicsModSounds.TIDAL_CUBE_DEATH, SoundTypeBuilder.of(HydrodynamicsModSounds.TIDAL_CUBE_DEATH)
                .sound(ofEvent(SoundEvents.SLIME_DEATH))
        );
        exporter.add(HydrodynamicsModSounds.TIDAL_CUBE_SQUISH, SoundTypeBuilder.of()
                .sound(ofEvent(SoundEvents.SLIME_SQUISH))
                .subtitle(TIDAL_CUBE_SPLASH)
        );
        exporter.add(HydrodynamicsModSounds.TIDAL_CUBE_EAT, SoundTypeBuilder.of(HydrodynamicsModSounds.TIDAL_CUBE_EAT)
                .sound(ofEvent(SoundEvents.SULFUR_CUBE_SMALL_EAT))
        );
        exporter.add(HydrodynamicsModSounds.BUCKET_EMPTY_TIDAL_CUBE, SoundTypeBuilder.of()
                .sound(ofEvent(SoundEvents.BUCKET_EMPTY_SULFUR_CUBE))
                .subtitle("subtitles.item.bucket.empty")
        );
        exporter.add(HydrodynamicsModSounds.BUCKET_FILL_TIDAL_CUBE, SoundTypeBuilder.of(HydrodynamicsModSounds.BUCKET_FILL_TIDAL_CUBE)
                .sound(ofEvent(SoundEvents.BUCKET_FILL_SULFUR_CUBE))
        );
    }

    @Override
    public String getName() {
        return "Sounds";
    }
}

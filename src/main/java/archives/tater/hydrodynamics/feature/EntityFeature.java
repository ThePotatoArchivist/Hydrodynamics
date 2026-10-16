package archives.tater.hydrodynamics.feature;

import archives.tater.hydrodynamics.Hydrodynamics;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.storage.TagValueInput;

public record EntityFeature(EntityType<?> entity, CompoundTag tag) implements Feature {
    public static final CompoundTag EMPTY_TAG = new CompoundTag();

    public static final MapCodec<EntityFeature> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            EntityType.CODEC.fieldOf("entity").forGetter(EntityFeature::entity),
            CompoundTag.CODEC.optionalFieldOf("tag", EMPTY_TAG).forGetter(EntityFeature::tag)
    ).apply(instance, EntityFeature::new));

    public EntityFeature(EntityType<?> entity) {
        this(entity, EMPTY_TAG);
    }

    @Override
    public MapCodec<? extends Feature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
        var realLevel = switch (level) {
            case Level level1 -> level1;
            case WorldGenRegion worldGenRegion -> worldGenRegion.getLevel();
            default -> null;
        };
        if (realLevel == null) return false;

        try (var reporter = new ProblemReporter.ScopedCollector(Hydrodynamics.LOGGER)) {
            var entity = EntityType.create(entity(), TagValueInput.create(reporter, level.registryAccess(), tag), realLevel, EntitySpawnReason.CHUNK_GENERATION).orElse(null);
            if (entity == null) return false;
            entity.snapTo(origin, entity.getYRot(), entity.getXRot());
            if (entity instanceof Mob mob)
                mob.finalizeSpawn(level, level.getCurrentDifficultyAt(origin), EntitySpawnReason.CHUNK_GENERATION, null);
            level.addFreshEntity(entity);
            return true;
        }
    }
}

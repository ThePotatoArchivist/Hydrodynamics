package archives.tater.hydrodynamics.feature;

import archives.tater.hydrodynamics.Hydrodynamics;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
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
        var serverLevel = level.getLevel();
        try (var reporter = new ProblemReporter.ScopedCollector(Hydrodynamics.LOGGER)) {
            var entity = EntityType.create(entity(), TagValueInput.create(reporter, level.registryAccess(), tag), serverLevel, EntitySpawnReason.CHUNK_GENERATION).orElse(null);
            if (entity == null) return false;
            entity.snapTo(origin, entity.getYRot(), entity.getXRot());
            if (entity instanceof Mob mob)
                mob.finalizeSpawn(serverLevel, level.getCurrentDifficultyAt(origin), EntitySpawnReason.CHUNK_GENERATION, null);
            serverLevel.addFreshEntity(entity);
            return true;
        }
    }
}

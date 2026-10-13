package archives.tater.hydrodynamics.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;

public record TidalCubeFood(ResolvableInt sizes, boolean consume) {

    public static final Codec<TidalCubeFood> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResolvableInt.CODEC.fieldOf("sizes").forGetter(TidalCubeFood::sizes),
            Codec.BOOL.optionalFieldOf("consume", true).forGetter(TidalCubeFood::consume)
    ).apply(instance, TidalCubeFood::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, TidalCubeFood> STREAM_CODEC = StreamCodec.composite(
            ResolvableInt.STREAM_CODEC, TidalCubeFood::sizes,
            ByteBufCodecs.BOOL, TidalCubeFood::consume,
            TidalCubeFood::new
    );

    public TidalCubeFood(ResolvableInt sizes) {
        this(sizes, true);
    }

    public TidalCubeFood(final ResourceKey<ContextIntProvider> sizes) {
        this(ResolvableInt.fromKey(sizes));
    }
}

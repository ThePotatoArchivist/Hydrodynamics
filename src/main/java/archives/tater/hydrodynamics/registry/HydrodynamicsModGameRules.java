package archives.tater.hydrodynamics.registry;

import archives.tater.hydrodynamics.Hydrodynamics;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;

import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;

public interface HydrodynamicsModGameRules {
    GameRule<Integer> TIDAL_CUBE_MAX_SIZE = GameRuleBuilder.forInteger(6)
            .category(GameRuleCategory.MOBS)
            .buildAndRegister(Hydrodynamics.id("tidal_cube_max_size"));

    static void init() {

    }
}

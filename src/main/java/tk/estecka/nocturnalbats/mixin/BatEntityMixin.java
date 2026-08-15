package tk.estecka.nocturnalbats.mixin;

import net.minecraft.core.BlockPos;
// import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.level.LevelAccessor;
// import net.minecraft.world.level.LightLayer;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Bat.class)
public class BatEntityMixin
{
	// static private final Logger LOGGER = LoggerFactory.getLogger("nocturnal-bats");
	// static private long nextLog = 0;

	@Redirect(
		method = "checkBatSpawnRules",
		at = @At(
			value = "INVOKE",
			ordinal = 0,
			target = "net/minecraft/core/BlockPos.getY()I"
		)
	)
	static private int SpawnAtAllAltitudes(BlockPos spawnPos) {
		return Integer.MIN_VALUE;
	}

	/**
	 * @implNote Bats need a light level lesser than 4 to spawn, this requires
	 * an ambient darkness of at least 12. At dusk and dawn, the darkness is 6
	 * in vanilla. Only skylight is affected  by ambient darkness. Block light
	 * levels are unaffected.
	 */
	@Redirect(
		method = "checkBatSpawnRules",
		at = @At(
			value = "INVOKE",
			target = "net/minecraft/world/level/LevelAccessor.getMaxLocalRawBrightness(Lnet/minecraft/core/BlockPos;)I"
		)
	)
	static private int FearNotSkylight(LevelAccessor world, BlockPos pos) {
		int darkness = world.getSkyDarken() * 12/6;
		int i = world.getMaxLocalRawBrightness(pos, darkness);

		// int skylight = world.getBrightness(LightLayer.SKY, pos);
		// if (skylight > 0 && nextLog < world.getGameTime()){
		// 	var blockState = world.getBlockState(pos.below());
		// 	LOGGER.info(String.format("SKY:%d - DRK:%d = %d", skylight, darkness, i));
		// 	LOGGER.info("Block is Spawnable: {} ({})", blockState.is(BlockTags.BATS_SPAWNABLE_ON), blockState);
		// 	nextLog = world.getGameTime() + 20;
		// }
		
		return i;
	}
}

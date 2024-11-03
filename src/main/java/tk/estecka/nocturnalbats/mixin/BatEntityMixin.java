package tk.estecka.nocturnalbats.mixin;

import net.minecraft.entity.passive.BatEntity;
import net.minecraft.util.math.BlockPos;
// import net.minecraft.world.LightType;
import net.minecraft.world.WorldAccess;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BatEntity.class)
public class BatEntityMixin
{
	// private static final Logger LOGGER = LoggerFactory.getLogger("nocturnal-bats");

	@Redirect( method="canSpawn", at=@At(value="INVOKE", ordinal=0, target="net/minecraft/util/math/BlockPos.getY()I") )
	static private int	SpawnAtAllAltitudes(BlockPos spawnPos) {
		return Integer.MIN_VALUE;
	}

	/**
	 * @implNote Bats need a light level lesser than 4 to spawn, this requires
	 * an ambient darkness of at least 12. At dusk and dawn, the darkness is 6
	 * in vanilla. Only skylight is affected  by ambient darkness. Block light
	 * levels are unaffected.
	 */
	@Redirect( method="canSpawn", at=@At(value="INVOKE", target="net/minecraft/world/WorldAccess.getLightLevel (Lnet/minecraft/util/math/BlockPos;)I") )
	static private int	FearNotSkylight(WorldAccess world, BlockPos pos) {
		int darkness = world.getAmbientDarkness() * 12/6;
		int i = world.getLightLevel(pos, darkness);

		// int Skylight = world.getLightLevel(LightType.SKY, pos);
		// if (Skylight > 0)
		// 	LOGGER.info(String.format("SKY:%d - DRK:%d = %d", Skylight, darkness, i));
		
		return i;
	}
}

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

	@Redirect( method="canSpawn", at=@At(value="INVOKE", target="net/minecraft/world/WorldAccess.getSeaLevel ()I") )
	static private int	SpawnAtAllAltitudes(WorldAccess world) {
		return world.getTopY();
	}

	/**
	 * @implNote Bats need a light level  of less than 4  in order to spawn. The
	 * default  ambient darkness  at dusk  and dawn  is 6. Here  it is  linearly
	 * remapped to 12, the minimum required to allow bat spawns at those times.
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

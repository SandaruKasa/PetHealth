package sandarukasa.pethealth.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.util.Nameable;
import net.minecraft.world.entity.EntityLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin
// https://fabricmc.net/wiki/tutorial:mixin_examples#access_the_this_instance_of_the_class_your_mixin_is_targeting
        implements Nameable, EntityLike, CommandOutput {
    @Inject(
            method = "hasCustomName",
            at = @At("RETURN"),
            cancellable = true
    )
    private void injected(CallbackInfoReturnable<Boolean> cir) {
        final var entity = (Entity) (Object) this;
        if (entity instanceof TameableEntity tameable
                && tameable.isOwner(MinecraftClient.getInstance().player)) {
            cir.setReturnValue(true);
        }
    }
}

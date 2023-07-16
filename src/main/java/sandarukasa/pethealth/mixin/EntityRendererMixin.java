package sandarukasa.pethealth.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
    @Redirect(
            method = "render(Lnet/minecraft/entity/Entity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;getDisplayName()Lnet/minecraft/text/Text;"
            )
    )
    private Text getDisplayName(Entity entity) {
        final var name = entity.getDisplayName();
        if (entity instanceof TameableEntity tameable
                && tameable.isOwner(MinecraftClient.getInstance().player)) {
            final var result = Text.empty();
            result.append(name);
            result.append(" " + fmt(tameable.getHealth()) + "/" + fmt(tameable.getMaxHealth()));
            return result;
        } else {
            return name;
        }
    }

    // https://stackoverflow.com/a/14126736
    private static String fmt(double d) {
        if (d == (long) d) {
            return String.format("%d", (long) d);
        } else {
            return String.format("%s", d);
        }
    }
}

package me.cephetir.bladecore.mixins.render;

import me.cephetir.bladecore.core.config.BladeConfig;
import me.cephetir.bladecore.utils.UwUtils;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FontRenderer.class)
public class FontRendererMixin {
    @ModifyVariable(method = "drawString(Ljava/lang/String;FFIZ)I", at = @At(value = "HEAD"), argsOnly = true)
    private String drawString(String text) {
        return BladeConfig.INSTANCE.getUwuify() ? UwUtils.INSTANCE.uwuify(text) : text;
    }

    @ModifyVariable(method = "getStringWidth", at = @At(value = "HEAD"), argsOnly = true)
    private String getStringWidth(String text) {
        return BladeConfig.INSTANCE.getUwuify() ? UwUtils.INSTANCE.uwuify(text) : text;
    }
}

package me.cephetir.bladecore.mixins.optimization;

import me.cephetir.bladecore.core.config.BladeConfig;
import net.minecraft.client.gui.ServerListEntryNormal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerListEntryNormal.class)
public class ServerListEntryNormalMixin {
    @Inject(method = "drawEntry", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ServerList;saveServerList()V"), cancellable = true)
    private void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, CallbackInfo ci) {
        if (BladeConfig.INSTANCE.getServerlistFix().getValue())
            ci.cancel();
    }
}

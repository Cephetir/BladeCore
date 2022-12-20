package me.cephetir.bladecore.mixins;

import me.cephetir.bladecore.core.event.BladeEventBus;
import me.cephetir.bladecore.core.event.events.SendChatMessageEvent;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public abstract class GuiScreenMixin extends Gui implements GuiYesNoCallback {

    @Inject(method = "sendChatMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessage(String message, boolean addToChat, CallbackInfo ci) {
        if (BladeEventBus.INSTANCE.post(new SendChatMessageEvent(message, addToChat)))
            ci.cancel();
    }
}

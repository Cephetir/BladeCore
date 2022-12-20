package me.cephetir.bladecore.mixins;

import me.cephetir.bladecore.core.event.BladeEventBus;
import me.cephetir.bladecore.core.event.listener.AsyncListener;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(value = EventBus.class, remap = false)
public class EventBusMixin {

    @Inject(method = "post", at = @At("HEAD"), cancellable = true)
    private void postPre(Event event, CallbackInfoReturnable<Boolean> cir) {
        Set<AsyncListener<?>> listenersSet = BladeEventBus.INSTANCE.getSubscribedListenersAsync().get(event.getClass());
        if (listenersSet == null || listenersSet.isEmpty()) BladeEventBus.INSTANCE.postForgePre(event);
        else cir.setReturnValue(BladeEventBus.INSTANCE.postForge(event, (EventBus) (Object) this));
    }

    @Inject(method = "post", at = @At("RETURN"), cancellable = true)
    private void post(Event event, CallbackInfoReturnable<Boolean> cir) {
        BladeEventBus.INSTANCE.postForgePost(event);
        cir.setReturnValue(event.isCancelable() && event.isCanceled());
    }
}

package me.cephetir.bladecore.mixins;

import me.cephetir.bladecore.core.event.BladeEventBus;
import me.cephetir.bladecore.core.event.events.RunGameLoopEvent;
import me.cephetir.bladecore.utils.ShutdownHook;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "shutdown", at = @At("HEAD"))
    private void shutdown(CallbackInfo ci) {
        ShutdownHook.INSTANCE.execute();
    }

    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Timer;updateTimer()V", shift = At.Shift.BEFORE))
    public void runGameLoopStart(CallbackInfo ci) {
        BladeEventBus.INSTANCE.post(new RunGameLoopEvent.Start());
    }

    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;startSection(Ljava/lang/String;)V", ordinal = 2, shift = At.Shift.AFTER))
    public void runGameLoopTickPre(CallbackInfo ci) {
        BladeEventBus.INSTANCE.post(new RunGameLoopEvent.Tick.Pre());
    }

    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", ordinal = 0, shift = At.Shift.BEFORE))
    public void runGameLoopTickPost(CallbackInfo ci) {
        BladeEventBus.INSTANCE.post(new RunGameLoopEvent.Tick.Post());
    }

    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;startSection(Ljava/lang/String;)V", ordinal = 3, shift = At.Shift.AFTER))
    public void runGameLoopRender(CallbackInfo ci) {
        BladeEventBus.INSTANCE.post(new RunGameLoopEvent.Render());
    }

    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;isFramerateLimitBelowMax()Z", shift = At.Shift.BEFORE))
    public void runGameLoopEnd(CallbackInfo ci) {
        BladeEventBus.INSTANCE.post(new RunGameLoopEvent.End());
    }
}

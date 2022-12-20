package me.cephetir.bladecore.mixins;

import io.netty.channel.ChannelHandlerContext;
import me.cephetir.bladecore.core.event.BladeEventBus;
import me.cephetir.bladecore.core.event.events.PacketEvent;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class NetworkManagerMixin {
    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void sendPacketPre(Packet<?> packet, CallbackInfo callbackInfo) {
        if (BladeEventBus.INSTANCE.post(new PacketEvent.Send(packet)))
            callbackInfo.cancel();
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void channelReadPre(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo) {
        if (BladeEventBus.INSTANCE.post(new PacketEvent.Receive(packet)))
            callbackInfo.cancel();
    }
}
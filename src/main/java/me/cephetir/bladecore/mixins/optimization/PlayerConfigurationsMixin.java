package me.cephetir.bladecore.mixins.optimization;

import me.cephetir.bladecore.core.config.BladeConfig;
import me.cephetir.bladecore.utils.threading.BackgroundScope;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.optifine.http.HttpPipeline;
import net.optifine.http.HttpUtils;
import net.optifine.player.PlayerConfiguration;
import net.optifine.player.PlayerConfigurationReceiver;
import net.optifine.player.PlayerConfigurations;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Map;

@Pseudo
@Mixin(value = PlayerConfigurations.class, remap = false)
public abstract class PlayerConfigurationsMixin {
    @Unique
    private static final ArrayList<AbstractClientPlayer> doingPlayerList = new ArrayList<>();

    @Shadow
    private static Map getMapConfigurations() {
        return null;
    }

    @Shadow
    private static boolean reloadPlayerItems;

    @Shadow
    private static long timeReloadPlayerItemsMs;

    @Shadow
    public static void setPlayerConfiguration(String player, PlayerConfiguration pc) {
    }

    @Dynamic
    @Inject(method = "getPlayerConfiguration", at = @At("HEAD"), cancellable = true)
    private static void getPlayerConfiguration(AbstractClientPlayer player, CallbackInfoReturnable<PlayerConfiguration> cir) {
        if (!BladeConfig.INSTANCE.getOptifineCapes().getValue()) return;
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().thePlayer.ticksExisted < 20) {
            cir.setReturnValue(null);
            return;
        }

        if (reloadPlayerItems && System.currentTimeMillis() > timeReloadPlayerItemsMs + 5000L) {
            AbstractClientPlayer currentPlayer = Minecraft.getMinecraft().thePlayer;
            if (currentPlayer != null) {
                setPlayerConfiguration(currentPlayer.getName(), null);
                timeReloadPlayerItemsMs = System.currentTimeMillis();
            }
        }

        String name = player.getName();
        if (name == null) {
            cir.setReturnValue(null);
        } else {
            PlayerConfiguration pc = (PlayerConfiguration) getMapConfigurations().get(name);
            if (pc == null && !doingPlayerList.contains(player)) {
                doingPlayerList.add(player);
                BackgroundScope.INSTANCE.launch("Optifine Cfg Download " + player.getName(), (coroutineScope, continuation) -> {
                    PlayerConfiguration pcc = new PlayerConfiguration();
                    getMapConfigurations().put(name, pcc);
                    PlayerConfigurationReceiver pcl = new PlayerConfigurationReceiver(name);
                    String url = HttpUtils.getPlayerItemsUrl() + "/users/" + name + ".cfg";
                    try {
                        byte[] bytes = HttpPipeline.get(url, Minecraft.getMinecraft().getProxy());
                        pcl.fileDownloadFinished(url, bytes, null);
                    } catch (Exception var2) {
                        pcl.fileDownloadFinished(url, null, var2);
                    }
                    doingPlayerList.remove(player);
                    return null;
                });
            }

            cir.setReturnValue(pc);
        }
    }
}

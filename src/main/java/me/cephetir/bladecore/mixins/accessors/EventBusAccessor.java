package me.cephetir.bladecore.mixins.accessors;

import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.IEventExceptionHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = EventBus.class, remap = false)
public interface EventBusAccessor {
    @Accessor
    int getBusID();

    @Accessor
    IEventExceptionHandler getExceptionHandler();
}

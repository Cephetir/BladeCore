package me.cephetir.bladecore.mixins.accessors;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Render.class)
public interface RenderAccessor<T extends Entity> {
    @Invoker("renderName")
    void renderName(T entity, double x, double y, double z);
}

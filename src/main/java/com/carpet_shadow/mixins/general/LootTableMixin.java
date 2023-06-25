package com.carpet_shadow.mixins.general;

import com.carpet_shadow.CarpetShadowSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(LootTable.class)
public class LootTableMixin {
    @Inject(method = "processStacks", at=@At("RETURN"), cancellable = true)
    private static void fix_survival_shulkers(ServerWorld world, Consumer<ItemStack> lootConsumer, CallbackInfoReturnable<Consumer<ItemStack>> cir) {
        Consumer<ItemStack> consumer = cir.getReturnValue();
        Consumer<ItemStack> ret = itemStack -> {
            if (CarpetShadowSettings.shadowItemMode != CarpetShadowSettings.Mode.UNLINK &&
                    itemStack.getCount() == itemStack.getMaxCount()) {
                lootConsumer.accept(itemStack);
            } else {
                consumer.accept(itemStack);
            }
        };
        cir.setReturnValue(ret);
    }
}

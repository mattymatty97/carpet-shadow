package com.carpet_shadow.mixins.general;

import com.carpet_shadow.CarpetShadowSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@Mixin(LootTable.class)
public class LootTableMixin {
    @ModifyReturnValue(method = "processStacks", at=@At("RETURN"))
    private static Consumer<ItemStack> fix_survival_shulkers(Consumer<ItemStack> consumer, ServerWorld world, Consumer<ItemStack> lootConsumer) {
        return itemStack -> {
            if (CarpetShadowSettings.shadowItemMode != CarpetShadowSettings.Mode.UNLINK &&
                    itemStack.getCount() == itemStack.getMaxCount()) {
                lootConsumer.accept(itemStack);
            } else {
                consumer.accept(itemStack);
            }
        };
    }
}

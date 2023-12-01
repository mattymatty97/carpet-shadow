package com.carpet_shadow;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Rule;
import carpet.api.settings.Validator;
import com.carpet_shadow.utility.RandomString;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;

import static carpet.api.settings.RuleCategory.*;

public class CarpetShadowSettings {
    public static final String SHADOW = "shadow_items";
    @Rule( categories = {SHADOW, BUGFIX})
    public static Mode shadowItemMode = Mode.UNLINK;
    @Rule( categories = {SHADOW}, validators = {IdSizeValidator.class})
    public static int shadowItemIdSize = 5;
    @Rule( categories = {SHADOW, FEATURE})
    public static boolean shadowSuppressionGeneration = false;
    @Rule( categories = {SHADOW, FEATURE})
    public static boolean shadowCraftingGeneration = false;
    @Rule( categories = {SHADOW, FEATURE})
    public static boolean shadowItemTooltip = false;
    @Rule( categories = {SHADOW, BUGFIX})
    public static boolean shadowItemInventoryFragilityFix = false;
    @Rule( categories = {SHADOW, BUGFIX})
    public static boolean shadowItemTransferFragilityFix = false;
    @Rule( categories = {SHADOW, BUGFIX, EXPERIMENTAL})
    public static boolean shadowItemUpdateFix = false;
    @Rule( categories = {SHADOW, OPTIMIZATION, FEATURE})
    public static boolean shadowItemPreventCombine = false;
    @Rule( categories = {SHADOW, OPTIMIZATION, FEATURE})
    public static boolean shadowItemUseFix = false;

    private static class IdSizeValidator extends Validator<Integer> {
        @Override
        public Integer validate(@Nullable ServerCommandSource source, CarpetRule<Integer> changingRule, Integer newValue, String userInput) {
            try {
                CarpetShadow.shadow_id_generator = new RandomString(newValue);
                return newValue;
            } catch (IllegalArgumentException ex) {
                return changingRule.value();
            }
        }
    }

    public enum Mode{
        UNLINK(false,false),
        PERSIST(true,false),
        VANISH(true,true);

        private final boolean shouldLoadItem;
        private final boolean shouldResetCount;

        public boolean shouldLoadItem() {
            return shouldLoadItem;
        }
        public boolean shouldResetCount() {
            return shouldResetCount;
        }

        Mode(boolean shouldLoadItem, boolean shouldResetCount) {
            this.shouldLoadItem = shouldLoadItem;
            this.shouldResetCount = shouldResetCount;
        }
    }
}

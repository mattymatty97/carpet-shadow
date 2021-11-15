package com.carpet_shadow;

import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import carpet.settings.Validator;
import com.carpet_shadow.utility.RandomString;
import net.minecraft.server.command.ServerCommandSource;

import static carpet.settings.RuleCategory.EXPERIMENTAL;
import static carpet.settings.RuleCategory.OPTIMIZATION;

public class CarpetShadowSettings {
    public static final String SHADOW = "shadow_items";
    @Rule(desc = "Shadow Items Action over Unloading ( Unlink is default vanilla )", category = {SHADOW, OPTIMIZATION})
    public static Mode shadowItemMode = Mode.UNLINK;
    @Rule(desc = "Shadow Items Id Length", category = {SHADOW}, validate = {IdSizeValidator.class})
    public static int shadowItemIdSize = 5;
    @Rule(desc = "Show Shadow Items Id in Item Names", category = {SHADOW, EXPERIMENTAL})
    public static boolean shadowItemTooltip = false;
    @Rule(desc = "Prevent Unlinking Shadow Items on base inventory movements", category = {SHADOW, EXPERIMENTAL})
    public static boolean shadowItemFragilityFixes = false;
    @Rule(desc = "Makes Shadow Items produce inventory updates", category = {SHADOW, EXPERIMENTAL})
    public static boolean shadowItemUpdateFix = false;
    @Rule(desc = "Prevent Merging Shadow Items instances on base inventory movements", category = {SHADOW, EXPERIMENTAL})
    public static boolean shadowItemPreventCombine = false;

    private static class IdSizeValidator extends Validator<Integer> {
        @Override
        public Integer validate(ServerCommandSource source, ParsedRule<Integer> currentRule, Integer newValue, String string) {
            try {
                CarpetShadow.shadow_id_generator = new RandomString(newValue);
                return newValue;
            } catch (IllegalArgumentException ex) {
                return currentRule.get();
            }
        }
    }

    public enum Mode{
        UNLINK(false,false,false),
        PERSIST(true,true,false),
        VANISH(true,true,true);

        private final boolean shouldLoadItem;
        private final boolean shouldWriteItem;
        private final boolean shouldResetCount;

        public boolean shouldLoadItem() {
            return shouldLoadItem;
        }

        public boolean shouldWriteItem() {
            return shouldWriteItem;
        }

        public boolean shouldResetCount() {
            return shouldResetCount;
        }

        Mode(boolean shouldLoadItem, boolean shouldWriteItem, boolean shouldResetCount) {
            this.shouldLoadItem = shouldLoadItem;
            this.shouldWriteItem = shouldWriteItem;
            this.shouldResetCount = shouldResetCount;
        }
    }
}

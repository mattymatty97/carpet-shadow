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
    @Rule(desc = "Shadow Items Persistence over Serialization", category = {SHADOW, EXPERIMENTAL, OPTIMIZATION})
    public static boolean shadowItemPersistence = false;
    @Rule(desc = "Shadow Items Id Length", category = {SHADOW, EXPERIMENTAL}, validate = {IdSizeValidator.class})
    public static int shadowItemIdSize = 5;

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
}

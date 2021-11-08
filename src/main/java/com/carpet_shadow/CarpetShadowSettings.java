package com.carpet_shadow;

import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import carpet.settings.Validator;
import com.carpet_shadow.utility.RandomString;
import net.minecraft.server.command.ServerCommandSource;

import static carpet.settings.RuleCategory.*;

public class CarpetShadowSettings {
    public static final String SHADOW = "shadow_items";
    @Rule(desc = "Shadow Items Persistence over Serialization", category = { SHADOW, EXPERIMENTAL, OPTIMIZATION })
    public static boolean shadowItemPersistence = false;
    @Rule(desc = "Shadow Items Id Length", category = { SHADOW, EXPERIMENTAL }, validate = {IdSizeValidator.class})
    public static int shadowItemIdSize = 5;
    @Rule(desc = "Show Shadow Items Id in Item Names", category = { SHADOW, EXPERIMENTAL })
    public static boolean shadowItemTooltip = false;
    @Rule(desc = "Prevent Unlinking Shadow Items on base inventory movements", category = { SHADOW, EXPERIMENTAL})
    public static boolean shadowItemFragilityFix = false;
    @Rule(desc = "Prevent Merging Shadow Items instances on base inventory movements", category = { SHADOW, EXPERIMENTAL})
    public static boolean shadowItemPreventCombine = false;

    private static class IdSizeValidator extends Validator<Integer> {
        @Override
        public Integer validate(ServerCommandSource source, ParsedRule<Integer> currentRule, Integer newValue, String string) {
            try{
                CarpetShadow.shadow_id_generator = new RandomString(newValue);
                return newValue;
            }catch (IllegalArgumentException ex){
                return currentRule.get();
            }
        }
    }
}

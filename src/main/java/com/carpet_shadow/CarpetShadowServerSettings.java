package com.carpet_shadow;

import carpet.settings.Rule;

import static carpet.settings.RuleCategory.FEATURE;
import static carpet.settings.RuleCategory.OPTIMIZATION;
import static com.carpet_shadow.CarpetShadowSettings.SHADOW;

@SuppressWarnings({"removal"})
public class CarpetShadowServerSettings {
    @Rule(desc = "Prevents desync betwen client and server when using a fast refilling shadow stack", category = {SHADOW, OPTIMIZATION, FEATURE})
    public static boolean shadowItemUseFix = false;
}

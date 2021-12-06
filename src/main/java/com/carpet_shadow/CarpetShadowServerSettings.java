package com.carpet_shadow;

import carpet.settings.Rule;

import static carpet.settings.RuleCategory.*;
import static com.carpet_shadow.CarpetShadowSettings.*;

public class CarpetShadowServerSettings {
    @Rule(desc = "Prevents desync betwen client and server when using a fast refilling shadow stack", category = {SHADOW, OPTIMIZATION, FEATURE})
    public static boolean shadowItemUseFix = false;
}

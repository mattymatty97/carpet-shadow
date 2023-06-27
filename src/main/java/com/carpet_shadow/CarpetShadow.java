package com.carpet_shadow;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.carpet_shadow.utility.RandomString;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

public class CarpetShadow implements CarpetExtension, ModInitializer {
    public static final Cache<String, ItemStack> shadowMap = CacheBuilder.newBuilder().weakValues().build();
    public static final Logger LOGGER = LogManager.getLogger("carpet-shadow");
    public static RandomString shadow_id_generator = new RandomString(CarpetShadowSettings.shadowItemIdSize);



    @Override
    public void onGameStarted() {
        CarpetShadow.LOGGER.info("Carpet Shadow Loaded!");
        CarpetServer.settingsManager.parseSettingsClass(CarpetShadowSettings.class);
        shadow_id_generator = new RandomString(CarpetShadowSettings.shadowItemIdSize);
    }

    @Override
    public void onInitialize() {
        MixinExtrasBootstrap.init();
        CarpetServer.manageExtension(new CarpetShadow());
        CarpetShadow.LOGGER.info("Carpet Shadow Loading!");
    }

    @Override
    public void onServerClosed(MinecraftServer server) {
        shadowMap.invalidateAll();
    }

    @Override
    public void onTick(MinecraftServer server) {
        if (server.getTicks() % 1000 == 0)
            CarpetShadow.shadowMap.cleanUp();
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        InputStream langFile = getClass().getClassLoader().getResourceAsStream("assets/carpet-shadow/lang/%s.json".formatted(lang));
        if (langFile == null) {
            return Collections.emptyMap();
        }
        String jsonData;
        try {
            jsonData = IOUtils.toString(langFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return Collections.emptyMap();
        }
        Gson gson = new GsonBuilder().setLenient().create();
        return gson.fromJson(jsonData, new TypeToken<Map<String, String>>() {}.getType());
    }
}

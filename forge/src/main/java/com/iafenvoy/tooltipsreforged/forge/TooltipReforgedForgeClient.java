package com.iafenvoy.tooltipsreforged.forge;

import com.iafenvoy.jupiter.render.screen.ConfigContainerScreen;
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.hook.RarityHook;
import com.iafenvoy.tooltipsreforged.util.TooltipKeyManager;
import net.minecraft.text.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TooltipReforgedForgeClient {
    @SubscribeEvent
    public static void onInit(FMLClientSetupEvent event) {
        event.enqueueWork(TooltipReforgedClient::init);
        MinecraftForge.registerConfigScreen(parent -> new ConfigContainerScreen(parent, TooltipReforgedConfig.INSTANCE, true));
        //Register Hooks
        RarityHook.register((text, rarity) -> text.fillStyle(rarity.getStyleModifier().apply(Style.EMPTY)));
    }

    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(TooltipKeyManager.SHOW_ITEM_TAG);
        event.register(TooltipKeyManager.SHOW_NBT_SPAWN_EGG);
        event.register(TooltipKeyManager.SHOW_SPECIFIC_INFO);
    }
}

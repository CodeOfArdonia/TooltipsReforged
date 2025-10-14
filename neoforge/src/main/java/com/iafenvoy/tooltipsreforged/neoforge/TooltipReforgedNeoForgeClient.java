package com.iafenvoy.tooltipsreforged.neoforge;

import com.iafenvoy.jupiter.render.screen.ClientConfigScreen;
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.hook.RarityHook;
import com.iafenvoy.tooltipsreforged.util.TooltipKeyManager;
import net.minecraft.text.Style;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@OnlyIn(Dist.CLIENT)
@Mod(value = TooltipReforgedClient.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(Dist.CLIENT)
public class TooltipReforgedNeoForgeClient {
    public TooltipReforgedNeoForgeClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, (c, parent) -> new ClientConfigScreen(parent, TooltipReforgedConfig.INSTANCE));
    }

    @SubscribeEvent
    public static void onInit(FMLClientSetupEvent event) {
        event.enqueueWork(TooltipReforgedClient::init);
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

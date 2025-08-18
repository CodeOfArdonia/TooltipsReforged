package com.iafenvoy.tooltipsreforged.forge;

import com.iafenvoy.jupiter.render.screen.ClientConfigScreen;
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.hook.RarityHook;
import net.minecraft.text.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TooltipReforgedForgeClient {
    @SuppressWarnings("removal")
    @SubscribeEvent
    public static void onInit(FMLClientSetupEvent event) {
        event.enqueueWork(TooltipReforgedClient::init);
        FMLJavaModLoadingContext.get().getContainer().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory(parent -> new ClientConfigScreen(parent, TooltipReforgedConfig.INSTANCE)));
        //Register Hooks
        RarityHook.register((text, rarity) -> text.fillStyle(rarity.getStyleModifier().apply(Style.EMPTY)));
    }
}

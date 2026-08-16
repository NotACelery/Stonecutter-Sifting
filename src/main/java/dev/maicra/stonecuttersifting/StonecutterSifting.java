package dev.maicra.stonecuttersifting;

import dev.maicra.stonecuttersifting.sifting.SiftingEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(StonecutterSifting.MOD_ID)
public final class StonecutterSifting {
    public static final String MOD_ID = "stonecutter_sifting";

    public StonecutterSifting(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.addListener(SiftingEvents::onEntityTickPost);
    }
}

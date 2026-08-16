package dev.maicra.stonecuttersifting.sifting;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public record SiftingOutput(Supplier<ItemStack> stackSupplier, Component note) {
    public ItemStack stack() {
        return stackSupplier.get().copy();
    }
}

package dev.maicra.stonecuttersifting.sifting;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Function;

public record SiftingTable(
        ResourceLocation id,
        Item input,
        List<SiftingOutput> outputs,
        Component description,
        Function<RandomSource, List<ItemStack>> roller
) {
    public List<ItemStack> roll(RandomSource random) {
        return roller.apply(random);
    }
}

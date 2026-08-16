package dev.maicra.stonecuttersifting.integration.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import dev.maicra.stonecuttersifting.StonecutterSifting;
import dev.maicra.stonecuttersifting.sifting.SiftingTables;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

@EmiEntrypoint
public final class StonecutterSiftingEmiPlugin implements EmiPlugin {
    public static final EmiRecipeCategory SIFTING = new EmiRecipeCategory(
            ResourceLocation.fromNamespaceAndPath(StonecutterSifting.MOD_ID, "stonecutter_sifting"), EmiStack.of(Items.STONECUTTER));

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(SIFTING);
        registry.addWorkstation(SIFTING, EmiStack.of(Items.STONECUTTER));
        for (var table : SiftingTables.TABLES) registry.addRecipe(new SiftingEmiRecipe(table));
    }
}

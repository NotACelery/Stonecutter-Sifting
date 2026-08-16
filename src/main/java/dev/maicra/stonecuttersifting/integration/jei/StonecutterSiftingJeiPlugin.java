package dev.maicra.stonecuttersifting.integration.jei;

import dev.maicra.stonecuttersifting.StonecutterSifting;
import dev.maicra.stonecuttersifting.sifting.SiftingTable;
import dev.maicra.stonecuttersifting.sifting.SiftingTables;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public final class StonecutterSiftingJeiPlugin implements IModPlugin {
    public static final RecipeType<SiftingTable> SIFTING = RecipeType.create(StonecutterSifting.MOD_ID, "stonecutter_sifting", SiftingTable.class);

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(StonecutterSifting.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new SiftingJeiCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(SIFTING, SiftingTables.TABLES);
    }
}

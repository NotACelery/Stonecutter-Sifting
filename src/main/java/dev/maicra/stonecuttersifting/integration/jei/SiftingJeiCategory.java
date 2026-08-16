package dev.maicra.stonecuttersifting.integration.jei;

import dev.maicra.stonecuttersifting.sifting.SiftingTable;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class SiftingJeiCategory implements IRecipeCategory<SiftingTable> {
    private final IDrawable icon;
    public SiftingJeiCategory(IGuiHelper guiHelper) { icon = guiHelper.createDrawableItemStack(new ItemStack(Items.STONECUTTER)); }
    @Override public RecipeType<SiftingTable> getRecipeType() { return StonecutterSiftingJeiPlugin.SIFTING; }
    @Override public Component getTitle() { return Component.translatable("jei.stonecutter_sifting.sifting"); }
    @Override public IDrawable getIcon() { return icon; }
    @Override public int getWidth() { return 154; }
    @Override public int getHeight() { return 100; }
    @Override public void setRecipe(IRecipeLayoutBuilder builder, SiftingTable recipe, IFocusGroup focuses) {
        builder.addInputSlot(6, 15).addItemStack(new ItemStack(recipe.input())).addRichTooltipCallback((slot, tooltip) -> tooltip.add(recipe.description()));
        builder.addSlot(RecipeIngredientRole.CATALYST, 29, 15).addItemStack(new ItemStack(Items.STONECUTTER));
        for (int i = 0; i < recipe.outputs().size(); i++) {
            var output = recipe.outputs().get(i);
            builder.addOutputSlot(59 + (i % 4) * 22, 5 + (i / 4) * 22).addItemStack(output.stack())
                    .addRichTooltipCallback((slot, tooltip) -> tooltip.add(output.note()));
        }
    }
    @Override public void draw(SiftingTable recipe, IRecipeSlotsView slots, GuiGraphics graphics, double mouseX, double mouseY) {
        var font = Minecraft.getInstance().font;
        graphics.drawString(font, "+", 22, 20, 0x404040, false);
        graphics.drawString(font, "→", 48, 20, 0x404040, false);
        graphics.drawString(font, Component.translatable("stonecutter_sifting.viewer.processing"), 6, 56, 0x404040, false);
        int y = 67;
        for (var line : font.split(recipe.description(), 146)) { graphics.drawString(font, line, 6, y, 0x555555, false); y += 9; if (y > 97) break; }
    }
}

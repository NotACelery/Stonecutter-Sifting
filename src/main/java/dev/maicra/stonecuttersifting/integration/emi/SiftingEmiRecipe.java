package dev.maicra.stonecuttersifting.integration.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.maicra.stonecuttersifting.sifting.SiftingTable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.List;

public final class SiftingEmiRecipe implements EmiRecipe {
    private final SiftingTable table;
    private final EmiStack input;
    private final EmiStack stonecutter = EmiStack.of(Items.STONECUTTER);
    private final List<EmiStack> outputs;
    public SiftingEmiRecipe(SiftingTable table) {
        this.table = table;
        input = EmiStack.of(table.input());
        outputs = table.outputs().stream().map(output -> EmiStack.of(output.stack())).toList();
    }
    @Override public EmiRecipeCategory getCategory() { return StonecutterSiftingEmiPlugin.SIFTING; }
    @Override public ResourceLocation getId() { return table.id(); }
    @Override public List<EmiIngredient> getInputs() { return List.of(input); }
    @Override public List<EmiIngredient> getCatalysts() { return List.of(stonecutter); }
    @Override public List<EmiStack> getOutputs() { return outputs; }
    @Override public boolean supportsRecipeTree() { return false; }
    @Override public int getDisplayWidth() { return 154; }
    @Override public int getDisplayHeight() { return 96; }
    @Override public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(input, 6, 13).appendTooltip(table.description());
        widgets.addTexture(EmiTexture.PLUS, 27, 16);
        widgets.addSlot(stonecutter, 36, 13).catalyst(true);
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 58, 13);
        for (int i = 0; i < table.outputs().size(); i++) {
            var output = table.outputs().get(i);
            widgets.addSlot(outputs.get(i), 84 + (i % 3) * 22, 3 + (i / 3) * 22).appendTooltip(output.note()).recipeContext(this);
        }
        widgets.addText(Component.translatable("stonecutter_sifting.viewer.processing"), 6, 74, 0x404040, false);
    }
}

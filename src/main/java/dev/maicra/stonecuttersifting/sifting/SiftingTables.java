package dev.maicra.stonecuttersifting.sifting;

import dev.maicra.stonecuttersifting.StonecutterSifting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/** The single source of truth for all Stonecutter Sifting gameplay and recipe-viewer data. */
public final class SiftingTables {
    private static final TagKey<Item> NON_OVERWORLD_SAPLINGS = TagKey.create(
            Registries.ITEM, ResourceLocation.fromNamespaceAndPath(StonecutterSifting.MOD_ID, "non_overworld_saplings"));

    private static final float SAND_CACTUS = .20F, SAND_SUGAR_CANE = .15F, SAND_DEAD_BUSH = .05F, SAND_TURTLE_EGG = .005F, SAND_SNIFFER_EGG = .0025F;
    private static final float RED_SAND_GOLD_NUGGET = .25F, RED_SAND_DEAD_BUSH = .15F, RED_SAND_CACTUS = .12F, RED_SAND_TERRACOTTA = .07F, RED_SAND_RAW_GOLD = .01F;
    private static final float GRAVEL_BONUS = .50F, GRAVEL_DIAMOND_IN_BONUS = .02F, GRAVEL_SECOND_COMMON = .30F;
    private static final float SOUL_WART = .15F, SOUL_BONE = .20F, SOUL_GOLD = .15F, SOUL_CRIMSON = .05F, SOUL_WARPED = .05F;
    private static final float DIRT_BONEMEAL = .30F, DIRT_SEEDS = .15F, DIRT_SAPLINGS = .15F, DIRT_NOTHING = 1F - DIRT_BONEMEAL - DIRT_SEEDS - DIRT_SAPLINGS, DIRT_WHEAT_IN_SEEDS = .70F, DIRT_OAK_IN_SAPLINGS = .80F;
    private static final float GRAVEL_IRON = .30F, GRAVEL_COPPER = .25F, GRAVEL_COAL = .20F, GRAVEL_GOLD = .15F, GRAVEL_LAPIS = .10F;
    private static final float PODZOL_SPRUCE = .20F, PODZOL_BONEMEAL = .20F;
    private static final float ROOTED_ROOTS = .25F, ROOTED_MOSS = .20F, ROOTED_AZALEA = .10F, ROOTED_FLOWERING_AZALEA = .05F, ROOTED_GLOW_BERRIES = .05F, ROOTED_SMALL_DRIPLEAF = .03F, ROOTED_BIG_DRIPLEAF = .015F, ROOTED_SPORE_BLOSSOM = .005F;
    private static final float MOSS_GLOW_BERRIES = .25F, MOSS_AZALEA = .18F, MOSS_FLOWERING_AZALEA = .10F, MOSS_SMALL_DRIPLEAF = .07F, MOSS_BIG_DRIPLEAF = .04F, MOSS_SPORE_BLOSSOM = .01F;
    private static final float CLAY_KELP = .25F, CLAY_SEAGRASS = .15F, CLAY_LILY_PAD = .10F, CLAY_SEA_PICKLE = .08F, CLAY_CORAL_FAN = .02F;
    private static final float MUD_CLAY_BALL = .20F, MUD_PROPAGULE = .15F, MUD_ROOTS = .10F, MUD_MOSS = .10F;
    private static final float NETHERRACK_QUARTZ = .25F, NETHERRACK_GOLD = .20F, NETHERRACK_GLOWSTONE = .10F, NETHERRACK_SPROUTS = .03F, NETHERRACK_CRIMSON_ROOTS = .01F, NETHERRACK_WARPED_ROOTS = .01F, NETHERRACK_ANCIENT_DEBRIS = .0001F;
    private static final float BLACKSTONE_GOLD = .25F, BLACKSTONE_QUARTZ = .15F, BLACKSTONE_BASALT = .10F, BLACKSTONE_GLOWSTONE = .04F, BLACKSTONE_GILDED = .01F;
    private static final float END_STONE_CHORUS = .30F, END_STONE_POPPED_CHORUS = .12F, END_STONE_FLOWER = .05F, END_STONE_ROD = .02F, END_STONE_PURPUR = .01F;

    public static final List<SiftingTable> TABLES = List.of(
            sandTable(),
            exclusive("red_sand", Items.RED_SAND, "red_sand", chance(Items.GOLD_NUGGET, RED_SAND_GOLD_NUGGET), chance(Items.DEAD_BUSH, RED_SAND_DEAD_BUSH), chance(Items.CACTUS, RED_SAND_CACTUS), chance(Items.TERRACOTTA, RED_SAND_TERRACOTTA), chance(Items.RAW_GOLD, RED_SAND_RAW_GOLD)),
            gravelTable(),
            soulSandTable(),
            dirtTable(),
            exclusive("podzol", Items.PODZOL, "podzol", chance(Items.SPRUCE_SAPLING, PODZOL_SPRUCE), chance(Items.BONE_MEAL, PODZOL_BONEMEAL)),
            exclusive("rooted_dirt", Items.ROOTED_DIRT, "rooted_dirt", chance(Items.HANGING_ROOTS, ROOTED_ROOTS), chance(Items.MOSS_BLOCK, ROOTED_MOSS), chance(Items.AZALEA, ROOTED_AZALEA), chance(Items.FLOWERING_AZALEA, ROOTED_FLOWERING_AZALEA), chance(Items.GLOW_BERRIES, ROOTED_GLOW_BERRIES), chance(Items.SMALL_DRIPLEAF, ROOTED_SMALL_DRIPLEAF), chance(Items.BIG_DRIPLEAF, ROOTED_BIG_DRIPLEAF), chance(Items.SPORE_BLOSSOM, ROOTED_SPORE_BLOSSOM)),
            exclusive("moss", Items.MOSS_BLOCK, "moss", chance(Items.GLOW_BERRIES, MOSS_GLOW_BERRIES), chance(Items.AZALEA, MOSS_AZALEA), chance(Items.FLOWERING_AZALEA, MOSS_FLOWERING_AZALEA), chance(Items.SMALL_DRIPLEAF, MOSS_SMALL_DRIPLEAF), chance(Items.BIG_DRIPLEAF, MOSS_BIG_DRIPLEAF), chance(Items.SPORE_BLOSSOM, MOSS_SPORE_BLOSSOM)),
            exclusive("clay", Items.CLAY, "clay", chance(Items.KELP, CLAY_KELP), chance(Items.SEAGRASS, CLAY_SEAGRASS), chance(Items.LILY_PAD, CLAY_LILY_PAD), chance(Items.SEA_PICKLE, CLAY_SEA_PICKLE), chance(Items.TUBE_CORAL_FAN, CLAY_CORAL_FAN)),
            exclusive("mud", Items.MUD, "mud", chance(Items.CLAY_BALL, MUD_CLAY_BALL), chance(Items.MANGROVE_PROPAGULE, MUD_PROPAGULE), chance(Items.MANGROVE_ROOTS, MUD_ROOTS), chance(Items.MOSS_BLOCK, MUD_MOSS)),
            exclusive("netherrack", Items.NETHERRACK, "netherrack", chance(Items.QUARTZ, NETHERRACK_QUARTZ), chance(Items.GOLD_NUGGET, NETHERRACK_GOLD), chance(Items.GLOWSTONE_DUST, NETHERRACK_GLOWSTONE), chance(Items.NETHER_SPROUTS, NETHERRACK_SPROUTS), chance(Items.CRIMSON_ROOTS, NETHERRACK_CRIMSON_ROOTS), chance(Items.WARPED_ROOTS, NETHERRACK_WARPED_ROOTS), chance(Items.ANCIENT_DEBRIS, NETHERRACK_ANCIENT_DEBRIS)),
            exclusive("blackstone", Items.BLACKSTONE, "blackstone", chance(Items.GOLD_NUGGET, BLACKSTONE_GOLD), chance(Items.QUARTZ, BLACKSTONE_QUARTZ), chance(Items.BASALT, BLACKSTONE_BASALT), chance(Items.GLOWSTONE_DUST, BLACKSTONE_GLOWSTONE), chance(Items.GILDED_BLACKSTONE, BLACKSTONE_GILDED)),
            exclusive("end_stone", Items.END_STONE, "end_stone", chance(Items.CHORUS_FRUIT, END_STONE_CHORUS), chance(Items.POPPED_CHORUS_FRUIT, END_STONE_POPPED_CHORUS), chance(Items.CHORUS_FLOWER, END_STONE_FLOWER), chance(Items.END_ROD, END_STONE_ROD), chance(Items.PURPUR_BLOCK, END_STONE_PURPUR)),
            prismarineTable(),
            prismarineBricksTable(),
            darkPrismarineTable()
    );

    private static final Map<Item, SiftingTable> BY_INPUT = byInput();

    private SiftingTables() {
    }

    public static Optional<SiftingTable> find(Item item) {
        return Optional.ofNullable(BY_INPUT.get(item));
    }

    private static Map<Item, SiftingTable> byInput() {
        Map<Item, SiftingTable> tables = new HashMap<>();
        for (SiftingTable table : TABLES) tables.put(table.input(), table);
        return Map.copyOf(tables);
    }

    private static SiftingTable exclusive(String id, Item input, String description, Entry... entries) {
        List<SiftingOutput> outputs = Arrays.stream(entries).map(Entry::output).toList();
        return table(id, input, outputs, description, random -> {
            float roll = random.nextFloat();
            for (Entry entry : entries) {
                if (roll < entry.chance()) return List.of(entry.output().stack());
                roll -= entry.chance();
            }
            return List.of();
        });
    }

    private static SiftingTable gravelTable() {
        List<SiftingOutput> outputs = List.of(guaranteed(Items.FLINT), chance(Items.DIAMOND, GRAVEL_BONUS * GRAVEL_DIAMOND_IN_BONUS).output(), pool(Items.IRON_NUGGET, GRAVEL_IRON), pool(Items.RAW_COPPER, GRAVEL_COPPER), pool(Items.COAL, GRAVEL_COAL), pool(Items.GOLD_NUGGET, GRAVEL_GOLD), pool(Items.LAPIS_LAZULI, GRAVEL_LAPIS));
        return table("gravel", Items.GRAVEL, outputs, "gravel", random -> {
            List<ItemStack> results = new ArrayList<>(List.of(new ItemStack(Items.FLINT)));
            if (random.nextFloat() >= GRAVEL_BONUS) return results;
            results.add(random.nextFloat() < GRAVEL_DIAMOND_IN_BONUS ? new ItemStack(Items.DIAMOND) : commonGravel(random));
            if (random.nextFloat() < GRAVEL_SECOND_COMMON) results.add(commonGravel(random));
            return merge(results);
        });
    }

    private static SiftingTable sandTable() {
        SiftingTable base = exclusive("sand", Items.SAND, "sand",
                chance(Items.CACTUS, SAND_CACTUS), chance(Items.SUGAR_CANE, SAND_SUGAR_CANE),
                chance(Items.DEAD_BUSH, SAND_DEAD_BUSH), chance(Items.TURTLE_EGG, SAND_TURTLE_EGG));
        List<SiftingOutput> outputs = new ArrayList<>(base.outputs());
        outputs.add(chance(Items.SNIFFER_EGG, SAND_SNIFFER_EGG).output());
        return table("sand", Items.SAND, List.copyOf(outputs), "sand", random -> {
            List<ItemStack> results = new ArrayList<>(base.roll(random));
            if (random.nextFloat() < SAND_SNIFFER_EGG) results.add(new ItemStack(Items.SNIFFER_EGG));
            return merge(results);
        });
    }

    private static SiftingTable soulSandTable() {
        return exclusive("soul_sand", Items.SOUL_SAND, "soul_sand", chance(Items.NETHER_WART, SOUL_WART), chance(Items.BONE, SOUL_BONE), chance(Items.GOLD_NUGGET, SOUL_GOLD), chance(Items.CRIMSON_ROOTS, SOUL_CRIMSON), chance(Items.WARPED_ROOTS, SOUL_WARPED));
    }

    private static SiftingTable prismarineTable() {
        List<SiftingOutput> outputs = List.of(
                guaranteed(Items.PRISMARINE_SHARD, 2), chanceStack(Items.PRISMARINE_SHARD, 2, .25F),
                chance(Items.PRISMARINE_CRYSTALS, .08F).output(), chance(Items.WET_SPONGE, .01F).output(),
                chance(Items.HEART_OF_THE_SEA, .005F).output());
        return table("prismarine", Items.PRISMARINE, outputs, "prismarine", random -> {
            List<ItemStack> results = new ArrayList<>(List.of(new ItemStack(Items.PRISMARINE_SHARD, 2)));
            if (random.nextFloat() < .25F) results.add(new ItemStack(Items.PRISMARINE_SHARD, 2));
            if (random.nextFloat() < .08F) results.add(new ItemStack(Items.PRISMARINE_CRYSTALS));
            if (random.nextFloat() < .01F) results.add(new ItemStack(Items.WET_SPONGE));
            if (random.nextFloat() < .005F) results.add(new ItemStack(Items.HEART_OF_THE_SEA));
            return merge(results);
        });
    }

    private static SiftingTable prismarineBricksTable() {
        List<SiftingOutput> outputs = List.of(
                guaranteed(Items.PRISMARINE_SHARD, 4), chanceStack(Items.PRISMARINE_SHARD, 2, .35F), chanceStack(Items.PRISMARINE_SHARD, 3, .10F),
                chance(Items.PRISMARINE_CRYSTALS, .12F).output(), chance(Items.WET_SPONGE, .015F).output(), chance(Items.HEART_OF_THE_SEA, .0075F).output(),
                new SiftingOutput(() -> new ItemStack(Items.TUBE_CORAL), Component.translatable("stonecutter_sifting.viewer.random_coral", percent(.03F))));
        return table("prismarine_bricks", Items.PRISMARINE_BRICKS, outputs, "prismarine_bricks", random -> {
            List<ItemStack> results = new ArrayList<>(List.of(new ItemStack(Items.PRISMARINE_SHARD, 4)));
            if (random.nextFloat() < .35F) results.add(new ItemStack(Items.PRISMARINE_SHARD, 2));
            if (random.nextFloat() < .10F) results.add(new ItemStack(Items.PRISMARINE_SHARD, 3));
            if (random.nextFloat() < .12F) results.add(new ItemStack(Items.PRISMARINE_CRYSTALS));
            if (random.nextFloat() < .015F) results.add(new ItemStack(Items.WET_SPONGE));
            if (random.nextFloat() < .0075F) results.add(new ItemStack(Items.HEART_OF_THE_SEA));
            if (random.nextFloat() < .03F) results.add(randomCoral(random));
            return merge(results);
        });
    }

    private static SiftingTable darkPrismarineTable() {
        List<SiftingOutput> outputs = List.of(
                guaranteed(Items.PRISMARINE_SHARD, 4), chanceStack(Items.PRISMARINE_SHARD, 2, .35F), chanceStack(Items.PRISMARINE_SHARD, 2, .10F),
                chance(Items.INK_SAC, .20F).output(), chance(Items.PRISMARINE_CRYSTALS, .15F).output(), chance(Items.WET_SPONGE, .02F).output(), chance(Items.HEART_OF_THE_SEA, .01F).output(),
                new SiftingOutput(() -> new ItemStack(Items.TUBE_CORAL), Component.translatable("stonecutter_sifting.viewer.random_coral", percent(.04F))));
        return table("dark_prismarine", Items.DARK_PRISMARINE, outputs, "dark_prismarine", random -> {
            List<ItemStack> results = new ArrayList<>(List.of(new ItemStack(Items.PRISMARINE_SHARD, 4)));
            if (random.nextFloat() < .35F) results.add(new ItemStack(Items.PRISMARINE_SHARD, 2));
            if (random.nextFloat() < .10F) results.add(new ItemStack(Items.PRISMARINE_SHARD, 2));
            if (random.nextFloat() < .20F) results.add(new ItemStack(Items.INK_SAC));
            if (random.nextFloat() < .15F) results.add(new ItemStack(Items.PRISMARINE_CRYSTALS));
            if (random.nextFloat() < .02F) results.add(new ItemStack(Items.WET_SPONGE));
            if (random.nextFloat() < .01F) results.add(new ItemStack(Items.HEART_OF_THE_SEA));
            if (random.nextFloat() < .04F) results.add(randomCoral(random));
            return merge(results);
        });
    }

    private static SiftingTable dirtTable() {
        List<SiftingOutput> outputs = List.of(chance(Items.BONE_MEAL, DIRT_BONEMEAL).output(), chance(Items.WHEAT_SEEDS, DIRT_SEEDS * DIRT_WHEAT_IN_SEEDS).output(), chance(Items.COCOA_BEANS, DIRT_SEEDS * (1F - DIRT_WHEAT_IN_SEEDS) / 4F).output(), chance(Items.MELON_SEEDS, DIRT_SEEDS * (1F - DIRT_WHEAT_IN_SEEDS) / 4F).output(), chance(Items.PUMPKIN_SEEDS, DIRT_SEEDS * (1F - DIRT_WHEAT_IN_SEEDS) / 4F).output(), chance(Items.BEETROOT_SEEDS, DIRT_SEEDS * (1F - DIRT_WHEAT_IN_SEEDS) / 4F).output(), chance(Items.OAK_SAPLING, DIRT_SAPLINGS * DIRT_OAK_IN_SAPLINGS).output(), new SiftingOutput(() -> new ItemStack(Items.BIRCH_SAPLING), Component.translatable("stonecutter_sifting.viewer.other_sapling", percent(DIRT_SAPLINGS * (1F - DIRT_OAK_IN_SAPLINGS)))));
        return table("dirt", Items.DIRT, outputs, "dirt", random -> {
            float roll = random.nextFloat();
            if (roll < DIRT_NOTHING) return List.of();
            if (roll < DIRT_NOTHING + DIRT_BONEMEAL) return List.of(new ItemStack(Items.BONE_MEAL));
            if (roll < DIRT_NOTHING + DIRT_BONEMEAL + DIRT_SEEDS) return List.of(dirtSeed(random));
            return List.of(dirtSapling(random));
        });
    }

    private static SiftingTable table(String id, Item input, List<SiftingOutput> outputs, String description, java.util.function.Function<RandomSource, List<ItemStack>> roller) {
        return new SiftingTable(ResourceLocation.fromNamespaceAndPath(StonecutterSifting.MOD_ID, id), input, outputs, Component.translatable("stonecutter_sifting.viewer." + description), roller);
    }

    private static Entry chance(Item item, float chance) {
        return chance(() -> new ItemStack(item), chance);
    }

    private static SiftingOutput chanceStack(Item item, int count, float chance) {
        return new SiftingOutput(() -> new ItemStack(item, count), Component.translatable("stonecutter_sifting.viewer.chance", percent(chance)));
    }

    private static Entry chance(Supplier<ItemStack> stack, float chance) {
        return new Entry(chance, new SiftingOutput(stack, Component.translatable("stonecutter_sifting.viewer.chance", percent(chance))));
    }

    private static SiftingOutput guaranteed(Item item) {
        return guaranteed(item, 1);
    }

    private static SiftingOutput guaranteed(Item item, int count) {
        return new SiftingOutput(() -> new ItemStack(item, count), Component.translatable("stonecutter_sifting.viewer.guaranteed"));
    }

    private static SiftingOutput pool(Item item, float weight) {
        return new SiftingOutput(() -> new ItemStack(item), Component.translatable("stonecutter_sifting.viewer.pool_weight", percent(weight)));
    }

    private static ItemStack commonGravel(RandomSource random) {
        float roll = random.nextFloat();
        if (roll < GRAVEL_IRON) return new ItemStack(Items.IRON_NUGGET);
        if (roll < GRAVEL_IRON + GRAVEL_COPPER) return new ItemStack(Items.RAW_COPPER);
        if (roll < GRAVEL_IRON + GRAVEL_COPPER + GRAVEL_COAL) return new ItemStack(Items.COAL);
        if (roll < GRAVEL_IRON + GRAVEL_COPPER + GRAVEL_COAL + GRAVEL_GOLD) return new ItemStack(Items.GOLD_NUGGET);
        return new ItemStack(Items.LAPIS_LAZULI);
    }

    private static ItemStack dirtSeed(RandomSource random) {
        if (random.nextFloat() < DIRT_WHEAT_IN_SEEDS) return new ItemStack(Items.WHEAT_SEEDS);
        return switch (random.nextInt(4)) {
            case 0 -> new ItemStack(Items.COCOA_BEANS);
            case 1 -> new ItemStack(Items.MELON_SEEDS);
            case 2 -> new ItemStack(Items.PUMPKIN_SEEDS);
            default -> new ItemStack(Items.BEETROOT_SEEDS);
        };
    }

    private static ItemStack dirtSapling(RandomSource random) {
        if (random.nextFloat() < DIRT_OAK_IN_SAPLINGS) return new ItemStack(Items.OAK_SAPLING);
        var tag = BuiltInRegistries.ITEM.getTag(ItemTags.SAPLINGS);
        if (tag.isPresent()) {
            List<Item> alternatives = new ArrayList<>();
            for (Holder<Item> holder : tag.get()) {
                Item item = holder.value();
                if (item != Items.OAK_SAPLING && item != Items.AIR && !holder.is(NON_OVERWORLD_SAPLINGS)) alternatives.add(item);
            }
            if (!alternatives.isEmpty()) return new ItemStack(alternatives.get(random.nextInt(alternatives.size())));
        }
        return new ItemStack(Items.OAK_SAPLING);
    }

    private static ItemStack randomCoral(RandomSource random) {
        return switch (random.nextInt(5)) {
            case 0 -> new ItemStack(Items.TUBE_CORAL);
            case 1 -> new ItemStack(Items.BRAIN_CORAL);
            case 2 -> new ItemStack(Items.BUBBLE_CORAL);
            case 3 -> new ItemStack(Items.FIRE_CORAL);
            default -> new ItemStack(Items.HORN_CORAL);
        };
    }

    private static List<ItemStack> merge(List<ItemStack> stacks) {
        Map<Item, Integer> counts = new HashMap<>();
        for (ItemStack stack : stacks) if (!stack.isEmpty()) counts.merge(stack.getItem(), stack.getCount(), Integer::sum);
        return counts.entrySet().stream().map(entry -> new ItemStack(entry.getKey(), entry.getValue())).toList();
    }

    private static String percent(float chance) {
        float percentage = chance * 100F;
        if (percentage < .1F) return String.format(java.util.Locale.ROOT, "%.2f%%", percentage);
        if (Math.abs(percentage - Math.round(percentage)) < .0001F) return String.format(java.util.Locale.ROOT, "%.0f%%", percentage);
        return String.format(java.util.Locale.ROOT, "%.3f%%", percentage).replaceAll("0+%$", "%");
    }

    private record Entry(float chance, SiftingOutput output) {
    }
}

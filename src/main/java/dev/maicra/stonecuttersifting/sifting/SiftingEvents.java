package dev.maicra.stonecuttersifting.sifting;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/** Processes one supported dropped block every four ticks per Stonecutter. */
public final class SiftingEvents {
    public static final int PROCESSING_TICKS = 4;
    private static final Map<ServerLevel, Map<Long, Long>> LAST_PROCESS_TICK = Collections.synchronizedMap(new WeakHashMap<>());

    private SiftingEvents() {
    }

    public static void onEntityTickPost(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof ItemEntity entity) || !(entity.level() instanceof ServerLevel level) || !entity.isAlive() || entity.getItem().isEmpty()) return;
        Item input = entity.getItem().getItem();
        var table = SiftingTables.find(input);
        if (table.isEmpty()) return;
        BlockPos cutter = findStonecutter(entity, level);
        if (cutter == null) return;
        entity.setPickUpDelay(10);
        long time = level.getGameTime();
        Map<Long, Long> times = LAST_PROCESS_TICK.computeIfAbsent(level, ignored -> new HashMap<>());
        long key = cutter.asLong();
        if (time - times.getOrDefault(key, Long.MIN_VALUE / 2L) < PROCESSING_TICKS) return;
        times.put(key, time);
        List<ItemStack> results = table.get().roll(level.getRandom());
        ItemStack consumed = entity.getItem();
        consumed.shrink(1);
        if (consumed.isEmpty()) entity.discard(); else entity.setItem(consumed);
        playEffects(level, cutter, input);
        spawnResults(level, cutter, results);
    }

    private static BlockPos findStonecutter(ItemEntity entity, ServerLevel level) {
        BlockPos at = BlockPos.containing(entity.getX(), entity.getY() - .05D, entity.getZ());
        if (level.getBlockState(at).is(Blocks.STONECUTTER)) return at;
        BlockPos below = at.below();
        return level.getBlockState(below).is(Blocks.STONECUTTER) ? below : null;
    }

    private static void playEffects(ServerLevel level, BlockPos pos, Item input) {
        RandomSource random = level.getRandom();
        level.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, .18F, .9F + random.nextFloat() * .2F);
        Block block = Block.byItem(input);
        if (block != Blocks.AIR) level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, block.defaultBlockState()), pos.getX() + .5D, pos.getY() + .72D, pos.getZ() + .5D, 3, .18D, .08D, .18D, .02D);
    }

    private static void spawnResults(ServerLevel level, BlockPos pos, List<ItemStack> results) {
        RandomSource random = level.getRandom();
        for (ItemStack result : results) {
            ItemEntity output = new ItemEntity(level, pos.getX() + .5D, pos.getY() + .85D, pos.getZ() + .5D, result.copy());
            output.setDeltaMovement((random.nextDouble() - .5D) * .18D, .16D + random.nextDouble() * .08D, (random.nextDouble() - .5D) * .18D);
            output.setDefaultPickUpDelay();
            level.addFreshEntity(output);
        }
    }
}

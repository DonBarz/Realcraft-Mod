package net.donbarz.realcraft.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class AddLootModifier extends LootModifier {
    public static final Supplier<Codec<AddLootModifier>> CODEC = Suppliers.memoize(()
    -> RecordCodecBuilder.create(inst -> codecStart(inst)
            .and(ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item))
            .and(Codec.INT.fieldOf("amount").forGetter(m -> m.amount))
            .apply(inst, AddLootModifier::new)));

    private final Item item;
    private final int amount;

    public AddLootModifier(LootItemCondition[] conditionsIn, Item item, int amount) {
        super(conditionsIn);
        this.item = item;
        this.amount = amount;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> returnLoot, LootContext lootContext) {
        for (LootItemCondition condition : conditions) {
            if (!condition.test(lootContext)) {
                return returnLoot;
            }
        }

        returnLoot.add(amount, new ItemStack(item));

        return returnLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}

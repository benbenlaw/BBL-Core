package com.benbenlaw.core.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class ShapedComponentCopyRecipe extends NormalCraftingRecipe {

    private final ShapedRecipePattern pattern;
    private final ItemStackTemplate result;
    private final Ingredient source;
    private final List<DataComponentType<?>> componentsToCopy;

    public static final MapCodec<ShapedComponentCopyRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
                    CraftingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
                    ShapedRecipePattern.MAP_CODEC.forGetter(o -> o.pattern),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result),
                    Ingredient.CODEC.fieldOf("source").forGetter(o -> o.source),
                    BuiltInRegistries.DATA_COMPONENT_TYPE.byNameCodec()
                            .listOf()
                            .fieldOf("copy_components")
                            .forGetter(o -> o.componentsToCopy)
            ).apply(instance, ShapedComponentCopyRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ShapedComponentCopyRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    CommonInfo.STREAM_CODEC,          o -> o.commonInfo,
                    CraftingBookInfo.STREAM_CODEC,    o -> o.bookInfo,
                    ShapedRecipePattern.STREAM_CODEC, o -> o.pattern,
                    ItemStackTemplate.STREAM_CODEC,   o -> o.result,
                    Ingredient.CONTENTS_STREAM_CODEC, o -> o.source,
                    ByteBufCodecs.fromCodec(
                            BuiltInRegistries.DATA_COMPONENT_TYPE.byNameCodec().listOf()
                    ),                                o -> o.componentsToCopy,
                    ShapedComponentCopyRecipe::new
            );

    public static final RecipeSerializer<ShapedComponentCopyRecipe> SERIALIZER =
            new RecipeSerializer<>(CODEC, STREAM_CODEC);

    public static final RecipeType<ShapedComponentCopyRecipe> TYPE = new RecipeType<>() {};


    public ShapedComponentCopyRecipe(CommonInfo commonInfo, CraftingBookInfo bookInfo,
                                     ShapedRecipePattern pattern, ItemStackTemplate result,
                                     Ingredient source, List<DataComponentType<?>> componentsToCopy) {
        super(commonInfo, bookInfo);
        this.pattern = pattern;
        this.result = result;
        this.source = source;
        this.componentsToCopy = componentsToCopy;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return this.pattern.matches(input);
    }

    @Override
    public @NonNull ItemStack assemble(CraftingInput input) {
        // Find the first slot matching the source ingredient
        ItemStack sourceStack = ItemStack.EMPTY;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (this.source.test(stack)) {
                sourceStack = stack;
                break;
            }
        }

        ItemStack output = this.result.create();

        for (DataComponentType<?> type : componentsToCopy) {
            copyComponent(type, sourceStack, output);
        }

        return output;
    }

    private <T> void copyComponent(DataComponentType<T> type, ItemStack from, ItemStack to) {
        T value = from.get(type);
        if (value != null) {
            to.set(type, value);
        }
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(
                new ShapedCraftingRecipeDisplay(
                        this.pattern.width(),
                        this.pattern.height(),
                        this.pattern.ingredients().stream()
                                .map(e -> e.map(Ingredient::display).orElse(SlotDisplay.Empty.INSTANCE))
                                .toList(),
                        new SlotDisplay.ItemStackSlotDisplay(this.result),
                        new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)
                )
        );
    }

    @Override
    public PlacementInfo createPlacementInfo() {
        return PlacementInfo.createFromOptionals(this.pattern.ingredients());
    }

    @Override
    public RecipeSerializer<? extends NormalCraftingRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public boolean isSpecial() {
        return false;
    }
}
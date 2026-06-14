package com.benbenlaw.core.recipe;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
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
import java.util.Optional;

public class ShapedTagOutputRecipe extends NormalCraftingRecipe {

    public final TagKey<Item> outputTag;
    private final int count;
    private final ShapedRecipePattern pattern;

    public static final MapCodec<Pair<TagKey<Item>, Integer>> RESULT_CODEC = RecordCodecBuilder.mapCodec(pairInstance ->
            pairInstance.group(
                    Identifier.CODEC.fieldOf("tag").forGetter(pair -> pair.getFirst().location()),
                    Codec.INT.optionalFieldOf("count", 1).forGetter(Pair::getSecond)
            ).apply(pairInstance, (tagLoc, count) ->
                    Pair.of(TagKey.create(Registries.ITEM, tagLoc), count)
            )
    );

    public static final MapCodec<ShapedTagOutputRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    CommonInfo.MAP_CODEC.forGetter((o) -> o.commonInfo),
                    CraftingBookInfo.MAP_CODEC.forGetter((o) -> o.bookInfo),
                    ShapedRecipePattern.MAP_CODEC.forGetter(o -> o.pattern),
                    RESULT_CODEC.fieldOf("tag_result").forGetter(recipe -> Pair.of(recipe.outputTag, recipe.count)

            )).apply(instance, (commonInfo, bookInfo, pattern, tagResult) ->
                    new ShapedTagOutputRecipe(commonInfo, bookInfo, pattern, tagResult.getFirst(), tagResult.getSecond())
            )
    );


    public static final StreamCodec<RegistryFriendlyByteBuf, ShapedTagOutputRecipe> STREAM_CODEC = StreamCodec.of(
            ShapedTagOutputRecipe::write, ShapedTagOutputRecipe::read);

    public static final RecipeType<ShapedTagOutputRecipe> TYPE = new RecipeType<>() {};

    public static final RecipeSerializer<ShapedTagOutputRecipe> SERIALIZER =
            new RecipeSerializer<>(CODEC, STREAM_CODEC);


    private static ShapedTagOutputRecipe read(RegistryFriendlyByteBuf buf) {
        CommonInfo commonInfo = CommonInfo.STREAM_CODEC.decode(buf);
        CraftingBookInfo bookInfo = CraftingBookInfo.STREAM_CODEC.decode(buf);
        ShapedRecipePattern pattern = ShapedRecipePattern.STREAM_CODEC.decode(buf);
        Identifier tagId = buf.readIdentifier();
        int count = buf.readVarInt();
        return new ShapedTagOutputRecipe(commonInfo, bookInfo, pattern, TagKey.create(Registries.ITEM, tagId), count);
    }

    private static void write(RegistryFriendlyByteBuf buf, ShapedTagOutputRecipe recipe) {
        CommonInfo.STREAM_CODEC.encode(buf, recipe.commonInfo);
        CraftingBookInfo.STREAM_CODEC.encode(buf, recipe.bookInfo);
        ShapedRecipePattern.STREAM_CODEC.encode(buf, recipe.pattern);
        buf.writeIdentifier(recipe.outputTag.location());
        buf.writeVarInt(recipe.count);
    }

    public ShapedTagOutputRecipe(CommonInfo commonInfo, CraftingBookInfo craftingBookInfo, ShapedRecipePattern pattern, TagKey<Item> outputTag, int count) {
        super(commonInfo, craftingBookInfo);
        this.outputTag = outputTag;
        this.count = count;
        this.pattern = pattern;
    }

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        return this.pattern.matches(craftingInput);
    }

    @Override
    public @NonNull ItemStack assemble(CraftingInput input) {
        ItemStackTemplate result = ItemStackTemplate.fromNonEmptyStack(getItem().getDefaultInstance());
        result.withCount(this.count);
        return result.create();
    }

    public Item getItem() {
        Optional<Item> item = BuiltInRegistries.ITEM.get(outputTag)
                .flatMap(tag -> tag.stream().findFirst())
                .map(Holder::value);

        return item.map(value -> new ItemStack(value, count)).get().getItem();
    }

    @Override
    public List<RecipeDisplay> display() {
        ItemStackTemplate result = ItemStackTemplate.fromNonEmptyStack(getItem().getDefaultInstance());
        result.withCount(this.count);
        return List.of(
                new ShapedCraftingRecipeDisplay(
                        this.pattern.width(),
                        this.pattern.height(),
                        this.pattern.ingredients().stream().map(e -> e.map(Ingredient::display).orElse(SlotDisplay.Empty.INSTANCE)).toList(),
                        new SlotDisplay.ItemStackSlotDisplay(result),
                        new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)
                )
        );
    }

    public TagKey<Item> outputTag() {
        return outputTag;
    }

    public int count() {
        return count;
    }

    @Override
    public RecipeSerializer<? extends NormalCraftingRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public PlacementInfo createPlacementInfo() {
        return PlacementInfo.createFromOptionals(this.pattern.ingredients());
    }

    @Override
    public boolean isSpecial() {
        return false;
    }

}

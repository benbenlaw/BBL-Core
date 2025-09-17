package com.benbenlaw.core.command;

/*
public class RecipeIDCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("recipeID")
                .executes(RecipeIDCommand::execute) // /recipeID
                .then(Commands.argument("modid", StringArgumentType.string())
                        .executes(RecipeIDCommand::executeWithMod))); // /recipeID <modid>
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        return process(context, null); // No mod filter
    }

    private static int executeWithMod(CommandContext<CommandSourceStack> context) {
        String modid = StringArgumentType.getString(context, "modid");
        return process(context, modid); // With mod filter
    }

    private static int process(CommandContext<CommandSourceStack> context, String modFilter) {
        if (!(context.getSource().getEntity() instanceof ServerPlayer player)) {
            context.getSource().sendFailure(Component.literal("Only players can use this command."));
            return 0;
        }

        ItemStack heldItem = player.getMainHandItem();
        if (heldItem.isEmpty()) {
            player.sendSystemMessage(Component.literal("You're not holding any item."));
            return 0;
        }

        RecipeManager recipeManager = player.level().getRecipeManager();
        Collection<RecipeHolder<?>> recipes = recipeManager.getRecipes();

        int found = 0;
        for (RecipeHolder<?> recipe : recipes) {
            ResourceLocation id = recipe.id();

            // Filter by mod namespace if specified
            if (modFilter != null && !id.getNamespace().equals(modFilter)) {
                continue;
            }

            if (ItemStack.isSameItem(recipe.value().getResultItem(player.level().registryAccess()), heldItem)) {
                ResourceLocation typeId = net.minecraft.core.registries.BuiltInRegistries.RECIPE_TYPE.getKey(recipe.value().getType());
                String hoverText = "Click to copy ID: " + id + "\nRecipe Type: " + (typeId != null ? typeId : "unknown");

                Component clickableId = Component.literal(id.toString())
                        .withStyle(style -> style
                                .withColor(ChatFormatting.GREEN)
                                .withUnderlined(true)
                                .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, id.toString()))
                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        Component.literal(hoverText)))
                        );

                player.sendSystemMessage(Component.literal("Recipe ID: ").append(clickableId));
                found++;
            }
        }

        if (found == 0) {
            String message = "No recipe found that outputs this item" +
                    (modFilter != null ? " in mod '" + modFilter + "'." : ".");
            player.sendSystemMessage(Component.literal(message));
        }

        return 1;
    }
}

 */

package com.modforge.swift_sword;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwiftSword implements ModInitializer {
    public static final String MOD_ID = "swift_sword";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Item SWIFT_SWORD = new SwiftSwordItem(ToolMaterials.DIAMOND, new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.DIAMOND, 3, -2.4f)));

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "swift_sword"), SWIFT_SWORD);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> {
            content.add(SWIFT_SWORD);
        });

        LOGGER.info("Swift Sword initialized.");
    }

    public static class SwiftSwordItem extends SwordItem {

        public SwiftSwordItem(ToolMaterial toolMaterial, Item.Settings settings) {
            super(toolMaterial, settings);
        }

        @Override
        public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
            ItemStack itemStack = user.getStackInHand(hand);

            if (!world.isClient) {
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 1)); // Speed II for 5 seconds
                itemStack.damage(5, user, user.getSlotForHand(hand));
                user.getItemCooldownManager().set(this, 100); // 5 second cooldown
            }

            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_PLAYER_BREATH, SoundCategory.PLAYERS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);

            return TypedActionResult.success(itemStack, world.isClient());
        }
    }
}

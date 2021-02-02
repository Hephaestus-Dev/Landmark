package dev.hephaestus.landmark.impl;

import dev.hephaestus.landmark.impl.item.DeedItem;
import dev.hephaestus.landmark.impl.item.EvictionNoticeItem;
import dev.hephaestus.landmark.impl.names.NameGenerator;
import dev.hephaestus.landmark.impl.util.LandmarkHandler;
import dev.hephaestus.landmark.impl.util.ThreadFactory;
import dev.hephaestus.landmark.impl.world.LandmarkTrackingComponent;
import dev.hephaestus.landmark.impl.world.chunk.LandmarkChunkComponent;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.ChunkComponentCallback;
import nerdhub.cardinal.components.api.event.WorldComponentCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LandmarkMod implements ModInitializer, ChunkComponentInitializer, WorldComponentInitializer {
	public static final String MODID = "landmark";
	public static final String MOD_NAME = "Landmark";
	public static final Logger LOG = LogManager.getLogger(MOD_NAME);

	public static final Executor EXECUTOR = Executors.newFixedThreadPool(8, new ThreadFactory());

	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(id("items")).icon(() -> new ItemStack(LandmarkMod.COMMON_DEED)).build();

	public static final Item COMMON_DEED = new DeedItem(new Item.Settings().group(ITEM_GROUP).rarity(Rarity.COMMON), 4096);
	public static final Item UNCOMMON_DEED = new DeedItem(new Item.Settings().group(ITEM_GROUP).rarity(Rarity.UNCOMMON), 32768);
	public static final Item RARE_DEED = new DeedItem(new Item.Settings().group(ITEM_GROUP).rarity(Rarity.RARE), 262144);
	public static final Item CREATIVE_DEED = new DeedItem(new Item.Settings().group(ITEM_GROUP).rarity(Rarity.EPIC), Double.MAX_VALUE);

	public static final Item EVITION_NOTICE = new EvictionNoticeItem(new Item.Settings().group(ITEM_GROUP).rarity(Rarity.EPIC));

	public static final ComponentKey<LandmarkChunkComponent> CHUNK_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(
			id("component", "chunk"),
			LandmarkChunkComponent.class
	);

	public static final ComponentKey<LandmarkTrackingComponent> TRACKING_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(
			id("component", "tracking"),
			LandmarkTrackingComponent.class
	);

	public static Identifier id(String... path) {
		return new Identifier(MODID, String.join(".", path));
	}

	@Override
	public void onInitialize() {
		NameGenerator.init();
		LandmarkHandler.init();

		Registry.register(Registry.ITEM, id("common_deed"), COMMON_DEED);
		Registry.register(Registry.ITEM, id("uncommon_deed"), UNCOMMON_DEED);
		Registry.register(Registry.ITEM, id("rare_deed"), RARE_DEED);
		Registry.register(Registry.ITEM, id("creative_deed"), CREATIVE_DEED);
		Registry.register(Registry.ITEM, id("eviction_notice"), EVITION_NOTICE);
	}

	@Override
	public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
		registry.register(CHUNK_COMPONENT, LandmarkChunkComponent::new);
	}

	@Override
	public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
		registry.register(TRACKING_COMPONENT, LandmarkTrackingComponent::new);
	}
}

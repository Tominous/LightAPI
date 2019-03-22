/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 Vladimir Mikhailov <beykerykt@gmail.com>
 * Copyright (c) 2016-2017 The ImplexDevOne Project
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *  
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ru.beykerykt.lightapi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ru.beykerykt.lightapi.chunks.ChunkInfo;
import ru.beykerykt.lightapi.events.DeleteLightEvent;
import ru.beykerykt.lightapi.events.SetLightEvent;
import ru.beykerykt.lightapi.events.UpdateChunkEvent;
import ru.beykerykt.minecraft.lightapi.common.Build;
import ru.beykerykt.minecraft.lightapi.common.IChunkData;
import ru.beykerykt.minecraft.lightapi.common.LightType;
import ru.beykerykt.minecraft.lightapi.impl.bukkit.BukkitChunkData;
import ru.beykerykt.minecraft.lightapi.impl.bukkit.IBukkitLightHandler;

@Deprecated
public class LightAPI extends JavaPlugin {

	@Deprecated
	private static void log(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.RED + "<LightAPI-Backward>: " + message);
	}

	@Deprecated
	public static LightAPI getInstance() {
		return null;
	}

	@Deprecated
	public static boolean createLight(Location location, int lightlevel, boolean async) {
		return createLight(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(),
				lightlevel, async);
	}

	@Deprecated
	public static boolean createLight(final World world, final int x, final int y, final int z, final int lightlevel,
			boolean async) {
		if (Build.CURRENT_VERSION > Build.VERSION_CODES.ONE) {
			log(Bukkit.getServer().getConsoleSender(), "Sorry, but now you can not use the old version of the API.");
			return false;
		}
		final SetLightEvent event = new SetLightEvent(world, x, y, z, lightlevel, async);
		Bukkit.getPluginManager().callEvent(event);

		if (!event.isCancelled()) {
			IBukkitLightHandler handler = (IBukkitLightHandler) ru.beykerykt.minecraft.lightapi.common.LightAPI
					.getLightHandler();
			return handler.createLight(event.getWorld(), LightType.BLOCK, event.getX(), event.getY(), event.getZ(),
					lightlevel);
		}
		return false;
	}

	@Deprecated
	public static boolean deleteLight(Location location, boolean async) {
		return deleteLight(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(),
				async);
	}

	@Deprecated
	public static boolean deleteLight(final World world, final int x, final int y, final int z, boolean async) {
		if (Build.CURRENT_VERSION > Build.VERSION_CODES.ONE) {
			log(Bukkit.getServer().getConsoleSender(), "Sorry, but now you can not use the old version of the API.");
			return false;
		}
		final DeleteLightEvent event = new DeleteLightEvent(world, x, y, z, async);
		Bukkit.getPluginManager().callEvent(event);

		if (!event.isCancelled()) {
			IBukkitLightHandler handler = (IBukkitLightHandler) ru.beykerykt.minecraft.lightapi.common.LightAPI
					.getLightHandler();
			return handler.deleteLight(event.getWorld(), LightType.BLOCK, event.getX(), event.getY(), event.getZ());
		}
		return false;
	}

	@Deprecated
	public static List<ChunkInfo> collectChunks(Location location) {
		return collectChunks(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}

	@Deprecated
	public static List<ChunkInfo> collectChunks(final World world, final int x, final int y, final int z) {
		if (Build.CURRENT_VERSION > Build.VERSION_CODES.ONE) {
			log(Bukkit.getServer().getConsoleSender(), "Sorry, but now you can not use the old version of the API.");
			return null;
		}
		List<ChunkInfo> list = new CopyOnWriteArrayList<ChunkInfo>();
		IBukkitLightHandler handler = (IBukkitLightHandler) ru.beykerykt.minecraft.lightapi.common.LightAPI
				.getLightHandler();
		for (IChunkData newData : handler.collectChunks(world, x, y, z, 8)) {
			BukkitChunkData bcd = (BukkitChunkData) newData;
			ChunkInfo info = new ChunkInfo(world, bcd.getChunkX(), bcd.getChunkYHeight(), bcd.getChunkZ(),
					bcd.getReceivers());
			if (!list.contains(info)) {
				list.add(info);
			}
		}
		return list;
	}

	@Deprecated
	public static boolean updateChunks(ChunkInfo info) {
		return updateChunk(info);
	}

	@Deprecated
	public static boolean updateChunk(ChunkInfo info) {
		if (Build.CURRENT_VERSION > Build.VERSION_CODES.ONE) {
			log(Bukkit.getServer().getConsoleSender(), "Sorry, but now you can not use the old version of the API.");
			return false;
		}
		UpdateChunkEvent event = new UpdateChunkEvent(info);
		Bukkit.getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			IBukkitLightHandler handler = (IBukkitLightHandler) ru.beykerykt.minecraft.lightapi.common.LightAPI
					.getLightHandler();
			BukkitChunkData newData = new BukkitChunkData(event.getChunkInfo().getWorld(),
					event.getChunkInfo().getChunkX(), event.getChunkInfo().getChunkYHeight(),
					event.getChunkInfo().getChunkZ(), new ArrayList<>(event.getChunkInfo().getReceivers()));
			handler.sendChunk(newData);
			return true;
		}
		return false;
	}

	@Deprecated
	public static boolean updateChunks(Location location, Collection<? extends Player> players) {
		return updateChunks(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(),
				players);
	}

	@Deprecated
	public static boolean updateChunks(World world, int x, int y, int z, Collection<? extends Player> players) {
		if (Build.CURRENT_VERSION > Build.VERSION_CODES.ONE) {
			log(Bukkit.getServer().getConsoleSender(), "Sorry, but now you can not use the old version of the API.");
			return false;
		}
		IBukkitLightHandler handler = (IBukkitLightHandler) ru.beykerykt.minecraft.lightapi.common.LightAPI
				.getLightHandler();
		for (IChunkData newData : handler.collectChunks(world, x, y, z, 8)) {
			if (newData instanceof BukkitChunkData) {
				BukkitChunkData bcd = (BukkitChunkData) newData;
				bcd.setReceivers(new ArrayList<Player>(players));
				handler.sendChunk(bcd);
			}
		}
		return true;
	}

	@Deprecated
	public static boolean updateChunk(Location location, Collection<? extends Player> players) {
		return updateChunk(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(),
				players);
	}

	@Deprecated
	public static boolean updateChunk(World world, int x, int y, int z, Collection<? extends Player> players) {
		if (Build.CURRENT_VERSION > Build.VERSION_CODES.ONE) {
			log(Bukkit.getServer().getConsoleSender(), "Sorry, but now you can not use the old version of the API.");
			return false;
		}
		IBukkitLightHandler handler = (IBukkitLightHandler) ru.beykerykt.minecraft.lightapi.common.LightAPI
				.getLightHandler();
		handler.sendChunk(world, x >> 4, y, z >> 4, players);
		return true;
	}

	@Deprecated
	private static BlockFace[] SIDES = { BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH,
			BlockFace.WEST };

	@Deprecated
	public static Block getAdjacentAirBlock(Block block) {
		for (BlockFace face : SIDES) {
			if (block.getY() == 0x0 && face == BlockFace.DOWN)
				continue;
			if (block.getY() == 0xFF && face == BlockFace.UP)
				continue;

			Block candidate = block.getRelative(face);

			if (candidate.getType().isTransparent()) {
				return candidate;
			}
		}
		return block;
	}

	@Deprecated
	public int getUpdateDelayTicks() {
		return 0;
	}

	@Deprecated
	public void setUpdateDelayTicks(int update_delay_ticks) {
	}

	@Deprecated
	public int getMaxIterationsPerTick() {
		return 0;
	}

	@Deprecated
	public void setMaxIterationsPerTick(int max_iterations_per_tick) {
	}
}

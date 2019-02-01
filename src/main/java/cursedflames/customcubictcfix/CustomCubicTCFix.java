package cursedflames.customcubictcfix;

import java.util.Collection;
import java.util.Iterator;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.common.lib.events.ChunkEvents;

@Mod(modid = "customcubictcfix", name = "CustomCubicTCFix", version = "0.0.0", useMetadata = true)
@Mod.EventBusSubscriber
public class CustomCubicTCFix {
	@Mod.Instance
	public static CustomCubicTCFix instance;

	@SubscribeEvent
	public static void chunkLoad(ChunkEvent.Load event) {
		if (event.getWorld().isRemote)
			return;
		WorldVis inst = WorldVis.getInstance(event.getWorld());
		NBTTagCompound vis = inst.visData.get(event.getChunk().getPos());
		if (vis==null) {
			vis = new NBTTagCompound();
		}
		ChunkEvents.chunkLoad(new ChunkDataEvent.Load(event.getChunk(), vis));
//		if (vis.getCompoundTag("Thaumcraft").hasKey("base")) {
//			NBTTagCompound nbt = vis.getCompoundTag("Thaumcraft");
//			short base = nbt.getShort("base");
//			float flux = nbt.getFloat("flux");
//			float visNum = nbt.getFloat("vis");
//			AuraHandler.addAuraChunk(event.getWorld().provider.getDimension(), event.getChunk(),
//					base, visNum, flux);
//			inst.markDirty();
//		}
	}

	@SubscribeEvent
	public static void chunkSave(ChunkEvent.Unload event) {
		if (event.getWorld().isRemote)
			return;
		ChunkDataEvent.Save save = new ChunkDataEvent.Save(event.getChunk(), new NBTTagCompound());
		ChunkEvents.chunkSave(save);
		WorldVis inst = WorldVis.getInstance(event.getWorld());
		inst.visData.put(event.getChunk().getPos(), save.getData());
		inst.markDirty();
	}

	@SubscribeEvent
	public static void worldSave(WorldEvent.Save event) {
		World world = event.getWorld();
		if (world.isRemote)
			return;
		Collection<Chunk> chunks = ((WorldServer) world).getChunkProvider().getLoadedChunks();
		WorldVis inst = WorldVis.getInstance(event.getWorld());
		for (Iterator<Chunk> iter = chunks.iterator(); iter.hasNext();) {
			Chunk chunk = iter.next();
			ChunkDataEvent.Save save = new ChunkDataEvent.Save(chunk, new NBTTagCompound());
			ChunkEvents.chunkSave(save);
			inst.visData.put(chunk.getPos(), save.getData());
		}
		inst.markDirty();
	}
}

package cursedflames.customcubictcfix;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class WorldVis extends WorldSavedData {
	public static final String DATA_NAME = "CCTCFIX_VIS";
	public Map<ChunkPos, NBTTagCompound> visData = new HashMap<>();

	public WorldVis() {
		super(DATA_NAME);
	}

	// Apparently this constructor is required?
	public WorldVis(String s) {
		super(s);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		if (!tag.hasKey("visData"))
			return;
		NBTTagCompound tag2 = tag.getCompoundTag("visData");
		for (int i = 0; tag2.hasKey(String.valueOf(i)); i++) {
			NBTTagCompound tag3 = tag2.getCompoundTag(String.valueOf(i));
			if (!tag3.hasKey("x")||!tag3.hasKey("y")||!tag3.hasKey("data"))
				continue;
			visData.put(new ChunkPos(tag3.getInteger("x"), tag3.getInteger("y")),
					tag3.getCompoundTag("data"));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		NBTTagCompound tag2 = new NBTTagCompound();
		Iterator<ChunkPos> iter = visData.keySet().iterator();
		for (int i = 0; iter.hasNext(); i++) {
			ChunkPos pos = iter.next();
			NBTTagCompound tag3 = new NBTTagCompound();
			tag3.setInteger("x", pos.x);
			tag3.setInteger("y", pos.z);
			tag3.setTag("data", visData.get(pos));
			tag2.setTag(String.valueOf(i), tag3);
		}
		tag.setTag("visData", tag2);
		return tag;
	}

	public static WorldVis getInstance(World world) {
		if (world==null)
			return null;
		MapStorage storage = world.getPerWorldStorage();
		WorldVis instance = (WorldVis) (storage.getOrLoadData(WorldVis.class, DATA_NAME));
		if (instance==null) {
			instance = new WorldVis();
			storage.setData(DATA_NAME, instance);
		}
		return instance;
	}
}

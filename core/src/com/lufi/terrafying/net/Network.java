package com.lufi.terrafying.net;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.lufi.terrafying.entities.Entity;
import com.lufi.terrafying.entities.Player;
import com.lufi.terrafying.items.Inventory;
import com.lufi.terrafying.items.Item;
import com.lufi.terrafying.items.ItemStack;
import com.lufi.terrafying.util.Options;
import com.lufi.terrafying.util.Vector2i;
import com.lufi.terrafying.world.Block.MineType;
import com.lufi.terrafying.world.Chunk;

public class Network {
	static public final int port0 = 6567;
	static public final int port1 = 6567;
	
	public static void register(Kryo k) {
		register(k,
				ConnectionRequestPacket.class,
				ConnectionDeniedPacket.class,
				ConnectionResponsePacket.class,
				ServerClosedPacket.class,
				ClientDisconnectPacket.class,
				EntityAddPacket.class,
				EntityRemovePacket.class,
				EntityUpdatePacket.class,
				ChunkRequestPacket.class,
				ChunkResponsePacket.class,
				BlockUpdatePacket.class,
				
				InventoryUpdatePacket.class,
				
				MetaAddPacket.class,
				MetaUpdatePacket.class,
				
				Player.class,
				Entity.class,
				Chunk.class,
				
				Inventory.class,
				ItemStack.class,
				ItemStack[].class,
				Item.class,
				MineType.class,
				Options.class,
				
				Vector2.class,
				Vector2i.class,
				Array.class,
				ArrayList.class,
				Object[].class,
				int[][].class,
				int[].class
				);
		//k.register(int[][].class, new GameMapSerializer());
	}
	
	static public class ConnectionRequestPacket {
		public ConnectionRequestPacket() {}
		
		public String name;
	}
	
	static public class ConnectionDeniedPacket {
		public String reason;
	}
	
	static public class ConnectionResponsePacket {
		public ConnectionResponsePacket() {}
		
		public int id;
		public String name;
		public ArrayList<Entity> entities;
		public Vector2i startChunkId;
		public Chunk startChunk;
		public Vector2 spawnpoint;
		public Inventory inventory;
	}
	
	static public class ChunkRequestPacket {
		public ChunkRequestPacket() {}
		
		public Vector2i id;
	}
	
	static public class ChunkResponsePacket {
		public ChunkResponsePacket() {}
		
		public Vector2i chunkId;
		public Chunk chunk;
	}
	
	static public class EntityAddPacket {
		public EntityAddPacket() {}
		
		public Entity entity;
	}
	
	static public class EntityRemovePacket {
		public int id;
	}
	
	static public class EntityUpdatePacket {
		public EntityUpdatePacket() {}
		
		public Entity entity;
	}
	
	static public class BlockUpdatePacket {
		public BlockUpdatePacket() {}
		
		public Vector2i pos;
		public int blockId;
	}
	
	static public class InventoryUpdatePacket {
		public Inventory inv;
	}
	
	static public class MetaAddPacket {
		
	}
	
	static public class MetaUpdatePacket {
		
	}
	
	static public class ServerClosedPacket {}
	
	static public class ClientDisconnectPacket {}

	
	private static void register(Kryo k, Class<?>...classes){
		for(Class<?> c : classes){
			k.register(c);
		}
	}
	
//	public static class GameMapSerializer extends Serializer<int[][]> {
//	   public void write (Kryo kryo, Output output, int[][] map) {
//	      output.writeInt(map.length);
//	      output.writeInt(map[0].length);
//	      for(int x=0; x<map.length; x++) {
//	    	  for(int y=0; y<map[0].length; y++) {
//	    		  output.writeInt(map[x][y]);
//	    	  }
//	      }
//	   }
//
//	   public int[][] read (Kryo kryo, Input input, Class<int[][]> type) {
//		  int width = input.readInt();
//		  int height = input.readInt();
//		  int map[][] = new int[width][height];
//		  
//		  for(int x=0; x<width; x++) {
//			  for(int y=0; y<height; y++) {
//				  map[x][y] = input.readInt();
//			  }
//		  }
//		  
//	      return map;
//	   }
//	}	
}

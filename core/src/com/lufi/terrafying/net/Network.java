package com.lufi.terrafying.net;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.lufi.terrafying.entities.Entity;
import com.lufi.terrafying.entities.Player;

public class Network {
	static public final int port0 = 54555;
	static public final int port1 = 54556;
	
	public static void register(Kryo k) {
		register(k,
				ConnectionRequestPacket.class,
				ConnectionResponsePacket.class,
				EntityAddPacket.class,
				EntityUpdatePacket.class,
				
				Player.class,
				Entity.class,
				
				Array.class,
				Object[].class
				);
	}
	
	static public class ConnectionRequestPacket {
		public ConnectionRequestPacket() {}
		
		public String name;
	}
	
	static public class ConnectionResponsePacket {
		public ConnectionResponsePacket() {}
		
		public int id;
		public Array<Entity> entities;
	}

	static public class EntityAddPacket {
		public EntityAddPacket() {}
		
		public Entity entity;
	}
	
	static public class EntityUpdatePacket {
		public EntityUpdatePacket() {}
		
		public Entity entity;
	}

	
	private static void register(Kryo k, Class<?>...classes){
		for(Class<?> c : classes){
			k.register(c);
		}
	}
}

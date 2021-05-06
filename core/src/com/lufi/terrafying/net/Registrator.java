package com.lufi.terrafying.net;

import com.esotericsoftware.kryo.Kryo;
import com.lufi.terrafying.net.packets.*;

public class Registrator {
	public static void register(Kryo k) {
		register(k,
				ConnectionRequestPacket.class,
				ConnectionResponsePacket.class
				);
	}
	
	private static void register(Kryo k, Class<?>...classes){
		for(Class<?> c : classes){
			k.register(c);
		}
	}
}

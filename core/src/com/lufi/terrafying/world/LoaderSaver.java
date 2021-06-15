package com.lufi.terrafying.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.lufi.terrafying.entities.Entity;
import com.lufi.terrafying.entities.EntityManager;
import com.lufi.terrafying.items.Inventory;
import com.lufi.terrafying.net.OfflinePlayers;
import com.lufi.terrafying.net.OfflinePlayers.OfflinePlayer;

public class LoaderSaver {
	public static String[] getAvailableMaps() {
		File folder = new File(".");
		File files[] = folder.listFiles();
		if(files != null) {
			ArrayList<String> names = new ArrayList<String>();
			for(int i=0; i<files.length; i++) {
				if(files[i].getName().endsWith(".ser"))
					names.add(files[i].getName());
			}
			String ret[] = new String[names.size()];
			return names.toArray(ret);
		}
		return null;
	}
	
	public static ServerWorld loadWorld(String name) {
		try {
			FileInputStream fin = new FileInputStream(name);
			ObjectInputStream oin = new ObjectInputStream(fin);
			ServerWorld world = (ServerWorld)oin.readObject();
			oin.close();
			world.entityManager = new EntityManager();
			world.playerInvs = new HashMap<String, Inventory>();
			return world;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	public static void saveWorld(ServerWorld world) {
		try {
			System.out.println("saving map as " + world.map.getName());
			System.out.println("num entities: " + world.entityManager.getNumEntites());
			
			for(Entity e : world.entityManager.getEntities()) {
				String name = e.name;
				OfflinePlayer oPlayer = new OfflinePlayer();
				oPlayer.inv = world.playerInvs.get(name);
				oPlayer.pos.x = e.posx;
				oPlayer.pos.y = e.posy;
				world.offlinePlayers.players.put(name, oPlayer);				
			}

			FileOutputStream fout = new FileOutputStream(world.map.getName().concat(".ser"));
			ObjectOutputStream oout = new ObjectOutputStream(fout);
			oout.writeObject(world);
			oout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void genAndSaveMap(String name, int width, int height) {
		try {
			Map map = new Map(name, width, height);
			map.generate();		
			ServerWorld world = new ServerWorld();
			world.map = map;
			world.offlinePlayers = new OfflinePlayers();
			saveWorld(world);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

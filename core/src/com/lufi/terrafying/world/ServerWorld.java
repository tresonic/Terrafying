package com.lufi.terrafying.world;

import java.io.Serializable;
import java.util.HashMap;

import com.lufi.terrafying.entities.EntityManager;
import com.lufi.terrafying.items.Inventory;
import com.lufi.terrafying.net.OfflinePlayers;

public class ServerWorld implements Serializable {
	public Map map;
	public transient HashMap<String, Inventory> playerInvs;
	public transient EntityManager entityManager;
	public OfflinePlayers offlinePlayers;
	
	public ServerWorld() {
		entityManager = new EntityManager();
		playerInvs = new HashMap<String, Inventory>();
	}
	
	public ServerWorld(String name, int width, int height) {
		entityManager = new EntityManager();
		playerInvs = new HashMap<String, Inventory>();
		map = new Map(name, width, height);
		map.generate();		
		offlinePlayers = new OfflinePlayers();
	}
}

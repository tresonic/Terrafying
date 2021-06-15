package com.lufi.terrafying.net;

import java.io.Serializable;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.lufi.terrafying.entities.Entity;
import com.lufi.terrafying.items.Inventory;

public class OfflinePlayers implements Serializable {
	public static class OfflinePlayer implements Serializable {
		public Vector2 pos;
		public Inventory inv;
		
		public OfflinePlayer() {
			pos = new Vector2();
			inv = new Inventory();
		}
	}
	
	public HashMap<String, OfflinePlayer> players;
	
	public OfflinePlayers() {
		players = new HashMap<String, OfflinePlayer>();
	}
}

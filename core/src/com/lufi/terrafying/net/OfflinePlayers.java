package com.lufi.terrafying.net;

import java.io.Serializable;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.lufi.terrafying.items.Inventory;

public class OfflinePlayers implements Serializable {
	private static final long serialVersionUID = 1L;

	public static class OfflinePlayer implements Serializable {
		private static final long serialVersionUID = 1L;
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

package com.lufi.terrafying.net;

import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.kryonet.Listener;
import com.lufi.terrafying.entities.Entity;
import com.lufi.terrafying.net.packets.*;
import com.lufi.terrafying.world.Map;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;

public class TerrafyingServer {
	private static TerrafyingServer instance;
	
	private Server server;
	private Map map;
	private Array<Entity> entities;
	
	private TerrafyingServer() {}
	
	public static TerrafyingServer the() {
		if(instance == null) {
			instance = new TerrafyingServer();
		}
		return instance;
	}
	
	public void start() {
		try {
			server = new Server();
			server.start();
			server.bind(30000, 30001);
			server.addListener(new Listener() {
				public void received(Connection connection, Object object) {
					packetReceived(connection, object);
				}
			});
			Registrator.register(server.getKryo());
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("error while starting server");
		}
	}
	
	public void stop() {
		server.stop();
	}
	
	public void packetReceived(Connection connection, Object object) {
		if(object instanceof ConnectionRequestPacket) {
			System.out.println("Connection request: " + ((ConnectionRequestPacket)object).name);
			connection.sendTCP(new ConnectionResponsePacket());
		}
	}
}

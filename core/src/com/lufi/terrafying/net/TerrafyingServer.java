package com.lufi.terrafying.net;

import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.kryonet.Listener;
import com.lufi.terrafying.entities.Entity;
import com.lufi.terrafying.entities.EntityManager;
import com.lufi.terrafying.net.Network.*;
import com.lufi.terrafying.world.Map;
import com.lufi.terrafying.world.Vector2i;
import com.badlogic.gdx.utils.Array;

public class TerrafyingServer {
	private static TerrafyingServer instance;
	
	private Server server;
	private Map map;
	private EntityManager entityManager;
	private int entityCounter;
	
	private TerrafyingServer() {
		entityManager = new EntityManager();	
		map = new Map(50, 50);
		long b = System.nanoTime();
		map.generate();
		long e = System.nanoTime();
		System.out.println("mapgen took " + (e-b)/1000000 + " ms");
		entityCounter = 0;
	}
	
	public static TerrafyingServer the() {
		if(instance == null) {
			instance = new TerrafyingServer();
		}
		return instance;
	}
	
	public void start() {
		System.out.println("starting server");
		try {
			server = new Server(Network.port0, Network.port1);
			server.bind(Network.port0, Network.port1);
			
			server.start();
			server.addListener(new Listener() {
				public void received(Connection connection, Object object) {
					packetReceived(connection, object);
				}
				
				@Override
				public void disconnected(Connection connection) {
					System.out.println("client disconnected from server!");
				}
				
			});
			Network.register(server.getKryo());
			
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
			System.out.println("Connection request from client: " + ((ConnectionRequestPacket)object).name + connection.getRemoteAddressTCP());
			ConnectionResponsePacket p = new ConnectionResponsePacket();
			
			p.id = entityCounter++;
			p.entities = entityManager.getEntities();
			p.startChunk = map.getChunkAt(map.spawnpoint);
			p.startChunkId = map.getChunkIdAt(map.spawnpoint);
			p.spawnpoint = map.spawnpoint;
			connection.sendTCP(p);
			
			Entity e = new Entity(0, 0, p.id);
			entityManager.addEntity(e);
			
			EntityAddPacket aP = new EntityAddPacket();
			aP.entity = e;
			server.sendToAllExceptTCP(connection.getID(), aP);
		}
		
		if(object instanceof EntityUpdatePacket) {
			//System.out.println("server received EntityUpdate");
			server.sendToAllExceptUDP(connection.getID(), object);
		}
		
		if(object instanceof ChunkRequestPacket) {
			ChunkResponsePacket cRP = new ChunkResponsePacket();
			Vector2i cId = ((ChunkRequestPacket)object).id;
			cRP.chunk = map.getChunkAtChunkId(cId);
			cRP.chunkId = cId;
			connection.sendTCP(cRP);
		}
	}
}

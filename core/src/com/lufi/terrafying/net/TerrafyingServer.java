package com.lufi.terrafying.net;

import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.lufi.terrafying.entities.Entity;
import com.lufi.terrafying.entities.EntityManager;
import com.lufi.terrafying.entities.Player;
import com.lufi.terrafying.net.Network.*;
import com.lufi.terrafying.util.Vector2i;
import com.lufi.terrafying.world.Map;
import com.lufi.terrafying.world.MapLoaderSaver;
import com.badlogic.gdx.utils.Array;

public class TerrafyingServer {
	private static TerrafyingServer instance;
	
	private Server server;
	private Map map;
	private EntityManager entityManager;
	
	private TerrafyingServer() {
		entityManager = new EntityManager();	
	}
	
	public static TerrafyingServer the() {
		if(instance == null) {
			instance = new TerrafyingServer();
		}
		return instance;
	}
	
	public void start(String mapname) {
		Map nMap = MapLoaderSaver.loadMap(mapname);
		if(nMap == null) {
			System.out.println("starting server");
			nMap = new Map(mapname, 50, 15);
			long beg = System.nanoTime();
			nMap.generate();
			long end = System.nanoTime();
			System.out.println("mapgen took " + (end-beg)/1000000 + " ms");
		}
		map = nMap;
		
		try {
			server = new Server(Network.port0, Network.port1);
			server.bind(Network.port0, Network.port1);
			
			server.start();
			server.addListener(new ThreadedListener(new Listener() {
				public void received(Connection connection, Object object) {
					packetReceived(connection, object);
				}
				
				@Override
				public void disconnected(Connection connection) {
					System.out.println("client disconnected from server!");
				}
				
			}));
			Network.register(server.getKryo());
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("error while starting server");
		}
		
	}
	
	public void stop() {
		if(server != null) {
			server.sendToAllTCP(new ServerClosedPacket());
			server.stop();
			MapLoaderSaver.saveMap(map);
		}
	}
	
	public void packetReceived(Connection connection, Object object) {
		if(object instanceof ConnectionRequestPacket) {
			System.out.println("Connection request from client: " + ((ConnectionRequestPacket)object).name + connection.getRemoteAddressTCP());
			ConnectionResponsePacket p = new ConnectionResponsePacket();
			
			p.id = connection.getID();
			p.entities = entityManager.getEntities();
			p.startChunk = map.getChunkAt(map.spawnpoint);
			p.startChunkId = map.getChunkIdAt(map.spawnpoint);
			p.spawnpoint = map.spawnpoint;
			p.name = ((ConnectionRequestPacket)object).name;
			connection.sendTCP(p);
			
			Entity e = new Entity(0, 0, p.id, p.name);
			e.isPlayer = true;
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
		
		if(object instanceof BlockUpdatePacket) {
			BlockUpdatePacket p = (BlockUpdatePacket)object;
			map.setBlock(p.pos.x, p.pos.y, p.blockId);
			server.sendToAllExceptTCP(connection.getID(), p);
		}
		
		if(object instanceof ClientDisconnectPacket) {
			EntityRemovePacket p = new EntityRemovePacket();
			p.id = connection.getID();
			server.sendToAllExceptTCP(connection.getID(), p);
		}
	}
}

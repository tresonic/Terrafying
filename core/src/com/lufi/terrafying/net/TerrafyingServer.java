package com.lufi.terrafying.net;

import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.lufi.terrafying.entities.Entity;
import com.lufi.terrafying.entities.EntityManager;
import com.lufi.terrafying.entities.Player;
import com.lufi.terrafying.items.Inventory;
import com.lufi.terrafying.items.Item;
import com.lufi.terrafying.items.ItemStack;
import com.lufi.terrafying.net.Network.*;
import com.lufi.terrafying.net.OfflinePlayers.OfflinePlayer;
import com.lufi.terrafying.util.Vector2i;
import com.lufi.terrafying.world.LoaderSaver;
import com.lufi.terrafying.world.Map;
import com.lufi.terrafying.world.ServerWorld;

import java.util.HashMap;

import com.badlogic.gdx.utils.Array;

public class TerrafyingServer {
	private static TerrafyingServer instance;
	
	private boolean persistence;
	
	private Server server;
	private ServerWorld world;
	
	private TerrafyingServer() {
		world = new ServerWorld();
	}
	
	public static TerrafyingServer the() {
		if(instance == null) {
			instance = new TerrafyingServer();
		}
		return instance;
	}
	
	public void startTest() {
		persistence = false;
		
		world = new ServerWorld("testtesttest", 50, 15);
		
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
	
	public void start(String mapname) {
		persistence = true;
		ServerWorld nWorld = LoaderSaver.loadWorld(mapname);
		if(nWorld == null) {
			nWorld = new ServerWorld();
			System.out.println("starting server with NULL map");
			nWorld.map = new Map(mapname, 50, 15);
			nWorld.playerInvs = new HashMap<String, Inventory>();
			long beg = System.nanoTime();
			nWorld.map.generate();
			long end = System.nanoTime();
			System.out.println("mapgen took " + (end-beg)/1000000 + " ms");
		}
		world = nWorld;
		
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
			if(persistence)
				LoaderSaver.saveWorld(world);
			server.sendToAllTCP(new ServerClosedPacket());
			server.stop();
		}
	}
	
	public void packetReceived(Connection connection, Object object) {
		if(object instanceof ConnectionRequestPacket) {
			System.out.println("Connection request from client: " + ((ConnectionRequestPacket)object).name + connection.getRemoteAddressTCP());
			String clientName = ((ConnectionRequestPacket)object).name;
			
			if(world.playerInvs.containsKey(clientName)) {
				System.out.println("client with same name already on the server");
				connection.sendTCP(new ConnectionDeniedPacket());
				return;
			}
			ConnectionResponsePacket p = new ConnectionResponsePacket();
			if(world.offlinePlayers.players.containsKey(clientName)) {
				System.out.println("old player");
				OfflinePlayer oPlayer = world.offlinePlayers.players.get(clientName);
				
				p.id = connection.getID();
				p.entities = world.entityManager.getEntities();
				p.startChunk = world.map.getChunkAt(oPlayer.pos);
				p.startChunkId = world.map.getChunkIdAt(oPlayer.pos);
				p.spawnpoint = oPlayer.pos;
				p.inventory = oPlayer.inv;
				p.name = ((ConnectionRequestPacket)object).name;
				connection.sendTCP(p);
			} else {	
				System.out.println("new player");
				p.id = connection.getID();
				p.entities = world.entityManager.getEntities();
				p.startChunk = world.map.getChunkAt(world.map.spawnpoint);
				p.startChunkId = world.map.getChunkIdAt(world.map.spawnpoint);
				p.spawnpoint = world.map.spawnpoint;
				p.name = ((ConnectionRequestPacket)object).name;
				p.inventory = new Inventory(27);
				p.inventory.addItem(new ItemStack(Item.getItemByName("stone"), 10));
				connection.sendTCP(p);
			}
			
			world.playerInvs.put(p.name, p.inventory);
			
			Entity e = new Entity(0, 0, p.id, p.name);
			e.isPlayer = true;
			world.entityManager.addEntity(e);
			
			EntityAddPacket aP = new EntityAddPacket();
			aP.entity = e;
			server.sendToAllExceptTCP(connection.getID(), aP);
		}
		
		if(object instanceof EntityUpdatePacket) {
			//System.out.println("server received EntityUpdate");
			world.entityManager.updateEntity(((EntityUpdatePacket)object).entity);
			server.sendToAllExceptUDP(connection.getID(), object);
		}
		
		if(object instanceof ChunkRequestPacket) {
			ChunkResponsePacket cRP = new ChunkResponsePacket();
			Vector2i cId = ((ChunkRequestPacket)object).id;
			cRP.chunk = world.map.getChunkAtChunkId(cId);
			cRP.chunkId = cId;
			connection.sendTCP(cRP);
		}
		
		if(object instanceof BlockUpdatePacket) {
			BlockUpdatePacket p = (BlockUpdatePacket)object;
			world.map.setBlock(p.pos.x, p.pos.y, p.blockId);
			server.sendToAllExceptTCP(connection.getID(), p);
		}
		
		if(object instanceof InventoryUpdatePacket) {
			Entity e = world.entityManager.getEntity(connection.getID());
			world.playerInvs.put(e.name, ((InventoryUpdatePacket)object).inv);
		}
		
		if(object instanceof ClientDisconnectPacket) {

			String name = world.entityManager.getEntity(connection.getID()).name;
			OfflinePlayer oPlayer = new OfflinePlayer();
			oPlayer.inv = world.playerInvs.get(name);
			oPlayer.pos.x = world.entityManager.getEntity(connection.getID()).posx;
			oPlayer.pos.y = world.entityManager.getEntity(connection.getID()).posy;
			System.out.println("player left at: " + world.entityManager.getEntities());
			System.out.println("aaa: " + world.playerInvs.get(name));
			world.offlinePlayers.players.put(name, oPlayer);
			world.playerInvs.remove(name);
			
			EntityRemovePacket p = new EntityRemovePacket();
			p.id = connection.getID();
			world.entityManager.removeEntity(p.id);
			server.sendToAllExceptTCP(connection.getID(), p);
		}
	}
}

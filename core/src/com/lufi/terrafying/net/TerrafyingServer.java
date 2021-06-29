package com.lufi.terrafying.net;

import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.lufi.terrafying.entities.Entity;
import com.lufi.terrafying.entities.EntityManager;
import com.lufi.terrafying.items.Inventory;
import com.lufi.terrafying.items.Item;
import com.lufi.terrafying.items.ItemStack;
import com.lufi.terrafying.net.Network.*;
import com.lufi.terrafying.net.OfflinePlayers.OfflinePlayer;
import com.lufi.terrafying.util.Log;
import com.lufi.terrafying.util.Vector2i;
import com.lufi.terrafying.world.LoaderSaver;
import com.lufi.terrafying.world.Map;
import com.lufi.terrafying.world.ServerWorld;

import java.util.HashMap;

public class TerrafyingServer {
	private static TerrafyingServer instance;

	private boolean persistence;

	private Server server;
	private ServerWorld world;

	private TerrafyingServer() {
		world = new ServerWorld();
	}

	public static TerrafyingServer the() {
		if (instance == null) {
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
					Log.serverLog("client disconnected from server!");
				}

			}));
			Network.register(server.getKryo());

		} catch (Exception e) {
			e.printStackTrace();
			Log.serverLog("error while starting server");
		}
	}

	public void start(String mapname) {
		persistence = true;
		ServerWorld nWorld = LoaderSaver.loadWorld(mapname);
		if (nWorld == null) {
			nWorld = new ServerWorld();
			Log.serverLog("starting with NULL map");
			nWorld.map = new Map(mapname, 50, 15);
			nWorld.playerInvs = new HashMap<String, Inventory>();
			nWorld.entityManager = new EntityManager();
			nWorld.offlinePlayers = new OfflinePlayers();
			long beg = System.nanoTime();
			nWorld.map.generate();
			long end = System.nanoTime();
			Log.serverLog("mapgen took " + (end - beg) / 1000000 + " ms");
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
					Log.serverLog("client disconnected from server!");
				}

			}));
			Network.register(server.getKryo());

		} catch (Exception e) {
			e.printStackTrace();
			Log.serverLog("error while starting server");
		}

	}

	public void stop() {
		if (server != null) {
			if (persistence)
				LoaderSaver.saveWorld(world);
			server.sendToAllTCP(new ServerClosedPacket());
			server.stop();
		}
	}

	public void packetReceived(Connection connection, Object object) {
		if (object instanceof ConnectionRequestPacket) {
			Log.serverLog("Connection request from client: " + ((ConnectionRequestPacket) object).name
					+ connection.getRemoteAddressTCP());
			String clientName = ((ConnectionRequestPacket) object).name;

			if (world.playerInvs.containsKey(clientName)) {
				Log.serverLog("client with same name already on the server");
				connection.sendTCP(new ConnectionDeniedPacket());
				return;
			}
			ConnectionResponsePacket p = new ConnectionResponsePacket();
			if (world.offlinePlayers.players.containsKey(clientName)) {
				OfflinePlayer oPlayer = world.offlinePlayers.players.get(clientName);

				p.id = connection.getID();
				p.entities = world.entityManager.getEntities();
				p.startChunk = world.map.getChunkAt(oPlayer.pos);
				p.startChunkId = world.map.getChunkIdAt(oPlayer.pos);
				p.width = world.map.getWidth();
				p.height = world.map.getHeight();
				p.spawnpoint = oPlayer.pos;
				p.inventory = oPlayer.inv;
				p.metadata = world.map.getMetadata();
				p.metaLock = world.map.getMetaLockData();
				p.name = ((ConnectionRequestPacket) object).name;
				connection.sendTCP(p);
			} else {
				p.id = connection.getID();
				p.entities = world.entityManager.getEntities();
				p.startChunk = world.map.getChunkAt(world.map.spawnpoint);
				p.startChunkId = world.map.getChunkIdAt(world.map.spawnpoint);
				p.spawnpoint = world.map.spawnpoint;
				p.width = world.map.getWidth();
				p.height = world.map.getHeight();
				p.name = ((ConnectionRequestPacket) object).name;
				p.inventory = new Inventory(27);
				if (clientName.contains("4dm1n")) {
					p.inventory.addItem(new ItemStack(Item.getItemByName("stone"), 10));
					p.inventory.addItem(new ItemStack(Item.getItemByName("admintool"), 1));
					p.inventory.addItem(new ItemStack(Item.getItemByName("jetpack"), 1));
					p.inventory.addItem(new ItemStack(Item.getItemByName("ironsword"), 1));
					p.inventory.addItem(new ItemStack(Item.getItemByName("computer"), 1));
					p.inventory.addItem(new ItemStack(Item.getItemByName("bricks"), 4));
					p.inventory.addItem(new ItemStack(Item.getItemByName("window"), 1));
					p.inventory.addItem(new ItemStack(Item.getItemByName("door-open"), 1));
					p.inventory.addItem(new ItemStack(Item.getItemByName("chest"), 1));
					p.inventory.addItem(new ItemStack(Item.getItemByName("oakwoodplanks"), 20));
				}
				p.metadata = world.map.getMetadata();
				p.metaLock = world.map.getMetaLockData();
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

		if (object instanceof EntityUpdatePacket) {
			// System.out.println("server received EntityUpdate");
			world.entityManager.updateEntity(((EntityUpdatePacket) object).entity);
			server.sendToAllExceptUDP(connection.getID(), object);
		}
		
		if (object instanceof HealthUpdatePacket) {
			Entity e = world.entityManager.getEntityByName(((HealthUpdatePacket)object).name);
			server.sendToTCP(e.id, object);
		}

		if (object instanceof ChunkRequestPacket) {
			ChunkResponsePacket cRP = new ChunkResponsePacket();
			Vector2i cId = ((ChunkRequestPacket) object).id;
			cRP.chunk = world.map.getChunkAtChunkId(cId);
			cRP.chunkId = cId;
			connection.sendTCP(cRP);
		}

		if (object instanceof BlockUpdatePacket) {
			BlockUpdatePacket p = (BlockUpdatePacket) object;
			world.map.setBlock(p.pos.x, p.pos.y, p.blockId);
			server.sendToAllExceptTCP(connection.getID(), p);
		}

		if (object instanceof InventoryUpdatePacket) {
			Entity e = world.entityManager.getEntity(connection.getID());
			world.playerInvs.put(e.name, ((InventoryUpdatePacket) object).inv);
		}

		if (object instanceof MetaUpdatePacket) {
			MetaUpdatePacket p = (MetaUpdatePacket) object;
			world.map.setMeta(p.pos, p.inv);
			server.sendToAllExceptTCP(connection.getID(), p);
		}

		if (object instanceof LockUpdatePacket) {
			LockUpdatePacket p = (LockUpdatePacket) object;
			world.map.setMetaLock(p.pos, p.lock);
			server.sendToAllExceptTCP(connection.getID(), p);
		}

		if (object instanceof ClientDisconnectPacket) {

			String name = world.entityManager.getEntity(connection.getID()).name;
			OfflinePlayer oPlayer = new OfflinePlayer();
			oPlayer.inv = world.playerInvs.get(name);
			oPlayer.pos.x = world.entityManager.getEntity(connection.getID()).posx;
			oPlayer.pos.y = world.entityManager.getEntity(connection.getID()).posy;
			Log.serverLog("player left at: "
					+ world.entityManager.getEntity(world.entityManager.getEntity(connection.getID()).id)
					+ " with inv: " + world.playerInvs.get(name));
			world.offlinePlayers.players.put(name, oPlayer);
			world.playerInvs.remove(name);

			EntityRemovePacket p = new EntityRemovePacket();
			p.id = connection.getID();
			world.entityManager.removeEntity(p.id);
			server.sendToAllExceptTCP(connection.getID(), p);
		}
	}
}

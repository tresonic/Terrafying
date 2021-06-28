package com.lufi.terrafying.net;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.lufi.terrafying.entities.Entity;
import com.lufi.terrafying.entities.Player;
import com.lufi.terrafying.net.Network.*;
import com.lufi.terrafying.screens.GameScreen;
import com.lufi.terrafying.util.Log;
import com.lufi.terrafying.util.Options;
import com.lufi.terrafying.world.Block;
import com.lufi.terrafying.world.Chunk;
import com.lufi.terrafying.world.Map;
import com.lufi.terrafying.world.World;

public class TerrafyingClient {
	private final World world;
	
	private Client client;
	
	public boolean connected;
	public boolean connecting;
	
	private Options options;
	
	public TerrafyingClient(final World w, Options nOptions) {
		world = w;
		connected = false;
		connecting = false;
		client = new Client(Network.port0, Network.port1);
		Network.register(client.getKryo());
		options = nOptions;
	}
	
	public void connect(final String playerName, final String address) {		
		client.start();
		
		new Thread("Connect") {
			public void run () {
				try {
					Log.clientLog("connecting to " + address);
					connecting = true;
					client.connect(5000, address, Network.port0, Network.port1);
					// Log.clientLog("connect call finished");
					// Server communication after connection can go here, or in Listener#connected().
				} catch (IOException ex) {
					ex.printStackTrace();
					connecting = false;
				}
			}
		}.start();
		
		client.addListener(new ThreadedListener(new Listener() {
			public void received(Connection connection, Object object) {
				packetReceived(connection, object);
			}
			
			public void connected(Connection connection) {
				Log.clientLog("established connection");
				ConnectionRequestPacket p = new ConnectionRequestPacket();
				p.name = playerName;
				client.sendTCP(p);
			}
			@Override
			public void disconnected(Connection connection) {
				Log.clientLog("lost connection");
				connected = false;
			}
		}));

	}
	
	public void disconnect() {
		sendPlayerInv();
		client.sendTCP(new ClientDisconnectPacket());
		client.close();
		connected = false;
		connecting = false;
	}
	
	public void update(OrthographicCamera camera) {
		EntityUpdatePacket p = new EntityUpdatePacket();
		
		p.entity = (Entity)world.player;
		client.sendUDP(p);
		
		final int chunkDist = (int)(camera.viewportWidth * camera.zoom) / Block.BLOCK_SIZE / Chunk.CHUNK_SIZE + 2;
		
		for(int x = -chunkDist; x<chunkDist; x++) {
			for(int y = -chunkDist; y<chunkDist; y++) {
				int offsetX = x * Block.BLOCK_SIZE * Chunk.CHUNK_SIZE;
				int offsetY = y * Block.BLOCK_SIZE * Chunk.CHUNK_SIZE;
				
				if(world.map.getChunkAt(world.player.posx + offsetX, world.player.posy + offsetY) == null) {
					ChunkRequestPacket cRP = new ChunkRequestPacket();
					cRP.id = world.map.getChunkIdAt(world.player.posx + offsetX, world.player.posy + offsetY);
					client.sendTCP(cRP);
				}
			}
		}

	}
	
	public void sendBlockUpdate(float x, float y) {
		BlockUpdatePacket p = new BlockUpdatePacket();
		p.blockId = world.map.getBlockAt(x, y);
		p.pos = Map.getBlockPos(x, y);
		client.sendTCP(p);
	}
	
	public void sendHealthUpdate(String name, int damage) {
		HealthUpdatePacket p = new HealthUpdatePacket();
		p.name = name;
		p.damage = damage;
		client.sendTCP(p);
	}
	
	public void sendMetaUpdate(float x, float y) {
		MetaUpdatePacket p = new MetaUpdatePacket();
		p.pos = Map.getBlockPos(x, y);
		p.inv = world.map.getMeta(p.pos);
		client.sendTCP(p);
	}
	
	public void sendMetaLock(float x, float y, boolean lock) {
		LockUpdatePacket p = new LockUpdatePacket();
		p.pos = Map.getBlockPos(x, y);
		p.lock = lock;
		client.sendTCP(p);
	}
	
	public void sendPlayerInv() {
		InventoryUpdatePacket p = new InventoryUpdatePacket();
		p.inv = world.player.inventory;
		client.sendTCP(p);
	}
	
	public void packetReceived(Connection connection, Object object) {
		if(object instanceof ConnectionResponsePacket) {
			ConnectionResponsePacket p = (ConnectionResponsePacket)object;

			world.map.addChunk(p.startChunkId, p.startChunk);
			world.player = new Player(p.spawnpoint.x, p.spawnpoint.y, p.id, p.name, options);
			world.map.spawnpoint.set(p.spawnpoint);
			world.map.setWidth(p.width);
			world.map.setHeight(p.height);
			world.player.isPlayer = true;
			world.entityManager.addEntity(world.player);
			world.entityManager.addEntities(p.entities);
			world.player.inventory = p.inventory;
			world.map.setMetadata(p.metadata);
			world.map.setMetaLockData(p.metaLock);
			
			connecting = false;
			connected = true;
		}
		
		if(object instanceof EntityAddPacket) {
			world.entityManager.addEntity(((EntityAddPacket)object).entity);
			//System.out.println("client: player joined " + ((EntityAddPacket)object).entity.id);
			//System.out.println(world.entityManager.getEntities());
		}
		
		if(object instanceof EntityRemovePacket) {
			Log.clientLog("entity removed packet");
			int id = ((EntityRemovePacket)object).id;
			world.entityManager.removeEntity(id);
		}
		
		if(object instanceof EntityUpdatePacket) {
			//System.out.println("client: received EntityUpdate " + ((EntityUpdatePacket)object).entity.id);
			world.entityManager.updateEntity(((EntityUpdatePacket)object).entity);
		}
		
		if(object instanceof HealthUpdatePacket) {
			world.player.takeDamage(((HealthUpdatePacket)object).damage);
		}
		
		if(object instanceof ChunkResponsePacket) {
			ChunkResponsePacket p = (ChunkResponsePacket)object;
			world.map.addChunk(p.chunkId, p.chunk);
		}
		
		if(object instanceof BlockUpdatePacket) {
			BlockUpdatePacket p = (BlockUpdatePacket)object;
			world.map.setBlock(p.pos.x, p.pos.y, p.blockId);
		}
		
		if(object instanceof InventoryUpdatePacket) {
			world.player.inventory = ((InventoryUpdatePacket)object).inv;
		}
		
		if(object instanceof MetaUpdatePacket) {
			MetaUpdatePacket p = (MetaUpdatePacket)object;
			world.map.setMeta(p.pos, p.inv);
		}
		
		if(object instanceof LockUpdatePacket) {
			LockUpdatePacket p = (LockUpdatePacket)object;
			world.map.setMetaLock(p.pos, p.lock);
		}
		
		if(object instanceof ServerClosedPacket) {
			client.close();
			connected = false;
			connecting = false;
		}
		
		if(object instanceof ConnectionDeniedPacket) {
			client.close();
			connected = false;
			connecting = false;
		}
		
	}
	
	public static String discoverServer() {
		Client nC = new Client();
		return nC.discoverHost(Network.port1, 5000).toString().substring(1);
	}
}

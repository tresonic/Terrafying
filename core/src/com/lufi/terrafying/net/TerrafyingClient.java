package com.lufi.terrafying.net;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.lufi.terrafying.entities.Entity;
import com.lufi.terrafying.entities.Player;
import com.lufi.terrafying.net.Network.*;
import com.lufi.terrafying.world.World;

public class TerrafyingClient {
	private final World world;
	
	private Client client;
	
	public boolean connected;
	public boolean connecting;
	
	public TerrafyingClient(final World w) {
		world = w;
		connected = false;
		connecting = false;
		client = new Client(Network.port0, Network.port1);
		Network.register(client.getKryo());
	}
	
	public void connect(final String playerName, final String address) {
		client.start();
		new Thread("Connect") {
			public void run () {
				try {
					connecting = true;
					client.connect(5000, address, Network.port0, Network.port1);
					// Server communication after connection can go here, or in Listener#connected().
				} catch (IOException ex) {
					ex.printStackTrace();
					connecting = false;
				}
			}
		}.start();
		client.addListener(new Listener() {
			public void received(Connection connection, Object object) {
				packetReceived(connection, object);
			}
			
			public void connected(Connection connection) {
				System.out.println("client established connection");
				ConnectionRequestPacket p = new ConnectionRequestPacket();
				p.name = playerName;
				client.sendTCP(p);
			}
			@Override
			public void disconnected(Connection connection) {
				System.out.println("client lost connection");
				connected = false;
			}
		});

	}
	
	public void update() {
		EntityUpdatePacket p = new EntityUpdatePacket();
		p.entity = world.player;
		//System.out.println("sending position..." + client.getRemoteAddressTCP());
		client.sendTCP(p);
	}
	
	public void packetReceived(Connection connection, Object object) {
		if(object instanceof ConnectionResponsePacket) {
			ConnectionResponsePacket p = (ConnectionResponsePacket)object;
			connecting = false;
			connected = true;
			System.out.println("connected!");
			world.player = new Player(0, 0, p.id);
			world.entityManager.addEntity(world.player);
			System.out.println("before " + world.entityManager.getEntities().size);
			world.entityManager.addEntities(p.entities);
			System.out.println("joined server with entites: " + p.entities.size + " and entities is now " + world.entityManager.getEntities().size);
			System.out.println(p.entities);
		}
		
		if(object instanceof EntityAddPacket) {
			world.entityManager.addEntity(((EntityAddPacket)object).entity);
			//System.out.println("client: player joined " + ((EntityAddPacket)object).entity.id);
			//System.out.println(world.entityManager.getEntities());
		}
		
		if(object instanceof EntityUpdatePacket) {
			//System.out.println("client: received EntityUpdate " + ((EntityUpdatePacket)object).entity.id);
			world.entityManager.updateEntity(((EntityUpdatePacket)object).entity);
		}
	}
}

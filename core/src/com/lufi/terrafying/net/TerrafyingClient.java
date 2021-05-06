package com.lufi.terrafying.net;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.lufi.terrafying.net.packets.*;
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
		client = new Client();
		Registrator.register(client.getKryo());
	}
	
	public void connect(String playerName, final String address, int port0, int port1) {
		client.start();
		new Thread("Connect") {
			public void run () {
				try {
					connecting = true;
					client.connect(5000, address, 30000, 30001);
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
				connecting = false;
				connected = true;
			}
		});
		ConnectionRequestPacket p = new ConnectionRequestPacket();
		p.name = playerName;
		
		client.sendTCP(p);
	}
	
	public void packetReceived(Connection connection, Object object) {
		if(object instanceof ConnectionResponsePacket) {
			System.out.println("connected!");
		}
	}
}

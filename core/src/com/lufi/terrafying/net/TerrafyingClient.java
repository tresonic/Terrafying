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
	
	public boolean connected = false;
	
	public TerrafyingClient(final World w) {
		world = w;
		client = new Client();
		Registrator.register(client.getKryo());
	}
	
	public void connect(String playerName, String address, int port0, int port1) {
		client.start();
		try {
			client.connect(1000, address, port0, port1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.addListener(new Listener() {
			public void received(Connection connection, Object object) {
				packetReceived(connection, object);
			}
		});
		ConnectionRequestPacket p = new ConnectionRequestPacket();
		p.name = playerName;
		
		client.sendTCP(p);
	}
	
	public void packetReceived(Connection connection, Object object) {
		if(object instanceof ConnectionResponsePacket) {
			connected = true;
			System.out.println("connected!");
		}
	}
}

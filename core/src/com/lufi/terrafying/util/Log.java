package com.lufi.terrafying.util;

public class Log {
	public static void serverLog(String s) {
		System.out.println("[SERVER] " + s);
	}
	
	public static void clientLog(String s) {
		System.out.println("[CLIENT] " + s);
	}
}

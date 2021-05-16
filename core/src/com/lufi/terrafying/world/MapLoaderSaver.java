package com.lufi.terrafying.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MapLoaderSaver {
	public static String[] getAvailableMaps() {
		File folder = new File(".");
		File files[] = folder.listFiles();
		if(files != null) {
			ArrayList<String> names = new ArrayList<String>();
			for(int i=0; i<files.length; i++) {
				if(files[i].getName().endsWith(".ser"))
					names.add(files[i].getName());
			}
			String ret[] = new String[names.size()];
			return names.toArray(ret);
		}
		return null;
	}
	
	public static Map loadMap(String name) {
		try {
			FileInputStream fin = new FileInputStream(name);
			ObjectInputStream oin = new ObjectInputStream(fin);
			Map map = (Map)oin.readObject();
			oin.close();
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void saveMap(Map map) {
		try {
			System.out.println("saving map as " + map.getName());
			FileOutputStream fout = new FileOutputStream(map.getName().concat(".ser"));
			ObjectOutputStream oout = new ObjectOutputStream(fout);
			oout.writeObject(map);
			oout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void genAndSaveMap(String name, int width, int height) {
		try {
			Map map = new Map(name, width, height);
			map.generate();
			
			saveMap(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

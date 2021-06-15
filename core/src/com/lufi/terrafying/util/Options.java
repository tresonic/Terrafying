package com.lufi.terrafying.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.badlogic.gdx.Input.Keys;
import com.lufi.terrafying.world.Map;

public class Options implements Serializable {

	float viewingrange;
	int keyJump, keyLeft, keyRight, keyUp, keyDown, keyInv;

	public Options() {
		viewingrange = 0.5f;
		keyJump = Keys.SPACE;
		keyLeft = Keys.A;
		keyRight = Keys.D;
		keyUp = Keys.SPACE;
		keyDown = Keys.S;
		keyInv = Keys.E;
	}

	public void setViewingrange(float f) {
		viewingrange = f;

	}

	public float getViewingrange() {
		return viewingrange;
	}

	public static Options loadOptions() {
		try {
			FileInputStream fin = new FileInputStream("settings.simsim");
			ObjectInputStream oin = new ObjectInputStream(fin);
			Options options = (Options) oin.readObject();
			oin.close();
			return options;
		} catch (Exception e) {
			e.printStackTrace();
			return new Options();
		}

	}

	public static void saveOptions(Options options) {
		try {
			FileOutputStream fout = new FileOutputStream("settings.simsim");
			ObjectOutputStream oout = new ObjectOutputStream(fout);
			oout.writeObject(options);
			oout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

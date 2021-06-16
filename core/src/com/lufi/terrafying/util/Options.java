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
		keyUp = Keys.W;
		keyDown = Keys.S;
		keyInv = Keys.E;
	}

	public void setViewingrange(float f) {
		viewingrange = f;

	}

	public float getViewingrange() {
		return viewingrange;
	}
	
	

	public int getKeyJump() {
		return keyJump;
	}

	public void setKeyJump(int nkeyJump) {
		keyJump = nkeyJump;
	}

	public int getKeyLeft() {
		return keyLeft;
	}

	public void setKeyLeft(int nkeyLeft) {
		keyLeft = nkeyLeft;
	}

	public int getKeyRight() {
		return keyRight;
	}

	public void setKeyRight(int nkeyRight) {
		keyRight = nkeyRight;
	}

	public int getKeyUp() {
		return keyUp;
	}

	public void setKeyUp(int nkeyUp) {
		keyUp = nkeyUp;
	}

	public int getKeyDown() {
		return keyDown;
	}

	public void setKeyDown(int nkeyDown) {
		keyDown = nkeyDown;
	}

	public int getKeyInv() {
		return keyInv;
	}

	public void setKeyInv(int nkeyInv) {
		keyInv = nkeyInv;
	}

	public static Options loadOptions() {
		try {
			FileInputStream fin = new FileInputStream("settings.simsim");
			ObjectInputStream oin = new ObjectInputStream(fin);
			Options options = (Options) oin.readObject();
			oin.close();
			return options;
		} catch (Exception e) {
			System.out.println("no settings file");
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

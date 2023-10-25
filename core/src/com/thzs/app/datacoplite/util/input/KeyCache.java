package com.thzs.app.datacoplite.util.input;

import java.util.ArrayList;
import java.util.List;

public class KeyCache {
	
	List<Integer> keydown = new ArrayList<>();
	
	boolean touch = false;

	public boolean keyDown(int keycode) {
		keydown.add(keycode);
		return true;
	}

	public boolean keyUp(int keycode) {
		keydown.remove((Object)keycode);
		return true;
	}
	
	public boolean isPress(Integer keycode) {
		return keydown.contains(keycode);
	}
	
	public boolean touchDown() {
		return touch = true;
	}

	public boolean touchUp() {
		return !(touch = false);
	}

	public boolean isTouch() {
		return touch;
	}
	
}

package com.thzs.app.datacoplite.util.input;

import com.badlogic.gdx.InputProcessor;

public class InputProcessorEx implements InputProcessor{
	
	public KeyCache keyCache = new KeyCache();
	
	boolean allowBubble = true;
	
	public void bubble(boolean flag) {
		allowBubble = flag;
	}

	public boolean keyDown(int keycode) {
		return keyCache.keyDown(keycode) && allowBubble;
	}

	public boolean keyUp(int keycode) {
		return keyCache.keyUp(keycode) && allowBubble;
	}

	public boolean keyTyped(char character) {
		return allowBubble;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return keyCache.touchDown() && allowBubble;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return keyCache.touchUp() && allowBubble;
	}

	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return allowBubble;
	}

	public boolean mouseMoved(int screenX, int screenY) {
		return allowBubble;
	}

	public boolean scrolled(float amountX,float amountY) {
		return allowBubble;
	}
	
	public boolean isPress(int keycode) {
		return keyCache.isPress(keycode);
	}
	
	public boolean isTouch() {
		return keyCache.isTouch();
	}

}

package com.thzs.app.datacoplite.ui.view;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.thzs.app.datacoplite.Views;
import com.thzs.app.datacoplite.core.Game;
import com.thzs.app.datacoplite.core.Query.$;
import com.thzs.app.datacoplite.core.Query.CustomCallback;

import java.util.ArrayList;
import java.util.List;

/**
 *	GDX-RPG 视窗
 *	
 *	<p>View为游戏中视窗，受{@link Views}调度，用来显示游戏的画面，每个View中拥有一个{@link View#stage stage}变量，{@link Views}每帧将对当前View进行 {@link #act()}、{@link #draw()} 操作用以画图</p>
 */
public abstract class View implements InputProcessor{
	public Stage stage;
	
	private List<InputProcessor> processors = new ArrayList<>(), removeList = new ArrayList<>();
	
	/**
	 * 每个View被创建后，第一次将调用create() 方法<br>
	 * 要注意的是，stage变量仍然是空的(null)，需要手动来实例化这个变量
	 * */
	public abstract void create();
	
	/**每帧被调用时执行*/
	public abstract void draw();
	
	/**每帧被调用时执行，他将在{@link #draw()}方法之前调用*/
	public abstract void act();
	
	
	
	private boolean removeable = false;
	/**是否允许{@link Views}移除自身，如果返回true则在下一帧前被删除*/
	public boolean removeable(){
		return removeable;
	}
	
	public void removeable(boolean flag){
		removeable = flag;
	}
	
	/**在下一帧后删除自身*/
	public void remove(){
		removeable = true;
		onRemove();
	}
	
	/**当被删除时的回调*/
	public void onRemove(){
		if(stage!=null){
			stage.dispose();
		}
		System.gc();
	}
	
	
	private boolean bubble = false;
	/**
	 * 是否允许当前View的输入继续向下冒泡
	 * <br>
	 * 它和onInput的区别是，接受到输入首先执行{@link #onInput()}，然后执行相应输入回调，最后才执行这个方法
	 * */
	public boolean buuleable(){
		return bubble;
	}
	
	public void buuleable(boolean flag){
		bubble = flag;
	}
	
	/**
	 * 当接收到输入（鼠标、键盘）时，被调用
	 * @return 是否允许输入
	 * */
	public boolean onInput(){
		return true && stage != null;
	}
	
	/** Called when a key was pressed
	 * 
	 * @param keycode one of the constants in {@link Input.Keys}
	 * @return whether the input was processed */
	public boolean keyDown (final int keycode){
		if($.allMatch(processors(), new CustomCallback<InputProcessor, Boolean>() {
			@Override
			public Boolean run(InputProcessor p) {
				return p.keyDown(keycode);
			}
		}) && onInput()){
			stage.keyDown(keycode);
			return bubble;
		}
		return false;
	};

	/** Called when a key was released
	 * 
	 * @param keycode one of the constants in {@link Input.Keys}
	 * @return whether the input was processed */
	public boolean keyUp (final int keycode){
		if($.allMatch(processors(), new CustomCallback<InputProcessor, Boolean>() {
			@Override
			public Boolean run(InputProcessor p) {
				return p.keyUp(keycode);
			}
		}) && onInput()){
			stage.keyUp(keycode);
			return bubble;
		}
		return false;
	};

	/** Called when a key was typed
	 * 
	 * @param character The character
	 * @return whether the input was processed */
	public boolean keyTyped (final char character){
		if($.allMatch(processors(), new CustomCallback<InputProcessor, Boolean>() {
			@Override
			public Boolean run(InputProcessor p) {
				return p.keyTyped(character);
			}
		}) && onInput()){
			stage.keyTyped(character);
			return bubble;
		}
		return false;
	};

	/** Called when the screen was touched or a mouse button was pressed. The button parameter will be {@link Buttons#LEFT} on iOS.
	 * @param screenX The x coordinate, origin is in the upper left corner
	 * @param screenY The y coordinate, origin is in the upper left corner
	 * @param pointer the pointer for the event.
	 * @param button the button
	 * @return whether the input was processed */
	public boolean touchDown (final int screenX, final int screenY, final int pointer, final int button){
		if($.allMatch(processors(), new CustomCallback<InputProcessor, Boolean>() {
			@Override
			public Boolean run(InputProcessor p) {
				return p.touchDown(screenX, screenY, pointer, button);
			}
		}) && onInput()){
			stage.touchDown(screenX, screenY, pointer, button);
			return bubble;
		}
		return false;
	};

	/** Called when a finger was lifted or a mouse button was released. The button parameter will be {@link Buttons#LEFT} on iOS.
	 * @param pointer the pointer for the event.
	 * @param button the button
	 * @return whether the input was processed */
	public boolean touchUp (final int screenX, final int screenY, final int pointer, final int button){
		if($.allMatch(processors(), new CustomCallback<InputProcessor, Boolean>() {
			@Override
			public Boolean run(InputProcessor p) {
				return p.touchUp(screenX, screenY, pointer, button);
			}
		}) && onInput()){
			stage.touchUp(screenX, screenY, pointer, button);
			return bubble;
		}
		return false;
	};

	/** Called when a finger or the mouse was dragged.
	 * @param pointer the pointer for the event.
	 * @return whether the input was processed */
	public boolean touchDragged (final int screenX, final int screenY, final int pointer){
		if($.allMatch(processors(), new CustomCallback<InputProcessor, Boolean>() {
			@Override
			public Boolean run(InputProcessor p) {
				return p.touchDragged(screenX, screenY, pointer);
			}
		}) && onInput()){
			stage.touchDragged(screenX, screenY, pointer);
			return bubble;
		}
		return false;
	};

	/** Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
	 * @return whether the input was processed */
	public boolean mouseMoved (final int screenX, final int screenY){
		if($.allMatch(processors(), new CustomCallback<InputProcessor, Boolean>() {
			@Override
			public Boolean run(InputProcessor p) {
				return p.mouseMoved(screenX, screenY);
			}
		}) && onInput()){
			stage.mouseMoved(screenX, screenY);
			return bubble;
		}
		return false;
	};

	/** Called when the mouse wheel was scrolled. Will not be called on iOS.
	 * @param amountX,amountY the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
	 * @return whether the input was processed. */

	public boolean scrolled (final float amountX, final float amountY){
		if($.allMatch(processors(), new CustomCallback<InputProcessor, Boolean>() {
			@Override
			public Boolean run(InputProcessor p) {
				return p.scrolled(amountX, amountY);
			}
		}) && onInput()){
			stage.scrolled(amountX,amountY);
			return bubble;
		}
		return false;
	};
	public boolean touchCancelled (final int screenX, final int screenY, final int pointer, final int button){
		if($.allMatch(processors(), new CustomCallback<InputProcessor, Boolean>() {
			@Override
			public Boolean run(InputProcessor p) {
				return p.touchCancelled(screenX,screenY,pointer,button);
			}
		}) && onInput()){
			stage.touchCancelled(screenX, screenY,pointer,button);
			return bubble;
		}
		return false;
	}
	public InputProcessor addProcessor(InputProcessor p) {
		processors.add(p);
		return p;
	}
	
	public boolean removeProcessor(InputProcessor p) {
		return removeList.add(p);
	}
	
	private List<InputProcessor> processors() {
		processors.removeAll(removeList);
		return processors;
	}

	public void resize() {
		if(stage != null)
			stage.getViewport().update(Game.width(), Game.height(), true);
	}
	
}

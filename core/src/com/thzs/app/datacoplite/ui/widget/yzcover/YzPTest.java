package com.thzs.app.datacoplite.ui.widget.yzcover;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzPWidget;
import com.thzs.app.datacoplite.util.position.PositionConvert;

/**
 * 2D物理测试
 * */
public class YzPTest extends YzPWidget {
    public Drawable background;
    public Texture texture;
    public YzPTest(){
        super();
        texture=new Texture(Gdx.files.internal("logo.png"));
        setTouchable(Touchable.enabled);
        this.setBackground(new TextureRegionDrawable(texture));
        setListener();
    }
    public YzPTest(int x, int y,int width,int height){
        super();
        texture=new Texture(Gdx.files.internal("logo.png"));
        setTouchable(Touchable.enabled);
        setBounds(x,y,width,height);
        this.setBackground(new TextureRegionDrawable(texture));
        setListener();
    }
    protected void setListener(){
        addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Off2DPhysics();
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                Vector2 o= PositionConvert.mouse2origin(new Vector2(Gdx.input.getX(),Gdx.input.getY()));
                setPosition(o.x-getWidth()/2,o.y-getHeight()/2);
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                On2DPhysics();
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }
    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        setOriginX(getWidth()/2);
        setOriginY(getHeight()/2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (getBackground() == null) return;
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        ((TextureRegionDrawable)getBackground()).draw(batch, getX(), getY(), getOriginX(), getOriginY(),getWidth(),getHeight(),1.0f,1.0f,getRotation());
    }

    public Drawable getBackground() {
        return background;
    }
    public void setBackground(Drawable background) {
        this.background = background;
    }

    @Override
    public void dispose() {
        super.dispose();
        texture.dispose();
    }
}

package com.thzs.app.datacoplite.ui.widget.yzcover.base;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.thzs.app.datacoplite.core.Game;

/**
 * 控件2D物理底层 Yue-Z 2D Physics Widget
 * */

public class YzPWidget extends YzWidget{
    public YzPWidget() {
    }

    protected boolean FG2DPhysics=false;
    protected Body body;
    protected Fixture fixture;
    public Body getBody(){
        return body;
    }
    public Fixture getFixture(){
        return fixture;
    }
    public void loadPhysics() {
        Off2DPhysics();
        body= Game.world.createBody(getBodyDef());
        fixture=body.createFixture(getFixtureDef());
        On2DPhysics();
    }
    protected BodyDef getBodyDef(){
        BodyDef bodyDef=new BodyDef();
        bodyDef.type= BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX()+getWidth()/2,getY()+getHeight()/2);
        return bodyDef;
    }
    protected PolygonShape getPolygonShape(){
        PolygonShape shape=new PolygonShape();
        shape.setAsBox(getWidth()/2f,getHeight()/2f);
        return shape;
    }
    protected FixtureDef getFixtureDef(){
        FixtureDef fixtureDef=new FixtureDef();
        fixtureDef.shape=getPolygonShape();
        fixtureDef.density=4.0f;
        fixtureDef.friction=0.15f;
        return fixtureDef;
    }
    /**
     * 更改配置前先调用Off2DPhysics()
     * <br/>
     * 完成后调用On2DPhysics()
     * */
    public void Off2DPhysics(){
        FG2DPhysics=false;
    }
    public void On2DPhysics(){
        UpdatePhysics();
        FG2DPhysics=true;
    }
    @Override
    public void act(float delta) {
        if(FG2DPhysics){
            Vector2 pos=body.getPosition();
            setPosition(pos.x-getWidth()/2,pos.y-getHeight()/2);
            setRotation((float) (body.getAngle()*(180/Math.PI)));
        }
        super.act(delta);
    }
    public void UpdatePhysics(){
        UpdatePhysicsPosition();
        UpdatePhysicsRotation();
    }
    public void UpdatePhysicsPosition(){
        body.setTransform(new Vector2(getX()+getWidth()/2f,getY()+getHeight()/2f),getRotation());
    }
    public void UpdatePhysicsRotation(){
        body.setTransform(new Vector2(getX()+getWidth()/2f,getY()+getHeight()/2f), (float) (getRotation()*(Math.PI/180.0)));
    }
    @Override
    protected void sizeChanged() {
        if (FG2DPhysics){
            body.destroyFixture(fixture);
            body.createFixture(getFixtureDef());
        }
        super.sizeChanged();
    }

    @Override
    public void dispose() {
        super.dispose();
        Game.world.destroyBody(body);
    }
}

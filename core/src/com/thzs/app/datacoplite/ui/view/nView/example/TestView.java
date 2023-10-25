package com.thzs.app.datacoplite.ui.view.nView.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.thzs.app.datacoplite.core.Game;
import com.thzs.app.datacoplite.ui.view.View;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzWindowTopBar;
import com.thzs.app.datacoplite.ui.widget.selectgroup.SelectGroup;
import com.thzs.app.datacoplite.ui.widget.selectgroup.SelectTextButton;
import com.thzs.app.datacoplite.ui.widget.selectgroup.SelectTextField;
import com.thzs.app.datacoplite.util.grapices.MyColorTools;
import com.thzs.app.datacoplite.util.position.PositionComplete;

public class TestView extends View {
    VisImage image;
    VisTextButton button1,button2;
    SelectTextField textInputActor;
    Pixmap   img;
    String def="The Path";
    YzWindowTopBar topBar;
    SelectGroup selectGroup;
    @Override
    public void create() {
        stage= Game.stage();

        topBar = new YzWindowTopBar(this);

        img= MyColorTools.getRandomColorPixmap();
        image=new VisImage(new Texture(img));
        image.setBounds(100,100,320,320);

        button1=new VisTextButton("Flesh");
        button1.setBounds(100,10,100,40);
        button1.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                img=MyColorTools.getRandomColorPixmap();
                image.setDrawable(new Texture(img));
                return true;
            }
        });

        button2=new VisTextButton("Start");
        button2.setSize(100,40);
        button2.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        textInputActor=new SelectTextField("你好hello");
        textInputActor.setSize(400,40);
        PositionComplete.UnitRightCenterDistance(button1,image,10);
        PositionComplete.UnitBottomCenterDistance(button2,button1,10*PositionComplete.Y_X);
        PositionComplete.UnitRightTopTRDistance(textInputActor,image,-textInputActor.getHeight(),10);

        stage.addActor(image);
        stage.addActor(button1);
        stage.addActor(button2);
        stage.addActor(textInputActor);

        selectGroup = new SelectGroup(stage);
        selectGroup.setBounds(0,0, Gdx.graphics.getWidth()*0.1f,Gdx.graphics.getHeight()-topBar.getHeight());
        selectGroup.addChild(new SelectTextButton("Hello"));
        selectGroup.addChild(new SelectTextButton("World"));
    }

    @Override
    public void draw() {
        stage.draw();
    }

    @Override
    public void act() {
        stage.act();
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public void onRemove() {
        super.onRemove();
        stage.dispose();
        img.dispose();
    }
}

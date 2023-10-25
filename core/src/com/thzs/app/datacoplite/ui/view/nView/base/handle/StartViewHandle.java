package com.thzs.app.datacoplite.ui.view.nView.base.handle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.thzs.app.datacoplite.Views;
import com.thzs.app.datacoplite.core.Game;
import com.thzs.app.datacoplite.core.Log;
import com.thzs.app.datacoplite.core.Query.$;
import com.thzs.app.datacoplite.ui.view.View;
import com.thzs.app.datacoplite.ui.view.eView.ConsoleView;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzButton;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzPTest;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzWindowTopBar;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzGdxQuery;
import top.thzs.bluetooth.BlueTools;
import top.thzs.bluetooth.BlueToothCoreLib;

import java.io.IOException;

public class StartViewHandle extends BaseViewHandle{
    //-------------------------------------------------------------------------
    //控件
    public YzWindowTopBar Actor_WindowBar;    //窗体标题
    public Table Table_Body,Table_Menu;
    public YzButton Actor_Button1,Actor_Button2,Actor_Button3,Actor_Button4,Actor_Button5;
    public YzGdxQuery ActorGroup;
    //-------------------------------------------------------------------------
    //区域
    public Rectangle Rect_Body;
    public Texture texture;

    public StartViewHandle(View view) {
        super(view);
    }

    public void ViewInit(){
        try {
            BlueToothCoreLib.initWindows(this.getClass());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ActorGroup=new YzGdxQuery();
        texture=new Texture(Gdx.files.internal("img/00036.png"));
        //加载舞台
        instance.stage=Game.stage();
        //标题栏
        ActorGroup.add(Actor_WindowBar=new YzWindowTopBar(instance,YzWindowTopBar.TYPE_HORIZONTAL));
        //设置Body区域
        Rect_Body=new Rectangle(0,0,Game.STAGE_WIDTH,Game.STAGE_HEIGHT-Actor_WindowBar.getHeight());
        Table_Body=new Table();
        Table_Body.right();
        Table_Body.setBackground(new TextureRegionDrawable(texture));
        Table_Body.setBounds(Rect_Body.x,Rect_Body.y,Rect_Body.width,Rect_Body.height);
        instance.stage.addActor(Table_Body);
        //设置Menu区域
        Table_Menu=new Table();
        Table_Menu.setTransform(true);
        Table_Body.add(Table_Menu).height(Game.STAGE_HEIGHT*0.7f).width(Game.STAGE_WIDTH*0.255f);
        //设置Body控件
        Actor_Button1=new YzButton(" 开始游戏 ");
        Actor_Button2=new YzButton(" 存档管理 ");
        Actor_Button3=new YzButton(" 日志管理 ");
        Actor_Button4=new YzButton(" 退出游戏 ");
        Actor_Button5=new YzButton(" 测试物理 ");
        ActorGroup.add($.add(Actor_Button1,Actor_Button2,Actor_Button3,Actor_Button4,Actor_Button5).to(Table_Menu).each(gdxQuery -> gdxQuery.cell().pad(5)));
        //2D物理测试
        ActorGroup.add(new YzGdxQuery($.add(new YzPTest(),new YzPTest()).height(200).width(200).y(Actor_WindowBar.getY()).first().x(Actor_WindowBar.getX()).next().x(Actor_WindowBar.getX()+200).father()).loadPhysics().to(instance.stage));
        //标题栏在最后添加
        instance.stage.addActor(Actor_WindowBar);
    }
    public void ViewListener(){
        super.ViewListener();
        Actor_Button1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int connectIndex=-1;
                        BlueTools.SearchBluetoothLE(2500);
                        for (int i = 0; i < BlueTools.GetBluetoothNumber(); i++) {
                            if (BlueTools.GetBluetoothIdentifier(i).startsWith("WM")){
                                BlueTools.ConnectBluetooth(i);
                                connectIndex=i;
                            }
                        }
                        if(connectIndex==-1){
                            return;
                        }
                        Log.i("Connect to "+BlueTools.GetBluetoothIdentifier(connectIndex));
                        if (connectIndex!=-1){
                            byte[] context={0,0,0,0,0,0,0,0};
                            BlueTools.BluetoothWrite(connectIndex,0,context,8);
                        }
                        BlueTools.CloseBluetooth(connectIndex);
                    }
                }).start();
                super.clicked(event, x, y);
            }
        });
        Actor_Button2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ActorGroup.find(YzButton.class).action(() -> Actions.moveBy(100, 0, 1f));
                super.clicked(event, x, y);
            }
        });
        Actor_Button3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Views.addView(ConsoleView.class);
                super.clicked(event, x, y);
            }
        });
        Actor_Button5.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                YzPTest p= (YzPTest) ActorGroup.find(YzPTest.class).first().get(0);

                p.addAction(new Action() {
                    float t=0;
                    @Override
                    public boolean act(float delta) {
                        float diffVel=100-p.getBody().getLinearVelocity().x;
                        Log.i("Vel:"+diffVel);
                        if(diffVel<=0){
                            return true;
                        }
                        float force = p.getBody().getMass() * diffVel/(1/60f); // step time
                        p.getBody().applyForce(new Vector2(force, 0), p.getBody().getWorldCenter(), true);
                        return false;
                    }
                });
            }
        });
    }
    public void dispose(){
        super.dispose();
        ActorGroup.dispose();
        texture.dispose();
    }
}

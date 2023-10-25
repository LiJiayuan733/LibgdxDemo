package com.thzs.app.datacoplite.ui.view.nView.base.handle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.thzs.app.datacoplite.core.Game;
import com.thzs.app.datacoplite.ui.view.View;
import com.thzs.app.datacoplite.ui.widget.Label;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzButton;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzCheckButton;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzEditArea2;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzWindowTopBar;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzCenterControl;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzGdxQuery;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzListener;
import com.thzs.app.datacoplite.util.API.OCR;
import com.thzs.app.datacoplite.util.API.Translate;
import com.thzs.app.datacoplite.util.API.Yolo;

import java.util.Properties;

public class ServerControlViewHandle extends BaseViewHandle{
    public YzGdxQuery ActorGroup;
    public YzWindowTopBar Actor_WindowBar;
    public YzCheckButton Actor_CheckButton1,Actor_CheckButton2,Actor_CheckButton3;
    public YzEditArea2 Actor_EditArea1,Actor_EditArea2,Actor_EditArea3;
    public YzButton Actor_Button1,Actor_Button2,Actor_Button3;
    public Label Actor_Label1,Actor_Label2,Actor_Label3;
    public Table Table_Body,Table_Left,Table_Left2,Table_Left3,Table_Left4;
    public Texture Background;
    public ServerControlViewHandle(View view) {
        super(view);
    }

    @Override
    public void ViewInit() {
        ActorGroup = new YzGdxQuery();
        instance.stage= Game.stage();
        ActorGroup.add(Actor_WindowBar=new YzWindowTopBar(instance));
        ActorGroup.add(Actor_Label1=new Label("启动OCR", YzCenterControl.DEFAULT_FONT_SIZE),Actor_Label2=new Label("启动TRANSLATE",YzCenterControl.DEFAULT_FONT_SIZE),Actor_Label3=new Label("启动Yolo",YzCenterControl.DEFAULT_FONT_SIZE));
        ActorGroup.add(Actor_CheckButton1=new YzCheckButton(Actor_Label1.getHeight()),Actor_CheckButton2=new YzCheckButton(Actor_Label1.getHeight()),Actor_CheckButton3=new YzCheckButton(Actor_Label1.getHeight()));
        ActorGroup.add(Actor_EditArea1=new YzEditArea2("OCR Python文件"),Actor_EditArea2=new YzEditArea2("Translate Python文件"),Actor_EditArea3=new YzEditArea2("Yolo Python文件"));
        ActorGroup.add(Actor_Button1=new YzButton("选择"),Actor_Button2=new YzButton("选择"),Actor_Button3=new YzButton("选择"));

        Pixmap pixmap=new Pixmap(Game.STAGE_WIDTH, (int) (Game.STAGE_HEIGHT-Actor_WindowBar.getHeight()), Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.08f,0.08f,0.08f,1f));
        pixmap.fill();
        Background=new Texture(pixmap);
        pixmap.dispose();

        Table_Body = new Table();Table_Left = new Table();Table_Left2 = new Table();Table_Left3=new Table();Table_Left4=new Table();
        Table_Body.setBackground(new TextureRegionDrawable(Background));
        Table_Left.setTransform(true);Table_Left2.setTransform(true);Table_Left3.setTransform(true);Table_Left4.setTransform(true);
        Table_Body.setBounds(0,0,Game.STAGE_WIDTH,Game.STAGE_HEIGHT-Actor_WindowBar.getHeight());
        Table_Body.pad(8,8,8,8);Table_Left.pad(5);Table_Left2.pad(5);
        Table_Body.top();

        ActorGroup.find(YzEditArea2.class).width(Game.STAGE_WIDTH/2f);
        ActorGroup.find(YzButton.class).height(Actor_EditArea1.getHeight());
        ActorGroup.find(Label.class).height(Actor_EditArea1.getHeight());
        ActorGroup.find(YzCheckButton.class).height(Actor_EditArea1.getHeight());

        ActorGroup.find(Label.class).to(Table_Left);
        ActorGroup.find(YzCheckButton.class).to(Table_Left2);
        ActorGroup.find(YzButton.class).to(Table_Left4);
        ActorGroup.find(YzEditArea2.class).to(Table_Left3);
        Table_Body.add(Table_Left3,Table_Left4,Table_Left,Table_Left2);
        instance.stage.addActor(Table_Body);
        instance.stage.addActor(Actor_WindowBar);
        ActorGroup.loadYzFocus();
    }

    @Override
    public void dispose() {
        super.dispose();
        ActorGroup.dispose();
        Background.dispose();
    }

    @Override
    public void loadData(Properties properties) {
        super.loadData(properties);
        Actor_EditArea1.setText(properties.getProperty("OCR Python文件"));
        Actor_EditArea2.setText(properties.getProperty("Translate Python文件"));
        Actor_EditArea3.setText(properties.getProperty("Yolo Python文件"));
    }

    @Override
    public void SaveData(Properties properties) {
        super.SaveData(properties);
        properties.put("OCR Python文件",Actor_EditArea1.getText());
        properties.put("Translate Python文件",Actor_EditArea2.getText());
        properties.put("Yolo Python文件",Actor_EditArea3.getText());
    }

    @Override
    public void ViewListener() {
        super.ViewListener();
        if (OCR.isServerRunning()){
            Actor_CheckButton1.setCheck(true);
        }
        Actor_CheckButton1.setYzCheckButtonListener(checked -> {
            if(checked){
                OCR.RunServer(Actor_EditArea1.getText());
            }else if(OCR.isServerRunning()){
                OCR.Destroy();
            }
        });
        if(Translate.isServerRunning()){
            Actor_CheckButton2.setCheck(true);
        }
        Actor_CheckButton2.setYzCheckButtonListener(checked -> {
            if(checked){
                Translate.RunServer(Actor_EditArea2.getText());
            }else if(Translate.isServerRunning()){
                Translate.Destroy();
            }
        });
        if (Yolo.isServerRunning()) {
            Actor_CheckButton3.setCheck(true);
        }
        Actor_CheckButton3.setYzCheckButtonListener(checked -> {
            if(checked){
                Yolo.RunServer(Actor_EditArea3.getText());
            }else if(Yolo.isServerRunning()) {
                Yolo.Destroy();
            }
        });
        Actor_Button1.addListener(YzListener.FileChoose(Actor_EditArea1));
        Actor_Button2.addListener(YzListener.FileChoose(Actor_EditArea2));
        Actor_Button3.addListener(YzListener.FileChoose(Actor_EditArea3));
    }
}

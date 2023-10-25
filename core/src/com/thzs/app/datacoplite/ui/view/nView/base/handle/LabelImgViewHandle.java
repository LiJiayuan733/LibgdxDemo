package com.thzs.app.datacoplite.ui.view.nView.base.handle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.thzs.app.datacoplite.core.Game;
import com.thzs.app.datacoplite.core.Path;
import com.thzs.app.datacoplite.core.Query.$;
import com.thzs.app.datacoplite.ui.view.View;
import com.thzs.app.datacoplite.ui.view.nView.base.LabelImgView;
import com.thzs.app.datacoplite.ui.widget.Image;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzButton;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzEditArea2;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzWindowTopBar;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzGdxQuery;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzListener;
import com.thzs.app.datacoplite.util.API.Auto;
import com.thzs.app.datacoplite.util.yolotran.YoloV5TranToolV2;

import java.util.Properties;

public class LabelImgViewHandle extends BaseViewHandle{
    public Table Table_Body,Table_Left,Table_Center,Table_Right1,Table_Right2,Table_Right3;
    public Table Table_T1_Left,Table_T1_Center;
    public YzWindowTopBar Actor_WindowBar;
    public YzEditArea2 Actor_EditArea1,Actor_EditArea2,Actor_EditArea3,Actor_EditArea4,Actor_EditArea5,Actor_EditArea6,Actor_EditArea7,Actor_EditArea8;
    public YzEditArea2 Actor_EditArea9,Actor_EditArea10;
    public YzButton Actor_Button1,Actor_Button2,Actor_Button3,Actor_Button4,Actor_Button5,Actor_Button6;
    public YzGdxQuery ActorGroup,ActorGroup2,ActorGroup3,ActorGroup4;
    public Image viewer;
    public LabelImgViewHandle(View view) {
        super(view);
        ActorGroup = new YzGdxQuery();
        ActorGroup2 = new YzGdxQuery();
        ActorGroup3 = new YzGdxQuery();
        ActorGroup4 = new YzGdxQuery();
    }

    @Override
    public void ViewInit() {
        instance.stage= Game.stage();

        //WindowBar
        ActorGroup.add(Actor_WindowBar=new YzWindowTopBar(instance));
        Actor_WindowBar.setTitle("YoloV5训练集测试");

        //Table Body
        Table_Body=new Table();Table_Left=new Table();Table_Center=new Table();Table_Right1=new Table();Table_Right2=new Table();Table_Right3=new Table();
        Table_Body.setBounds(0,0,Game.STAGE_WIDTH,Game.STAGE_HEIGHT-Actor_WindowBar.getHeight());
        Table_Body.pad(8,8,8,8);
        Table_Left.setTransform(true);Table_Center.setTransform(true);Table_Right1.setTransform(true);Table_Right2.setTransform(true);Table_Right3.setTransform(true);
        Table_Center.padLeft(8);Table_Right1.padLeft(8);Table_Right2.padLeft(8);Table_Right3.padLeft(8);
        Table_Body.top();Table_Body.add(Table_Left,Table_Center,Table_Right1,Table_Right2,Table_Right3);

        //Weight
        ActorGroup.add(Actor_EditArea1=new YzEditArea2("图片设置目录"),Actor_EditArea2=new YzEditArea2("标签输出目录"),Actor_EditArea9=new YzEditArea2("图标目录"),Actor_EditArea10=new YzEditArea2("背景目录"));
        ActorGroup.find(YzEditArea2.class).width(Game.STAGE_WIDTH/2f);

        //Button
        ActorGroup.add(Actor_Button1=new YzButton("选择"),Actor_Button2=new YzButton("选择"),Actor_Button5=new YzButton("选择"),Actor_Button6=new YzButton("选择"));
        ActorGroup.find(YzButton.class).height(Actor_EditArea1.getHeight());

        //Right1
        ActorGroup2.add(Actor_EditArea3=new YzEditArea2("数量"),Actor_EditArea4=new YzEditArea2("识别名字"));
        ActorGroup2.find(YzEditArea2.class).width(Game.STAGE_WIDTH/8f);

        //Right2
        ActorGroup3.add(Actor_EditArea5=new YzEditArea2("宽度"),Actor_EditArea6=new YzEditArea2("高度"));
        ActorGroup3.find(YzEditArea2.class).width(Game.STAGE_WIDTH/8f);

        //Right3
        ActorGroup4.add(Actor_EditArea7=new YzEditArea2("待定"),Actor_EditArea8=new YzEditArea2("待定"));
        ActorGroup4.find(YzEditArea2.class).width(Game.STAGE_WIDTH/8f);

        instance.stage.addActor(Table_Body);
        ActorGroup.find(YzWindowTopBar.class).to(instance.stage);
        ActorGroup.find(YzEditArea2.class).to(Table_Left);
        ActorGroup.find(YzButton.class).to(Table_Center);
        ActorGroup2.to(Table_Right1);
        ActorGroup3.to(Table_Right2);
        ActorGroup4.to(Table_Right3);
        ActorGroup.add(ActorGroup2,ActorGroup3,ActorGroup4);

        Table_Body.row();
        Table_T1_Left = new Table();Table_T1_Center = new Table();
        Table_T1_Left.setTransform(true);Table_T1_Center.setTransform(true);
        Table_T1_Left.padTop(8);
        Table_Body.add(Table_T1_Left,Table_T1_Center);
        ActorGroup.add($.add(viewer=new Image(new Texture("logo.png"))).to(Table_T1_Left));
        ActorGroup.add($.add(Actor_Button3=new YzButton("生成"),Actor_Button4=new YzButton("检测测试")).to(Table_T1_Center));

        ActorGroup.loadYzFocus();
        load();
    }

    @Override
    public void dispose() {
        super.dispose();
        ActorGroup.dispose();
    }

    @Override
    public void loadData(Properties properties) {
        super.loadData(properties);
        Actor_EditArea1.setText(properties.getProperty("图片输出目录"));
        Actor_EditArea2.setText(properties.getProperty("标签输出目录"));
        Actor_EditArea9.setText(properties.getProperty("图标目录"));
        Actor_EditArea10.setText(properties.getProperty("背景目录"));
        Actor_EditArea3.setText(properties.getProperty("数量"));
        Actor_EditArea5.setText(properties.getProperty("宽度"));
        Actor_EditArea6.setText(properties.getProperty("高度"));
    }

    @Override
    public void SaveData(Properties properties) {
        super.SaveData(properties);
        properties.put("图片输出目录",Actor_EditArea1.getText());
        properties.put("标签输出目录",Actor_EditArea2.getText());
        properties.put("图标目录",Actor_EditArea9.getText());
        properties.put("背景目录",Actor_EditArea10.getText());
        properties.put("数量",Actor_EditArea3.getText());
        properties.put("宽度",Actor_EditArea5.getText());
        properties.put("高度",Actor_EditArea6.getText());
    }

    @Override
    public String getName() {
        return "LabelImgView";
    }

    @Override
    public void ViewListener() {
        super.ViewListener();
        Actor_Button1.addListener(YzListener.FolderChoose(Actor_EditArea1));
        Actor_Button2.addListener(YzListener.FolderChoose(Actor_EditArea2));
        Actor_Button5.addListener(YzListener.FolderChoose(Actor_EditArea9));
        Actor_Button6.addListener(YzListener.FolderChoose(Actor_EditArea10));
        Actor_Button3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String outputImgPath=Actor_EditArea1.getText();
                String outputLabelPath=Actor_EditArea2.getText();
                int width = Integer.parseInt(Actor_EditArea5.getText()),height = Integer.parseInt(Actor_EditArea6.getText());
                int count = Integer.parseInt(Actor_EditArea3.getText());
                ((LabelImgView)instance).yoloV5TranTool=new YoloV5TranToolV2(width, height,outputImgPath,outputLabelPath, Actor_EditArea9.getText(),Actor_EditArea10.getText(),count,viewer);
                ((LabelImgView)instance).FGYoloV5=true;
                super.clicked(event, x, y);
            }
        });
        Actor_Button4.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                WinDef.HWND HWND_MINE= User32.INSTANCE.FindWindow(null, Game.APP_TITLE);
//                User32.INSTANCE.SendMessage(HWND_MINE, User32.WM_SYSCOMMAND, new WinDef.WPARAM(User32.SC_MINIMIZE),new WinDef.LPARAM(0));
//                Timer.add(Timer.TimeType.millisecond, 1, new Runnable() {
//                    @Override
//                    public void run() {
//                        MutApiSupport.SaveCaptureSAC(MutApiSupport.getWindowHandle());
//                        JsonFind jf= Yolo.SendRequestDefault("C:\\Users\\32827\\Desktop\\AppStart\\assets\\screen.png");
//                        List<Vector2> c=Yolo.getData(jf,1);
//                        if(c.size()==0) return;
//                        int X= (int) ((c.get(1).x+c.get(0).x)/2);
//                        int Y= (int) ((c.get(1).y+c.get(0).y)/2);
//                        Log.i("Mouse Click Double("+X+","+Y+")");
//                        MutApiSupport.Click(X,Y);
//                    }
//                });
                Auto a=new Auto();
                a.run(Gdx.files.internal(Path.SCRIPT+"mk.js"));
                super.clicked(event, x, y);
            }
        });
    }
}

package com.thzs.app.datacoplite.ui.view.nView.base.handle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.thzs.app.datacoplite.core.Game;
import com.thzs.app.datacoplite.ui.view.View;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzButton;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzEditArea2;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzImage;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzWindowTopBar;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzGdxQuery;
import com.thzs.app.datacoplite.util.Native.MutApiSupport;
import com.thzs.app.datacoplite.util.stm32utils.Led9341Picture;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

public class STM32DataCreateViewHandle extends BaseViewHandle{
    public YzGdxQuery ActorGroup;
    public YzWindowTopBar Actor_WindowBar;
    public float BodyWidth,BodyHeight;
    public Table Table_Center,Table_Left,Table_Right;

    public YzButton Actor_Button1,Actor_Button2,Actor_Button3,Actor_Button4,Actor_Button5;
    public YzEditArea2 Edit_PictureDir,Edit_PictureName;
    public YzImage image;
    public String imagePath=null;
    public STM32DataCreateViewHandle(View view) {
        super(view);
        ActorGroup=new YzGdxQuery();
        Debug= true;
    }
    public static byte[] toBytes(int number){
        byte[] bytes = new byte[4];
        bytes[0] = (byte)number;
        bytes[1] = (byte) (number >> 8);
        bytes[2] = (byte) (number >> 16);
        bytes[3] = (byte) (number >> 24);
        return bytes;
    }

    @Override
    public void ViewInit() {
        instance.stage= Game.stage();

        ActorGroup.add(Actor_WindowBar=new YzWindowTopBar(instance));
        Actor_WindowBar.setTitle("STM32 数据生成");

        //可用内容空间
        BodyWidth=Game.STAGE_WIDTH;
        BodyHeight=Game.STAGE_HEIGHT-Actor_WindowBar.getHeight();

        //Table设置
        Table_Center=new Table();Table_Left=new Table();Table_Right=new Table();
        Table_Center.setBounds(0,0,BodyWidth,BodyHeight);
        Table_Center.pad(8,8,8,8);
        Table_Left.setTransform(true);
        Table_Right.setTransform(true);
        Table_Center.top();

        ActorGroup.add(Actor_Button1=new YzButton("选择位置"),Actor_Button2=new YzButton("选择图片"),Actor_Button3=new YzButton("生成565"),Actor_Button4=new YzButton("生成字体"),Actor_Button5=new YzButton("1608选择图片"));
        ActorGroup.add(Edit_PictureDir=new YzEditArea2("图片保存位置"),Edit_PictureName=new YzEditArea2("图片名字"));
        ActorGroup.add(image=new YzImage(Led9341Picture.LED_WIDTH,Led9341Picture.LED_HEIGHT));
        ActorGroup.find(YzEditArea2.class).width(BodyWidth/2f);

        ActorGroup.find(YzButton.class).to(Table_Left).each(gdxQuery -> gdxQuery.cell().pad(5));
        ActorGroup.find(YzEditArea2.class).to(Table_Right).each(gdxQuery -> gdxQuery.cell().pad(5));
        ActorGroup.find(YzImage.class).width(Led9341Picture.LED_WIDTH).height(Led9341Picture.LED_HEIGHT).to(Table_Right);

        Table_Center.add(Table_Left);
        Table_Center.add(Table_Right);
        instance.stage.addActor(Table_Center);
        instance.stage.addActor(Actor_WindowBar);

        ActorGroup.loadYzFocus();
    }

    @Override
    public void dispose() {
        super.dispose();
        ActorGroup.dispose();
    }

    @Override
    public void ViewListener() {
        super.ViewListener();
        Actor_Button1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                try {
                    Edit_PictureDir.setText(MutApiSupport.SystemFolderChoose());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        Actor_Button2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                try {
                    imagePath=MutApiSupport.SystemFileChoose();
                    image.setImage(new Texture(Gdx.files.absolute(imagePath)));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        Actor_Button3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                try {
                    DataOutputStream output=new DataOutputStream(new FileOutputStream(new File(Edit_PictureDir.getText()+"/"+Edit_PictureName.getText()+".565")));
                    int index=0;
                    Led9341Picture led9341Picture = new Led9341Picture(Gdx.files.absolute(imagePath));
                    led9341Picture.PictureConvert();
                    for(;index<Led9341Picture.LED_HEIGHT*Led9341Picture.LED_WIDTH/256;index++){
                        led9341Picture.PictureToFile(output,index);
                    }
                    output.flush();
                    output.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        Actor_Button4.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            }
        });
    }

    @Override
    public void loadData(Properties properties) {
        super.loadData(properties);
    }

    @Override
    public void SaveData(Properties properties) {
        super.SaveData(properties);
    }

    @Override
    public String getName() {
        return instance.toString()+"Handle";
    }
}

package com.thzs.app.datacoplite.ui.view.nView.base.handle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.thzs.app.datacoplite.core.Game;
import com.thzs.app.datacoplite.core.Log;
import com.thzs.app.datacoplite.ui.view.View;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzButton;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzEditArea2;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzImage;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzWindowTopBar;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzGdxQuery;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzListener;
import com.thzs.app.datacoplite.util.API.OCR;
import com.thzs.app.datacoplite.util.API.OCRDataLine;
import com.thzs.app.datacoplite.util.JsonFind;

import java.util.List;
import java.util.Properties;

public class AIResultViewHandle extends BaseViewHandle{
    public YzGdxQuery ActorGroup;
    public YzWindowTopBar Actor_WindowBar;
    public YzImage Image_Result;
    public YzEditArea2 Edit_ImagePath;
    public YzButton  Button_Detect,Button_SelectDir;
    public Table Table_Center,Table_L1_Left,Table_L1_Right,Table_L2_Left,Table_L2_Right;
    public AIResultViewHandle(View view) {
        super(view);
        ActorGroup=new YzGdxQuery();
    }

    @Override
    public void ViewInit() {
        instance.stage= Game.stage();

        ActorGroup.add(Actor_WindowBar=new YzWindowTopBar(instance));
        Actor_WindowBar.setTitle("AI识别结果展示");

        Table_Center=new Table();Table_L1_Left=new Table();Table_L1_Right=new Table();Table_L2_Left=new Table();Table_L2_Right=new Table();
        Table_Center.setBounds(0,0,Game.STAGE_WIDTH,Game.STAGE_HEIGHT-Actor_WindowBar.getHeight());
        Table_Center.pad(8,8,8,8);
        Table_L1_Left.setTransform(true);
        Table_L1_Right.setTransform(true);
        Table_L2_Left.setTransform(true);
        Table_L2_Right.setTransform(true);
        Table_Center.top();

        ActorGroup.add(Edit_ImagePath=new YzEditArea2("图片路径"));
        ActorGroup.find(YzEditArea2.class).width(Game.STAGE_WIDTH/2f);

        ActorGroup.add(Button_SelectDir=new YzButton("选择文件"));

        ActorGroup.add(Image_Result=new YzImage());
        ActorGroup.find(YzImage.class).width(Game.STAGE_WIDTH/2f);
        ActorGroup.find(YzImage.class).height(Game.STAGE_HEIGHT/2f);

        instance.stage.addActor(Table_Center);
        ActorGroup.find(YzWindowTopBar.class).to(instance.stage);
        ActorGroup.find(YzEditArea2.class).to(Table_L1_Left);
        ActorGroup.find(YzButton.class).to(Table_L1_Right);
        ActorGroup.find(YzImage.class).to(Table_L2_Left);

        ActorGroup.add(Button_Detect=new YzButton("开始识别"));

        Table_L2_Right.add(Button_Detect);

        Table_Center.add(Table_L1_Left,Table_L1_Right);
        Table_Center.row();
        Table_Center.add(Table_L2_Left,Table_L2_Right);
        ActorGroup.loadYzFocus();
    }

    @Override
    public void ViewListener() {
        super.ViewListener();
        Button_SelectDir.addListener(YzListener.FileChoose(Edit_ImagePath));
        Button_Detect.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(Edit_ImagePath.getText().length()>0){
                    FileHandle fileHandle=Gdx.files.absolute(Edit_ImagePath.getText());
                    Pixmap pixmap=new Pixmap(fileHandle);
                    if(fileHandle.exists()) {
                        JsonFind OcrResult;
                        if (OCR.isServerRunning()) {
                            OcrResult = OCR.SendRequestDefault(fileHandle.file().getAbsolutePath(), "ZH");
                            List<OCRDataLine> FResult=OCR.FormatResult(OcrResult);
                            pixmap.setColor(Color.RED);
                            for (OCRDataLine line:FResult) {
                                Rectangle rectangle=line.getRectangle();
                                pixmap.drawRectangle((int) rectangle.x, (int) rectangle.y, (int) rectangle.width, (int) rectangle.height);
                            }
                        }else {
                            Log.e("请先开启OCR本地服务");
                        }
                        Image_Result.setImage(pixmap);
                    }
                    pixmap.dispose();
                }
                super.clicked(event, x, y);
            }
        });
    }

    @Override
    public void loadData(Properties properties) {
        super.loadData(properties);
        Edit_ImagePath.setText(properties.getProperty("ImagePath"));
    }

    @Override
    public void SaveData(Properties properties) {
        super.SaveData(properties);
        properties.put("ImagePath", Edit_ImagePath.getText());
    }

    @Override
    public String getName() {
        return "AIResultViewHandle";
    }

    @Override
    public void dispose() {
        super.dispose();
        ActorGroup.dispose();
    }
}

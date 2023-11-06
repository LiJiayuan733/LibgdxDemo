package com.thzs.app.datacoplite.ui.view.nView.base.handle;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.thzs.app.datacoplite.Views;
import com.thzs.app.datacoplite.core.Game;
import com.thzs.app.datacoplite.ui.view.View;
import com.thzs.app.datacoplite.ui.view.nView.base.*;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzButton;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzCheckButton;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzLabel;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzWindowTopBar;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzGdxQuery;

public class WhiteViewHandle extends BaseViewHandle {
    public YzWindowTopBar Actor_WindowBar;
    public YzButton Actor_Button1,Actor_Button2,Actor_Button3,Actor_Button4,Actor_Button5;
    public YzLabel label;
    public YzCheckButton Actor_CheckButton1,Actor_CheckButton2,Actor_CheckButton3;
    public Table Actor_Body,Actor_Menu;
    public YzGdxQuery ActorGroup;
    public WhiteViewHandle(View view) {
        super(view);
    }

    @Override
    public void ViewInit() {
        ActorGroup=new YzGdxQuery();
        instance.stage= Game.stage();

        ActorGroup.add(Actor_WindowBar=new YzWindowTopBar(instance));
        Actor_WindowBar.setTitle("Leader View");
        Actor_Body=new Table();
        Actor_Menu=new Table();
        Actor_Menu.setTransform(true);
        Actor_Menu.setFillParent(false);
        Actor_Menu.pad(5);
        Actor_Body.setBounds(0,0,Game.STAGE_WIDTH,Game.STAGE_HEIGHT-Actor_WindowBar.getHeight());
        ActorGroup.add(Actor_Button1=new YzButton("界面测试一"),Actor_Button2=new YzButton("yoloV5训练集生成"),Actor_Button3=new YzButton("后台服务开启"),Actor_Button4=new YzButton("界面测试二"),Actor_Button5=new YzButton("STM32测试")).find(YzButton.class).to(Actor_Menu).each(gdxQuery -> gdxQuery.cell().pad(5).right());
        Actor_Body.add(Actor_Menu).width(Game.STAGE_WIDTH*0.3f).height(Actor_Body.getHeight()*0.7f);
        Actor_Body.left();
        //ActorGroup.add(label=new YzLabel()).find(YzLabel.class).width(Actor_Menu.getWidth()).color(Color.valueOf("#66CCFFFF")).to(Actor_Menu).fillParent();
        instance.stage.addActor(Actor_Body);
        instance.stage.addActor(Actor_WindowBar);
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
                Views.addView(StartView.class);
                super.clicked(event, x, y);
            }
        });
        Actor_Button2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                JsonFind json=Translate.SendRequestDefault("2003年のリリース以来進化を続けるVOCALOID。\n" +
//                        "VOCALOID6ではAI技術を用いたVOCALOID:AIを搭載し、よりナチュラルで表現力豊かな歌声合成を実現しました。\n" +
//                        "更に便利になった編集ツールと新機能が楽曲制作の自由度を高め、あなたのクリエイティビティを解放します。");
//                if(json==null){
//                    return;
//                }
//                Log.i("Translator Test:"+Translate.getData(json).get(0));
//                super.clicked(event, x, y);
                instance.remove();
                Views.addView(LabelImgView.class);
            }
        });
        Actor_Button3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Views.addView(ServerControlView.class);
                super.clicked(event, x, y);
            }
        });
        Actor_Button4.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                instance.remove();
                Views.addView(AIResultView.class);
                super.clicked(event, x, y);
            }
        });
        Actor_Button5.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                instance.remove();
                Views.addView(STM32View.class);
                super.clicked(event, x, y);
            }
        });
    }
}

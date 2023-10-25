package com.thzs.app.datacoplite.ui.view.nView.example;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.thzs.app.datacoplite.Views;
import com.thzs.app.datacoplite.core.Game;
import com.thzs.app.datacoplite.core.Log;
import com.thzs.app.datacoplite.ui.view.View;
import com.thzs.app.datacoplite.ui.view.eView.LoadView;
import com.thzs.app.datacoplite.ui.view.nView.base.StartView;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzWindowTopBar;
import com.thzs.app.datacoplite.ui.widget.selectgroup.*;
import com.thzs.app.datacoplite.ui.widget.selectgroup.bili.BiliCommentListAdapted;
import com.thzs.app.datacoplite.util.API.OCR;
import com.thzs.app.datacoplite.util.JsonFind;
import com.thzs.app.datacoplite.util.Native.MutApiSupport;
import com.thzs.app.datacoplite.util.Timer;
import com.thzs.app.datacoplite.util.crawler.*;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;

import java.util.HashMap;

public class TestView2 extends View {
    public YzWindowTopBar topBar;
    public SelectList<BiliCommentListAdapted> selectGroup;
    public SelectGroup selectGroup2;
    public SelectTab tab=new SelectTab("你好"),tab3=new SelectTab("截图"),tab4=new SelectTab("置顶"),tab5=new SelectTab("切换");
    public SelectTab tab2=new SelectTab("评论");
    public SelectLabel label;
    public SelectImage image;
    @Override
    public void create(){
        stage= Game.stage();
        topBar=new YzWindowTopBar(this);
        selectGroup=new SelectList<>(this);
        selectGroup.setBounds(0,0, stage.getWidth()*0.1f,stage.getHeight()-topBar.getHeight());

        selectGroup2=new SelectGroup(stage);
        selectGroup2.setBounds(selectGroup.getX()+selectGroup.getWidth(),0,stage.getWidth()*0.06f,stage.getHeight()-topBar.getHeight());

        label=new SelectLabel("Select Group", Color.BLACK);
        selectGroup2.addChild(label);
        label.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //MutApiSupport.SaveCaptureSAC(((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("OCR Request->");
                        JsonFind jsonFind=OCR.SendRequestDefault("C:\\Users\\32827\\Desktop\\AppStart\\assets\\screen.png","ZH");
                        assert jsonFind != null;
                        Log.i(jsonFind.getObj("Message").toString());
                    }
                }).start();
            }
        });

        selectGroup2.addChild(tab);
        tab.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //NativeWindowsSupportUtils.LongTest(((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle());
                String path=MutApiSupport.SystemFileChoose();
                if(!path.equals("failure")){
                    image.setDrawable(new Texture(Gdx.files.absolute(path)));
                }
                super.clicked(event, x, y);
            }
        });
        tab2.setName("Message");
        tab2.setBounds(selectGroup2.getX()+selectGroup2.getWidth(),0, stage.getWidth()-selectGroup.getWidth()-selectGroup2.getWidth(),topBar.getHeight());
        stage.addActor(tab2);
        Timer.add(Timer.TimeType.millisecond, 10000, new Runnable() {
            @Override
            public void run() {
                selectGroup.remove(1);
            }
        });
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                BiliCommentsRequests biliReq = new BiliCommentsRequests("231528522", "0");
                BiliSpriderConfig.AddCookies(biliReq);
                Spider.create(new BiliProcessor()).addRequest(biliReq.setFunction(new BiliRequests.Function() {
                    @Override
                    public void call(BiliProcessor process, Page page) {
                        try {
                            JsonFind jf=new JsonFind(page.getRawText());
                            JSONObject data = (JSONObject) jf.get("data");
                            JSONArray rplist = data.getJSONArray("replies");
                            for (int i = 0; i < rplist.size(); i++) {
                                BiliCommentData bcd=new BiliCommentData(rplist.getJSONObject(i));
//                                BiliCommentShowRange image=new BiliCommentShowRange();

                                final BiliCommentListAdapted img=new BiliCommentListAdapted();
                                final HashMap<String, Object> dat=new HashMap<String, Object>();
                                dat.put("Comment",bcd);
                                Gdx.app.postRunnable(new Runnable() {
                                    @Override
                                    public void run() {
                                        selectGroup.addChild(img,dat);
                                    }
                                });

//                                image.setData(bcd);
//                                selectGroup2.addChildByAsync(image);
//                                image.loadNetImage(bcd.member.avatar, new SelectImage.NetImageListener() {
//                                    @Override
//                                    public void onImageCreateFinish(Texture texture, SelectImage image) {
//                                        float unit= ((float) texture.getHeight()) / ((float)texture.getWidth());
//                                        image.setHeight(image.getWidth()*unit);
//                                        image.setDrawable(texture);
//                                        selectGroup2.complete();
//                                    }
//                                });
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }

                    }
                })).thread(2).start();
            }
        });
        t.setDaemon(true);
        t.start();
        image=new SelectImage(new Texture(Gdx.files.internal("screen.png")));
        image.setBounds(selectGroup.getWidth()+selectGroup2.getWidth(),tab2.getHeight(),stage.getWidth()-selectGroup2.getWidth()-selectGroup.getWidth(),stage.getHeight()-topBar.getHeight()-tab2.getHeight());
        stage.addActor(image);
        selectGroup2.addChild(tab3);
        tab3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MutApiSupport.SaveCapture(MutApiSupport.getWindowHandle(),"C:\\Users\\32827\\Desktop\\AppStart\\assets\\screen.png");
                super.clicked(event, x, y);
            }
        });
        selectGroup2.addChild(tab4);
        tab4.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                WinDef.HWND HWND_MINE=User32.INSTANCE.FindWindow(null,Game.APP_TITLE);
                // Make sure ArkPets has been set as foreground window once
                for (int i = 0; ; i++) {
                    if (HWND_MINE.equals(User32.INSTANCE.GetForegroundWindow())) {
                        Log.i("Window "+"SetForegroundWindow succeeded" + (i > 0 ? " (retry #" + i + ")" : ""));
                        break;
                    } else if (i > 1000) {
                        Log.e("Window"+ "SetForegroundWindow failed because max retries exceeded (retry #" + (i - 1) + ")");
                        break;
                    }
                    User32.INSTANCE.SetForegroundWindow(HWND_MINE);
                }
                WinDef.RECT position=new WinDef.RECT();
                User32.INSTANCE.GetWindowRect(HWND_MINE,position);
                User32.INSTANCE.SetWindowPos(HWND_MINE, new WinDef.HWND(Pointer.createConstant(-1)),
                        position.left,position.top,
                        Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), WinUser.SWP_NOACTIVATE
                );
                super.clicked(event, x, y);
            }
        });
        selectGroup2.addChild(tab5);
        tab5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
                HashMap<String ,Object> param=new HashMap<String ,Object>();
                param.put(LoadView.KEY_NEXT, StartView.class);
                param.put(LoadView.KEY_BG_IMG,"img/bg.jpg");
                Views.addView(LoadView.class,param);
                Game.timer.scheduleTask(new com.badlogic.gdx.utils.Timer.Task() {
                    @Override
                    public void run() {
                        LoadView.st= LoadView.statue.finished;
                    }
                },3);
            }
        });
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
    public void onRemove() {
        for (Actor actor : stage.getActors()){
            if (actor instanceof Disposable)
                ((Disposable) actor).dispose();
        }
        stage.dispose();
        super.onRemove();
    }

    @Override
    public String toString() {
        return "测试窗口 2 ";
    }
}

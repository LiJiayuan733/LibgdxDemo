package com.thzs.app.datacoplite.ui.widget.selectgroup.bili;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.Disposable;
import com.thzs.app.datacoplite.core.Log;
import com.thzs.app.datacoplite.core.Query.$;
import com.thzs.app.datacoplite.core.Query.RemoveTest;
import com.thzs.app.datacoplite.ui.widget.selectgroup.SelectGroupSettingSupport;
import com.thzs.app.datacoplite.ui.widget.selectgroup.SelectList;
import com.thzs.app.datacoplite.ui.widget.selectgroup.SelectListSettingSupport;
import com.thzs.app.datacoplite.ui.widget.selectgroup.SelectTab;
import com.thzs.app.datacoplite.util.crawler.BiliCommentData;

import java.util.HashMap;
import java.util.Map;

public class BiliCommentListAdapted extends Widget implements SelectGroupSettingSupport, SelectListSettingSupport, Disposable {
    public Map<String ,Object> data;
    public SelectList selectList;
    public boolean flagFocus=false;

    public BiliCommentListAdapted() {

    }

    @Override
    public boolean isFlagFocus() {
        return flagFocus;
    }

    @Override
    public void OnSetFocus(boolean flag) {
        this.flagFocus=flag;
        if(flag){
            for (Actor actor:selectList.getStage().getActors()){
                if(actor.getName()!=null&&actor.getName().equals("Message")){
                    SelectTab tab = (SelectTab) actor;
                    tab.setText(((BiliCommentData)data.get("Comment")).message);
                }
            }
        }
    }

    @Override
    public void SelectInit() {
        data=new HashMap<String,Object>();
    }
    public Pixmap head=null;

    @Override
    public Pixmap refresh(SelectList selectGroupSettingSupport, Pixmap pixmap) {
        if(head==null){
            return pixmap;
        }else {
            pixmap.drawPixmap(head,0,0,head.getWidth(),head.getHeight(),0,0, (head.getWidth()/head.getHeight())*pixmap.getHeight(),pixmap.getHeight());
            return pixmap;
        }
    }

    @Override
    public void setData(SelectList selectListSettingSupport, Map<String, Object> data) {
        final Object o=this;
        this.selectList=selectListSettingSupport;
        this.data.putAll(data);
        loadNetImage(((BiliCommentData) data.get("Comment")).member.avatar, new NetImageListener() {
            @Override
            public void onImageCreateFinish(Pixmap texture, BiliCommentListAdapted image) {
                head=texture;
                image.setHeight(image.getWidth()/2);
                selectList.refresh($.getIndex(selectList.children, new RemoveTest<Actor>() {
                    @Override
                    public boolean test(Actor object) {
                        return object.equals(o);
                    }
                }));
                selectList.complete();
                selectList.synthesized();
            }
        });
    }

    @Override
    public void setData(SelectList selectListSettingSupport, String key, Object value) {
        this.data.put(key, value);
    }

    @Override
    public Rectangle getPreSize(SelectList selectListSettingSupport) {
        Rectangle rectangle=new Rectangle(getX(),getY(),getWidth(),getHeight());
        switch (selectListSettingSupport.type){
            case HOR:
                rectangle.setHeight(selectListSettingSupport.getHeightForChild());
                rectangle.setWidth(rectangle.getHeight()*2);
                break;
            case VER:
                rectangle.setWidth(selectListSettingSupport.getWidthForChild());
                rectangle.setHeight(rectangle.getWidth()/2);
                break;
        }

        return rectangle;
    }

    @Override
    public int getIndex(SelectList selectListSettingSupport) {
        return (int) data.get("INDEX");
    }

    @Override
    public Object getData(SelectList selectListSettingSupport, String key) {
        return data.get(key);
    }

    HttpRequestBuilder requestBuilder;

    @Override
    public void dispose() {
        if(head!=null)
            head.dispose();
    }

    public interface NetImageListener{
        public abstract void onImageCreateFinish(Pixmap img, BiliCommentListAdapted widget);
    }
    public void loadNetImage(final String URL, final NetImageListener listener){
        final BiliCommentListAdapted instance=this;
        if(requestBuilder==null){
            requestBuilder= new HttpRequestBuilder();
        }
        Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url(URL).build();
        Gdx.net.sendHttpRequest(httpRequest,new Net.HttpResponseListener(){

            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus httpStatus = httpResponse.getStatus();
                if(httpStatus.getStatusCode()==200){
                    final byte[] result = httpResponse.getResult();
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            // 把字节数组加载为 Pixmap
                            Pixmap pixmap;
                            try {
                                pixmap= new Pixmap(result, 0, result.length);
                            }catch (Exception e){
                                Log.e("Loading Pixmap failed.");
                                pixmap=new Pixmap(96,96, Pixmap.Format.RGBA8888);
                                pixmap.setColor(Color.RED);
                                pixmap.fill();
                            }
                            listener.onImageCreateFinish(pixmap,instance);
                        }
                    });
                }else {
                    Log.i("Image Load Failed Code:"+httpStatus.getStatusCode()+":"+URL);
                }
            }

            @Override
            public void failed(Throwable t) {
                Log.i("Image Load Failed:"+URL);
            }

            @Override
            public void cancelled() {
                Log.i("Image Load Cancelled:"+URL);
            }
        });
    }
}

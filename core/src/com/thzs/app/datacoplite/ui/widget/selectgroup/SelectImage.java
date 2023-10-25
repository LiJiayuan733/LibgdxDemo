package com.thzs.app.datacoplite.ui.widget.selectgroup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.widget.VisImage;
import com.thzs.app.datacoplite.core.Log;

public class SelectImage extends VisImage implements SelectGroupSettingSupport {
    HttpRequestBuilder requestBuilder;
    public SelectImage() {
        requestBuilder = new HttpRequestBuilder();
    }

    public SelectImage(NinePatch patch) {
        super(patch);
    }

    public SelectImage(TextureRegion region) {
        super(region);
    }

    public SelectImage(Texture texture) {
        super(texture);
    }

    public SelectImage(String drawableName) {
        super(drawableName);
    }

    public SelectImage(Skin skin, String drawableName) {
        super(skin, drawableName);
    }

    public SelectImage(Drawable drawable) {
        super(drawable);
    }

    public SelectImage(Drawable drawable, Scaling scaling) {
        super(drawable, scaling);
    }

    public SelectImage(Drawable drawable, Scaling scaling, int align) {
        super(drawable, scaling, align);
    }
    public interface NetImageListener{
        public abstract void onImageCreateFinish(Texture texture, SelectImage image);
    }
    public void loadNetImage(final String URL, final NetImageListener listener){
        final SelectImage instance=this;
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
                            Pixmap pixmap = new Pixmap(result, 0, result.length);

                            // 把 pixmap 加载为纹理
                            Texture texture = new Texture(pixmap);

                            // pixmap 不再需要使用到, 释放内存占用
                            pixmap.dispose();
                            listener.onImageCreateFinish(texture,instance);
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
    public Texture focusTexture;
    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        if (getWidth()<=0||getHeight()<=0){
            return;
        }
        Pixmap pixmap=new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(0,0,0,0);
        pixmap.fill();
        pixmap.setColor(Color.RED);
        pixmap.drawRectangle(1,1, (int) getWidth()-2, (int) getHeight()-2);
        pixmap.drawRectangle(0,0,(int) getWidth(), (int) getHeight());
        focusTexture=new Texture(pixmap);
        pixmap.dispose();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(focusFlag){
            batch.draw(focusTexture,getX(),getY());
        }
    }

    public boolean focusFlag=false;
    @Override
    public boolean isFlagFocus() {
        return focusFlag;
    }

    @Override
    public void OnSetFocus(boolean flag) {
        focusFlag=flag;
    }

    @Override
    public void SelectInit() {

    }
}

package com.thzs.app.datacoplite.ui.widget.yzcover;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzWidget;

public class YzImage extends YzWidget {
    public Texture img=null;
    public YzImage() {
        setSize(20,20);
    }
    public void setImage(Texture image){
        this.img=image;
    }
    public void setImage(Pixmap pixmap){
        this.img=new Texture(pixmap);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(img!=null) {
            int width=img.getWidth(),height=img.getHeight();
            boolean flag= ((float) width) /((float) height)>=getWidth()/getHeight();
            if (flag){
                float img_height=(((float) height)/(float) width)*getWidth();
                float x=getX(),y=getY()+(getHeight()-img_height)/2;
                batch.draw(img,x,y,getWidth(),img_height);
            }else {
                float img_width=(((float) width)/(float) height)*getHeight();
                float x=getX()+(getWidth()-img_width)/2,y=getY();
                batch.draw(img,x,y,img_width,getHeight());
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (img!=null)
            img.dispose();
    }
}

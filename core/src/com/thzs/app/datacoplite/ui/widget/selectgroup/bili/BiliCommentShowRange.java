package com.thzs.app.datacoplite.ui.widget.selectgroup.bili;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import com.thzs.app.datacoplite.ui.widget.selectgroup.SelectImage;
import com.thzs.app.datacoplite.ui.widget.selectgroup.SelectTab;
import com.thzs.app.datacoplite.util.crawler.BiliCommentData;

public class BiliCommentShowRange extends SelectImage {
    public BiliCommentData data;
    public BiliCommentShowRange() {
    }

    public BiliCommentShowRange(NinePatch patch) {
        super(patch);
    }

    public BiliCommentShowRange(TextureRegion region) {
        super(region);
    }

    public BiliCommentShowRange(Texture texture) {
        super(texture);
    }

    public BiliCommentShowRange(String drawableName) {
        super(drawableName);
    }

    public BiliCommentShowRange(Skin skin, String drawableName) {
        super(skin, drawableName);
    }

    public BiliCommentShowRange(Drawable drawable) {
        super(drawable);
    }

    public BiliCommentShowRange(Drawable drawable, Scaling scaling) {
        super(drawable, scaling);
    }

    public BiliCommentShowRange(Drawable drawable, Scaling scaling, int align) {
        super(drawable, scaling, align);
    }
    public void setData(BiliCommentData data){
        this.data = data;
    }

    @Override
    public void OnSetFocus(boolean flag) {
        super.OnSetFocus(flag);
        if(flag){
            for (Actor actor:getStage().getActors()){
                if(actor.getName()!=null&&actor.getName().equals("Message")){
                    SelectTab tab = (SelectTab) actor;
                    tab.setText(data.message);
                }
            }
        }
    }
}

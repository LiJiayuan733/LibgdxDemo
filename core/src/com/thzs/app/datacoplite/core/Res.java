package com.thzs.app.datacoplite.core;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Res {
    /**资源管理线程*/
    public static AssetManager assetManager;

    /**字体管理*/
    public static Text text;

    /**初始化*/
    public static void init(){
        assetManager = new AssetManager();
        /**添加字体管理器*/
        text = new Text();
        Log.i("Resource[created]");
    }

    /**
     * 立即获取一张图片
     */
    public static Image sync(String path){
        return new Image(getTexture(path));
    }

    /**
     * 立即获取一张drawable
     */
    public static Drawable getDrawable(String path) {
        return new TextureRegionDrawable(new TextureRegion(getTexture(path)));
    }

    /**
     * 更新资源管理器
     */
    public static boolean act(){
        return assetManager.update();
    }

    /**
     * 立即获取一张纹理
     */
    public static Texture getTexture(String path){
        assetManager.load(path, Texture.class);
        while(!assetManager.update());
        return assetManager.get(path, Texture.class);
    }

    /**
     * 预加载纹理
     */
    public static void preInit(String[] paths){
        for(String path : paths)
            assetManager.load(path, Texture.class);
    }

    /**卸载全部纹理*/
    public static void dispose(){
        assetManager.dispose();
    }
}

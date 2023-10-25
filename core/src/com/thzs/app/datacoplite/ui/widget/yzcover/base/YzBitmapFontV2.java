package com.thzs.app.datacoplite.ui.widget.yzcover.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

//TODO: to create an new BitmapFont Manager
@Deprecated
public class YzBitmapFontV2 implements Disposable {
    public static FreeTypeFontGenerator Generator;
    public String name;
    public static Map<String, BitmapFont> FontsCache = new HashMap<String, BitmapFont>();
    public BitmapFont font;
    public float x=0,y=0,targetWidth=-1,hAlign= Align.left;
    public boolean warp=false;
    public boolean reloadType=true;
    public boolean singleLine=true;
    public String text="";
    public GlyphLayout layout;
    public float FontHeight=0;
    public FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    public YzBitmapFontV2(String name,int size){
        this.name = name;
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color= Color.WHITE;
        parameter.size= size;
        BitmapFont font;
        if(!FontsCache.containsKey(name)){
            YzBitmapFontData data=new YzBitmapFontData(Generator,parameter.color, parameter.size, this);
            font = Generator.generateFont(parameter,data);
            try {
                Field f = font.getClass().getSuperclass().getDeclaredField("data");
                f.setAccessible(true);
                f.set(font, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            genrateData();
            FontsCache.put(name, font);
        }else{
            font = FontsCache.get(name);
        }
        this.font=font;
        FontHeight=font.getAscent()+font.getCapHeight();
        reLoad(true);
    }
    private void genrateData() {
        FreeType.Face face = null;
        try {
            Field field = Generator.getClass().getDeclaredField("face");
            field.setAccessible(true);
            face = (FreeType.Face) field.get(Generator);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // set general font data
        FreeType.SizeMetrics fontMetrics = face.getSize().getMetrics();

        // Set space glyph.
        BitmapFont.Glyph spaceGlyph = font.getData().getGlyph(' ');
        if (spaceGlyph == null) {
            spaceGlyph = new BitmapFont.Glyph();
            spaceGlyph.xadvance = (int) font.getData().spaceXadvance;
            spaceGlyph.id = (int) ' ';
            font.getData().setGlyph(' ', spaceGlyph);
        }
        if (spaceGlyph.width == 0)
            spaceGlyph.width = (int) (spaceGlyph.xadvance + font.getData().padRight);

        // set general font data
        font.getData().flipped = parameter.flip;
        font.getData().ascent = FreeType.toInt(fontMetrics.getAscender());
        font.getData().descent = FreeType.toInt(fontMetrics.getDescender());
        font.getData().lineHeight = FreeType.toInt(fontMetrics.getHeight());

        // determine x-height
        for (char xChar : font.getData().xChars) {
            if (!face.loadChar(xChar, FreeType.FT_LOAD_DEFAULT))
                continue;
            font.getData().xHeight = FreeType.toInt(face.getGlyph().getMetrics().getHeight());
            break;
        }
        if (font.getData().xHeight == 0)
            throw new GdxRuntimeException("No x-height character found in font");
        for (char capChar : font.getData().capChars) {
            if (!face.loadChar(capChar, FreeType.FT_LOAD_DEFAULT))
                continue;
            font.getData().capHeight = FreeType.toInt(face.getGlyph().getMetrics().getHeight());
            break;
        }

        // determine cap height
        if (font.getData().capHeight == 1)
            throw new GdxRuntimeException("No cap character found in font");
        font.getData().ascent = font.getData().ascent - font.getData().capHeight;
        font.getData().down = -font.getData().lineHeight;
        if (parameter.flip) {
            font.getData().ascent = -font.getData().ascent;
            font.getData().down = -font.getData().down;
        }

    }
    public void setPosition(float x,float y){
        this.x=x;
        this.y=y;
        reLoad(reloadType);
    }
    public void setText(String text){
        this.text=text;
        reLoad(reloadType);
    }
    public void setColor(Color color){
        font.setColor(color);
    }
    public void reLoad(boolean Default){
        float X=x,Y=y;
        if(singleLine){
            Y+=FontHeight;
        }
        if(Default) {
            layout = font.getCache().setText(this.text, X,Y);
        }else{
            layout = font.getCache().setText(this.text,X,Y,targetWidth,Align.left,warp);
        }
    }
    public void setSize(int size){
        dispose(name);

        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color= Color.WHITE;
        parameter.size= size;
        BitmapFont font;
        if(!FontsCache.containsKey(name)){
            YzBitmapFontData data=new YzBitmapFontData(Generator,parameter.color, parameter.size, this);
            font = Generator.generateFont(parameter,data);
            try {
                Field f = font.getClass().getSuperclass().getDeclaredField("data");
                f.setAccessible(true);
                f.set(font, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            genrateData();
            FontsCache.put(name, font);
        }else{
            font = FontsCache.get(name);
        }
        this.font=font;
        FontHeight=font.getAscent()+font.getCapHeight();
        reLoad(reloadType);
    }
    public void clear(){
        font.getCache().clear();
    }
    @Override
    public void dispose() {
        FontsCache.get(name).dispose();
    }
    public static void dispose(String name){
        FontsCache.remove(name).dispose();
    }
    public void draw(Batch batch, float alpha){
        font.getCache().draw(batch,alpha);
    }
    public int getCharEndIndex(int runs){
        if(layout.runs.size==0){
            return -1;
        }
        FloatArray fa=layout.runs.get(runs).xAdvances;
        int c=-1;
        for(int i=1;i<fa.size;i++){
            if(fa.get(i)!=0){
                c++;
            }
        }
        return c;
    }
    public Vector2 getCharacterPosition(int runs, int index){
        if(index==-1||layout.runs.size==0){
            return new Vector2(this.x,this.y);
        }
        FloatArray fa=layout.runs.get(runs).xAdvances;
        float X=this.x;
        for(int i = 0; i<(Math.min(index + 2, fa.size)); i++){
            X+=fa.get(i);
        }
        return new Vector2(X,y);
    }
    public float getWidth(){
        return layout.width;
    }
    public float getHeight(){
        return FontHeight+font.getAscent();
    }
    public static class YzBitmapFontData extends FreeTypeFontGenerator.FreeTypeBitmapFontData {

        private FreeTypeFontGenerator generator;
        private int fontSize;
        private YzBitmapFontV2 font;
        private int page = 1;
        private Color fontColor;

        @Override
        public void dispose() {
            super.dispose();
            font=null;
            fontColor=null;
        }

        public YzBitmapFontData(FreeTypeFontGenerator generator, Color fontColor, int fontSize,YzBitmapFontV2 lbf) {
            this.generator = generator;
            this.fontColor = fontColor;
            this.fontSize = fontSize;
            this.font = lbf;
        }

        public BitmapFont.Glyph getGlyph(char ch) {
            BitmapFont.Glyph glyph = super.getGlyph(ch);
            if (glyph == null && ch != 0)
                glyph = generateGlyph(ch);
            return glyph;
        }

        protected BitmapFont.Glyph generateGlyph(char ch) {
            FreeTypeFontGenerator.GlyphAndBitmap gab = generator.generateGlyphAndBitmap(ch, fontSize, false);
            if (gab == null || gab.bitmap == null)
                return null;
            Pixmap map = gab.bitmap.getPixmap(Pixmap.Format.RGBA8888,fontColor,1);
            TextureRegion rg = new TextureRegion(new Texture(map));
            map.dispose();

            font.font.getRegions().add(rg);

            gab.glyph.page = page++;
            super.setGlyph(ch, gab.glyph);
            setGlyphRegion(gab.glyph, rg);

            return gab.glyph;
        }

    }
}

package com.thzs.app.datacoplite.util.grapices;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.thzs.app.datacoplite.core.File;
import com.thzs.app.datacoplite.core.Path;

/**
 * XML EXAMPLE FOR the Pixmap
 * <?xml version="1.0" encoding="UTF-8" ?>
 *     <items>
 *         <item id="CAN_NULL" unitX="CAN_NULL" unitY="CAN_NULL" >
 *              <rectangle x="500" y="500" height="500" width="500" color="CAN_NULL" fill="CAN_NULL Default true"/>
 *              <circle x="500" y="500" radius="400（ * unitY）" color="CAN_NULL" fill="CAN_NULL Default true"/>
 *              <line x1="100" y1="100" x2="500" y2="500" color="CAN_NULL"/>
 *              <triangle x1="100" y1="100" x2="500" y2="500" x3="100" y3="500" color="CAN_NULL"/>
 *              <img type="INTERNET/ABSOLUTE/INTERNAL/EXTERNAL" src=“PATH" x="500" y="500" height="500" width="500"/>
 *         </item>
 *         ...
 *     </items>
 * */
public class XmlPixmap extends Pixmap {
    public XmlReader reader;
    public XmlReader.Element elementRoot;
    /**
     * adopted to {@link Path#XML_PATH}
     * */
    public XmlPixmap(int width, int height,String path,String ID){
        super(width, height, Format.RGBA8888);
        loadXmlFile(File.readString(Path.XML_PATH+path),ID);
    }
    public XmlPixmap(int width, int height,String path){
        this(width, height,path,null);
    }

    public XmlPixmap(int width, int height, Format format, FileHandle xmlFile) throws GdxRuntimeException {
        this(width, height,format,xmlFile,null);
    }
    public XmlPixmap(int width, int height, Format format, FileHandle xmlFile, String ID) throws GdxRuntimeException {
        super(width, height, format);
        loadXmlFile(xmlFile,ID);
    }
    public void clear(){
        reader=null;
        System.gc();
    }
    public void loadXmlFile(FileHandle xmlFile,String ID){
        String text=xmlFile.readString("utf-8");
        loadXmlFile(text,ID);
        text=null;
        xmlFile=null;
    }
    /**
     * load the xml file
     * @param text the xml file context
     * @param ID the ID use to find the item that you need in this xml file
     * */
    public void loadXmlFile(String text,String ID){
        try {
            reader = new XmlReader();
            elementRoot=reader.parse(text);
            //iterates through the elements
            XmlReader.Element element1;
            int count= elementRoot.getChildCount();
            for (int i=0; i<count; i++){
                element1=elementRoot.getChild(i);
                //judge the element name is 'item'
                if(element1.getName().equals("item")){
                    if(ID==null){
                        loadNoteItem(element1);
                    }else{
                        //if ID is not null
                        try {
                            if(element1.getAttribute("id").equals(ID)){
                                loadNoteItem(element1);
                            }
                        }catch (GdxRuntimeException e) {
                            e.printStackTrace();
                            //此处为未找到Id
                        }
                    }
                }
            }
        } catch (GdxRuntimeException e) {
            e.printStackTrace();
        }
    }
    /**
     * pass the element to create the pixmap picture
     * @param element the element name must be 'item'
     * */
    public void loadNoteItem(XmlReader.Element element) {
        float unitX=1000f,unitY=1000f;
        try {
            unitX=Float.parseFloat(element.getAttribute("unitX"));
        } catch (GdxRuntimeException e) {
            //此处报错为未找到unitX
        }
        try{
            unitY=Float.parseFloat(element.getAttribute("unitY"));
        }catch (GdxRuntimeException e) {
            //此处报错为未找到unitY
        }
        float unit_X=((float)getWidth())/unitX;
        float unit_Y= ((float) getHeight())/unitY;

        int count = element.getChildCount();
        //default color is black
        this.setColor(Color.BLACK);
        XmlPixmapF xmlPixmapF=new XmlPixmapF();
        for (int i=0; i<count; i++) {
            XmlReader.Element child = element.getChild(i);
            xmlPixmapF.parse(child);
            xmlPixmapF.set(this,unit_X,unit_Y);
        }
    }

    @Override
    public void dispose() {
        reader=null;
        elementRoot=null;
        super.dispose();
    }
}

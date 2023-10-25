package com.thzs.app.datacoplite.ui.view.nView.base.handle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.thzs.app.datacoplite.core.Path;
import com.thzs.app.datacoplite.ui.view.View;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class BaseViewHandle extends ViewHandle{
    public Properties properties=null;
    public boolean Debug=false;
    public BaseViewHandle(View view) {
        super(view);
    }

    @Override
    public void ViewInit() {

    }

    @Override
    public void dispose() {
        if(!Debug){
            save();
        }
    }

    @Override
    public void ViewListener() {
        load();
    }

    @Override
    public void load() {
        FileHandle file= Gdx.files.internal(Path.SAVE+getName()+".properties");
        if (!file.exists())
            return;
        properties = new Properties();
        try {
            properties.load(new FileReader(file.file()));
            loadData(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadData(Properties properties){

    }

    @Override
    public void save() {
        FileHandle file = Gdx.files.internal(Path.SAVE+getName()+".properties");
        if(!file.exists()) {
            try {
                file.file().createNewFile();
                properties=new Properties();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        SaveData(properties);
        try {
            properties.store(new FileWriter(file.file()),"ViewSave");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void SaveData(Properties properties){

    }
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
}

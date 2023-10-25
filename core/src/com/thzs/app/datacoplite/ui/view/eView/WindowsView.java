package com.thzs.app.datacoplite.ui.view.eView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.thzs.app.datacoplite.core.Game;
import com.thzs.app.datacoplite.ui.view.ParameterizableView;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzWindowTopBar;

import java.util.Map;

public class WindowsView extends ParameterizableView {
    public Stage stage;
    public VisLabel label;
    public YzWindowTopBar topBar;

    public ModelBatch modelBatch;
    public PerspectiveCamera cam;
    public Model model;
    public ModelInstance instance;
    @Override
    public void create(Map<String, Object> param) {
        //init 2d
        stage = Game.stage();
        label = new VisLabel("Welcome to this view!");
        label.setAlignment(Align.center);
        topBar=new YzWindowTopBar(this);
        stage.addActor(label);
        //init 3d
        cam=new PerspectiveCamera(67, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        cam.position.set(10f,10f,10f);
        cam.lookAt(new Vector3(0,0,0));
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        ModelBuilder modelBuilder = new ModelBuilder();
        model=modelBuilder.createBox(5f, 5f, 5f,
                new Material(ColorAttribute.createDiffuse(Color.WHITE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model);
        modelBatch=new ModelBatch();
        addProcessor(stage);
    }

    @Override
    public void draw() {
        stage.draw();
        modelBatch.begin(cam);
        modelBatch.render(instance);
        modelBatch.end();
    }

    @Override
    public void act() {
        stage.act();
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public void onRemove() {
        super.onRemove();
        stage.dispose();
        model.dispose();
        modelBatch.dispose();
    }

    @Override
    public String toString() {
        return "WindowsView{}";
    }
}

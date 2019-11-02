package de.toboidev.saimiri.examples.esCollision;


import com.jme3.app.DebugKeysAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.audio.AudioListenerState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.base.DefaultEntityData;
import com.simsilica.state.GameSystemsState;
import de.toboidev.saimiri.es.DefaultEntityDataProvider;
import de.toboidev.saimiri.es.components.Position;
import de.toboidev.saimiri.es.components.Rotation;
import de.toboidev.saimiri.es.components.Size;
import de.toboidev.saimiri.game.collision.World;
import de.toboidev.saimiri.game.collision.staticbodies.TileMap;
import de.toboidev.saimiri.game.components.*;
import de.toboidev.saimiri.game.systems.CollisionSystem;
import de.toboidev.saimiri.game.systems.PlatformerCharacterSystem;
import de.toboidev.saimiri.game.systems.TopDownCharacterSystem;
import de.toboidev.saimiri.gfx.components.CameraController;
import de.toboidev.saimiri.gfx.components.PointLightComponent;
import de.toboidev.saimiri.gfx.components.RenderComponent;
import de.toboidev.saimiri.gfx.components.SpriteComponent;
import de.toboidev.saimiri.gfx.deferred.DeferredLightingProcessor;
import de.toboidev.saimiri.gfx.deferred.LightSystem;
import de.toboidev.saimiri.gfx.render.CameraControllerState;
import de.toboidev.saimiri.gfx.render.SpriteRenderState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main extends SimpleApplication implements ActionListener {

    private World world = new World();
    private EntityData ed;
    private EntityId playerId;
    private int playerSizeX = 20;
    private int playerSizeY = 30;
    private boolean platformerControls = true;
    private boolean deferredLighting = false;
    private DeferredLightingProcessor deferredLightingProcessor;

    public Main() {
        super(new AudioListenerState(), new DebugKeysAppState(), new StatsAppState());

    }


    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }

    @Override public void simpleInitApp() {

        setDisplayFps(true);
        setDisplayStatView(true);

        ed = new DefaultEntityData();
        stateManager.attach(new DefaultEntityDataProvider(ed));

        GameSystemsState gss = new GameSystemsState(false);
        gss.register(EntityData.class, ed);
        gss.register(CollisionSystem.class, new CollisionSystem(world));
        gss.addSystem(new PlatformerCharacterSystem());
        gss.addSystem(new TopDownCharacterSystem());
        stateManager.attach(gss);


        stateManager.attach(new SpriteRenderState(rootNode));
        stateManager.attach(new CameraControllerState());


        setupInput();
        setupWorld();
        setupPlayerEntity();
        setupGroundEntity();

        deferredLightingProcessor = new DeferredLightingProcessor(assetManager);
        stateManager.attach(new LightSystem(deferredLightingProcessor));

    }

    private void setupWorld() {

        BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("map1.txt")));

        ArrayList<boolean[]> lines = new ArrayList<>();
        try {
            String line = br.readLine();
            while (line != null && !line.isEmpty()) {
                boolean[] l = new boolean[line.length()];
                for (int i = 0; i < l.length; i++) {
                    l[i] = line.charAt(i) == '1';
                }
                lines.add(l);
                line = br.readLine();
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }

        TileMap tm = new TileMap(64, lines.get(0).length, lines.size());
        for (int i = 0; i < lines.size(); i++) {
            boolean[] line = lines.get(i);
            for (int j = 0; j < line.length; j++) {
                tm.tileBlocking[j][lines.size() - 1 - i] = line[j];
            }
        }

        world.addBody(tm);

        Node worldNode = new Node();

        Material mat = new Material(assetManager, "de/toboidev/saimiri/gfx/geometry.j3md");
        mat.setColor("Color", ColorRGBA.Blue);

        for (int i = 0; i < tm.tileBlocking.length; i++) {
            for (int j = 0; j < tm.tileBlocking[i].length; j++) {
                boolean block = tm.tileBlocking[i][j];

                if (block) {
                    Quad quad = new Quad((float) tm.tileSize, (float) tm.tileSize);
                    Geometry geom = new Geometry("a", quad);
                    geom.setMaterial(mat);
                    geom.setLocalTranslation((i - 0.5f) * (float) tm.tileSize, (j - 0.5f) * (float) tm.tileSize, -100);
                    worldNode.attachChild(geom);
                }
            }
        }
        rootNode.attachChild(worldNode);
    }

    @Override public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("toggleInput") && isPressed) {
            togglePlatformer();
        } else if (name.equals("toggleLight") && isPressed) {
            toggleLight();
        } else if (platformerControls) {
            handlePlatformerInput(name, isPressed);
        } else {
            handleTopDownInput(name, isPressed);
        }
    }

    private void setupInput() {

        inputManager.addMapping("up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("toggleInput", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("toggleLight", new KeyTrigger(KeyInput.KEY_L));
        inputManager.addListener(this, "left", "right", "up", "down", "toggleInput", "toggleLight");
    }

    private void setupPlayerEntity() {
        playerId = ed.createEntity();
        ed.setComponents(playerId,
                new Position(100, 100),
                new Rotation(),
                new Size(playerSizeX, playerSizeY),
                new RenderComponent(0),
                new CameraController(800),
                new SpriteComponent("Common/Textures/MissingTexture.png"),
                new DynamicBodyComponent(),
                new PlatformerCharacterComponent(),
                new PlatformerInput(),
                new PointLightComponent(300, ColorRGBA.Cyan, 10));
    }

    private void setupGroundEntity() {
        EntityId ground = ed.createEntity();
        ed.setComponents(ground,
                new Position(500, -20),
                new Rotation(),
                new Size(5000, 40),
                new RenderComponent(-3),
                new SpriteComponent("Common/Textures/MissingModel.png"),
                new StaticCollider()
        );
    }

    private void handlePlatformerInput(String name, boolean isPressed) {
        PlatformerInput input = ed.getComponent(playerId, PlatformerInput.class);
        switch (name) {
            case "right":
                input = input.withRight(isPressed);
                break;
            case "left":
                input = input.withLeft(isPressed);
                break;
            case "up":
                if (isPressed) {
                    input = input.withJump(true);
                }
                break;
        }
        ed.setComponent(playerId, input);
    }

    private void handleTopDownInput(String name, boolean isPressed) {
        TopDownInput input = ed.getComponent(playerId, TopDownInput.class);
        if (input == null) {
            input = new TopDownInput(false, false, false, false);
        }
        switch (name) {
            case "right":
                input = input.withRight(isPressed);
                break;
            case "left":
                input = input.withLeft(isPressed);
                break;
            case "up":
                input = input.withUp(isPressed);
                break;
            case "down":
                input = input.withDown(isPressed);
                break;
        }
        ed.setComponent(playerId, input);
    }

    private void togglePlatformer() {
        if (platformerControls) {
            ed.removeComponent(playerId, PlatformerCharacterComponent.class);
            ed.setComponent(playerId, new TopDownCharacterComponent(400));
            ed.setComponent(playerId, new TopDownInput());
        } else {
            ed.removeComponent(playerId, TopDownCharacterComponent.class);
            ed.setComponent(playerId, new PlatformerCharacterComponent());
            ed.setComponent(playerId, new PlatformerInput());
        }
        platformerControls = !platformerControls;
    }

    private void toggleLight() {
        if (deferredLighting) {
            viewPort.removeProcessor(deferredLightingProcessor);
        } else {
            viewPort.addProcessor(deferredLightingProcessor);
        }
        deferredLighting = !deferredLighting;
    }
}

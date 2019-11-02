package de.toboidev.saimiri.examples.simpleCollision;


import com.jme3.app.DebugKeysAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioListenerState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import de.toboidev.saimiri.game.collision.DynamicBody;
import de.toboidev.saimiri.game.collision.World;
import de.toboidev.saimiri.game.collision.controllers.PlatformerCharacterController;
import de.toboidev.saimiri.game.collision.staticbodies.TileMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main extends SimpleApplication implements ActionListener {

    private Node worldNode = new Node();
    private PlatformerCharacterController playerController;
    private DynamicBody player;
    private Geometry playerGeom;
    private World world = new World();

    private Main() {
        super(new AudioListenerState(), new DebugKeysAppState());

    }

    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }

    @Override public void simpleInitApp() {

        //setDisplayFps(false);
        //setDisplayStatView(false);
        setupInput();
        setupWorld();
        playerController = new PlatformerCharacterController();
        player = new DynamicBody(20, 30, 70, 104);
        player.setController(playerController);
        Quad playerQuad = new Quad((float) player.getWidth(), (float) player.getHeight());
        playerGeom = new Geometry("a", playerQuad);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        mat.setColor("Color", ColorRGBA.Red);   // set color of material to blue
        playerGeom.setMaterial(mat);
        playerGeom.setLocalTranslation(1, 1, 100);
        worldNode.attachChild(playerGeom);
        world.addBody(player);
        guiNode.attachChild(worldNode);

    }

    @Override public void simpleUpdate(float tpf) {
        world.tick(tpf);
        playerGeom.setLocalTranslation((float) (player.getX() - player.getWidth() / 2.0), (float) (player.getY() - player.getHeight() / 2.0), 0);

        float xOffset = (float) (cam.getWidth() - player.getWidth()) / 2;
        float yOffset = (float) (cam.getHeight() - player.getHeight()) / 2;
        worldNode.setLocalTranslation((float) -player.getX() + xOffset, (float) -player.getY() + yOffset, 0);
        //System.out.println("X: " + player.x + " - Y: " + player.y);

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

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);

        for (int i = 0; i < tm.tileBlocking.length; i++) {
            for (int j = 0; j < tm.tileBlocking[i].length; j++) {
                boolean block = tm.tileBlocking[i][j];

                if (block) {
                    Quad quad = new Quad((float) tm.tileSize, (float) tm.tileSize);
                    Geometry geom = new Geometry("quad", quad);
                    geom.setMaterial(mat);
                    geom.setLocalTranslation((float) ((i - 0.5) * tm.tileSize), (float) ((j - 0.5) * tm.tileSize), -100);
                    worldNode.attachChild(geom);
                }


            }
        }

    }

    @Override public void onAction(String name, boolean isPressed, float tpf) {
        switch (name) {
            case "right":
                playerController.setMoveRight(isPressed);
                break;
            case "left":
                playerController.setMoveLeft(isPressed);
                break;
            case "up":
                if (isPressed) {
                    playerController.requestJump();
                }
                break;
        }
    }

    private void setupInput() {

        inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addListener(this, "left", "right", "up");
    }
}

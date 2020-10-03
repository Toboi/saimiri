package de.toboidev.saimiri.tmx;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import de.toboidev.saimiri.es.components.Position;
import de.toboidev.saimiri.es.components.Rotation;
import de.toboidev.saimiri.game.collision.StaticBody;
import de.toboidev.saimiri.game.collision.staticbodies.StaticBox;
import de.toboidev.saimiri.game.collision.staticbodies.TileMap;
import org.tiledreader.*;

import java.util.ArrayList;
import java.util.List;

public class TiledMapHelper {

    public static TileMap getTileMapCollider(TiledMap map, String collisionLayer) {
        if (map.getTileHeight() != map.getTileWidth()) {
            throw new UnsupportedOperationException("Can at the moment only handle maps with square tiles (equal tileHeight and tileWidth)!");
        }

        int width = map.getWidth();
        int height = map.getHeight();
        TileMap tm = new TileMap(map.getTileWidth(), width, height);

        for (TiledLayer layer : map.getNonGroupLayers()) {
            Object layerCollision = layer.getProperty("collision");
            if (layerCollision != null && layerCollision.equals(collisionLayer)) {

                if (layer instanceof TiledTileLayer) {
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            TiledTile tile = ((TiledTileLayer) layer).getTile(x, height - 1 - y);

                            //No tile means no collision
                            if (tile == null) {
                                tm.tileBlocking[x][y] = TileMap.TileShape.EMPTY;
                                continue;
                            }

                            List<TiledObject> collisionObjects = tile.getCollisionObjects();
                            //We assume that the complete tile is blocking if no custom shapes have been defined
                            if (collisionObjects.isEmpty()) {
                                tm.tileBlocking[x][y] = TileMap.TileShape.FILLED;
                                continue;
                            }

                            tm.tileBlocking[x][y] = TileMap.TileShape.CUSTOM;
                            ArrayList<StaticBody> colliders = new ArrayList<>();
                            for (TiledObject obj : collisionObjects) {
                                if (obj.getShape() == TiledObject.Shape.RECTANGLE || obj.getRotation() != 0) {
                                    Vector2f position = getObjectPosition(map, obj);
                                    position.addLocal(x * map.getTileWidth(), (y + 1 - map.getHeight()) * map.getTileHeight());
                                    StaticBox box = new StaticBox(position.x, position.y, obj.getWidth(), obj.getHeight());
                                    colliders.add(box);
                                } else {
                                    throw new UnsupportedOperationException("Only axis aligned rectangle collision shapes are supported!");
                                }
                            }
                            tm.customShapes[x][y] = colliders;

                        }
                    }
                }
            }
        }
        return tm;
    }

    public static Vector2f getObjectPosition(TiledMap map, TiledObject obj) {

        float halfWidth = obj.getWidth() / 2.0f;
        float halfHeight = obj.getHeight() / 2.0f;
        float x = obj.getX();
        float y = obj.getY();
        y = (map.getHeight() - 1) * map.getTileHeight() - y;

        x = x - map.getTileWidth() / 2.0f;
        y = y + map.getTileHeight() / 2.0f;

        float rot = obj.getRotation() * FastMath.DEG_TO_RAD;
        float sin = FastMath.sin(rot);
        float cos = FastMath.cos(rot);
        x = x + cos * halfWidth - sin * halfHeight;
        y = y - sin * halfWidth - cos * halfHeight;

        return new Vector2f(x, y);
    }

    public static Rotation getObjectRotation(TiledObject obj) {
        return new Rotation(obj.getRotation() * FastMath.DEG_TO_RAD);
    }
}

package de.toboidev.saimiri.tmx;

import com.jme3.math.FastMath;
import de.toboidev.saimiri.es.components.Position;
import de.toboidev.saimiri.es.components.Rotation;
import de.toboidev.saimiri.game.collision.staticbodies.TileMap;
import org.tiledreader.TiledLayer;
import org.tiledreader.TiledMap;
import org.tiledreader.TiledObject;
import org.tiledreader.TiledTileLayer;

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
                            tm.tileBlocking[x][y] |= ((TiledTileLayer) layer).getTile(x, height - 1 - y) != null;
                        }
                    }
                }
            }
        }
        return tm;
    }

    public static Position getObjectPosition(TiledMap map, TiledObject obj) {

        float halfWidth = obj.getWidth()/2.0f;
        float halfHeight = obj.getHeight()/2.0f;
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

        return new Position(x, y);
    }

    public static Rotation getObjectRotation(TiledObject obj) {
        return new Rotation(obj.getRotation() * FastMath.DEG_TO_RAD);
    }
}

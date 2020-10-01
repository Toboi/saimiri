package de.toboidev.saimiri.tmx;

import de.toboidev.saimiri.game.collision.staticbodies.TileMap;
import org.tiledreader.TiledLayer;
import org.tiledreader.TiledMap;
import org.tiledreader.TiledTileLayer;

public class MapCollision {

    @Deprecated
    public static TileMap getTileMapCollider(TiledMap map) {
        return getTileMapColliderLayerName(map, "[Collision]");
    }

    @Deprecated
    public static TileMap getTileMapColliderLayerName(TiledMap map, String layerFilter) {
        if (map.getTileHeight() != map.getTileWidth()) {
            throw new UnsupportedOperationException("Can at the moment only handle maps with square tiles (equal tileHeight and tileWidth)!");
        }

        int width = map.getWidth();
        int height = map.getHeight();
        TileMap tm = new TileMap(map.getTileWidth(), width, height);

        for (TiledLayer layer : map.getNonGroupLayers()) {
            if (layer.getName().contains(layerFilter)) {
                fillCollisionMap(width, height, tm, layer);
            }
        }
        return tm;

    }

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
                fillCollisionMap(width, height, tm, layer);
            }
        }
        return tm;

    }

    private static void fillCollisionMap(int width, int height, TileMap tm, TiledLayer layer) {
        if (layer instanceof TiledTileLayer) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    tm.tileBlocking[x][y] |= ((TiledTileLayer) layer).getTile(x, height - 1 - y) != null;
                }
            }

        }
    }
}

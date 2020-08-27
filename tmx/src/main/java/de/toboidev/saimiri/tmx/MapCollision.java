package de.toboidev.saimiri.tmx;

import de.toboidev.saimiri.game.collision.staticbodies.TileMap;
import org.tiledreader.TiledLayer;
import org.tiledreader.TiledMap;
import org.tiledreader.TiledTileLayer;

public class MapCollision {

    public static TileMap getTileMapCollider(TiledMap map) {
        if (map.getTileHeight() != map.getTileWidth()) {
            throw new UnsupportedOperationException("Can at the moment only handle maps with equal tileHeight and tileWidth!");
        }

        int width = map.getWidth();
        int height = map.getHeight();
        TileMap tm = new TileMap(map.getTileWidth(), width, height);

        for (TiledLayer layer : map.getNonGroupLayers()) {
            if (layer.getName().contains("[Collision]")) {
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
}

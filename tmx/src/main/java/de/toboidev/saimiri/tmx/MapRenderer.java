package de.toboidev.saimiri.tmx;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.scene.*;
import com.jme3.texture.Texture;
import org.tiledreader.*;

import java.util.HashMap;

/**
 * @author Eike Foede <toboi@toboidev.de>
 */
public class MapRenderer
{
    public AssetManager assetManager;
    HashMap<TiledTileset, Material> materials;
    TiledMap map;
    Node mapNode;

    public Spatial getSpatial(TiledMap map)
    {
        this.map = map;
        materials = new HashMap<>();
        mapNode = new Node("TmxMap");
        for (TiledTileset tileSet : map.getTilesets())
        {
            loadMaterial(tileSet);
        }

        for (int i = 0; i < map.getNonGroupLayers().size(); i++)
        {
            TiledLayer layer = map.getNonGroupLayers().get(i);
            if(layer instanceof TiledTileLayer)
            {
                TiledTileLayer tileLayer = (TiledTileLayer) layer;
                if(layer.getAbsVisible())
                {
                    Node layerNode = generateNode(tileLayer);
                    //TODO: Make layer offset somehow configurable. What happens when other game objects should be between layers?
                    layerNode.setLocalTranslation(-map.getTileWidth() / 2.0f, ((float) (map.getWidth()) - 1.5f) * map.getTileHeight(), i - 1000);
                    mapNode.attachChild(layerNode);
                }
            }
        }
        return mapNode;
    }


    private Node generateNode(TiledTileLayer layer)
    {
        int tileWidth = map.getTileWidth();
        int tileHeight = map.getTileHeight();

        TiledMap.RenderOrder renderOrder = map.getRenderOrder();

        Node n = new Node(layer.getName());

        int startX = layer.getX1();
        int endX = layer.getX2() + 1;
        int incX = 1;

        if(renderOrder == TiledMap.RenderOrder.LEFT_DOWN || renderOrder == TiledMap.RenderOrder.LEFT_UP)
        {

            startX = layer.getX2();
            endX = layer.getX1()-1;
            incX = -1;
        }

        int startY = layer.getY1();
        int endY = layer.getY2() + 1;
        int incY = 1;

        if(renderOrder == TiledMap.RenderOrder.RIGHT_UP || renderOrder == TiledMap.RenderOrder.LEFT_UP)
        {
            startY = layer.getY2();
            endY = layer.getY1()-1;
            incY = -1;
        }

        int z = 1;

        for (int x = startX; x != endX; x += incX)
        {
            for (int y = startY; y != endY; y += incY)
            {
                TiledTile tileId = layer.getTile(x,y);
                if (tileId != null)
                {
                    Geometry g = getTile(tileId, x*tileWidth, -y * tileHeight, z++);
                    n.attachChild(g);
                }
            }
        }
        n.setLocalScale(1, 1, 1.0f / z);
        return n;
    }


    private void loadMaterial(TiledTileset tileSet)
    {
        Material mat = new Material(assetManager, "de/toboidev/saimiri/gfx/geometry.j3md");
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        TextureKey tk = new TextureKey(tileSet.getImage().getSource());
        tk.setGenerateMips(false);
        Texture t = assetManager.loadTexture(tk);
        t.setMagFilter(Texture.MagFilter.Nearest);
        mat.setTexture("ColorMap", t);
        try
        {

            tk = new TextureKey(tileSet.getImage().getSource().replace(".png", "_n.png"));
            tk.setGenerateMips(false);
            t = assetManager.loadTexture(tk);
            t.setMagFilter(Texture.MagFilter.Nearest);
            mat.setTexture("NormalMap", t);
        } catch (Exception e)
        {
        }
        materials.put(tileSet, mat);
    }

    private Geometry getTile(TiledTile tileId, int x, int y, int z)
    {

        TiledTileset ts = tileId.getTileset();

        int column = tileId.getTilesetX();
        int row = tileId.getTilesetY();

        int margin = ts.getMargin();
        int spacing = ts.getSpacing();

        int sourceWidth = ts.getImage().getWidth();
        int sourceHeight = ts.getImage().getHeight();

        float xLeft = margin + ts.getTileWidth() * column + spacing * (column);
        float xRight = margin + ts.getTileWidth() * (column + 1) + spacing * (column);

        float yTop = margin + ts.getTileHeight() * (row + 1) + spacing * (row);
        float yBottom = margin + ts.getTileHeight() * (row) + spacing * (row);

        //The y coordinates of the map are top-down, but we use a bottom-up coordinate system internally. So convert here:
        yTop = sourceHeight - yTop;
        yBottom = sourceHeight - yBottom;

        float x0 = xLeft / sourceWidth;
        float x1 = xRight / sourceWidth;

        float y0 = yTop / sourceHeight;
        float y1 = yBottom / sourceHeight;

        int w = ts.getTileWidth();
        int h = ts.getTileHeight();

        Mesh m = new Mesh();
        m.setBuffer(VertexBuffer.Type.Position, 3, new float[]{
                x, y, z,
                x, y+h, z,
                x+ w, y+h, z,
                x + w, y, z
        });
        m.setBuffer(VertexBuffer.Type.TexCoord, 2, new float[]{
                x0, y0,
                x0, y1,
                x1, y1,
                x1, y0
        });

        m.setBuffer(VertexBuffer.Type.Index, 3, new short[]{
                0, 2, 1,
                0, 3, 2
        });

        m.updateBound();
        m.setStatic();
        Geometry g = new Geometry("asdf", m);
        g.setMaterial(materials.get(ts));
        return g;
    }
}

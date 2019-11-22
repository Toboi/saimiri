package de.toboidev.saimiri.tmx;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetLoader;
import com.jme3.asset.AssetManager;
import org.tiledreader.TiledMap;
import org.tiledreader.TiledReader;
import org.tiledreader.TiledResource;

import java.io.IOException;
import java.io.InputStream;

public class TiledLoader extends TiledReader implements AssetLoader {

    AssetManager assetManager;

    @Override
    public Object load(AssetInfo assetInfo) throws IOException {
        String extension = assetInfo.getKey().getExtension();
        this.assetManager = assetInfo.getManager();
        if (extension.equals("tmx")) {
            return getMap(assetInfo.getKey().getName());
        }
        throw new IOException("TiledLoader does not support loading files with different extension: " + assetInfo.getKey().getName());
    }


    @Override
    public String getCanonicalPath(String path) {
        return path;
    }

    @Override
    public String joinPaths(String basePath, String relativePath) {
        String returnPath = relativePath;
        String folder = new AssetKey<TiledMap>(basePath).getFolder();
        if (!folder.isEmpty()) {
            returnPath = folder + relativePath;
        }
        System.out.println(returnPath);
        return returnPath;
    }

    @Override
    public InputStream getInputStream(String path) {
        return assetManager.locateAsset(new AssetKey<>(path)).openStream();
    }

    @Override
    public TiledResource getCachedResource(String path) {
        return assetManager.getFromCache(new AssetKey<TiledResource>(path));

    }

    @Override
    protected void setCachedResource(String path, TiledResource resource) {
        assetManager.addToCache(new AssetKey<TiledResource>(path), resource);
    }

    @Override
    protected void removeCachedResource(String path) {
        assetManager.deleteFromCache(new AssetKey<TiledResource>(path));
    }

    @Override
    protected void clearCachedResources() {
        throw new UnsupportedOperationException("Not implemented");
    }
}

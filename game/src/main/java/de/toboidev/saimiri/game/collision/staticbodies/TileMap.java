package de.toboidev.saimiri.game.collision.staticbodies;

import de.toboidev.saimiri.game.collision.DynamicBody;
import de.toboidev.saimiri.game.collision.StaticBody;
import de.toboidev.saimiri.game.collision.World;

import java.util.ArrayList;

public class TileMap extends StaticBody {

    public enum TileShape {
        EMPTY,
        FILLED,
        CUSTOM
    }

    public final double tileSize;
    private final int sizeX;
    private final int sizeY;
    public TileShape[][] tileBlocking;
    public ArrayList<StaticBody>[][] customShapes;

    public TileMap(int tileSize, int sizeX, int sizeY) {
        this.tileSize = tileSize;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.tileBlocking = new TileShape[sizeX][sizeY];
        this.customShapes = new ArrayList[sizeX][sizeY];
    }

    @Override
    public double limitMovement(DynamicBody body, double distance, boolean horizontal) {
        double startWorldPos = body.getPosition(horizontal);
        if (distance > 0) {
            //The body extends from it's position in positive direction, so we want the point that's farthest in the movement direction
            startWorldPos += body.getExtent(horizontal);
        } else {
            startWorldPos -= body.getExtent(horizontal);
        }
        double endWorldPos = startWorldPos + distance;


        int startTile = getTileId(startWorldPos);
        int endTile = getTileId(endWorldPos);

        //This are the tiles which would block the movement because the body extends this far orthogonal to our movement.
        int startWidthTile = getTileId(body.getPosition(!horizontal) - body.getExtent(!horizontal));
        int endWidthTile = getTileId(body.getPosition(!horizontal) + body.getExtent(!horizontal));

        startWidthTile = Math.max(startWidthTile, clampTile(startWidthTile, !horizontal));
        endWidthTile = Math.min(endWidthTile, clampTile(endWidthTile, !horizontal));

        if (startTile < endTile) {
            //If we move in positive direction
            startTile = Math.max(startTile, clampTile(startTile, horizontal));
            endTile = Math.min(endTile, clampTile(endTile, horizontal));
            //Iterate over all tiles we enter to check if there is something blocking
            movementLoop:
            for (int i = startTile; i <= endTile; i++) {
                //Iterate over the full width of the body
                for (int j = startWidthTile; j <= endWidthTile; j++) {
                    TileShape shape = horizontal ? tileBlocking[i][j] : tileBlocking[j][i];
                    if (shape == TileShape.FILLED) {
                        //Calculate movement distance
                        double limitPosition = i * tileSize - tileSize / 2.0f;

                        distance = limitPosition - startWorldPos - world.COLLISION_WIDTH;
//                        break movementLoop;
                    } else if (shape == TileShape.CUSTOM) {
                        ArrayList<StaticBody> colliders = horizontal ? customShapes[i][j] : customShapes[j][i];
                        for (StaticBody collider : colliders) {
                            distance = collider.limitMovement(body, distance, horizontal);
                        }
                    }
                }
            }
        } else {
            //If we move in negative direction
            startTile = Math.min(startTile, clampTile(startTile, horizontal));
            endTile = Math.max(endTile, clampTile(endTile, horizontal));

            //Iterate over all tiles we enter to check if there is something blocking
            movementLoop:
            for (int i = startTile; i >= endTile; i--) {
                //Iterate over the full width of the body
                for (int j = startWidthTile; j <= endWidthTile; j++) {
                    TileShape shape = horizontal ? tileBlocking[i][j] : tileBlocking[j][i];
                    if (shape == TileShape.FILLED) {
                        //Calculate movement distance
                        double limitPosition = i * tileSize + tileSize / 2.0f;
                        distance = limitPosition - startWorldPos + world.COLLISION_WIDTH;
                        break movementLoop;
                    } else if (shape == TileShape.CUSTOM) {
                        ArrayList<StaticBody> colliders = horizontal ? customShapes[i][j] : customShapes[j][i];
                        for (StaticBody collider : colliders) {
                            distance = collider.limitMovement(body, distance, horizontal);
                        }
                    }
                }
            }
        }
        return distance;
    }

    private int getTileId(double worldPosition) {
        return (int) Math.floor((worldPosition + tileSize / 2.0f) / tileSize);
    }

    private int clampTile(int position, boolean horizontal) {
        int limit = horizontal ? sizeX : sizeY;
        return Math.max(0, Math.min(limit - 1, position));
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                ArrayList<StaticBody> bodies = customShapes[x][y];
                if(bodies!=null)
                {
                    for(StaticBody body : bodies)
                    {
                        body.setWorld(world);
                    }
                }
            }
        }
    }

}

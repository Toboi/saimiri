package de.toboidev.saimiri.game.collision.staticbodies;

import de.toboidev.saimiri.game.collision.DynamicBody;
import de.toboidev.saimiri.game.collision.StaticBody;

public class TileMap extends StaticBody {

    public final double tileSize;
    private final int sizeX;
    private final int sizeY;
    public boolean[][] tileBlocking;

    public TileMap(int tileSize, int sizeX, int sizeY) {
        this.tileSize = tileSize;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.tileBlocking = new boolean[sizeX][sizeY];
    }

    @Override public double limitMovement(DynamicBody body, double distance, boolean horizontal) {
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

        //If we don't move over tile borders we won't limit the movement at all
        if (startTile == endTile) {
            return distance;
        }

        //This are the tiles which would block the movement because the body extends this far orthogonal to our movement.
        int startWidthTile = getTileId(body.getPosition(!horizontal) - body.getExtent(!horizontal));
        int endWidthTile = getTileId(body.getPosition(!horizontal) + body.getExtent(!horizontal));

        startWidthTile = Math.max(startWidthTile, clampTile(startWidthTile, !horizontal));
        endWidthTile = Math.min(endWidthTile, clampTile(endWidthTile, !horizontal));

        if (startTile < endTile) {
            //If we move in positive direction
            startTile = Math.max(startTile + 1, clampTile(startTile + 1, horizontal));
            endTile = Math.min(endTile, clampTile(endTile, horizontal));
            //Iterate over all tiles we enter to check if there is something blocking
            movementLoop:
            for (int i = startTile; i <= endTile; i++) {
                //Iterate over the full width of the body
                for (int j = startWidthTile; j <= endWidthTile; j++) {
                    boolean block = horizontal ? tileBlocking[i][j] : tileBlocking[j][i];
                    if (block) {
                        //Calculate movement distance
                        double limitPosition = i * tileSize - tileSize / 2.0f;

                        distance = limitPosition - startWorldPos - world.COLLISION_WIDTH;
                        break movementLoop;
                    }
                }
            }
        } else {
            //If we move in negative direction
            startTile = Math.min(startTile - 1, clampTile(startTile - 1, horizontal));
            endTile = Math.max(endTile, clampTile(endTile, horizontal));

            //Iterate over all tiles we enter to check if there is something blocking
            movementLoop:
            for (int i = startTile; i >= endTile; i--) {
                //Iterate over the full width of the body
                for (int j = startWidthTile; j <= endWidthTile; j++) {
                    boolean block = horizontal ? tileBlocking[i][j] : tileBlocking[j][i];
                    if (block) {
                        //Calculate movement distance
                        double limitPosition = i * tileSize + tileSize / 2.0f;
                        distance = limitPosition - startWorldPos + world.COLLISION_WIDTH;
                        break movementLoop;
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
}

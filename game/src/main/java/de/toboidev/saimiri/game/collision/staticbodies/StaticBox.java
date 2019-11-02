package de.toboidev.saimiri.game.collision.staticbodies;

import de.toboidev.saimiri.game.collision.DynamicBody;
import de.toboidev.saimiri.game.collision.StaticBody;

public class StaticBox extends StaticBody {

    private final double minX;
    private final double maxX;
    private final double minY;
    private final double maxY;

    public StaticBox(double x, double y, double width, double height) {
        minX = x - width / 2.0f;
        maxX = x + width / 2.0f;
        minY = y - height / 2.0f;
        maxY = y + height / 2.0f;
    }

    @Override public double limitMovement(DynamicBody body, double distance, boolean horizontal) {
        double startPos = body.getPosition(horizontal);
        startPos += distance > 0 ? body.getExtent(horizontal) : -body.getExtent(horizontal);

        double endPos = startPos + distance;


        double minWidthPos = body.getPosition(!horizontal) - body.getExtent(!horizontal);
        double maxWidthPos = body.getPosition(!horizontal) + body.getExtent(!horizontal);

        if (minWidthPos > getBoxMax(!horizontal) - world.COLLISION_WIDTH || maxWidthPos < getBoxMin(!horizontal) + world.COLLISION_WIDTH) {
            //The movement happens outside of the box, so we don't block it
            return distance;
        }

        if (distance > 0) {
            //Movement in positive direction
            double boxMin = getBoxMin(horizontal);
            if (startPos <= boxMin + world.COLLISION_WIDTH && endPos >= boxMin) {
                //Box hit
                return boxMin - startPos;
            }
        } else {
            //Movement in negative direction
            double boxMax = getBoxMax(horizontal);
            if (startPos >= boxMax - world.COLLISION_WIDTH && endPos <= boxMax) {
                //Box hit
                return boxMax - startPos;
            }
        }
        return distance;
    }

    private double getBoxMin(boolean horizontal) {
        return horizontal ? minX : minY;
    }

    private double getBoxMax(boolean horizontal) {
        return horizontal ? maxX : maxY;
    }
}

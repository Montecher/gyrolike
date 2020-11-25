package gyrolike.model.mover;

import gyrolike.model.DiscretePosition;
import gyrolike.model.Game;

public abstract class TileMover implements Mover{
    protected DiscretePosition[] positionList;

    @Override
    public int getInterval() {
        return 10;
    }
}

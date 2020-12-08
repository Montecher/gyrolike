package gyrolike.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import gyrolike.model.mover.Mover;
import gyrolike.model.sprite.Player;
import gyrolike.model.sprite.Sprite;

public class Grid {
    private Tile[][] tileGrid;
    private HashMap<Sprite, ContinuousPosition> spriteHash;
	private List<Mover> movers;
    int width, height;

    private Grid(int width, int height) {
        this.tileGrid = new Tile[width][height];
        this.spriteHash = new HashMap<>();
        this.width = width;
        this.height = height;
    }

    public static Grid loadFrom(Level lvl) {
        Grid grid = new Grid(lvl.width, lvl.height);
        grid.tileGrid = lvl.getTiles();
        grid.spriteHash = lvl.getSprites();
		grid.movers = lvl.getMovers();
        return grid;
    }

    public ContinuousPosition getSpritePos(Sprite sprite) {
        return this.spriteHash.get(sprite);
    }

    public void setSpritePos(Sprite sprite, ContinuousPosition pos) {
        this.spriteHash.put(sprite, pos);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

	public Player getPlayer() {
		for(Sprite s: spriteHash.keySet()) if(s instanceof Player) return (Player) s;
		throw new IllegalStateException("No player!");
	}

    public Set<Sprite> getIntersectingSprites(DiscretePosition pos) {
        Set<Sprite> intersecting = new HashSet<>();

        for (Map.Entry<Sprite, ContinuousPosition> e: spriteHash.entrySet()) {
            if (e.getKey().getBoundingBox(e.getValue()).intersects(pos)) {
                intersecting.add(e.getKey());
            }
        }

        return intersecting;
    }

    public Set<Sprite> getIntersectingSprites(Sprite sprite) {
        Set<Sprite> intersecting = new HashSet<>();
        BoundingBox spriteBox = sprite.getBoundingBox(this.getSpritePos(sprite));

        for (Map.Entry<Sprite, ContinuousPosition> e: spriteHash.entrySet()) {
            if (e.getKey().getBoundingBox(e.getValue()).intersects(spriteBox)) {
                intersecting.add(e.getKey());
            }
        }

        return intersecting;
    }

    public Tile getTileAt(DiscretePosition coord) {
        return this.tileGrid[coord.getX()][coord.getY()];
    }
    public Tile getTileAt(int x, int y) {
        return this.tileGrid[x][y];
    }

    public void setTileAt(DiscretePosition goal, Tile tile) {
        this.tileGrid[goal.getX()][goal.getY()] = tile;
    }

    public Map<Sprite, ContinuousPosition> getSprites() {
        return Collections.unmodifiableMap(this.spriteHash);
    }

	public List<Mover> getMovers() {
		return Collections.unmodifiableList(this.movers);
	}

	public boolean touches(Sprite sp, Predicate<Tile> tilePred) {
		BoundingBox bbox = sp.getBoundingBox(spriteHash.get(sp));
		return touches(bbox, tilePred);
	}
	public boolean touches(BoundingBox bbox, Predicate<Tile> tilePred) {
		for(DiscretePosition dpos: bbox.getIntersectingCells()) {
			if(dpos.getX()<0 || dpos.getY()<0 || dpos.getX()>=width || dpos.getY()>=height) continue;
			if(tilePred.test(getTileAt(dpos))) return true;
		}
		return false;
	}

	public void move(Sprite sp, ContinuousDisplacement disp) {
		ContinuousPosition currPos = spriteHash.get(sp);
		ContinuousPosition newPos = currPos.displace(disp);
		if(newPos.getX()<0 || newPos.getY()<0 || newPos.getX()+sp.getWidth()>=width || newPos.getY()+sp.getHeight()>=height) {
			// fail
			return;
		}
		if(touches(sp.getBoundingBox(newPos), t -> t.solid)) {
			// fail
			return;
		}
		spriteHash.put(sp, newPos);
	}
}

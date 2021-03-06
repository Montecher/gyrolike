package gyrolike.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import gyrolike.model.exceptions.GameEnd;
import gyrolike.model.exceptions.GameLose;
import gyrolike.model.exceptions.GameWin;
import gyrolike.model.mover.Mover;
import gyrolike.model.mover.PlayerMover;
import gyrolike.model.sprite.Bomb;
import gyrolike.model.sprite.Enemy;
import gyrolike.model.sprite.Player;
import gyrolike.model.sprite.Sprite;
import gyrolike.util.Functional.MaybeConsumer;

public class Game {
	public static enum State {
		RUNNING, WON, LOST
	}

	public static double TICKRATE = 30;
	public static final boolean DEBUG = true;

    private Level currentLevel;
    private Grid grid;
    private int timeRemaining;
    private Direction columnDirection;
    private Tile selectedColor;
    private Map<Key, Boolean> keys = new HashMap<>();
    {
        for(Key k: Key.values()) keys.put(k, false);
    }
    private List<Runnable> tickListeners = new ArrayList<>();
	private Thread thread;
	private Map<Mover, Integer> delays = new HashMap<>();
	private List<MaybeConsumer<Game, GameEnd>> checkers = new ArrayList<>();
	private boolean paused;
	private State state = State.RUNNING;
	private String lossMessage;
	private List<Consumer<State>> stateListeners = new ArrayList<>();

    public Game() {
        this.columnDirection = Direction.NONE;
        this.selectedColor = Tile.COLUMN_RED;
    }

    public void loadLevel(String id) {
		if(isRunning()) throw new IllegalStateException("Game is running!");
        this.currentLevel = Level.getLevel(id);
        this.grid = Grid.loadFrom(this.currentLevel);
        this.timeRemaining = this.currentLevel.time;
    }

    public synchronized Direction getColumnDirection() {
        return columnDirection;
    }

    public synchronized Grid getGrid() {
        return grid;
    }

    public synchronized Tile getSelectedColor() {
        return selectedColor;
    }

    public synchronized int getTimeRemaining() {
        return timeRemaining;
    }

    public synchronized void setSelectedColor(Tile selectedColor) {
        if (selectedColor == Tile.COLUMN_RED || selectedColor == Tile.COLUMN_BLUE) {
            this.selectedColor = selectedColor;
			this.columnDirection = Direction.NONE;
        } else {
            throw new IllegalArgumentException("not a column !");
        }
    }
    public synchronized void setColumnDirection(Direction dir) {
        if(dir == Direction.UP || dir == Direction.DOWN || dir == Direction.NONE) {
            this.columnDirection = dir;
        } else {
            throw new IllegalArgumentException("not a column direction !");
        }
    }

    public synchronized void setKeys(Map<Key, Boolean> keys) {
        this.keys = keys;
    }
	public synchronized boolean hasKey(Key key) {
		return this.keys.get(key);
	}

    public synchronized void addTickListener(Runnable listener) {
        this.tickListeners.add(listener);
    }
	public synchronized void addStateListener(Consumer<State> listener) {
		this.stateListeners.add(listener);
	}

	public synchronized void start() {
		if(this.thread != null) return;
		this.thread = new Thread(this::threadFn);
		this.thread.start();
	}
	public synchronized void stop() {
		if(this.thread == null) return;
		this.thread.interrupt();
		this.thread = null;
	}
	public boolean isRunning() {
		return this.thread != null;
	}

	public synchronized void pause() { paused = true; }
	public synchronized void unpause() { paused = false; }
	public synchronized boolean isPaused() { return paused; }
	public synchronized void togglePause() { paused = !paused; }

	public synchronized State getState() { return state; }
	private void setState(State s) { state = s; }
	public synchronized String getLossMessage() { return lossMessage; }

	private void threadFn() {
		startGame();
		runTickListeners();
		try {
			long tickMs = (long) (1000 / TICKRATE);
			long nextTime = new Date().getTime() + tickMs;
			while(!Thread.interrupted() && state == State.RUNNING) {
				while(isPaused()) {
					Thread.sleep(tickMs);
					nextTime = new Date().getTime() + tickMs;
				}
				long beforeTime = new Date().getTime();
				tick();
				runTickListeners();
				long currentTime = new Date().getTime();
				if(DEBUG) System.out.println("Tick duration: "+(currentTime-beforeTime)+"ms");
				if(currentTime<nextTime) Thread.sleep(nextTime - currentTime);
				nextTime += tickMs;
			}
		} catch(InterruptedException e) {
			return;
		}
	}

	private void runTickListeners() {
		for(Runnable r: tickListeners) {
			try {
				r.run();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void startGame() {
		for(Mover m: grid.getMovers()) delays.put(m, 0);
		delays.put(new PlayerMover(grid.getPlayer()), 0);
		checkers.add(Game::testWin);
		checkers.add(Game::testCollision);
		checkers.add(Game::testCrush);
	}

	private int tickNo = 0;
	private synchronized void tick() {
		for(Entry<Mover, Integer> e: delays.entrySet()) {
			if(e.getValue() == 0) {
				e.setValue(e.getKey().getInterval()-1);
				try {
					e.getKey().move(this);
				} catch(Exception ex) {
					ex.printStackTrace();
				} catch(GameEnd ex) {
					if(e instanceof GameWin) {
						setState(State.WON);
					} else if(e instanceof GameLose) {
						lossMessage = ((GameLose) ex).getReason();
						setState(State.LOST);
					}
					stop();
					return;
				}
			} else {
				e.setValue(e.getValue()-1);
			}
		}

		for(MaybeConsumer<Game, ? extends GameEnd> c: checkers) {
			try {
				c.run(this);
			} catch(Exception e) {
				e.printStackTrace();
			} catch(GameEnd e) {
				if(e instanceof GameWin) {
					setState(State.WON);
				} else if(e instanceof GameLose) {
					lossMessage = ((GameLose) e).getReason();
					setState(State.LOST);
				}
				stop();
				return;
			}
		}

		if(tickNo++ % TICKRATE == 0) timeRemaining--;
		if(timeRemaining == 0) {
			lossMessage = "Out of time";
			setState(State.LOST);
			stop();
			return;
		}

		System.out.println("Time: "+timeRemaining);
		System.out.println("Player xy: "+grid.getSpritePos(grid.getPlayer()).getX()+","+grid.getSpritePos(grid.getPlayer()).getY());
	}

	public static void win() throws GameWin {
		throw new GameWin();
	}
	public static void lose(String reason) throws GameLose {
		throw new GameLose(reason);
	}

	private static void testWin(Game game) throws GameEnd {
		boolean hasBombs = false;
		ArrayList<Sprite> toRemove = new ArrayList<>();
		for(Sprite s: game.grid.getSprites().keySet()) if(s instanceof Bomb) {
			for(Sprite sp: game.grid.getIntersectingSprites(s)) if(sp instanceof Player) {
				toRemove.add(s);
			} else {
				hasBombs = true;
			}
		}
		for(Sprite s: toRemove) game.grid.deleteSprite(s);
		if(!hasBombs) win();
	}

	private static void testCollision(Game game) throws GameEnd {
		for(Sprite s: game.grid.getIntersectingSprites(game.grid.getPlayer())) {
			if(s instanceof Enemy) lose("Collided with a "+s.getClass().getSimpleName());
		}
	}

	private static void testCrush(Game game) throws GameEnd {
		if(game.grid.touches(game.getGrid().getPlayer(), t -> t.solid)) lose("Crushed");
	}
}

package gyrolike.model;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gyrolike.model.sprite.Sprite;
import gyrolike.model.mover.Mover;
import gyrolike.util.ResourceLoader;

public class Level {
	public final String name;
	public final int time;
	public final String previous;
	public final String next;
	public final int width;
	public final int height;
	private final Tile[][] tiles;
	private final HashMap<Sprite, ContinuousPosition> sprites;
	private final List<Mover> movers;

	@Override
	public String toString() {
		String header = String.format("%s (%d, %s, %s) %dx%d\n", name, time, previous, next, width, height);
		StringBuilder grid = new StringBuilder();
		for(int y=height-1; y>=0; y--) {
			for(int x=0; x<width; x++) {
				grid.append(tiles[x][y].name().charAt(0));
			}
			grid.append('\n');
		}
		return header + grid;
	}

	private Level(String name, int time, String previous, String next, Tile[][] tiles, HashMap<Sprite, ContinuousPosition> sprites, List<Mover> movers) {
		this.name = name;
		this.time = time;
		this.previous = previous;
		this.next = next;
		this.tiles = tiles;
		this.sprites = sprites;
		this.movers = movers;
		this.width = tiles.length;
		this.height = tiles[0].length;
	}

	public Tile[][] getTiles() {
		Tile[][] tiles = new Tile[width][height];
		for(int i=0; i<width; i++) {
			for(int j=0; j<height; j++) {
				tiles[i][j] = this.tiles[i][j];
			}
		}
		return tiles;
	}
	public HashMap<Sprite, ContinuousPosition> getSprites() {
		HashMap<Sprite, ContinuousPosition> sprites = new HashMap<>();
		sprites.putAll(this.sprites);
		return sprites;
	}
	public List<Mover> getMovers() {
		return Collections.unmodifiableList(movers);
	}

	public static Level getLevel(String id) {
		List<String> lines = ResourceLoader.getLines("/levels/"+id+".lvl");

		String name = "";
		int time = 0;
		String next = "";
		String previous = "";
		int width = 0;
		int height = 0;
		Tile[][] tiles = null;
		HashMap<Sprite, ContinuousPosition> sprites = null;
		HashMap<Character, Tile> tileset = null;
		ArrayList<Mover> movers = null;
		char parsemode = 0;
		int tileRow = 0;

		for(String line: lines) {
			Matcher section = sectionPattern.matcher(line);
			if(section.matches()) {
				String secName = section.group(1);
				switch(secName) {
					case "data":
						parsemode = 'd';
						break;
					case "tileset":
						tileset = new HashMap<>();
						parsemode = 'T';
						break;
					case "tiles":
						tiles = new Tile[width][height];
						tileRow = height-1;
						parsemode = 't';
						break;
					case "sprites":
						sprites = new HashMap<>();
						parsemode = 's';
						break;
					case "dynamic":
						movers = new ArrayList<>();
						parsemode = 'D';
						break;
					default:
						throw new IllegalStateException("Invalid section: "+line+" in level "+id);
				}
				continue;
			}

			switch(parsemode) {
				case 'd': {
					Matcher kv = kvPattern.matcher(line);
					if(!kv.matches()) throw new IllegalStateException("Invalid line for section [data] in level "+id+": "+line);
					String key = kv.group(1);
					String value = kv.group(2);
					switch(key) {
						case "name": name = value; break;
						case "time": time = Integer.parseInt(value); break;
						case "width": width = Integer.parseInt(value); break;
						case "height": height = Integer.parseInt(value); break;
						case "next": next = value; break;
						case "previous": previous = value; break;
						default: throw new IllegalStateException("Invalid key for section [data] in level "+id+": "+key);
					}
					break;
				}

				case 'T': {
					Matcher kv = kvPattern.matcher(line);
					if(!kv.matches()) throw new IllegalStateException("Invalid line for section [tileset] in level "+id+": "+line);
					String key = kv.group(1);
					if(key.length() != 1) throw new IllegalStateException("Invalid key for tileset: "+key+" in level "+id);
					char k = key.charAt(0);
					String value = kv.group(2);
					Tile tile = Tile.valueOf(value);
					tileset.put(k, tile);
					break;
				}

				case 't': {
					char[] chars = line.toCharArray();
					if(chars.length != width) throw new IllegalStateException("Invalid number of tiles for height "+tileRow+" in level "+id+", line: "+line);
					for(int i=0; i<width; i++) {
						Tile tile = tileset.get(chars[i]);
						if(tile==null) throw new IllegalStateException("Tile "+chars[i]+" is not in tileset in level "+id);
						tiles[i][tileRow] = tile;
					}
					tileRow--;
					break;
				}

				case 's': {
					String[] info = line.split(" ");
					sprites.put(Sprite.get(info[0]), new ContinuousPosition(Double.parseDouble(info[1]), Double.parseDouble(info[2])));
					break;
				}

				case 'D': {
					String[] info = line.split(" ");
					ArrayList<String> fckJava = new ArrayList<>();
					for (int i = 0; i < info.length; i++) {
						if (i != 0) {
							fckJava.add(info[i]);
						}
					}
					movers.add(Mover.get(info[0], fckJava));
					break;
				}

				default: throw new IllegalStateException("Shouldn't be in state "+((char) parsemode)+" line: "+line+", in level "+id);
			}
		}

		return new Level(name, time, previous, next, tiles, sprites, movers);
	}

	private static final Pattern sectionPattern = Pattern.compile("^\\[(\\S+)\\]$");
	private static final Pattern kvPattern = Pattern.compile("^(.+?)=(.*)$");
}

package gyrolike.view;

import gyrolike.model.*;
import gyrolike.model.sprite.Sprite;
import gyrolike.util.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 800;
	public static final int TILESIZE = 20;

	@SuppressWarnings("all")
	private static void assertions() {
		if(WIDTH % TILESIZE != 0) throw new AssertionError("Width is not a multiple of tile size");
		if(HEIGHT % TILESIZE != 0) throw new AssertionError("Height is not a multiple of tile size");
	}
	static {
		assertions();
	}

	private Game game;
	private int scrollX;
	private Map<Tile, BufferedImage> imageCache = new HashMap<>();
	private Map<Class<? extends Sprite>, BufferedImage[]> spriteCache = new HashMap<>();

	public GamePanel(Game game) {
		super();
		this.game = game;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setMinimumSize(getPreferredSize());
		setMaximumSize(getPreferredSize());
	}

	@Override
	public void paintComponent(Graphics g_) {
		Graphics2D g = (Graphics2D) g_;

		int startTileX = scrollX / TILESIZE;
		int endTileX = (scrollX + WIDTH) / TILESIZE;
		if((scrollX + WIDTH) % TILESIZE != 0) endTileX += 1;

		for(int x=startTileX; x<endTileX; x++) {
			if(x >= game.getGrid().getWidth() || x < 0) continue;
			for(int y=0; y<HEIGHT/TILESIZE; y++) {
				if(y >= game.getGrid().getHeight()) continue;
				int scrX = x * TILESIZE - scrollX;
				int scrY = HEIGHT - y * TILESIZE - TILESIZE;

				g.drawImage(getImageForTile(game.getGrid().getTileAt(x, y)), scrX, scrY, null);
			}
		}

		for(Map.Entry<Sprite, ContinuousPosition> e: game.getGrid().getSprites().entrySet()) {
			int scrX = (int) (e.getValue().getX() * TILESIZE - scrollX);
			int scrY = (int) (HEIGHT - e.getValue().getY() * TILESIZE - TILESIZE);
			int w = (int) (e.getKey().getWidth() * TILESIZE);
			int h = (int) (e.getKey().getHeight() * TILESIZE);

			g.drawImage(getImageForSprite(e.getKey()), scrX, scrY, w, h, null);
		}
	}

	private BufferedImage getImageForTile(Tile tile) {
		BufferedImage img = imageCache.get(tile);
		if(img != null) return img;

		BufferedImage src = ResourceLoader.getImage("/tiles/"+tile.name().toLowerCase()+".png");
		img = new BufferedImage(TILESIZE, TILESIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.drawImage(src, 0, 0, TILESIZE, TILESIZE, null);
		g.dispose();
		imageCache.put(tile, img);
		return img;
	}

	private BufferedImage getImageForSprite(Sprite spr) {
		Class<? extends Sprite> clazz = spr.getClass();

		BufferedImage[] imgs = spriteCache.get(clazz);
		if(imgs != null) return imgs[spr.getImageId()];

		int sprImgCount = spr.getImageCount();
		imgs = new BufferedImage[sprImgCount];
		for(int i=0; i<sprImgCount; i++) imgs[i] = ResourceLoader.getImage("/sprites/"+clazz.getSimpleName().toLowerCase()+i+".png");
		spriteCache.put(clazz, imgs);
		return imgs[spr.getImageId()];
	}
}

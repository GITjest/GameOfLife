package game.graphics;

import java.util.Random;

import game.Game;

public class Screen {

	private int width, height;
	public int[] pixels;
	private Random random = new Random();
	private int[] tile;
	private int[] updatePixels;
	private int scale = 2;

	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		tile = new int[(width * height) / scale];
		updatePixels = new int[tile.length];
		for (int i = 0; i < tile.length; i++) {
			tile[i] = random.nextInt(2);
		}
	}

	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
		
		for (int i = 0; i < updatePixels.length; i++) {
			updatePixels[i] = 0;
		}
		
	}

	public void render() {
		update();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (updatePixels[x / scale + y / scale * height] == 1) {
					pixels[x + y * width] = 0xffffff;
				}
			}
		}
	}
	
	public void update() {
		for (int y = 2; y < height - 2; y++) {
			for (int x = 2; x < width - 2; x++) {
				int neighbors = 0;
				if (tile[(x - 2) / scale + (y - 2)/ scale * height] == 1) neighbors++;
				if (tile[x / scale + (y - 2)/ scale * height] == 1) neighbors++;
				if (tile[(x + 2) / scale + (y - 2)/ scale * height] == 1) neighbors++;
				if (tile[(x - 2) / scale + y/ scale * height] == 1) neighbors++;
				if (tile[(x + 2) / scale + y/ scale * height] == 1) neighbors++;
				if (tile[(x - 2) / scale + (y + 2)/ scale * height] == 1) neighbors++;
				if (tile[x / scale + (y + 2)/ scale * height] == 1) neighbors++;
				if (tile[(x + 2) / scale + (y + 2)/ scale * height] == 1) neighbors++;
					
				if (neighbors == 3) updatePixels[x / scale + y / scale * height] = 1;
				if (tile[x / scale + y / scale * height] == 1 && (neighbors == 2 || neighbors == 3)) updatePixels[x / scale + y / scale * height] = 1;
				
			}
		}
		
		for (int i = 0; i < tile.length; i++) {
			tile[i] = updatePixels[i];
		}
		
	}
}

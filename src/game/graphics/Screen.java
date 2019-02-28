package game.graphics;

import java.util.Random;

import game.Game;
import game.input.Mouse;

public class Screen {

	private int width, height;
	public int[] pixels;
	private Random random = new Random();
	private int[] tile;
	private int[] updatePixels;
	private int scale = 2;
	
	public static int zoom = 2;
	public static int currentMouseX = 0;
	public static int currentMouseY = 0;
	private static int previousMouseX = 0;
	private static int previousMouseY = 0;
	private double realPositionX, realPositionY;

	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		tile = new int[(width * height) / scale];
		updatePixels = new int[tile.length];
		
		for (int i = 0; i < tile.length; i++) {
			 tile[i] = random.nextInt(2);
		}
		/* test
		tile[50000] = 1;
		tile[50001] = 1;
		tile[50002] = 1;

		tile[32613] = 1;
		tile[32614] = 1;
		tile[32615] = 1;
		*/

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
		double scalePosition = 0.0;
		int shiftX = 0, shiftY = 0;
		
		for (int i = 2; i < zoom; i++) {
			scalePosition += 0.5;
		}

		realPositionX = ((previousMouseX - currentMouseX) / (0.5 + scalePosition));
		realPositionY = ((previousMouseY - currentMouseY) / (0.5 + scalePosition));

		realPositionX = previousMouseX - realPositionX;
		realPositionY = previousMouseY - realPositionY;							//rzeczywista pozycja wskazanego punktu

		shiftX = (int) ((realPositionX + ((realPositionX - currentMouseX) / (0.5 + scalePosition))) * scalePosition);
		shiftY = (int) ((realPositionY + ((realPositionY - currentMouseY) / (0.5 + scalePosition))) * scalePosition);
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (updatePixels[(x + shiftX) / zoom + (y + shiftY) / zoom * height] == 1) {
					pixels[x + y * width] = 0xffffff;
				}
			}
		}
		
		previousMouseX = currentMouseX = (int) (realPositionX + (realPositionX - currentMouseX) / (0.5 + scalePosition));
		previousMouseY = currentMouseY = (int) (realPositionY + (realPositionY - currentMouseY) / (0.5 + scalePosition));
	}
	

	public void update() {
		for (int y = 2; y < height - 2; y++) {
			for (int x = 2; x < width - 2; x++) {
				int neighbors = 0;
				if (tile[(x - 2) / scale + (y - 2) / scale * height] == 1)
					neighbors++;
				if (tile[x / scale + (y - 2) / scale * height] == 1)
					neighbors++;
				if (tile[(x + 2) / scale + (y - 2) / scale * height] == 1)
					neighbors++;
				if (tile[(x - 2) / scale + y / scale * height] == 1)
					neighbors++;
				if (tile[(x + 2) / scale + y / scale * height] == 1)
					neighbors++;
				if (tile[(x - 2) / scale + (y + 2) / scale * height] == 1)
					neighbors++;
				if (tile[x / scale + (y + 2) / scale * height] == 1)
					neighbors++;
				if (tile[(x + 2) / scale + (y + 2) / scale * height] == 1)
					neighbors++;

				if (neighbors == 3)
					updatePixels[x / scale + y / scale * height] = 1;
				if (tile[x / scale + y / scale * height] == 1 && (neighbors == 2 || neighbors == 3))
					updatePixels[x / scale + y / scale * height] = 1;

			}
		}

		for (int i = 0; i < tile.length; i++) {
			tile[i] = updatePixels[i];
		}

	}
	
	public void updateStop() {

		for (int i = 0; i < tile.length; i++) {
			updatePixels[i] = tile[i];
		}

	}
}

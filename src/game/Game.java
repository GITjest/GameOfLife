package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import game.graphics.Screen;
import game.input.Mouse;

public class Game extends Canvas implements Runnable {

	private static int width = 1920;
	private static int height = width / 16 * 9;
	private static int scale = 1;
	private static String title = "Game Of Life";

	private Thread thread;
	private JFrame frame;
	private boolean running = false;

	private Screen screen;

	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	public Game() {
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);

		frame = new JFrame();
		screen = new Screen(width, height);

		Mouse mouse = new Mouse();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		addMouseWheelListener(mouse);
	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}

	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		while (running) {
			frame.requestFocus();
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				render();
				updates++;
				delta--;
			}
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frame.setTitle(title + " | " + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
	}

	public void update() {

	}
	boolean stop = true;

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		if (Mouse.mouseW == 1 && Screen.zoom > 2) {
			Screen.zoom--;
			Mouse.mouseW = 0;
		}
		
		if (Mouse.mouseW == -1) {
			Screen.zoom++;
			Screen.currentMouseX = Mouse.getX();
			Screen.currentMouseY = Mouse.getY();
			Mouse.mouseW = 0;
		}
		screen.clear();
		if (stop) screen.update();
		else screen.updateStop();
		screen.render();
		stop = false;
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.setColor(Color.RED);
		g.setFont(new Font("Verdena", 0, 50));
		//g.fillRect(Mouse.getX() - 32, Mouse.getY() - 32, 64, 64);
		g.drawString("x: " + Mouse.getX() + " | y: " + Mouse.getY(), 80, 160);
		//g.drawString("Button: " + Mouse.getButton(), 80, 80);
		//g.drawString("Wheel: " + Mouse.getMouseW(), 80, 160);
		g.dispose();
		bs.show();
		// Mouse.mouseW = 0;
	}

	public static void main(String[] args) {
		Game game = new Game();
		//game.frame.setResizable(true);
		game.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		game.frame.setTitle(Game.title);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);

		game.start();

	}

}

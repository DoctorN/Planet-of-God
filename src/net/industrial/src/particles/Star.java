package net.industrial.src.particles;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

public class Star {

	private float x, y;
	private Polygon star;
	private Color color;
	private float velocity;
	private int size;
	
	public Star(GameContainer gc) {
		
		Random random = new Random();
		
		this.x = random.nextInt(gc.getWidth());
		this.y = random.nextInt(gc.getHeight());
		
		size = (random.nextInt(3) + 1) * 2;
		
		velocity = (float) random.nextInt(10) / 1000f;
		
		color = new Color(1, 1, 1, 1 - 1 / (velocity * 500));
		
		setPolygon();
		
	}
	
	public void setPolygon() {

		star = new Polygon();
		
		star.addPoint(x, y - size / 2);
		star.addPoint(x + size / 2, y + size / 2);
		star.addPoint(x - size / 2, y + size / 2);
		
	}
	
	public void update(GameContainer gc, int delta) {
		
		this.x += velocity * delta;
		if (this.x > gc.getWidth()) this.x = 0;
		
		setPolygon();
		
	}
	
	public void render(GameContainer gc, Graphics g) {
		
		g.setColor(color);
		g.fill(star);
		
	}
	
}

package net.industrial.src.particles;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;

public class Particle {

	private float lifetime, age = 0f;
	private Vector2f velocity;
	private Polygon shape;
	private float x, y;
	private float red, green, blue;
	
	private boolean dead = false;
	
	public Particle(float x, float y, float size, float r, float g, float b, float lifetime, float velocityx, float velocityy) {
		
		Random random = new Random();
		
		float colorVar = - 0.05f + (float) random.nextInt(100) / 1000;
		
		this.x = x - 0.05f + (float) random.nextInt(100) / 1000;
		this.y = y - 0.05f + (float) random.nextInt(100) / 1000;
		this.red = r + colorVar;
		this.green = g + colorVar;
		this.blue = b + colorVar;
		
		this.lifetime = lifetime;
		
		this.velocity = new Vector2f(velocityx - 0.025f + (float) random.nextInt(50) / 1000, velocityy - 0.025f + (float) random.nextInt(50) / 1000);
		
		shape = new Polygon();
		
		shape.addPoint(x, y - size / 2);
		shape.addPoint(x + size / 2, y + size / 2);
		shape.addPoint(x - size / 2, y + size / 2);
		
	}
	
	public void render(Graphics g, GameContainer gc) {
		
		Color color = new Color(red, green, blue, (1 - age / lifetime));
		g.setColor(color);
		g.fill(shape);
		
	}
	
	public void update(GameContainer gc, int delta) {
		
		age += delta;
		
		if (age > lifetime) dead = true;
		else {
			
			this.x += velocity.x * delta;
			this.y += velocity.y * delta;
			
			shape.setCenterX(x);
			shape.setCenterY(y);
			
		}
		
	}
	
	public boolean isDead() {
		
		return dead;
		
	}
	
}

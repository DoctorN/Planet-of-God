package net.industrial.src.planet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.industrial.src.SoundBank;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Transform;

public class Cloud {

	private Polygon shape;
	private float speed, alpha = 0f, angle = 0f;
	private Color color;
	private boolean disappating = false, dead = false, raining = false, storm = false, lightning = false;
	private float lightningCool = 0f;
	private float heightRandom, y;
	private Planet planet;
	
	private List<Polygon> droplets;
	private Polygon bolt;
	
	public Cloud(float y, Planet planet) {
		
		color = new Color(230, 230, 230);
		color.a = alpha;
		
		this.y = y;
		
		this.planet = planet;
		
		shape = new Polygon();
		
		Random random = new Random();
		
		heightRandom = random.nextInt(50);
		
		shape.addPoint(Planet.CENTERX - 100, Planet.CENTERY - y + 8 - heightRandom);
		shape.addPoint(Planet.CENTERX,  Planet.CENTERY - y - heightRandom);
		shape.addPoint(Planet.CENTERX + 100,  Planet.CENTERY - y + 8 - heightRandom);
		
		int displacement = random.nextInt(80) + 10;
		int dif = random.nextInt(50) + 50;
		
		shape.addPoint(Planet.CENTERX + 100 - displacement, Planet.CENTERY - y - 20 - random.nextInt(5) - heightRandom);
		shape.addPoint(Planet.CENTERX + 100 - displacement - dif / 2, Planet.CENTERY - y - 40 - random.nextInt(5) - heightRandom);
		shape.addPoint(Planet.CENTERX + 100 - displacement - dif, Planet.CENTERY - y - 20 - random.nextInt(5) - heightRandom);
		
		speed = random.nextInt(2) + 1;
		angle = random.nextInt(500);
		
		shape = (Polygon) shape.transform(Transform.createRotateTransform(angle, Planet.CENTERX, Planet.CENTERY));
		
		droplets = new ArrayList<Polygon>();
		
	}
	
	public void rain() {
		
		raining = true;
		
	}
	
	public void render(GameContainer gc, Graphics g) {
		
		if (raining) {
			
			g.setColor(new Color(48, 113, 151, 200));
			for (Polygon r : droplets) g.fill(r);
			
		} 
		
		if (lightning && storm) {
			
			g.setColor(Color.white);
			g.setLineWidth(1f);
			if (lightningCool < 100) g.draw(bolt.transform(Transform.createRotateTransform(angle, Planet.CENTERX, Planet.CENTERY)));
			
		}
		
		g.setColor(color);
		g.fill(shape);
		
	}
	
	public void update(GameContainer gc, int delta) {
		
		if (!dead) {
		
			shape = (Polygon) shape.transform(Transform.createRotateTransform(0.000015f * delta * speed, Planet.CENTERX, Planet.CENTERY));
			angle += 0.000015f * delta * speed;
			
			if (disappating) {
				
				alpha -= 0.0003f * delta;
				
				if (alpha < 0) this.dead = true;
				
			} else {
				
				if (alpha < 0.9f) alpha += 0.0003f * delta;
				
			}
			
			color.a = alpha;
			
			Random random = new Random();
			
			if (raining && alpha >= 0.9f) {
				
				if (random.nextInt(5) == 0) {
						
					int translate = -50 + random.nextInt(100);
					
					Polygon drop = new Polygon();
					drop.addPoint(Planet.CENTERX + translate, Planet.CENTERY - y - 3 - heightRandom);
					drop.addPoint(Planet.CENTERX + 3 + translate, Planet.CENTERY - y + 3 - heightRandom);
					drop.addPoint(Planet.CENTERX - 3 + translate, Planet.CENTERY - y + 3 - heightRandom);
					
					drop = (Polygon) drop.transform(Transform.createRotateTransform(angle, Planet.CENTERX, Planet.CENTERY));
					droplets.add(drop);
					
				}
				
				for (Polygon r : droplets) {
					
					double a = r.getCenterX() - Planet.CENTERX;
					double b = r.getCenterY() - Planet.CENTERY;
					double c = Math.sqrt(a * a + b * b);
					
					double sf = 0.2f / c;
					
					float x = - (float) (a * sf);
					float y = - (float) (b * sf);
					
					r.setCenterX(r.getCenterX() + x * delta);
					r.setCenterY(r.getCenterY() + y * delta);
					
				}
					
				for (int i = 0; i < droplets.size(); i++) if (planet.water.contains(droplets.get(i).getCenterX(), droplets.get(i).getCenterY()) ||
						planet.planet.contains(droplets.get(i).getCenterX(), droplets.get(i).getCenterY())) {
					
					droplets.remove(i);
					SoundBank.RAIN.play(1f, 0.5f);
					
				}
				
				planet.incrementOcean(delta / 20, true);
				planet.incrementCO2((int) Math.round(delta / 25), false);
				
			} 
			
			if (storm && alpha >= 0.9f) {
				
				if (random.nextInt(10) == 0 && !lightning) {
					
					lightning = true;
					
					int translate = - 25 + random.nextInt(50);
					float distance = y + heightRandom;
					float travelled = 0f;
					
					bolt = new Polygon();
					
					bolt.addPoint(Planet.CENTERX  + translate, Planet.CENTERY - y - heightRandom + 5);
					
					while (travelled < distance) {
						
						travelled += random.nextInt(20) + 10;
						bolt.addPoint(Planet.CENTERX + translate - 5 + random.nextInt(10), Planet.CENTERY - y - heightRandom + travelled + 5);
						
					}
					
					while (travelled > 0) {
						
						travelled -= random.nextInt(20) + 10;
						bolt.addPoint(Planet.CENTERX + translate - 5 + random.nextInt(10), Planet.CENTERY - y - heightRandom + travelled + 5);
						
					}
					
					bolt.addPoint(Planet.CENTERX + translate, Planet.CENTERY - y - heightRandom + 5);
					
					planet.boltCollision();
					
				} else if (lightning) {
					
					lightningCool += delta;
					if (lightningCool > 700) {
						
						lightning = false;
						lightningCool = 0;
						
					}
					
				}
				
			}
			
		}
		
	}
	
	public void dissipate() {
		
		disappating = true;
		raining = false;
		
	}
	
	public boolean isDead() {
		
		return dead;
		
	}
	
	public void storm() {
		
		storm = true;
		lightning = false;
		
	}

	public boolean isDissipating() {
		
		return disappating;
		
	}

	public void stopRain() {
		
		raining = false;
		
	}

	public void disableStorm() {
		
		storm = false;
		lightning = false;
		
	}
	
}

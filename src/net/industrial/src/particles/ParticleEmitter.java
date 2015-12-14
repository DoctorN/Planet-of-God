package net.industrial.src.particles;

import java.util.ArrayList;
import java.util.List;

import net.industrial.src.planet.Planet;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

public class ParticleEmitter {
	
	private float x, y; 
	private boolean enabled = true;

	private float red = 0f, green = 0f, blue = 0f, size = 5;
	private Vector2f velocity = new Vector2f(0, 1);
	private float lifetime = 500f;
	private float emissionRate = 1, coolDown = 0f;
	
	private List<Particle> particles;
	
	public ParticleEmitter(float x, float y) {
		
		this.x = x;
		this.y = y;
		
		particles = new ArrayList<Particle>();
		
	}
	
	public ParticleEmitter(ParticleEmitter trail) {
		
		this.x = trail.x;
		this.y = trail.y;
		this.velocity = trail.velocity;
		this.red = trail.red;
		this.green = trail.green;
		this.blue = trail.blue;
		this.lifetime = trail.lifetime;
		this.emissionRate = trail.emissionRate;
		this.enabled = trail.enabled;
		
		particles = new ArrayList<Particle>();
		
	}

	public void update(GameContainer gc, int delta) {
		
		if (enabled) {
		
			coolDown += delta;
			if (coolDown > 1000 / emissionRate) {
				
				coolDown -= 1000 / emissionRate;
				Particle p = new Particle(x, y, size, red, green, blue, lifetime, velocity.x, velocity.y);
				particles.add(p);
				
			}
		
		}
		
		List<Particle> toRemove = new ArrayList<Particle>();
		
		for (Particle p : particles) {
			
			p.update(gc, delta);
			if (p.isDead()) toRemove.add(p);
			
		}
		
		for (Particle p : toRemove) particles.remove(p);
		
	}
	
	public void render(Graphics g, GameContainer gc) {
		
		for (Particle p : particles) p.render(g, gc);
		
	}
	
	public void setLocaton(float x, float y) {
		
		this.x = x;
		this.y = y;
		
	}
	
	public void setEmissionRate(float rate) {
		
		this.emissionRate = rate;
		
	}
	
	public void setLifetime(float lifetime) {
		
		this.lifetime = lifetime;
		
	}
	
	public void setSize(float size) {
		
		this.size = size;
		
	}
	
	public void setColor(Color color) {
		
		this.red = color.r;
		this.green = color.g;
		this.blue = color.b;
		
	}
	
	public void setVelocity(Vector2f velocity) {
		
		this.velocity = velocity;
		
	}
	
	public void setVelocityFromPlanet(float speed) {
		
		double a = this.x - Planet.CENTERX;
		double b = this.y - Planet.CENTERY;
		double c = Math.sqrt(a * a + b * b);
		
		double sf = speed / c;
		
		float x = (float) (a * sf);
		float y = (float) (b * sf);
		
		this.velocity = new Vector2f(x, y);
		
	}
	
	public void toggle() {
		
		enabled = !enabled;
		
	}
	
	public void disable() {
		
		enabled = false;
		
	}
	
	public void enable() {
		
		enabled = true;
		
	}
	
	public List<Particle> getParticles() {
		
		return this.particles;
		
	}
	
}

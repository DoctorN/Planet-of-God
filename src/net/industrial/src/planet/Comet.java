package net.industrial.src.planet;

import net.industrial.src.particles.ParticleEmitter;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

public class Comet {

	private Planet planet;
	private Vector2f velocity;
	private Polygon shape, explosion;
	private float x, y;
	private boolean dead = false, dying = false;
	private ParticleEmitter trail;
	private float cool = 0f;
	
	public Comet(Planet planet, float x, float y) {
		
		if (!dead) {
			
			this.planet = planet;
			
			this.x = x;
			this.y = y;
			
			this.shape = PlanetFactory.convertToPolygon(PlanetFactory.genNormalDeformedPolygon(10, 9), x, y);
			this.explosion = PlanetFactory.convertToPolygon(PlanetFactory.genPolygon(80, 16), x, y);
			
			trail = new ParticleEmitter(x, y);
			trail.setVelocityFromPlanet(0.01f);
			trail.setLifetime(2500f);
			trail.setSize(8);
			trail.setEmissionRate(50);
			trail.setColor(new Color(255, 210, 210));
			
			double a = this.x - Planet.CENTERX;
			double b = this.y - Planet.CENTERY;
			double c = Math.sqrt(a * a + b * b);
			
			double sf = 0.3f / c;
			
			float vx = - (float) (a * sf);
			float vy = - (float) (b * sf);
			
			this.velocity = new Vector2f(vx, vy);
			
		}
		
	}
	
	public void render(Graphics g, GameContainer gc) {
		
		trail.render(g, gc);
		
		if (!dying) {
			
			g.setColor(new Color(80, 80, 80));
			g.fill(shape);
			
		} else if (cool < 100) {
			
			if (cool < 50) g.setColor(Color.white);
			else g.setColor(Color.black);
			
			g.fill(explosion);
			
		}
		
	}
	
	public void update(GameContainer gc, int delta) {
		
		trail.update(gc, delta);
		
		if (!dying) {
		
			this.x += this.velocity.x * delta;
			this.y += this.velocity.y * delta;
			
			shape.setCenterX(x);
			shape.setCenterY(y);
			explosion.setCenterX(x);
			explosion.setCenterY(y);
			
			shape = (Polygon) shape.transform(Transform.createRotateTransform(0.1f * delta, x, y));
			
			trail.setLocaton(x, y);
			
			if (this.shape.intersects(planet.planet)) {
				
				planet.cometCollision();
				this.dying = true;
				trail.disable();
				
			}
			
		} else {
			
			cool += delta;
			if (trail.getParticles().size() == 0) this.dead = true;
			
		}
		
	}
	
	public boolean isDead() {
		
		return dead;
		
	}
	
}

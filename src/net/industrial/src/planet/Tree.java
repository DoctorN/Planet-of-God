package net.industrial.src.planet;

import java.util.List;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Polygon;

public class Tree {

	private Polygon trunk, leaves;
	
	public Tree(List<Point> surface, Planet planet) {
		
		Random random = new Random();
		
		while (trunk == null) {
		
			for (Point p : surface) {
				
				if (p.getX() * p.getX() + p.getY() * p.getY() >= (Planet.RADIUS) * (Planet.RADIUS) && random.nextInt(25) == 0) {
					
					trunk = new Polygon();
					leaves = new Polygon();
					
					double alpha = (Math.PI / 2) - Math.atan(p.getY() / p.getX());
					
					trunk.addPoint(Planet.CENTERX + p.getX() * 1.05f, Planet.CENTERY + p.getY() * 1.05f);
					
					float x = (float) (30 * Math.cos(alpha));
					float y = (float) (30 * Math.sin(alpha));
					
					trunk.addPoint(Planet.CENTERX - x, Planet.CENTERY + y);
					trunk.addPoint(Planet.CENTERX + x, Planet.CENTERY - y);
					
					leaves = PlanetFactory.convertToPolygon(PlanetFactory.genDeformedPolygon(5, 5), Planet.CENTERX + p.getX() * 1.05f, Planet.CENTERY + p.getY() * 1.05f);
					
					
				}
				
			}
		
		}
		
	}
	
	public void render(GameContainer gc, Graphics g) {
		
		g.setColor(new Color(99, 46, 31));
		g.fill(trunk);
		
		g.setColor(new Color(114, 144, 31));
		g.fill(leaves);
		
	}
	
}

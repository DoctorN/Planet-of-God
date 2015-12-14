package net.industrial.src.planet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.industrial.src.particles.ParticleEmitter;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Polygon;

public class PlanetFactory {

	public static List<Point> genPolygon(double radius, int point) {
		
		List<Point> points = new ArrayList<Point>();
		double alpha = (2 * point - 2) * 2 * Math.PI /(point * 2);
		
		for (int i = 0; i < point; i++) {
			
			double x;
			double y;
			
			x = radius * Math.sin(alpha * i);
			y = radius * Math.cos(alpha * i);
			
			points.add(new Point((float) x, (float) y));
			
		}
		
		return points;
		
	}
	
	public static List<Point> genDeformedPolygon(double radius, int point) {
		
		List<Point> points = new ArrayList<Point>();
		double alpha = (2 * point - 2) * 2 * Math.PI / (point * 2);
		
		Random random = new Random();
		boolean last = false, ocean = false;
		int oceanCount = 0, oceanMax = 0;
		
		for (int i = 0; i < point; i++) {
			
			double x;
			double y;
			double temporaryRadius = radius * (double)(random.nextInt(5) + 95) / 100;
			
			if (!ocean) {
				
				if (random.nextInt(25) == 0) {
					
					ocean = true;
					oceanCount = 1;
					oceanMax = random.nextInt(7);
					
					temporaryRadius *= (double)(random.nextInt(5) + 90) / 100;
					
				} else if (random.nextInt(3) == 0 || (last && random.nextInt(2) == 0)) {
					
					temporaryRadius *= (double)(random.nextInt(5) + 105) / 100;
					last = true;
					
				} else last = false;
				
			} else {
				
				temporaryRadius *= (double)(random.nextInt(5) + 90) / 100;
				oceanCount ++;
				if (oceanCount == oceanMax) ocean = false;
				
			}
			
			x = temporaryRadius * Math.sin(alpha * i);
			y = temporaryRadius * Math.cos(alpha * i);
			
			points.add(new Point((float) x, (float) y));
			
		}
		
		return points;
		
	}
	
	public static Polygon convertToPolygon(List<Point> points, float centerx, float centery) {
		
		Polygon polygon = new Polygon();
		
		for (Point p : points) {
			
			float x = p.getX();
			float y = p.getY();
			
			polygon.addPoint(x + centerx, y + centery);
			
		}
		
		return polygon;
		
	}

	public static List<Point> genNormalDeformedPolygon(double radius, int point) {
		
		List<Point> points = new ArrayList<Point>();
		double alpha = (2 * point - 2) * 2 * Math.PI / (point * 2);
		
		Random random = new Random();
		
		for (int i = 0; i < point; i++) {
			
			double x;
			double y;
			double temporaryRadius = radius * (double)(random.nextInt(5) + 95) / 100;
			
			x = temporaryRadius * Math.sin(alpha * i);
			y = temporaryRadius * Math.cos(alpha * i);
			
			points.add(new Point((float) x, (float) y));
			
		}
		
		return points;
		
	}
	
	public static List<Point> genVolcanoeSpotsFor(List<Point> points) {
		
		int i = 0;
		Random random = new Random();
		List<Point> tempPoints = new ArrayList<Point>(points);
		List<Point> volcanoes = new ArrayList<Point>();
		
		int loopCounter = 0;
		
		while (i < 5 + random.nextInt(3)) {

			List<Point> toRemove = new ArrayList<Point>();
			
			for (Point p : tempPoints) {
			
				if (random.nextInt(32) == 0 && (p.getX() * p.getX() + p.getY() * p.getY()) >= 0.95 * (Planet.RADIUS) * (Planet.RADIUS)) {
					
					volcanoes.add(p);
					toRemove.add(p);
				
					i++;
					
				}
				
			}
			
			for (Point p : toRemove)  tempPoints.remove(p);
			
			loopCounter++;
			
			if (loopCounter > 10) return volcanoes;
			
		}
		
		return volcanoes;
			
	}
	
	public static List<ParticleEmitter> genVolcanoeEmittersFor(List<Point> points, float centerx, float centery) {
		
		List<ParticleEmitter> emitters = new ArrayList<ParticleEmitter>();
		
		for (Point p : points) {
			
			ParticleEmitter emitter = new ParticleEmitter(p.getX() + centerx, p.getY() + centery);
			emitter.setVelocityFromPlanet(0.03f);
			emitter.setLifetime(1500f);
			emitter.setSize(12);
			emitter.setEmissionRate(10);
			emitter.setColor(new Color(60, 60, 60));
			emitter.disable();
			emitters.add(emitter);
			
		}
		
		return emitters;
		
	}

	public static List<Polygon> genVolcanoesFor(List<Point> points, float centerx, float centery) {
		
		List<Polygon> volcanoes = new ArrayList<Polygon>();
		
		for (Point p : points) {
			
			Polygon flow = new Polygon();
			flow.addPoint(centerx + p.getX(), centery + p.getY());
			
			double theta = (Math.PI / 2) - Math.atan(p.getY() / p.getX());
			
			float x = (float) (50 * Math.cos(theta));
			float y = (float) (50 * Math.sin(theta));
			
			flow.addPoint(centerx - x, centery + y);
			flow.addPoint(centerx + x, centery - y);
			
			volcanoes.add(flow);
			
			
		}
		
		return volcanoes;
		
	}
	
}

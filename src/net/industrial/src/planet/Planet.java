package net.industrial.src.planet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.industrial.src.Game;
import net.industrial.src.SoundBank;
import net.industrial.src.particles.ParticleEmitter;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Polygon;

public class Planet {
	
	Polygon planet, water, innerCore, outerCore, mantel, atmosphere, outerAtmosphere;
	List<Point> points, volcanoePoints;
	List<Polygon> volcanoes, craters;
	List<ParticleEmitter> emitters;
	List<Comet> comets;
	List<Cloud> clouds;
	List<Tree> treeObjects;
	
	private float angle = 0, o2Volume, co2Volume, h2oVolume, temperature, waterPercentage, vapourPercentage = 100, vegPercentage = 0;
	private boolean shaking = false, cometsEnabled = false, volcanoesEnabled = false, life = false, raining = false, waterStarted = false, atmosStarted = false;
	private float shakeTime = 0f, cool = 0f, lifeStart = 0, atmosStart = 0, waterStart = 0;
	
	private int trees = 0, fish = 0, birds = 0, sheep = 0, wolves = 0;
	
	public static int RADIUS = 300;
	public static float CENTERX, CENTERY;
	
	private boolean displayMode = false;
	
	public Planet(float centerx, float centery) {
		
		CENTERX = centerx;
		CENTERY = centery;
		
		points = PlanetFactory.genDeformedPolygon(RADIUS, 128);
		planet = PlanetFactory.convertToPolygon(points, CENTERX, CENTERY);
		innerCore = PlanetFactory.convertToPolygon(PlanetFactory.genPolygon(RADIUS / 5,  12), CENTERX, CENTERY);
		outerCore = PlanetFactory.convertToPolygon(PlanetFactory.genNormalDeformedPolygon(RADIUS / 2.5, 16), CENTERX, CENTERY);
		mantel = PlanetFactory.convertToPolygon(PlanetFactory.genNormalDeformedPolygon(RADIUS / 1.2, 64), CENTERX, CENTERY);
		
		volcanoePoints = new ArrayList<Point>();
		
		while (volcanoePoints.size() < 5) {

			points = PlanetFactory.genDeformedPolygon(RADIUS, 128);
			planet = PlanetFactory.convertToPolygon(points, CENTERX, CENTERY);
			volcanoePoints = PlanetFactory.genVolcanoeSpotsFor(points);
			
		}
		
		volcanoes = PlanetFactory.genVolcanoesFor(volcanoePoints, CENTERX, CENTERY);
		emitters = PlanetFactory.genVolcanoeEmittersFor(volcanoePoints, CENTERX, CENTERY);
		
		comets = new ArrayList<Comet>();
		clouds = new ArrayList<Cloud>();
		treeObjects = new ArrayList<Tree>();
		
		Random random = new Random();
		
		craters = new ArrayList<Polygon>();
		
		for (int i = 0; i < 30; i++) {
			
			Polygon crater = PlanetFactory.convertToPolygon(PlanetFactory.genDeformedPolygon(random.nextInt(15) + 20, random.nextInt(15) + 20),
					centerx - RADIUS * 4 / 5 + random.nextInt(RADIUS * 8 / 5), 
					centery - RADIUS * 4 / 5 + random.nextInt(RADIUS * 8 / 5));
			
			while (intersectsCrater(crater)) {
				
				crater = PlanetFactory.convertToPolygon(PlanetFactory.genDeformedPolygon(random.nextInt(15) + 20, random.nextInt(15) + 20),
						centerx - RADIUS * 4 / 5 + random.nextInt(RADIUS * 8 / 5), 
						centery - RADIUS * 4 / 5 + random.nextInt(RADIUS * 8 / 5));
				
			}
			
			craters.add(crater);
			
		}
		
		setH2OVolume(0);
		setO2Volume(0);
		setCO2Volume(0);
		setTemperature(600);
		setOceanPercentage(0);
		
	}
	
	public void incrementTrees(boolean up) {
		
		if (up) {
		
			trees += 1;
			treeObjects.add(new Tree(points, this));
			
		} else {
			
			Random random = new Random();
			trees -= 1;
			treeObjects.remove(random.nextInt(treeObjects.size()));
			
		}
		
	}
	
	public boolean intersectsCrater(Polygon p) {
		
		for (Polygon crater : craters) {
			
			if (p.intersects(crater) || crater.includes(p.getCenterX(), p.getCenterY())) return true;
			
		}
		
		if (p.intersects(planet)) return true;
		
		return false;
		
	}
	
	public void setVegPercentage(float percentage) {
		
		if (percentage < 0) percentage = 0;
		if (percentage < 100) this.vegPercentage = percentage;
		else this.vegPercentage = 100;
		
	}
	
	public void incrementVeg(int delta, boolean up) {
		
		if (up) this.setVegPercentage(vegPercentage + 0.01f * delta); 
		else this.setVegPercentage(vegPercentage - 0.01f * delta);
		
	}
	
	public void setTemperature(float temperature) {
		
		if (temperature < -273) temperature = -273;
		
		if (temperature < 2000) this.temperature = temperature;
		else this.temperature = 2000;
		
	}
	
	public void incrementTemperature(int delta, boolean up) {
		
		if (up) this.setTemperature(temperature + 0.01f * delta); 
		else this.setTemperature(temperature - 0.01f * delta);
		
	}
	
	public void setH2OVolume(float level) {
		
		if (level < 0) level = 0;
		
		if (level < 200) h2oVolume = level;
		else h2oVolume = 200;
		setWaterAndClouds();
		
		if (!waterStarted && h2oVolume > 0) waterStarted = true;
		
	}
	
	public void incrementH2OVolume(int delta, boolean up) {
		
		if (up) this.setH2OVolume(h2oVolume + 0.01f * delta); 
		else this.setH2OVolume(h2oVolume - 0.01f * delta);
		
	}
	
	public void setWaterAndClouds() {
		
		water = PlanetFactory.convertToPolygon(PlanetFactory.genPolygon(RADIUS - 50 + h2oVolume * waterPercentage / 350, 128), CENTERX, CENTERY);
		
		int cloudNo = (int) (vapourPercentage * h2oVolume) / 1000;
		
		if (clouds.size() < cloudNo && this.atmosphereLevel() > 100) {
			
			while (clouds.size() < cloudNo) clouds.add(new Cloud(350, this));
			
		} else if (clouds.size() > cloudNo) {
			
			Random random = new Random();
			int count = 0;
			
			for (Cloud c : clouds) {
				
				if (c.isDissipating()) cloudNo += 1;
				
			}
			
			while (clouds.size() - cloudNo > count) {
				
				clouds.get(random.nextInt(clouds.size())).dissipate();
				count += 1;
				
			}
			
		}
		
	}
	
	public void setOceanPercentage(float level) {
		
		if (level < 0) level = 0;
		
		if (level < 100) {
			
			waterPercentage = level;
			vapourPercentage = 100 - level;
			
		}
		
		setWaterAndClouds();
		
	}
	
	public void incrementOcean(int delta, boolean up) {
		
		if (up) this.setOceanPercentage(waterPercentage + 0.01f * delta); 
		else this.setOceanPercentage(waterPercentage - 0.01f * delta); 
		
	}
	
	public void setCO2Volume(float level) {
		
		if (level < 0) level = 0;
		
		if ((level + o2Volume) < RADIUS) this.co2Volume = level;
		else this.co2Volume = RADIUS - o2Volume;
		setAtmosphereLevel();
		
	}
	
	public void setO2Volume(float level) {
		
		if (level < 0) level = 0;
		
		if ((level + co2Volume) < RADIUS) this.o2Volume = level;
		else this.o2Volume = RADIUS - co2Volume;
		setAtmosphereLevel();
		
		if (!atmosStarted && this.atmosphereLevel() > 0) atmosStarted = true;
		
	}
	
	public void incrementCO2(int delta, boolean up) {
		
		if (up) setCO2Volume(co2Volume + 0.001f * delta);
		else setCO2Volume(co2Volume - 0.001f * delta);
		
		if (!atmosStarted && this.atmosphereLevel() > 0) atmosStarted = true;
		
	}
	
	public void incrementO2(int delta, boolean up) {
		
		if (up) setO2Volume(o2Volume + 0.001f * delta);
		else setO2Volume(o2Volume - 0.001f * delta);
		
	}
	
	public void setAtmosphereLevel() {
		
		atmosphere = PlanetFactory.convertToPolygon(PlanetFactory.genPolygon(RADIUS * 0.75 + atmosphereLevel(), 32), CENTERX, CENTERY);
		outerAtmosphere = PlanetFactory.convertToPolygon(PlanetFactory.genPolygon(RADIUS * 0.75 + atmosphereLevel() + RADIUS * 0.4, 32), CENTERX, CENTERY);
		
	}
	
	public float atmosphereLevel() {
		
		return o2Volume + co2Volume;
		
	}
	
	public void spawnComet(GameContainer gc) {
		
		Random random = new Random();
		Comet comet;
		
		if (random.nextBoolean()) {
		
			if (random.nextBoolean()) comet = new Comet(this, random.nextInt(gc.getWidth()), gc.getHeight() + 30);
			else comet = new Comet(this, random.nextInt(gc.getWidth()), -30);
		
		} else {
			
			if (random.nextBoolean()) comet = new Comet(this, gc.getWidth() + 30, random.nextInt(gc.getHeight()));
			else comet = new Comet(this, -30, random.nextInt(gc.getHeight()));
			
		}
		
		comets.add(comet);
		
	}
	
	public void update(GameContainer gc, int delta) {
		
		if ((new Random()).nextInt(100 / delta) == 0 && cometsEnabled) spawnComet(gc);
		
		for (Comet comet : comets) comet.update(gc, delta);
		for (int i = 0; i < comets.size(); i++) {
			
			if (comets.get(i).isDead()) {
				
				comets.remove(i);
				i--;
				
			}
			
		}
		
		if (temperature < 100 && raining) {
			
			for (Cloud c : clouds) c.rain();
			
		} else {
			
			for (Cloud c : clouds) c.stopRain();
			
		}
		
		if (shaking) {
			
			if (cometsEnabled) cool += delta;
			else cool += delta * 2;
			if (cool > shakeTime) {
				
				shaking = false;
				cool = 0f;
				
			}
			
		}
		
		angle += 0.0001f * delta;
		if (angle > Math.PI * 2) angle -= Math.PI * 2;
		
		for (ParticleEmitter e : emitters) e.update(gc, delta);
		for (Cloud c : clouds) c.update(gc, delta);
		
		for (int i = 0; i < clouds.size(); i++) {
			
			if (clouds.get(i).isDead()) {
				
				clouds.remove(i);
				i -= 1;
				
			}
			
		}
		
		if (!displayMode) {
			
			if (temperature > 0) this.incrementTemperature((int) Math.round(delta * 4 * (temperature - 10) / 1990f), false);
			if (volcanoesEnabled) this.incrementCO2((int) Math.round(delta * 1.1f), true);
			if (atmosphereLevel() < 100) this.incrementH2OVolume(delta / 5, false);
			else incrementOcean(delta * (int) Math.round(temperature / 300), false);
			if (temperature > 100) {
				
				this.incrementOcean(delta / 5, false);
				this.incrementCO2((int) Math.round(delta / 125), true);
				
			}
			
			if (life && temperature >= 20 && temperature <= 45 && h2oVolume > 100 && this.co2Volume > 0) {
				
				this.incrementVeg(delta, true);
				this.incrementCO2((int) Math.round(delta * this.vegPercentage / 100), false);
				this.incrementO2((int) Math.round(delta * this.vegPercentage / 100), true);
				
				this.incrementCO2((int) Math.round(delta * trees / 100), false);
				this.incrementO2((int) Math.round(delta * trees / 100), true);
				
				if (this.vegPercentage > 50) {
					
					Random random = new Random();
					
					if (trees < 40 && random.nextInt(32) == 0) {
						
						this.incrementTrees(true);
						
					}
					
				}
				
			} else if (life) {
				
				this.incrementVeg(delta / 5, false);
				
				Random random = new Random();
				
				if (trees > 0 && random.nextInt(32) == 0) {
					
					this.incrementTrees(false);
					
				}
				
			}
			
			if (life && lifeStart != 50f) {
				
				lifeStart += 0.1f * delta;
				if (lifeStart > 50) lifeStart = 50f;
				
			}
			
			if (waterStarted && waterStart != 50f) {
				
				waterStart += 0.1f * delta;
				if (waterStart > 50) waterStart = 50f;
				
			}
			
			if (atmosStarted && atmosStart != 50f) {
				
				atmosStart += 0.1f * delta;
				if (atmosStart > 50) atmosStart = 50f;
				
			}
			
		}
		
	}
	
	public void shake() {
		
		if (!shaking) {
			
			shakeTime += 100f;
			shaking = true;
			
		} else shakeTime += 5f;
		
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException {
		
		if (temperature < 300) g.setColor(new Color(102 + (int) Math.round(temperature * 154 / 300), 204 - (int) Math.round(temperature / 2), 255 - (int) Math.round(temperature / 2), 30));
		else g.setColor(new Color(255, 54, 105, 30));
		g.fill(outerAtmosphere);
		
		if (temperature < 300) g.setColor(new Color(102 + (int) Math.round(temperature * 154 / 300),  204 - (int) Math.round(temperature / 2), 255 - (int) Math.round(temperature / 2), 60));
		else g.setColor(new Color(255, 54, 105, 60));
		g.fill(atmosphere);
		
		if (life) {
			
			for (Tree tree : treeObjects) tree.render(gc, g);
			
			if (vegPercentage > 0f) {
			
				g.setLineWidth(5f * vegPercentage / 100);
				g.setColor(new Color(114, 144, 31));
				g.draw(planet);
				
			}
			
		}
			
		for (ParticleEmitter e : emitters) e.render(g, gc);
		for (Comet comet : comets) comet.render(g, gc);
		for (Cloud c : clouds) c.render(gc, g);
		
		g.rotate(CENTERX, CENTERY, angle * 180 / (float) Math.PI);
		g.setColor(new Color(48, 113, 151));
		g.fill(water);
		g.rotate(CENTERX, CENTERY, - angle * 180 / (float) Math.PI);
		
		g.setColor(new Color(99, 46, 31));
		g.fill(planet);
		
		g.setColor(new Color(158, 70, 14));
		g.fill(mantel);
		
		for (Polygon p : volcanoes) g.fill(p);
		
		g.setColor(new Color(217, 157, 37));
		g.fill(outerCore);
		
		g.setColor(new Color(255, 243, 190));
		g.fill(innerCore);
		
		g.resetTransform();
		
		if (atmosStarted) {
		
			Game.FONT.drawString(CENTERX - 1.5f * 348, atmosStart, "ATMOS: " + (int) Math.round(this.atmosphereLevel()), new Color(1f, 1f, 1f, atmosStart / 50));
			Game.FONT.drawString(CENTERX - 1.5f * 348, atmosStart + 22, "%CO2: " + (int) Math.round(co2Volume * 100 / this.atmosphereLevel()), new Color(1f, 1f, 1f, atmosStart / 50));
			Game.FONT.drawString(CENTERX - 1.5f * 348, atmosStart + 44, "%O2: " + (int) Math.round(o2Volume * 100 / this.atmosphereLevel()), new Color(1f, 1f, 1f, atmosStart / 50));
			Game.FONT.drawString(CENTERX - 1.5f * 348, atmosStart + 66, "TEMP: " + (int) Math.round(temperature), new Color(1f, 1f, 1f, atmosStart / 50));
			
		}
		
		if (waterStarted) {
			
			Game.FONT.drawString(CENTERX + 348, waterStart + 22, "H20: " + (int) Math.round(this.h2oVolume), new Color(1f, 1f, 1f, waterStart / 50));
			Game.FONT.drawString(CENTERX + 348, waterStart + 44, "%OCEAN: " + (int) Math.round(this.waterPercentage), new Color(1f, 1f, 1f, waterStart / 50));
			Game.FONT.drawString(CENTERX + 348, waterStart + 66, "%VAPOUR: " + (int) Math.round(this.vapourPercentage), new Color(1f, 1f, 1f, waterStart / 50));
			
		}
		
		if (life) {
			
			Game.FONT.drawString(CENTERX - 75, lifeStart + 22, "%VEG: " + (int) Math.round(vegPercentage), new Color(1f, 1f, 1f, lifeStart / 50));
			Game.FONT.drawString(CENTERX - 75, lifeStart + 44, "TREES: " + trees, new Color(1f, 1f, 1f, lifeStart / 50));
			Game.FONT.drawString(CENTERX - 75, lifeStart + 66, "FISH: " + fish, new Color(1f, 1f, 1f, lifeStart / 50));
			Game.FONT.drawString(CENTERX - 75, lifeStart + 88, "BIRDS: " + birds, new Color(1f, 1f, 1f, lifeStart / 50));
			Game.FONT.drawString(CENTERX - 75, lifeStart + 110, "SHEEP: " + sheep, new Color(1f, 1f, 1f, lifeStart / 50));
			Game.FONT.drawString(CENTERX - 75, lifeStart + 134, "WOLVES: " + wolves, new Color(1f, 1f, 1f, lifeStart / 50));
			
		}
		
	}
	
	public boolean isShaking() {
		
		return shaking;
		
	}

	public void cometCollision() {
		
		shake();
		this.incrementH2OVolume(100, true);
		this.incrementTemperature(200, true);
		this.incrementVeg(100, false);
		
		SoundBank.COMET.play();
		
	}
	
	public void boltCollision() {
		
		this.incrementTemperature(30, true);
		
		if (!life) {
		
			Random random = new Random();
			if (random.nextInt(100) == 0 && temperature > 20 && temperature < 45 && h2oVolume > 150 && atmosphereLevel() > 150) {
				
				life = true;
				lifeStart = 0f;
				
			}
			
		}
		
		SoundBank.THUNDER.play();
		
	}

	public void disableAll() {
		
		raining = false;
		for (Cloud c : clouds) c.disableStorm();
		cometsEnabled = false;
		volcanoesEnabled = false;
		for (ParticleEmitter p : emitters) p.disable();
		
	}

	public void enableVolcanoes() {
		
		volcanoesEnabled = true;
		for (ParticleEmitter p : emitters) p.enable();
		
	}

	public void enableComets() {
		
		cometsEnabled = true;
		
	}

	public void enableRain() {
	
		raining = true;
		
	}

	public void enableLightning() {
		
		for (Cloud c : clouds) c.storm();
		
	}

	public List<Point> getPoints() {
		
		return this.points;
		
	}

	public void displayMode() {
		
		displayMode = true;
		for (int i = 0; i < 40; i++) this.incrementTrees(true);
		
	}
	
	public void enableLife() {
		
		this.life = true;
		
	}
	
}

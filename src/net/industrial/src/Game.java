package net.industrial.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.industrial.src.particles.Star;
import net.industrial.src.planet.Planet;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.SpriteSheetFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class Game extends BasicGameState {
	
	Planet planet;
	List<Star> stars;
	public static Image LOGO;
	public static SpriteSheetFont FONT;
	
	public static int NEUTRAL = 2, VOLCANOES = 1, METEORS = 0, RAIN = 3,  LIGHTNING = 4;
	public static String[] OPTIONS = new String[]{"METEORS", "VOLCANOES", "NEUTRAL", "RAIN", "LIGHTNING"};
	
	private int selected = 2;
	
	private int id;
	
	public Game(int id) {
		
		this.id = id;
		
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		
		FONT = new SpriteSheetFont(new SpriteSheet(new Image("res/font.png"), 20, 22), ' ');
		
		LOGO = new Image("res/logo.png");
		planet = new Planet(gc.getWidth() / 2, gc.getHeight() / 2);
		
		stars = new ArrayList<Star>();
		
		for (int i = 0; i < 800; i++) stars.add(new Star(gc));
		
		new SoundBank();
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		
		g.setAntiAlias(false);
		g.setBackground(new Color(25, 20, 25));
		
		if (planet.isShaking()) {
			
			Random random = new Random();
			g.translate(- 3 + random.nextInt(6), - 3 + random.nextInt(6));
			
		}
		
		for (Star s : stars) s.render(gc, g);
		planet.render(gc, g);

		for (int i = 0; i < 5; i++) {
			
			if (selected != i) FONT.drawString((gc.getWidth()) * (2 * i + 1) / 10 - 10 * OPTIONS[i].length(), gc.getHeight() - 100, OPTIONS[i]);
			else FONT.drawString((gc.getWidth()) * (2 * i + 1) / 10 - 10 * ( "<" + OPTIONS[i] + ">").length(), gc.getHeight() - 100, "<" + OPTIONS[i] + ">");
			
		}
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		
		Input in = gc.getInput();
		
		if (in.isKeyPressed(Input.KEY_LEFT)) {
			
			selected -= 1;
			if (selected < 0) selected = 4;
			changeWeather();
			
		} else if (in.isKeyPressed(Input.KEY_RIGHT)) {
			
			selected += 1;
			if (selected > 4) selected = 0;
			changeWeather();
			
		}
		
		for (Star s : stars) s.update(gc, delta);
		planet.update(gc, delta);
		
		if (gc.getInput().isKeyPressed(Input.KEY_R)) {
			
			Game game = new Game(id + 1);
			game.init(gc, sbg);
			sbg.addState(game);
			sbg.enterState(getID() + 1, new FadeOutTransition(), new FadeInTransition());
		
		}
		
	}
	
	public void changeWeather() {
		
		if (selected == Game.NEUTRAL) planet.disableAll();
		else if (selected == Game.VOLCANOES) {
			
			planet.disableAll();
			planet.enableVolcanoes();
			
		} else if (selected == Game.METEORS) {
			
			planet.disableAll();
			planet.enableComets();
			
		} else if (selected == Game.RAIN) {
			
			planet.disableAll();
			planet.enableRain();
			
		} else if (selected == Game.LIGHTNING) {
			
			planet.disableAll();
			planet.enableLightning();
			
		}
		
		SoundBank.SELECT.play();
		
	}

	@Override
	public int getID() {
		
		return 2 + id;
		
	}
	
}

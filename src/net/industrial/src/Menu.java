package net.industrial.src;

import java.util.ArrayList;
import java.util.List;

import net.industrial.src.particles.Star;
import net.industrial.src.planet.Planet;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.SpriteSheetFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class Menu extends BasicGameState {
	
	Planet planet;
	List<Star> stars;
	public static Image LOGO;
	public static SpriteSheetFont FONT;
	
	private float introCool = 0f;
	private boolean loaded = false;
	
	private static String CREDITS = "(C) 2015 INDUSTRIAL GAMES // MADE FOR LUDUM DARE 34 // ALL CODE AND ASSETS BY NATHAN CORBYN (@DOCTOR_N_) // VISIT INDUSTRIAL-GAMES.NET FOR MORE FREE STUFF!",
			INSTRUCTIONS1 = "THIS IS A CROSS SECTION OF YOUR OWN WORLD...",
			INSTRUCTIONS2 = "USE THE ARROW KEYS, CHANGE THE WEATHER AND GROW AN ECOSYSTEM!",
			INSTRUCTIONS3 = "PRESS [R] TO START OR RESTART";
	
	private float credits = 0f;
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		
		FONT = new SpriteSheetFont(new SpriteSheet(new Image("res/font.png"), 20, 22), ' ');
		
		LOGO = new Image("res/logo.png");
		planet = new Planet(gc.getWidth() / 2, gc.getHeight() / 2);
		
		stars = new ArrayList<Star>();
		
		for (int i = 0; i < 800; i++) stars.add(new Star(gc));
		
		new SoundBank();
		
		planet.displayMode();
		planet.setH2OVolume(200);
		planet.setO2Volume(0);
		planet.setCO2Volume(300);
		planet.setTemperature(35);
		planet.setOceanPercentage(80);
		planet.setVegPercentage(100);
		planet.enableLife();
		
		credits = gc.getWidth();
		
		Music music = new Music("res/music.ogg");
		music.loop();
		
		gc.setMusicVolume(0.4f);
		gc.setSoundVolume(0.5f);
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		
		g.setAntiAlias(false);
		g.setBackground(new Color(25, 20, 25));
		
		for (Star s : stars) s.render(gc, g);
		planet.render(gc, g);
		
		g.drawImage(LOGO, gc.getWidth() / 2 - LOGO.getWidth() / 2, gc.getHeight() / 2 - LOGO.getHeight() / 2);
		
		FONT.drawString(credits, gc.getHeight() - 30, CREDITS);
		
		Color c = new Color(255, 255, 255, 0);
		
		if (introCool > 300f && introCool < 400f) {
			
			c.a = (introCool - 300f) / 100f;
			
			FONT.drawString(gc.getWidth() / 2 - INSTRUCTIONS1.length() * 10, gc.getHeight() / 2 + LOGO.getHeight() / 2 + 100, INSTRUCTIONS1, c);
			FONT.drawString(gc.getWidth() / 2 - INSTRUCTIONS2.length() * 10, gc.getHeight() / 2 + LOGO.getHeight() / 2 + 122, INSTRUCTIONS2, c);
			
		} else if (introCool > 400f) {
			
			if (introCool > 600f) c.a = (introCool - 600f) / 100f;
			
			FONT.drawString(gc.getWidth() / 2 - INSTRUCTIONS1.length() * 10, gc.getHeight() / 2 + LOGO.getHeight() / 2 + 100, INSTRUCTIONS1);
			FONT.drawString(gc.getWidth() / 2 - INSTRUCTIONS2.length() * 10, gc.getHeight() / 2 + LOGO.getHeight() / 2 + 122, INSTRUCTIONS2);
			FONT.drawString(gc.getWidth() / 2 - INSTRUCTIONS3.length() * 10, gc.getHeight() / 2 + LOGO.getHeight() / 2 + 210, INSTRUCTIONS3, c);
			
		}
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		
		for (Star s : stars) s.update(gc, delta);
		planet.update(gc, delta);
		planet.setO2Volume(0);
		planet.setCO2Volume(300);
		
		credits -= 0.1f * delta;
		
		if (credits < CREDITS.length() * - 20) credits = gc.getWidth();
		
		if (introCool < 700f && !loaded) {
			
			introCool += 0.1f * delta;
			if (introCool > 700f) loaded = true;
			
		} else if (introCool > 600f && loaded) {
			
			introCool -= 0.1f * delta;
			if (introCool < 600f) loaded = false;
			
		}
		
		if (gc.getInput().isKeyPressed(Input.KEY_R)) sbg.enterState(2, new FadeOutTransition(), new FadeInTransition());
		
	}

	@Override
	public int getID() {
		
		return 1;
		
	}
	
}

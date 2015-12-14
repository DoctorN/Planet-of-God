package net.industrial.src;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class IndustrialGames extends BasicGameState {

	Image logo;
	float cool = 0;
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		
		logo = new Image("res/ig.png");
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		
		g.setBackground(new Color(193, 193, 193));
		g.drawImage(logo, gc.getWidth() / 2 - logo.getWidth() / 2, gc.getHeight() / 2 - logo.getHeight() / 2);
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		
		cool += delta;
		if (cool > 2500) sbg.enterState(1, new FadeOutTransition(), new FadeInTransition());
		
	}

	@Override
	public int getID() {
		
		return 0;
		
	}

}

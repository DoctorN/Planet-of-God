package net.industrial.src;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Main extends StateBasedGame {

	public Main() {
		
		super("Planet of God");
		
	}

	@Override
	public void initStatesList(GameContainer gc) 
			throws SlickException {
		
		this.addState(new IndustrialGames());
		this.addState(new Menu());
		this.addState(new Game(0));
		this.enterState(0);
		
	}

	public static void main(String args[]) 
			throws SlickException {
		
		AppGameContainer app = new AppGameContainer(new Main());
		app.setDisplayMode(app.getScreenWidth(), app.getScreenHeight(), true);
		app.setShowFPS(false);
		app.setAlwaysRender(true);
		app.setVSync(true);
		app.setMouseGrabbed(true);
		app.start();
		
	}
	
}

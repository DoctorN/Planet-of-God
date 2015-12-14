package net.industrial.src;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class SoundBank {

	public static Sound COMET, RAIN, SELECT, THUNDER;
	
	public SoundBank() throws SlickException {
		
		COMET = new Sound("res/comet.ogg");
		RAIN = new Sound("res/rain.ogg");
		SELECT = new Sound("res/select.ogg");
		THUNDER = new Sound("res/thunder.ogg");
		
	}
	
}

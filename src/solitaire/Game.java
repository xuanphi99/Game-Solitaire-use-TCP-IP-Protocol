package solitaire;

import Clock.MinuteThread;
import Clock.SecondThread;
import control.ClientControl;
import java.util.Random;

public class Game {

	Engine game;
	GUI gui;
	int de;
          private ClientControl clientControl;

	public Game(int de,ClientControl clientControl) {
            // create IdGame
//            	Random id = new Random();
//		int de = id.nextInt(10)+1;
            this.clientControl = clientControl;
		game = new Engine(String.valueOf(de));
                 //  gui = new GUI(game,clientControl);
    
	}
	
//	public static void main(String[] args) {
//                      	Random id = new Random();
//		int de = id.nextInt(10)+1;
//		Game Solitaire = new Game();
//	}
}

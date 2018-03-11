package hangman;

import java.io.FileNotFoundException;

public class HangmanPlay {
	
	public static void main(String[] args) throws FileNotFoundException {
		HangmanGame hangmanGame = HangmanGame.getInstance();
		HangmanGame.start();

	}

}

package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import jaok.HangmanConsoleWindow;

public class HangmanGame {

	private static HangmanGame instance;
	private static int player;
	private static int lives;
	private static int randomword;
	private static int answersrow;
	private static String answer;
	private static String guess;
	private static String guessedletters;
	private static String[] guessword;
	private static int[] answerstreck;
	private static byte answerstreckplace;
	private static boolean wrongletter;
	private static HangmanConsoleWindow console = new HangmanConsoleWindow();

	public static HangmanGame getInstance() {
		if (instance == null) {
			instance = new HangmanGame();
			answerstreck = new int[70];
			answerstreckplace = 0;
		}
		return instance;
	}

	public static void start() throws FileNotFoundException {
		lives = 11;
		guessedletters = "";
		if (player == 0) {
			getPlayerCount();
		}
		getWord();
		guess();
	}

	private static void getPlayerCount() {
		console.println("Välkomen till hänga gubben, en eller två spelare?");
		while(!isInteger() || player != 1 && player != 2) {
			clear();
			console.println("Skriv in en siffra för hur många spelare, en eller två");
		}
	}

	private static void getWord() throws FileNotFoundException {
		clear();
		if (player == 1) {
			File file = new File("words");
			Scanner filereader = new Scanner(file);
			randomword = (int) (Math.random() * 70);
			for (int i = 0; i < answerstreck.length; i++) {
				if (randomword == answerstreck[i]) {
					getWord();
				}
			}
			for (int i = 0; i < randomword; i++) {
				answer = filereader.nextLine();
			}
			filereader.close();
			answerstreck[answerstreckplace] = randomword;
		} else if (player == 2) {
			console.println("Skriv in ett ord som ska gissas på!");
			answer = console.nextString().toLowerCase();
			if (!checkAlphabetic(answer)) {
				clear();
				console.println("Använd bara bokstäver");
				getWord();
			}
			clear();
		}
		guessword = new String[answer.length()];
		for (int i = 0; i < answer.length(); i++) {
			guessword[i] = "_";
		}
	}

	private static void guess() throws FileNotFoundException {
		while (true) {
			console.println("Gissa på en bokstav eller ett ord");
			console.println();
			console.println("Ordet:");
			for (int i = 0; i < guessword.length; i++) {
				console.print(" " + guessword[i]);
			}
			console.println();
			console.println();
			console.println("Hittils har du gissat på");
			console.println(guessedletters);
			guess = console.nextString().toLowerCase();
			if (checkAlphabetic(guess)) {
				if (guess.length() == 1 || guess.length() == answer.length()) {
					processGuess();
				} else {
					clear();
					console.println("Din gissning kan inte vara rätt ord, för många eller för få bokstäver");
					printMan();
				}
			} else {
				clear();
				console.println("Använd bara bokstäver");
				printMan();
			}

		}

	}

	private static void processGuess() throws FileNotFoundException {
		wrongletter = true;
		if (guess.length() == 1) {
			for (int i = 0; i < guessedletters.length(); i++) {
				if (guess.equals(guessedletters.substring(i, i+1))) {
					clear();
					printMan();
					console.println("Du har redan gissat på denna bokstaven");
					guess();
				}
			}
			for (int i = 0; i < answer.length(); i++) {
				if (guess.substring(0, 1).equals(answer.substring(i, i + 1))) {
					guessword[i] = guess;
					wrongletter = false;
				}
			}
			guessedletters = guessedletters + guess;
			
			for (int i = 0; i < guessword.length; i++) {
				if (!guessword[i].equals(answer.substring(i, i+1))) {
					break;
				}
				if (i == guessword.length-1) {
					answersrow++;
					win();
				}
			}
		}
		if (guess.length() == answer.length()) {
			if (guess.equals(answer)) {
				answersrow++;
				win();
			} else {
				loseLife();
			}
		} 
		if (wrongletter == true) {
			loseLife();
		}
		clear();
		printMan();
	}

	private static boolean checkAlphabetic(String str) {
		for (int i = 0; i != str.length(); ++i) {
			if (!Character.isLetter(str.charAt(i))) {
				return false;
			}
		}

		return true;
	}

	private static boolean isInteger(){
		try{
			player = Integer.parseInt(console.nextString());
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}

	private static void loseLife() {
		lives--;

	}

	private static void printMan() throws FileNotFoundException {
		if (lives == 10) {
			tenLives();
		}

		if (lives == 9) {
			nineLives();
		}

		if (lives == 8) {
			eightLives();
		}

		if (lives == 7) {
			sevenLives();
		}

		if (lives == 6) {
			sixLives();
		}

		if (lives == 5) {
			fiveLives();
		}

		if (lives == 4) {
			fourLives();
		}

		if (lives == 3) {
			threeLives();
		}

		if (lives == 2) {
			twoLives();
		}

		if (lives == 1) {
			oneLife();
		}

		if (lives == 0) {
			lost();
		}
		console.println();
	}

	private static void clear() {
		console.clear();
	}

	private void gameOver() throws FileNotFoundException {
		answerstreckplace++;
		answersrow = 0;
		console.println("Det rätta ordet var " + answer);
		console.println("Du förlorade! Nytt spel y/n");
		guess = console.nextString();
		if (guess.equals("y")) {
			start();
		} else if(guess.equals("n")) {
			System.exit(0);
		} else {
			gameOver();
		}
	}

	private static void win() throws FileNotFoundException {
		answerstreckplace++;
		clear();
		console.println("Du har " + answersrow + " rätt i rad");
		console.println("Du vann! Nytt spel y/n");
		guess = console.nextString();
		if (guess.equals("y")) {
			start();
		} else if(guess.equals("n")) {
			System.exit(0);
		} else {
			win();
		}
	}

	private static void lost() throws FileNotFoundException {
		console.println("   ___________ ");
		console.println("   | /       | ");
		console.println("   |/        0 ");
		console.println("   |        /|\\");
		console.println("   |        / \\");
		console.println("   |           ");
		console.println(" __|__         ");
		console.println("/     \\       ");
		HangmanGame.getInstance().gameOver();
	}

	private static void oneLife() {
		console.println("   ___________ ");
		console.println("   | /       | ");
		console.println("   |/        0 ");
		console.println("   |        /|\\");
		console.println("   |        /  ");
		console.println("   |           ");
		console.println(" __|__         ");
		console.println("/     \\       ");
	}

	private static void twoLives() {
		console.println("   ___________");
		console.println("   | /       | ");
		console.println("   |/        0 ");
		console.println("   |        /|\\");
		console.println("   |           ");
		console.println("   |           ");
		console.println(" __|__         ");
		console.println("/     \\       ");
	}

	private static void threeLives() {
		console.println("   ___________ ");
		console.println("   | /       | ");
		console.println("   |/        0 ");
		console.println("   |        /| ");
		console.println("   |           ");
		console.println("   |           ");
		console.println(" __|__         ");
		console.println("/     \\       ");
	}

	private static void fourLives() {
		console.println("   ___________ ");
		console.println("   | /       | ");
		console.println("   |/        0 ");
		console.println("   |         | ");
		console.println("   |           ");
		console.println("   |           ");
		console.println(" __|__         ");
		console.println("/     \\       ");
	}

	private static void fiveLives() {
		console.println("   ___________ ");
		console.println("   | /       | ");
		console.println("   |/        0 ");
		console.println("   |           ");
		console.println("   |           ");
		console.println("   |           ");
		console.println(" __|__         ");
		console.println("/     \\       ");
	}

	private static void sixLives() {
		console.println("   ___________ ");
		console.println("   | /       | ");
		console.println("   |/          ");
		console.println("   |           ");
		console.println("   |           ");
		console.println("   |           ");
		console.println(" __|__         ");
		console.println("/     \\       ");
	}

	private static void sevenLives() {
		console.println("   ___________ ");
		console.println("   | /         ");
		console.println("   |/          ");
		console.println("   |           ");
		console.println("   |           ");
		console.println("   |           ");
		console.println(" __|__         ");
		console.println("/     \\       ");
	}

	private static void eightLives() {
		console.println("   ___________ ");
		console.println("   |           ");
		console.println("   |           ");
		console.println("   |           ");
		console.println("   |           ");
		console.println("   |           ");
		console.println(" __|__         ");
		console.println("/     \\       ");
	}
	
	private static void nineLives() {
		console.println("               ");
		console.println("   |           ");
		console.println("   |           ");
		console.println("   |           ");
		console.println("   |           ");
		console.println("   |           ");
		console.println(" __|__         ");
		console.println("/     \\       ");
	}

	private static void tenLives() {
		console.println("               ");
		console.println("               ");
		console.println("               ");
		console.println("               ");
		console.println("               ");
		console.println("               ");
		console.println(" _____         ");
		console.println("/     \\       ");
	}

}
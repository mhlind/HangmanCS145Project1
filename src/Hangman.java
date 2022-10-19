
import java.util.Scanner;

public class Hangman {

    //basic variables needed for the game functions

    //whether to end the game loop
    public static boolean keepPlaying;

    //the hidden word
    public static String secretWord;
    //used for output to hold dashes, and the amount of revealed characters
    public static String revealedChars = "";

    //how many guesses are left before game over
    public static int remainingGuesses;

    //how many spots does the player get to guess?
    public static int howManySpaces;

    //reveals the word to the player if true, for testing purposes
    public static final boolean testingMode = true;

    //scanner to get input
    public static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {

        playGame();
    }

    /*
    * These initializeDifficulty methods all set the variables
    * as specified in the rules for each difficulty
    */
    public static void initializeEasy(){
        keepPlaying = true;
        secretWord = RandomWord.newWord();
        remainingGuesses = 15;
        howManySpaces = 4;
    }
    public static void initializeMedium(){
        keepPlaying = true;
        secretWord = RandomWord.newWord();
        remainingGuesses = 12;
        howManySpaces = 3;
    }
    public static void initializeHard (){
        keepPlaying = true;
        secretWord = RandomWord.newWord();
        remainingGuesses = 10;
        howManySpaces = 3;
    }

    /*
    * Initialize Game collects all of the initialize Difficulty methods
    * and performs the check for whichever difficulty the user chose, and runs that method
    */
    public static void initializeGame(){
        System.out.println("Choose your difficulty: Easy (e), Medium (m), Hard (h): ");
        char difficulty = scan.nextLine().charAt(0);
        //gets user input for the difficulty, and calls the relevant method
        switch (difficulty){
            default:
                //if the player put in an invalid choice, the method calls itself and gets a new input
                System.out.println("Invalid input, please try again");
                initializeGame();
                break;
            case 'e':
                initializeEasy();
                break;
            case 'm':
                initializeMedium();
                break;
            case 'h':
                initializeHard();
                break;
        }
        //creating a temporary string builder is better for performance
        //apparently, the concatenate operation on a string creates a new object every single time
        //which is quite annoying
        StringBuilder sb = new StringBuilder();

        //this initializes revealedChars, and gives it the appropriate amount of dashes
        //uses the append method to place a dash at the end
        //the dash is a String so as to use the repeat method,
        //which returns the given string the specified amount of times
        sb.append("-".repeat(secretWord.length()));
        revealedChars = sb.toString();
    }

    /*
    * The method which will be called in the main method
    * initializes the game, and contains the main loop of the game
    */
    public static void playGame(){
        initializeGame();

        while (keepPlaying){
            //at the beginning of each cycle, it will redisplay the needed info
            refresh(revealedChars);
            String input;
            System.out.println("Please enter the character you wish to guess: ");
            //gets the user's guess and passes it into the checker function
            input = scan.nextLine();
            checkMatch(input);
            checkGameOver(revealedChars);
        }

        boolean replay = playAgain();
        if (replay){
            //calling playGame within itself should reset the game for a new round
            playGame();
        }
        else {
            scan.close();
            System.exit(0);
        }

    }

    /*
    * Outputs all of the things which do not change
    * revealedChars will always be passed as the dashes String
    * doing it this way just helps ensure that the real String doesn't accidentally get changed
    * There's probably a better way to make it read only for certain methods but this works for now
    */
    public static void refresh(String dashes){
        //outputs the word if testing mode is on
        if (testingMode){
            System.out.println("The secret word is: " + secretWord);
        }
        //puts out the revealed letters
        System.out.println("The updated word is: " + dashes +
                "\nGuesses remaining: " + remainingGuesses);
    }

    //checks all win and loss conditions to decide whether to end the game
    //Same case with the dashes String in this method as with the refresh method
    //using the dashes String makes it so I don't accidentally alter the real one
    public static void checkGameOver(String dashes){
        //if the player is out of guesses, end the game, and output a game over message
        if (remainingGuesses == 0){
            System.out.println("You have run out of guesses! \nGAME OVER");
            keepPlaying = false;
        }
        //if the player guesses the word correctly, end the game and output a win message
        else if (dashes.equals(secretWord)){
            System.out.println("You have guessed the word! Congratulations!");
            keepPlaying = false;
        }
    }
    /* The method which handles
    * userInput has to be a String, to handle the "solve" case, otherwise I could use a char
    */
    public static void checkMatch(String userInput){

        int position;

        //String Builder class is mutable, allowing me to edit strings more easily
        StringBuilder checkString = new StringBuilder(revealedChars);

        //if the user asks to solve, it will
        //converts the String to lowercase, just so capitalization doesn't matter
        if(userInput.toLowerCase().equals("solve")){
            System.out.println("Solve the Word: ");
            /*probably a more elegant solution exists
            * but currently just gets a new user input and replaces solve with their guess
            */
            userInput = scan.next();
            //TODO implement the solve option
        }
        else{
            //Asks the player to input the indices of the characters they want to check
            System.out.println("Please enter the spaces you want to check (separated by spaces): ");

            //loops through the user inputs the amount of times given by the difficulty
            for (int i = 0; i < howManySpaces; i++){
                //gets the positions of the String the user wants to check
                position = scan.nextInt();
                //if the user checks a space that is out of bounds, it will reset to the max value
                if (position > (secretWord.length() - 1)){
                    position = howManySpaces -1;
                }
                //if the first character of your input is the same as the character at the current position
                if (userInput.charAt(0) == secretWord.charAt(position)){
                    //replace the dash at the current position with the correct character
                    checkString.setCharAt(position, userInput.charAt(0));
                }
            }
        }
        //finally just sets the revealed string to be equal to our StringBuilder
        revealedChars = checkString.toString();
    }

    /*boolean function to ask whether the player wants to keep going
    * returns true if they do, false if they don't
    */
    public static boolean playAgain (){
        System.out.println("Would you like to play again? (Y)es/(N)o");
        //gets the input and converts it to lowercase, to make checking it easier
        char input = scan.nextLine().charAt(0);
        input = Character.toLowerCase(input);
        if (input == 'y'){
            return true;
        }
        else if (input == 'n'){
            return false;
        }
        //if the player doesn't input a y or n, the function calls itself to go once more
        else {
            System.out.println("Input not recognized, please try again");
            return playAgain();
        }
    }
}
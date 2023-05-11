import java.util.Random;

public class MasterMindGame{
    public static String GenerateCode(int codeLength, char[] possibleChars){
        Random rand = new Random();
        StringBuilder code = new StringBuilder();
        for(int i = 0; i < codeLength; i++){
            char nextChar = possibleChars[rand.nextInt(possibleChars.length)];
            code.append(nextChar);
        }
        return code.toString();
    }

    public final String playerName;
    public final String code;
    private int guessesLeft;
    private int maxTurns;
    public int amountCorrect;
    public int amountMisplaced;

    public MasterMindGame(String playerName, String code, int numberOfTurns){
        this.playerName = playerName;
        this.code = code;
        guessesLeft = numberOfTurns;
        maxTurns = numberOfTurns;
    }

    public int getScore(){
        return maxTurns-guessesLeft;
    }

    public int makeGuess(String guess){
        if(amountCorrect == code.length())
            return 0;

        CheckCode results = new CheckCode(code,guess);
        amountCorrect = results.amountCorrect;
        amountMisplaced = results.amountMisplaced;
        guessesLeft--;
        return guessesLeft;
    }
}

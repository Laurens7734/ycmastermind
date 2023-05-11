import java.util.ArrayList;

public class CheckCode {
    private final String code;
    private final String guess;
    public int amountCorrect;
    public int amountMisplaced;
    private char[] remainingCharsCode;
    private String remainingCharsGuess;

    public CheckCode(String code, String guess){
        this.code = code;
        this.guess = guess;
        remainingCharsCode = new char[code.length()];
        numberOfCorrect();
        numberOfMisplaced();
    }

    private void numberOfCorrect(){
        StringBuilder incorrectGuesses = new StringBuilder();
        for (int i = 0; i < code.length(); i++) {
            if(code.charAt(i) == guess.charAt(i)){
                amountCorrect++;
            }
            else{
                remainingCharsCode[i] = code.charAt(i);
                incorrectGuesses.append(guess.charAt(i));
            }
        }
        remainingCharsGuess = incorrectGuesses.toString();
    }

    private void numberOfMisplaced(){
        for (char c:remainingCharsGuess.toCharArray()) {
            for(int i = 0; i < remainingCharsCode.length; i++){
                if(c == remainingCharsCode[i]){
                    amountMisplaced++;
                    remainingCharsCode[i] = '\0';
                    break;
                }
            }
        }
    }
}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Map;

public class Main {

    private static final int codeLength = 4;
    private static final char[] possibleChars = {'a', 'b', 'c', 'd', 'e', 'f'};
    private static final int maxTurns = 1;

    public static void main(String[] args){
        Map<String,String> env = LoadEnvironment.getVariables();
        IScoreEndpoint scoreRepo = new ScoreDatabase(env.get("DB_USER"),env.get("DB_PASSWORD"),env.get("DB_CONNECTION"));

        System.out.println("Welkom bij mastermind");
        System.out.println("Om een speler toe te voegen voert u de naam van de speler in.");
        System.out.println("Om het spel te starten typt u start.");
        System.out.println("Als u de ranglijst wilt bekijken typt u score");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean continuPlaying = true;
        boolean started = false;
        ArrayList<MasterMindGame> games = new ArrayList<>();
        int currentGame = 0;
        String code = MasterMindGame.GenerateCode(codeLength, possibleChars);

        String input;

        while (continuPlaying){
            try {
                input = reader.readLine();
            }
            catch( IOException e){
                System.out.println("Er is een fout opgetreden tijdens het lezen. Probeer het opnieuw.");
                continue;
            }
            if(input.equals("q")){
                continuPlaying = false;
                continue;
            }
            if(input.equals("start")){
                started = true;
                System.out.printf("%s mag beginnen%n", games.get(currentGame).playerName);
                continue;
            }
            if(input.equals("score")){
                showScore(scoreRepo);
                continue;
            }
            if(!started) {
                games.add(new MasterMindGame(input, code, maxTurns));
                System.out.println("speler toegevoegd");
            }
            else{
                if(!processGuess(input,currentGame,games))
                    continue;
                currentGame++;
                if(currentGame == games.size()){
                    showEndOfGame(games);
                    scoreRepo.saveScores(games);
                    started = false;
                    games = new ArrayList<>();
                    currentGame = 0;
                }
                else{
                    System.out.printf("De volgende speler is %s%n", games.get(currentGame).playerName);
                }
            }
        }
    }

    public static void showEndOfGame(ArrayList<MasterMindGame> games){
        System.out.println("Het spel is voorbij. De scores zijn als volgt:");
        List<MasterMindGame> sortedGames = games.stream().sorted(Comparator.comparing(MasterMindGame::getScore)).toList();
        for(MasterMindGame game : sortedGames){
            System.out.printf("%s : %s%n", game.playerName,game.getScore());
        }
    }

    public static boolean processGuess(String input, int currentGame, ArrayList<MasterMindGame> games){
        if(!verifyInput(input)){
            System.out.println("Input klopt niet");
            return false;
        }
        MasterMindGame game = games.get(currentGame);
        int turnsLeft = game.makeGuess(input);
        if(game.amountCorrect == codeLength) {
            clearScreen();
            System.out.printf("Goed gedaan. Je hebt de code geraden in %2d pogingen%n", maxTurns - turnsLeft);
            return true;
        }
        else
            System.out.println("+".repeat(Math.max(0, game.amountCorrect)) + "?".repeat(Math.max(0, game.amountMisplaced)));
        if(turnsLeft == 0) {
            clearScreen();
            System.out.println("Het is je niet gelukt :(");
            return true;
        }
        return false;
    }
    public static boolean verifyInput(String input){
        if(input.length() != codeLength)
            return false;
        for(char c : input.toCharArray()){
            if(!arrayContainsChar(possibleChars,c))
                return false;
        }
        return true;
    }

    public static void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void showScore(IScoreEndpoint scoreRepo){
        List<Score> highscore = scoreRepo.getHighScores();
        String inBetween = "+------------+------+-----+";
        System.out.println("De highscore is als volgt:");
        System.out.println(inBetween);
        System.out.println("|player name |code  |score|");
        System.out.println(inBetween);
        for(Score s : highscore){
            String scoreline = "|%s".formatted(s.name) +
                    " ".repeat(Math.max(0, 12 - s.name.length())) +
                    "|%s".formatted(s.code) +
                    " ".repeat(Math.max(0, 6 - s.code.length())) +
                    "|%s".formatted(s.turns) +
                    " ".repeat(Math.max(0, 5 - Integer.toString(s.turns).length())) +
                    "|";
            System.out.println(scoreline);
            System.out.println(inBetween);
        }
    }

    public static boolean arrayContainsChar(char[] chars, char toFind){
        for(char c : chars){
            if(c == toFind)
                return true;
        }
        return false;
    }
}
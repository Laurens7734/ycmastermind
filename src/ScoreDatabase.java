import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class ScoreDatabase implements IScoreEndpoint{
    private final String saveQuery = "insert into score (player, code, score, created_at) values (?, ?, ?, now())";
    private final String highscoreQuery = "select * from score order by score limit 0, 10";
    private final String username;
    private final String password;
    private final String connectionString;
    private Connection connection;
    public ScoreDatabase(String username, String password, String connectionString){
        this.username = username;
        this.password = password;
        this.connectionString = connectionString;
    }

    private void openConnection() throws SQLException{
        if(connection != null)
            return;
        connection = DriverManager.getConnection(connectionString,username,password);
    }

    private void closeConnection() throws SQLException{
        if(connection == null)
            return;
        connection.close();
        connection = null;
    }
    @Override
    public void saveScores(List<MasterMindGame> games) {
        try{
            openConnection();
            PreparedStatement saveScore = connection.prepareStatement(saveQuery);
            for (MasterMindGame game: games) {
                saveScore.setString(1, game.playerName);
                saveScore.setString(2, game.code);
                saveScore.setInt(3, game.getScore());
                saveScore.execute();
            }
            closeConnection();
        }
        catch(SQLException e){
            System.out.println("Opslaan van de scores is mislukt.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Score> getHighScores() {
        ArrayList<Score> scores = new ArrayList<>();
        try{
            openConnection();
            Statement getScore = connection.createStatement();
            ResultSet highscores = getScore.executeQuery(highscoreQuery);

            while(highscores.next()){
                Score s = new Score(highscores.getString(2),highscores.getString(3),highscores.getInt(4));
                scores.add(s);
            }
            closeConnection();
        }
        catch(SQLException e){
            System.out.println("Opslaan van de scores is mislukt.");
            System.out.println(e.getMessage());
        }
        return scores;
    }
}

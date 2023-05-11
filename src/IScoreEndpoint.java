import java.util.List;
public interface IScoreEndpoint {
    void saveScores(List<MasterMindGame> games);
    List<Score> getHighScores();
}

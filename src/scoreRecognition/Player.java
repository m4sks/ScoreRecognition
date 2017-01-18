package scoreRecognition;
import org.jfugue.player.*;

/**
 * Created by ev50063 on 2017/01/12.
 */
public class Player {
    Player() {
        
    }
    
    public void play(String input) {
        org.jfugue.player.Player player = new org.jfugue.player.Player();
        player.play(input);
    }
}

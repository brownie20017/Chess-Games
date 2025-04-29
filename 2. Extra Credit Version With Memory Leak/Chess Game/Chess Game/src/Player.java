import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    String color;

    Boolean isAi;
    Player(String colorinput, Boolean Aiinput) {
        color = colorinput;
        isAi = Aiinput;


    }
    ArrayList Pieces = new ArrayList<Piece>();


}

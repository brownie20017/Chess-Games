import java.io.Serializable;

public class Square implements Serializable {


    String nameofSpace;

    Piece pieceoccuping;

    String spacecolor;

    boolean occupied =false;

    public Piece getPieceoccuping() {
        return pieceoccuping;
    }

    public void setPieceoccuping(Piece pieceoccuping) {
        this.pieceoccuping = pieceoccuping;
    }

    public String getNameofSpace() {
        return nameofSpace;
    }

    public void setNameofSpace(String nameofSpace) {
        this.nameofSpace = nameofSpace;
    }


    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }




    Square(String space , String color){
        nameofSpace = space;
        spacecolor=color;

    }

    Square (Piece piece){
        pieceoccuping = piece;
        occupied = true;

    }

    public String Spaceinfo(){
        if (occupied == true){
            return pieceoccuping.getClass().getName();

        }else {
            return nameofSpace;

        }


    }


}

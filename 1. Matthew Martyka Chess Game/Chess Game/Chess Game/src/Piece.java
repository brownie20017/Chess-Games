import java.io.Serializable;

public class Piece implements Serializable {


    boolean enpassantable = false;
    String piecename;
    String color;
    int x;

    int y;

    boolean hasmoved = false;


    public Boolean getHasmoved() {
        return hasmoved;
    }

    public void setHasmoved(Boolean hasmoved) {
        this.hasmoved = hasmoved;
    }



    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }



    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    public String getPiecename() {
        return piecename;
    }

    public void setPiecename(String piecename) {
        this.piecename = piecename;
    }


    public boolean move(Board game, int x, int y){


        return false;
    }

    public boolean capture(Board game, int x, int y) {
        return false;

    }

    public boolean calculate(Board game, int x, int y){

        return false;
    }

    public void castle(Board game, int x, int y){


    }

    public void reset(Board game, int x, int y){
        game.tiles[this.x][this.y].setOccupied(false);
        game.tiles[this.x][this.y].setPieceoccuping(new Piece());
        game.tiles[x][y].setPieceoccuping(this);
        game.tiles[x][y].setOccupied(true);
        this.setX(x);
        this.setY(y);


    }

    public Piece(){


    }



}

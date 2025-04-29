public class Knight extends Piece{

    Knight(String playercolor){
        color = playercolor;



    }

    @Override
    public boolean calculate(Board game, int x, int y) {
        if (!game.tiles[x][y].isOccupied()) {

            if (((x == this.x + 2) && (y == this.y + 1 )) || ((x == this.x +2 ) && (y == this.y -1 )) || ((x == this .x + 1) && (y == this.y +2))|| ((x == this .x - 1) && (y == this.y +2)) || ((x == this.x - 2) && (y == this.y + 1 )) || ((x == this.x -2 ) && (y == this.y -1 )) || ((x == this .x + 1) && (y == this.y -2)) || ((x == this .x - 1) && (y == this.y -2)) ){

                return true;

            } else {
                return false;

            }



        }else if (game.tiles[x][y].isOccupied()) {


            if ((this.color.equals("White") && game.tiles[x][y].pieceoccuping.color.equals("Black")) || (this.color.equals("Black") && game.tiles[x][y].pieceoccuping.color.equals("White"))) {
                if (((x == this.x + 2) && (y == this.y + 1)) || ((x == this.x + 2) && (y == this.y - 1)) || ((x == this.x + 1) && (y == this.y + 2)) || ((x == this.x - 1) && (y == this.y + 2)) || ((x == this.x - 2) && (y == this.y + 1)) || ((x == this.x - 2) && (y == this.y - 1)) || ((x == this.x + 1) && (y == this.y - 2)) || ((x == this.x - 1) && (y == this.y - 2))) {

                    return true;


                } else {

                    return false;

                }
            }

            else{
                return false;

            }
        }

        return true;

    }

    public boolean move(Board game, int x, int y) {
        if (!game.tiles[x][y].isOccupied()) {

            if (((x == this.x + 2) && (y == this.y + 1 )) || ((x == this.x +2 ) && (y == this.y -1 )) || ((x == this .x + 1) && (y == this.y +2))|| ((x == this .x - 1) && (y == this.y +2)) || ((x == this.x - 2) && (y == this.y + 1 )) || ((x == this.x -2 ) && (y == this.y -1 )) || ((x == this .x + 1) && (y == this.y -2)) || ((x == this .x - 1) && (y == this.y -2)) ){

                game.tiles[this.x][this.y].setOccupied(false);
                game.tiles[this.x][this.y].setPieceoccuping(new Piece());
                game.tiles[x][y].setPieceoccuping(this);
                game.tiles[x][y].setOccupied(true);
                this.setX(x);
                this.setY(y);


            } else {
                return false;

            }



        }else if (game.tiles[x][y].isOccupied()) {


            if ((this.color.equals("White") && game.tiles[x][y].pieceoccuping.color.equals("Black")) || (this.color.equals("Black") && game.tiles[x][y].pieceoccuping.color.equals("White"))) {
                if (((x == this.x + 2) && (y == this.y + 1)) || ((x == this.x + 2) && (y == this.y - 1)) || ((x == this.x + 1) && (y == this.y + 2)) || ((x == this.x - 1) && (y == this.y + 2)) || ((x == this.x - 2) && (y == this.y + 1)) || ((x == this.x - 2) && (y == this.y - 1)) || ((x == this.x + 1) && (y == this.y - 2)) || ((x == this.x - 1) && (y == this.y - 2))) {

                    game.tiles[this.x][this.y].setOccupied(false);
                    game.tiles[this.x][this.y].setPieceoccuping(new Piece());
                    game.tiles[x][y].setPieceoccuping(this);
                    game.tiles[x][y].setOccupied(true);
                    this.setX(x);
                    this.setY(y);
                    game.removepiecefromarray(color, x ,y);

                } else {

                    return false;

                }
            }
        }

        else{
            return false;

        }
        return true;

    }

}

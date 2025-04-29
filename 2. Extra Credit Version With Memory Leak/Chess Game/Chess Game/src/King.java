public class King extends Piece{

    public boolean hasMoved = false;

    King(String playercolor){
        color = playercolor;
    }

    @Override
    public boolean calculate(Board game, int x, int y) {

        if (!game.tiles[x][y].isOccupied()) {
            if ((hasMoved == false) && (x == this.x) && (y == this.y + 2)) {

                return true;

            }


            if (((x == this.x) && (y == this.y + 1)) || ((x == this.x) && (y == this.y - 1)) || ((x == this.x + 1) && (y == this.y)) || ((x == this.x - 1) && (y == this.y)) || ((x == this.x + 1) && (y == this.y + 1)) || ((x == this.x - 1) && (y == this.y - 1)) || ((x == this.x + 1) && (y == this.y - 1)) || ((x == this.x - 1) && (y == this.y + 1))) {

                return true;

            } else {
                return false;
            }


        } else if ((this.color.equals("White") && game.tiles[x][y].pieceoccuping.color.equals("Black")) || (this.color.equals("Black") && game.tiles[x][y].pieceoccuping.color.equals("White"))) {
            if (((x == this.x) && (y == this.y + 1)) || ((x == this.x) && (y == this.y - 1)) || ((x == this.x + 1) && (y == this.y)) || ((x == this.x - 1) && (y == this.y)) || ((x == this.x + 1) && (y == this.y + 1)) || ((x == this.x - 1) && (y == this.y - 1)) || ((x == this.x + 1) && (y == this.y - 1)) || ((x == this.x - 1) && (y == this.y + 1))) {

                return true;

            } else {

                return false;

            }


        } else {
            return false;

        }

    }




    @Override
    public boolean move(Board game, int x, int y) {




        if (!game.tiles[x][y].isOccupied()) {
            if ((hasMoved == false) && (x == this.x) &&(y == this.y + 2)){

                game.tiles[this.x][this.y].setOccupied(false);
                game.tiles[this.x][this.y].setPieceoccuping(new Piece());
                game.tiles[x][y].setPieceoccuping(this);
                game.tiles[x][y].setOccupied(true);
                this.setX(x);
                this.setY(y);
                hasMoved = true;

            }





            if (((x == this.x) && (y == this.y +1 )) || ((x == this.x) && (y == this.y -1 )) || ((x == this .x + 1) && (y == this.y)) || ((x == this .x - 1) && (y == this.y)) || ((x == this .x + 1) && (y == this.y +1)) || ((x == this .x - 1) && (y == this.y - 1)) || ((x == this .x + 1) && (y == this.y - 1)) || ((x == this .x - 1) && (y == this.y + 1))){

                    game.tiles[this.x][this.y].setOccupied(false);
                    game.tiles[this.x][this.y].setPieceoccuping(new Piece());
                    game.tiles[x][y].setPieceoccuping(this);
                    game.tiles[x][y].setOccupied(true);
                    this.setX(x);
                    this.setY(y);
                    if (hasMoved == false){
                        hasMoved=true;

                    }

                } else {
                    return false;
                }



        }else if((this.color.equals("White") && game.tiles[x][y].pieceoccuping.color.equals("Black")) || (this.color.equals("Black") && game.tiles[x][y].pieceoccuping.color.equals("White")) ){
            if (((x == this.x) && (y == this.y +1 )) || ((x == this.x) && (y == this.y -1 )) || ((x == this .x + 1) && (y == this.y)) || ((x == this .x - 1) && (y == this.y)) || ((x == this .x + 1) && (y == this.y +1)) || ((x == this .x - 1) && (y == this.y - 1)) || ((x == this .x + 1) && (y == this.y - 1)) || ((x == this .x - 1) && (y == this.y + 1))){

                game.tiles[this.x][this.y].setOccupied(false);
                game.tiles[this.x][this.y].setPieceoccuping(new Piece());
                game.tiles[x][y].setPieceoccuping(this);
                game.tiles[x][y].setOccupied(true);
                this.setX(x);
                this.setY(y);
                if (hasMoved == false){
                    hasMoved=true;

                }

                game.removepiecefromarray(color, x ,y);
            } else {

                return false;

            }


        }
        else{
            return false;

        }
        return true;

    }


    public void castle(Board game, int x, int y){

        game.tiles[this.x][this.y].setOccupied(false);
        game.tiles[this.x][this.y].setPieceoccuping(new Piece());
        game.tiles[x][y].setPieceoccuping(this);
        game.tiles[x][y].setOccupied(true);
        this.setX(x);
        this.setY(y);



    }


}

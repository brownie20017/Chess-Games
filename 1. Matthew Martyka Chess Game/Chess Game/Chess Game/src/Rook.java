public class Rook extends Piece {


    Rook (String playercolor)
    {
        color = playercolor;

    }

    @Override
    public boolean calculate(Board game, int x, int y) {


            if ((this.x == x) && (this.y < y)) {


                for (int j = this.y + 1; j < y; j++) {
                    if (game.tiles[this.x][j].isOccupied()) {
                        return false;
                    }

                }


            }
            if ((this.x > x) && (this.y == y)) {

                for (int i = this.x - 1; i > x; i--) {

                    if (game.tiles[i][this.y].isOccupied()) {
                        return false;
                    }


                }
            }

            if ((this.x == x) && (this.y > y)) {


                for (int j = this.y - 1; j > y; j--) {
                    if (game.tiles[this.x][j].isOccupied()) {
                        return false;
                    }

                }


            }
            if ((this.x < x) && (this.y == y)) {

                for (int i = this.x + 1; i < x; i++) {

                    if (game.tiles[i][this.y].isOccupied()) {
                        return false;
                    }


                }
            }




        if (!game.tiles[x][y].isOccupied()) {
            if (((x == this.x) && (y < this.y)) || ((x == this.x) && (y > this.y)) || ((x < this.x) && (y == this.y)) || ((x > this.x) && (y == this.y))) {

                return true;

            }


        } else if ((this.color.equals("White") && game.tiles[x][y].pieceoccuping.color.equals("Black")) || (this.color.equals("Black") && game.tiles[x][y].pieceoccuping.color.equals("White"))) {
            if (((x == this.x) && (y < this.y)) || ((x == this.x) && (y > this.y)) || ((x < this.x) && (y == this.y)) || ((x > this.x) && (y == this.y))) {

                return true;
            }


        }

        return false;
    }



    public boolean move(Board game, int x, int y) {
        if (!game.tiles[x][y].isOccupied()) {


            if (((x == this.x) && (y < this.y)) || ((x == this.x) && (y > this.y)) || ((x < this.x) && (y == this.y)) || ((x > this.x) && (y == this.y))) {

                game.tiles[this.x][this.y].setOccupied(false);
                game.tiles[this.x][this.y].setPieceoccuping(new Piece());
                game.tiles[x][y].setPieceoccuping(this);
                game.tiles[x][y].setOccupied(true);
                this.setX(x);
                this.setY(y);


            } else {
                return false;

            }


        }

    else if ((this.color.equals("White") && game.tiles[x][y].pieceoccuping.color.equals("Black")) || (this.color.equals("Black") && game.tiles[x][y].pieceoccuping.color.equals("White"))) {


            if (((x == this.x) && (y < this.y)) || ((x == this.x) && (y > this.y)) || ((x < this.x) && (y == this.y)) || ((x > this.x) && (y == this.y))) {

                game.tiles[this.x][this.y].setOccupied(false);
                game.tiles[this.x][this.y].setPieceoccuping(new Piece());
                game.tiles[x][y].setPieceoccuping(this);
                game.tiles[x][y].setOccupied(true);
                this.setX(x);
                this.setY(y);


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

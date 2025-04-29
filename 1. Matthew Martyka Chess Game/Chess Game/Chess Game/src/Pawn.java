public class Pawn extends Piece {

    boolean enpassantable = false;


    boolean found;

    Pawn(String playercolor) {
        color = playercolor;


    }

    @Override
    public boolean calculate(Board game, int x, int y) {
        found = false;




        if (!game.tiles[x][y].isOccupied() && hasmoved == false) {
            if (this.color.equals("White")) {

                if (game.tiles[this.x-1][y].isOccupied()){
                    return false;
                }
                if ((x == this.x - 2) && (y == this.y)) {


                    return true;

                }
            }
            else if (this.color.equals("Black")) {
                if (game.tiles[this.x+1][y].isOccupied()){
                    return false;
                }
                if ((x == this.x + 2) && (y == this.y)) {


                    return true;

                }




            }


        }

        if (found == false) {
            /*if( y > 0) {

                if ((this.color.equals("White")) && (!game.tiles[x - 1][y - 1].isOccupied()) && (game.tiles[x][y - 1].getPieceoccuping().enpassantable == true)) {
                    if ((x == this.x - 1) && (y == this.y - 1)) {
                        return true;
                    } else {
                        return false;

                    }


                }else if ((this.color.equals("Black")) && (!game.tiles[x+1][y-1].isOccupied()) && (game.tiles[x][y-1].getPieceoccuping().enpassantable == true)){
                    if ((x == this.x + 1) && (y == this.y - 1)){
                        return true;
                    }else{
                        return false;

                    }


                }
            }



            else if (y < 7){

                if ((this.color.equals("White")) && (!game.tiles[x - 1][y + 1].isOccupied()) && (game.tiles[x][y + 1].getPieceoccuping().enpassantable == true)) {
                    if ((x == this.x - 1) && (y == this.y + 1)) {
                        return true;
                    } else {
                        return false;

                    }


                }else if ((this.color.equals("Black")) && (!game.tiles[x+1][y+1].isOccupied()) && (game.tiles[x][y+1].getPieceoccuping().enpassantable == true)){
                    if ((x == this.x + 1) && (y == this.y +1)){
                        return true;
                    }else{
                        return false;

                    }


                }
            }



            */


             if (!game.tiles[x][y].isOccupied()) {
                if (this.color.equals("White")) {
                    if ((x == this.x - 1) && (y == this.y)) {
                      return true;


                    } else {
                        return false;

                    }
                } else if (this.color.equals("Black")) {
                    if ((x == this.x + 1) && (y == this.y)) {
                        return true;


                    } else {
                        return false;

                    }


                }


            } else if (game.tiles[x][y].isOccupied()) {
                if (this.color.equals("White") && game.tiles[x][y].pieceoccuping.color.equals("Black")) {
                    if (((x == this.x - 1) && (y == this.y - 1)) || (x == this.x - 1) && (y == this.y + 1)) {
                        return true;

                    } else {
                        return false;

                    }
                } else if (this.color.equals("Black") && game.tiles[x][y].pieceoccuping.color.equals("White")) {
                    if (((x == this.x + 1) && (y == this.y - 1)) || (x == this.x + 1) && (y == this.y + 1)) {
                        return true;

                    } else {

                        return false;

                    }

                }
            }
        }

        else{
            return false;

        }
        return true;


    }

    @Override
    public boolean move(Board game, int x, int y) {
        found = false;
        enpassantable = false;
        if (!game.tiles[x][y].isOccupied() && hasmoved == false) {
            if (this.color.equals("White")) {
                if ((x == this.x - 2) && (y == this.y)) {
                    game.tiles[this.x][this.y].setOccupied(false);
                    game.tiles[this.x][this.y].setPieceoccuping(new Piece());
                    game.tiles[x][y].setPieceoccuping(this);
                    game.tiles[x][y].setOccupied(true);
                    this.setX(x);
                    this.setY(y);
                    hasmoved = true;
                    found = true;
                    enpassantable = true;

                }
            }
            else if (this.color.equals("Black")) {
                if ((x == this.x + 2) && (y == this.y)) {
                    game.tiles[this.x][this.y].setOccupied(false);
                    game.tiles[this.x][this.y].setPieceoccuping(new Piece());
                    game.tiles[x][y].setPieceoccuping(this);
                    game.tiles[x][y].setOccupied(true);
                    this.setX(x);
                    this.setY(y);
                    hasmoved = true;
                    found = true;
                    enpassantable = true;

                }




            }


        }

        if (found == false) {

            /*if (y > 0) {

                if ((this.color.equals("White")) && (!game.tiles[x - 1][y - 1].isOccupied()) && (game.tiles[x][y - 1].getPieceoccuping().enpassantable == true)) {
                    if ((x == this.x - 1) && (y == this.y - 1)) {
                        game.tiles[this.x][this.y].setOccupied(false);
                        game.tiles[this.x][this.y].setPieceoccuping(new Piece());
                        game.tiles[x][y].setPieceoccuping(this);
                        game.tiles[x][y].setOccupied(true);
                        this.setX(x);
                        this.setY(y);
                        hasmoved = true;

                    } else {
                        return false;

                    }
                }else if ((this.color.equals("Black")) && (!game.tiles[x+1][y-1].isOccupied()) && (game.tiles[x][y-1].getPieceoccuping().enpassantable == true)) {
                    if ((x == this.x + 1) && (y == this.y - 1)) {
                        game.tiles[this.x][this.y].setOccupied(false);
                        game.tiles[this.x][this.y].setPieceoccuping(new Piece());
                        game.tiles[x][y].setPieceoccuping(this);
                        game.tiles[x][y].setOccupied(true);
                        this.setX(x);
                        this.setY(y);
                        hasmoved = true;

                    } else {
                        return false;

                    }
                }
            }

            if (y < 7 ) {

                if ((this.color.equals("White")) && (!game.tiles[x - 1][y + 1].isOccupied()) && (game.tiles[x][y + 1].getPieceoccuping().enpassantable == true)) {
                    if ((x == this.x - 1) && (y == this.y + 1)) {
                        game.tiles[this.x][this.y].setOccupied(false);
                        game.tiles[this.x][this.y].setPieceoccuping(new Piece());
                        game.tiles[x][y].setPieceoccuping(this);
                        game.tiles[x][y].setOccupied(true);
                        this.setX(x);
                        this.setY(y);
                        hasmoved = true;

                    } else {
                        return false;

                    }
                }
                else if ((this.color.equals("Black")) && (!game.tiles[x+1][y+1].isOccupied()) && (game.tiles[x][y+1].getPieceoccuping().enpassantable == true)) {
                    if ((x == this.x + 1) && (y == this.y + 1)) {
                        game.tiles[this.x][this.y].setOccupied(false);
                        game.tiles[this.x][this.y].setPieceoccuping(new Piece());
                        game.tiles[x][y].setPieceoccuping(this);
                        game.tiles[x][y].setOccupied(true);
                        this.setX(x);
                        this.setY(y);
                        hasmoved = true;

                    } else {
                        return false;

                    }
                }
            }

             */


             if (!game.tiles[x][y].isOccupied()) {
                if (this.color.equals("White")) {
                    if ((x == this.x - 1) && (y == this.y)) {
                        game.tiles[this.x][this.y].setOccupied(false);
                        game.tiles[this.x][this.y].setPieceoccuping(new Piece());
                        game.tiles[x][y].setPieceoccuping(this);
                        game.tiles[x][y].setOccupied(true);
                        this.setX(x);
                        this.setY(y);


                    } else {
                        return false;

                    }
                } else if (this.color.equals("Black")) {
                    if ((x == this.x + 1) && (y == this.y)) {
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


            } else if (game.tiles[x][y].isOccupied()) {
                if (this.color.equals("White") && game.tiles[x][y].pieceoccuping.color.equals("Black")) {
                    if (((x == this.x - 1) && (y == this.y - 1)) || (x == this.x - 1) && (y == this.y + 1)) {
                        game.tiles[this.x][this.y].setOccupied(false);
                        game.tiles[this.x][this.y].setPieceoccuping(new Piece());
                        game.tiles[x][y].setPieceoccuping(this);
                        game.tiles[x][y].setOccupied(true);
                        this.setX(x);
                        this.setY(y);



                    } else {
                        return false;

                    }
                } else if (this.color.equals("Black") && game.tiles[x][y].pieceoccuping.color.equals("White")) {
                    if (((x == this.x + 1) && (y == this.y - 1)) || (x == this.x + 1) && (y == this.y + 1)) {
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
            }
        }

        else{
            return false;

        }
        return true;
    }




}

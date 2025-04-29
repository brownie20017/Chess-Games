public class Queen extends Piece {

    Queen(String playercolor) {
        color = playercolor;
    }

    @Override
    public boolean calculate(Board game, int x, int y) {
        if (!game.tiles[x][y].isOccupied()) {



            if (((x == this.x) && (y < this.y)) || ((x == this.x) && (y > this.y)) || ((x < this.x) && (y == this.y)) || ((x > this.x) && (y == this.y))) {

                if((this.x == x) && (this.y < y)) {


                    for (int j = this.y+1; j < y; j++) {
                        if (game.tiles[this.x][j].isOccupied()) {
                            return false;
                        }

                    }


                }
                if((this.x > x) && (this.y == y)) {

                    for (int i = this.x-1; i > x; i--) {

                        if (game.tiles[i][this.y].isOccupied()) {
                            return false;
                        }



                    }
                }

                if((this.x == x) && (this.y > y)) {


                    for (int j = this.y -1; j > y; j--) {
                        if (game.tiles[this.x][j].isOccupied()) {
                            return false;
                        }

                    }


                }
                if((this.x < x) && (this.y == y)) {

                    for (int i = this.x +1; i < x; x++) {

                        if (game.tiles[i][this.y].isOccupied()) {
                            return false;
                        }



                    }
                }

                return true;


            } else if ((Math.abs(x - this.x) - Math.abs(y - this.y) == 0)) {
                if ((this.x < x)&& (this.y < y)) {
                    for (int i = this.x +1; i < x; i++) {
                        for (int j = this.y +1; j < y; j++) {
                            if (Math.abs(i - this.x) - Math.abs(j - this.y) == 0) {
                                if (game.tiles[i][j].isOccupied()) {
                                    return false;
                                }

                            }

                        }

                    }
                }

                if ((this.x > x)&& (this.y < y)) {
                    for (int i = this.x -1 ; i > x; i--) {
                        for (int j = this.y +1; j < y; j++) {
                            if (Math.abs(i - this.x) - Math.abs(j - this.y) == 0) {
                                if (game.tiles[i][j].isOccupied()) {
                                    return false;
                                }

                            }

                        }

                    }
                }

                if ((this.x > x)&& (this.y > y)) {
                    for (int i = this.x-1; i > x; i--) {
                        for (int j = this.y-1; j > y; j--) {
                            if (Math.abs(i - this.x) - Math.abs(j - this.y) == 0) {
                                if (game.tiles[i][j].isOccupied()) {
                                    return false;
                                }

                            }

                        }

                    }
                }
                if ((this.x < x)&& (this.y > y)) {
                    for (int i = this.x+1; i < x; i++) {
                        for (int j = this.y -1; j > y; j--) {
                            if (Math.abs(i - this.x) - Math.abs(j - this.y) == 0) {
                                if (game.tiles[i][j].isOccupied()) {
                                    return false;
                                }

                            }

                        }

                    }
                }

                return true;


            } else {
                return false;

            }





        } else if ((this.color.equals("White") && game.tiles[x][y].pieceoccuping.color.equals("Black")) || (this.color.equals("Black") && game.tiles[x][y].pieceoccuping.color.equals("White"))) {


            if (((x == this.x) && (y < this.y)) || ((x == this.x) && (y > this.y)) || ((x < this.x) && (y == this.y)) || ((x > this.x) && (y == this.y))) {

                if((this.x == x) && (this.y < y)) {


                    for (int j = this.y+1; j < y; j++) {
                        if (game.tiles[this.x][j].isOccupied()) {
                            return false;
                        }

                    }


                }
                if((this.x > x) && (this.y == y)) {
                    for (int i = this.x-1; i > x; i--) {


                        if (game.tiles[i][this.y].isOccupied()) {
                            return false;
                        }



                    }
                }

                if((this.x == x) && (this.y > y)) {


                    for (int j = this.y -1; j > y; j--) {
                        if (game.tiles[this.x][j].isOccupied()) {
                            return false;
                        }

                    }


                }
                if((this.x < x) && (this.y == y)) {

                    for (int i = this.x +1; i < x; x++) {

                        if (game.tiles[i][this.y].isOccupied()) {
                            return false;
                        }

                    }
                }

                return true;


            } else if ((Math.abs(x - this.x) - Math.abs(y - this.y) == 0)) {

                if ((this.x < x)&& (this.y < y)) {
                    for (int i = this.x; i < x; i++) {
                        for (int j = this.y; j < y; j++) {
                            if (Math.abs(x - this.x) - Math.abs(y - this.y) == 0) {
                                if (game.tiles[i][j].isOccupied()) {
                                    return false;
                                }

                            }

                        }

                    }
                }

                if ((this.x > x)&& (this.y < y)) {
                    for (int i = this.x; i > x; i--) {
                        for (int j = this.y; j < y; j++) {
                            if (Math.abs(x - this.x) - Math.abs(y - this.y) == 0) {
                                if (game.tiles[i][j].isOccupied()) {
                                    return false;
                                }

                            }

                        }

                    }
                }

                if ((this.x > x)&& (this.y > y)) {
                    for (int i = this.x; i > x; i--) {
                        for (int j = this.y; j > y; j--) {
                            if (Math.abs(x - this.x) - Math.abs(y - this.y) == 0) {
                                if (game.tiles[i][j].isOccupied()) {
                                    return false;
                                }

                            }

                        }

                    }
                }
                if ((this.x < x)&& (this.y > y)) {
                    for (int i = this.x; i < x; i++) {
                        for (int j = this.y; j > y; j--) {
                            if (Math.abs(x - this.x) - Math.abs(y - this.y) == 0) {
                                if (game.tiles[i][j].isOccupied()) {
                                    return false;
                                }

                            }

                        }

                    }
                }




                return true;
            } else {
                return false;
            }


        } else {
            return false;

        }


    }

    public boolean move(Board game, int x, int y) {
        if (!game.tiles[x][y].isOccupied()) {

            //(`,+) (`,-) (+,`) (-,`) (+,+)(-,-)(+,-)(-,+)
            //     (+,-)(-,+)


            if (((x == this.x) && (y < this.y)) || ((x == this.x) && (y > this.y)) || ((x < this.x) && (y == this.y)) || ((x > this.x) && (y == this.y))) {



                game.tiles[this.x][this.y].setOccupied(false);
                game.tiles[this.x][this.y].setPieceoccuping(new Piece());
                game.tiles[x][y].setPieceoccuping(this);
                game.tiles[x][y].setOccupied(true);
                this.setX(x);
                this.setY(y);


            } else if ((Math.abs(x - this.x) - Math.abs(y - this.y) == 0)) {
                game.tiles[this.x][this.y].setOccupied(false);
                game.tiles[this.x][this.y].setPieceoccuping(new Piece());
                game.tiles[x][y].setPieceoccuping(this);
                game.tiles[x][y].setOccupied(true);
                this.setX(x);
                this.setY(y);


            } else {
                return false;
            }


        } else if ((this.color.equals("White") && game.tiles[x][y].pieceoccuping.color.equals("Black")) || (this.color.equals("Black") && game.tiles[x][y].pieceoccuping.color.equals("White"))) {


            if (((x == this.x) && (y < this.y)) || ((x == this.x) && (y > this.y)) || ((x < this.x) && (y == this.y)) || ((x > this.x) && (y == this.y))) {

                game.tiles[this.x][this.y].setOccupied(false);
                game.tiles[this.x][this.y].setPieceoccuping(new Piece());
                game.tiles[x][y].setPieceoccuping(this);
                game.tiles[x][y].setOccupied(true);
                this.setX(x);
                this.setY(y);
                game.removepiecefromarray(color, x ,y);


            } else if ((Math.abs(x - this.x) - Math.abs(y - this.y) == 0)) {
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


        } else {
            return false;

        }
        return true;

    }
}




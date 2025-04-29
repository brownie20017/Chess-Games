import java.io.*;

public class Board implements Serializable {

    Player White;
    Player Black;
    Square[][] tiles = new Square[8][8];

    Board(Player white, Player black) {

        White = white;
        Black = black;

        tiles[0][0]= new Square("a8", "White");
        tiles[0][1]= new Square("b8", "Black");
        tiles[0][2]= new Square("c8", "White");
        tiles[0][3]= new Square("d8", "Black");
        tiles[0][4]= new Square("e8", "White");
        tiles[0][5]= new Square("f8", "Black");
        tiles[0][6]= new Square("g8", "White");
        tiles[0][7]= new Square("h8", "Black");
        tiles[1][0]= new Square("a7", "Black");
        tiles[1][1]= new Square("b7", "White");
        tiles[1][2]= new Square("c7", "Black");
        tiles[1][3]= new Square("d7", "White");
        tiles[1][4]= new Square("e7", "Black");
        tiles[1][5]= new Square("f7", "White");
        tiles[1][6]= new Square("g7", "Black");
        tiles[1][7]= new Square("h7", "White");
        tiles[2][0]= new Square("a6", "White");
        tiles[2][1]= new Square("b6", "Black");
        tiles[2][2]= new Square("c6", "White");
        tiles[2][3]= new Square("d6", "Black");
        tiles[2][4]= new Square("e6", "White");
        tiles[2][5]= new Square("f6", "Black");
        tiles[2][6]= new Square("g6", "White");
        tiles[2][7]= new Square("h6", "Black");
        tiles[3][0]= new Square("a5", "Black");
        tiles[3][1]= new Square("b5", "White");
        tiles[3][2]= new Square("c5", "Black");
        tiles[3][3] = new Square("d5", "White");
        tiles[3][4]= new Square("e5", "Black");
        tiles[3][5]= new Square("f5", "White");
        tiles[3][6]= new Square("g5", "Black");
        tiles[3][7] = new Square("h5", "White");
        tiles[4][0] = new Square("a4", "White");
        tiles[4][1]= new Square("b4", "Black");
        tiles[4][2] = new Square("c4", "White");
        tiles[4][3] = new Square("d4", "Black");
        tiles[4][4] = new Square("e4", "White");
        tiles[4][5] = new Square("f4", "Black");
        tiles[4][6] = new Square("g4", "White");
        tiles[4][7] = new Square("h4", "Black");
        tiles[5][0] = new Square("a3", "Black");
        tiles[5][1] = new Square("b3", "White");
        tiles[5][2] = new Square("c3", "Black");
        tiles[5][3] = new Square("d3", "White");
        tiles[5][4] = new Square("e3", "Black");
        tiles[5][5] = new Square("f3", "White");
        tiles[5][6] = new Square("g3", "Black");
        tiles[5][7] = new Square("h3", "White");
        tiles[6][0] = new Square("a2", "White");
        tiles[6][1] = new Square("b2", "Black");
        tiles[6][2] = new Square("c2", "White");
        tiles[6][3] = new Square("d2", "Black");
        tiles[6][4] = new Square("e2", "White");
        tiles[6][5] = new Square("f2", "Black");
        tiles[6][6] = new Square("g2", "White");
        tiles[6][7] = new Square("h2", "Black");
        tiles[7][0] = new Square("a1", "Black");
        tiles[7][1] = new Square("b1", "White");
        tiles[7][2] = new Square("c1", "Black");
        tiles[7][3] = new Square("d1", "White");
        tiles[7][4] = new Square("e1", "Black");
        tiles[7][5] = new Square("f1", "White");
        tiles[7][6] = new Square("g1", "Black");
        tiles[7][7] = new Square("h1", "White");

   for(int i = 0; i < 8; i++) {

       for (int n = 0; n < 8; n++) {







           //Comment out for custom board set up



           if (((i == 0) && (n == 0)) || ((i == 0) && (n == 7))) {


               tiles[i][n].setPieceoccuping(new Rook("Black"));
               tiles[i][n].pieceoccuping.setX(i);
               tiles[i][n].pieceoccuping.setY(n);
               tiles[i][n].setOccupied(true);
               tiles[i][n].pieceoccuping.setPiecename("BR");
               black.Pieces.add(tiles[i][n].pieceoccuping);
           }





           else if (((i == 7) && (n == 0)) || ((i == 7) && (n == 7))) {

               tiles[i][n].setPieceoccuping(new Rook("White"));
               tiles[i][n].pieceoccuping.setX(i);
               tiles[i][n].pieceoccuping.setY(n);
               tiles[i][n].setOccupied(true);
               tiles[i][n].pieceoccuping.setPiecename("WR");
               white.Pieces.add(tiles[i][n].pieceoccuping);

           }



           else if (((i == 0) && (n == 1)) || ((i == 0) && (n == 6))) {
               tiles[i][n].setPieceoccuping( new Knight("Black"));
               tiles[i][n].pieceoccuping.setX(i);
               tiles[i][n].pieceoccuping.setY(n);
               tiles[i][n].setOccupied(true);
               tiles[i][n].pieceoccuping.setPiecename("BN");
               black.Pieces.add(tiles[i][n].pieceoccuping);
           }

           else if (((i == 7) && (n == 1)) || ((i == 7) && (n == 6))) {
               tiles[i][n].setPieceoccuping(new Knight("White"));
               tiles[i][n].pieceoccuping.setX(i);
               tiles[i][n].pieceoccuping.setY(n);
               tiles[i][n].setOccupied(true);
               tiles[i][n].pieceoccuping.setPiecename("WN");
               white.Pieces.add(tiles[i][n].pieceoccuping);
           }

           else if (((i == 0) && (n == 2)) || ((i == 0) && (n == 5))) {
               tiles[i][n].setPieceoccuping(new Bishop("Black"));
               tiles[i][n].pieceoccuping.setX(i);
               tiles[i][n].pieceoccuping.setY(n);
               tiles[i][n].setOccupied(true);
               tiles[i][n].pieceoccuping.setPiecename("BB");
               black.Pieces.add(tiles[i][n].pieceoccuping);
           }

           else if (((i == 7) && (n == 2)) || ((i == 7) && (n == 5))) {
               tiles[i][n].setPieceoccuping(new Bishop("White"));
               tiles[i][n].pieceoccuping.setX(i);
               tiles[i][n].pieceoccuping.setY(n);
               tiles[i][n].setOccupied(true);
               tiles[i][n].pieceoccuping.setPiecename("WB");
               white.Pieces.add(tiles[i][n].pieceoccuping);
           }

           else if ((i == 0) && (n == 3)) {
               tiles[i][n].setPieceoccuping(new Queen("Black"));
               tiles[i][n].pieceoccuping.setX(i);
               tiles[i][n].pieceoccuping.setY(n);
               tiles[i][n].setOccupied(true);
               tiles[i][n].pieceoccuping.setPiecename("BQ");
               black.Pieces.add(tiles[i][n].pieceoccuping);
           }

           else if ((i == 0) && (n == 4)) {
               tiles[i][n].setPieceoccuping(new King("Black"));
               tiles[i][n].pieceoccuping.setX(i);
               tiles[i][n].pieceoccuping.setY(n);
               tiles[i][n].setOccupied(true);
               tiles[i][n].pieceoccuping.setPiecename("BK");
               black.Pieces.add(tiles[i][n].pieceoccuping);

           }

           else if ((i == 7) && (n == 3)) {
               tiles[i][n].setPieceoccuping(new Queen("White"));
               tiles[i][n].pieceoccuping.setX(i);
               tiles[i][n].pieceoccuping.setY(n);
               tiles[i][n].setOccupied(true);
               tiles[i][n].pieceoccuping.setPiecename("WQ");
               white.Pieces.add(tiles[i][n].pieceoccuping);

           }

           else if ((i == 7) && (n == 4)) {
               tiles[i][n].setPieceoccuping(new King("White"));
               tiles[i][n].pieceoccuping.setX(i);
               tiles[i][n].pieceoccuping.setY(n);
               tiles[i][n].setOccupied(true);
               tiles[i][n].pieceoccuping.setPiecename("WK");
               white.Pieces.add(tiles[i][n].pieceoccuping);
           }

           else if (i == 1){
               tiles[i][n].setPieceoccuping(new Pawn("Black"));
               tiles[i][n].pieceoccuping.setX(i);
               tiles[i][n].pieceoccuping.setY(n);
               tiles[i][n].setOccupied(true);
               tiles[i][n].pieceoccuping.setPiecename("BP");
               black.Pieces.add(tiles[i][n].pieceoccuping);
           }

           else if (i == 6){
               tiles[i][n].setPieceoccuping(new Pawn("White"));
                tiles[i][n].pieceoccuping.setX(i);
                tiles[i][n].pieceoccuping.setY(n);
                tiles[i][n].setOccupied(true);
                tiles[i][n].pieceoccuping.setPiecename("WP");
               white.Pieces.add(tiles[i][n].pieceoccuping);
           }



           else {
               tiles[i][n].setPieceoccuping(new Piece());

           }
       }
  }
    }

    public void removepiecefromarray(String Piececolor, int x, int y){

        Piece PieceGotten;
        Player OpposingPlayer;

        if (Piececolor == "White") {
            OpposingPlayer = Black;

        }else {

            OpposingPlayer = White;
        }


        for (int i = 0; i < OpposingPlayer.Pieces.size(); i++) {

            PieceGotten = (Piece) OpposingPlayer.Pieces.get(i);

            if ((PieceGotten.getY() == y) &&(PieceGotten.getX() == x)) {

                OpposingPlayer.Pieces.remove(i);

            }
        }


    }







}



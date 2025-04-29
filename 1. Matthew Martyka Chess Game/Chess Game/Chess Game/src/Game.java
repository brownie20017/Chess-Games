import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Scanner;

public class Game implements Serializable {
    boolean Gamecontinue = true;
    String option;
    boolean loop = true;

    boolean Smallloop = true;

    Player White = new Player("White", false);
    Player Black = new Player("Black", false);

    Player CurrentPlayer = White;

    AI computer = new AI();

    Board game = new Board(White, Black);

    Boolean end = false;

    public void startgame(boolean newgame) {
        Scanner input = new Scanner(System.in);

        if (newgame == true) {

            Gamecontinue = true;
            loop = true;
            boolean spin = false;

            do {

                System.out.println("Is this game Hotseat, Player VS AI, or AI VS AI");
                String inputforgame = input.nextLine();


                if (inputforgame.equals("Hotseat")) {
                    spin = false;

                    White = new Player("White", false);
                    Black = new Player("Black", false);
                    CurrentPlayer = White;
                    game = new Board(White, Black);
                } else if (inputforgame.equals("Player VS AI")) {

                    spin = false;

                    do {
                        System.out.println("What color is the AI");
                        inputforgame = input.nextLine();
                        if (inputforgame.equals("Black")) {
                            Black = new Player("Black", true);
                            spin = false;
                        } else if (inputforgame.equals("White")) {
                            White = new Player("White", true);
                            spin = false;
                        } else {
                            System.out.println("That is not a valid color");
                            spin = true;
                        }

                    } while (spin);


                    CurrentPlayer = White;
                    game = new Board(White, Black);


                } else if (inputforgame.equals("AI VS AI")) {
                    spin = false;

                    White = new Player("White", true);
                    Black = new Player("Black", true);
                    CurrentPlayer = White;
                    game = new Board(White, Black);


                } else {
                    System.out.println("That is not a valid input");
                    spin = true;

                }
            } while (spin);
        }


        Printboard(game);


        do {

            do {
                try {


                    if (CurrentPlayer.isAi == true) {
                        System.out.println("It is " + CurrentPlayer.color + "'s turn");
                        option = computer.GenerateMove();
                        System.out.println(option);

                    } else {


                        System.out.println("It is " + CurrentPlayer.color + "'s turn");

                        System.out.println("Please type in one of the following");
                        System.out.println("Algebraic Notation, Draw, Surrender, or Main Menu");

                        option = input.nextLine();
                    }


                    if (option.equals("Draw")) {
                        System.out.println(CurrentPlayer.color + " Would like to offer a draw");
                        Smallloop = true;

                        do {
                            System.out.println("Would you like to accept the draw");
                            System.out.println("1) Yes 2) No");
                            option = input.nextLine();
                            if ((option.equals("Yes")) || (option.equals("yes")) || (option.equals("1"))) {
                                System.out.println("The game has ended in a draw");
                                Gamecontinue = false;
                                loop = true;
                                Smallloop = false;
                                DeleteSave();
                            } else if ((option.equals("No")) || (option.equals("no")) || (option.equals("2"))) {
                                System.out.println("Draw offer Rejected");
                                Gamecontinue = true;
                                Smallloop = false;
                            } else {
                                System.out.println("You did not answer Yes or No");


                            }

                        } while (Smallloop);

                    } else if (option.equals("Surrender")) {
                        System.out.println(CurrentPlayer.color + " Surrendered");
                        Gamecontinue = false;
                        loop = true;
                        DeleteSave();

                    } else if (option.equals("Main Menu")) {
                        Gamecontinue = false;
                        loop = true;


                    } else {


                        loop = movemenu(option, game, CurrentPlayer);

                        if (loop == false) {
                            System.out.println("That is not a valid move");


                        }


                        Gamecontinue = true;
                    }


                } catch (Exception e) {
                    System.out.println(e);

                    System.out.println("That is not a valid input");

                    input.nextLine();

                }

                if (loop == false) {
                    Printboard(game);

                }

            } while (loop == false);


            this.save_game();

            if ((CurrentPlayer.color.equals("White"))) {
                CurrentPlayer = Black;
            } else {
                CurrentPlayer = White;

            }


            if (Gamecontinue == false) {


            } else {
                Printboard(game);

                if (check(CurrentPlayer) == true) {
                    if (CurrentPlayer == White) {
                        System.out.println("White King in Check");
                        if (Checkmate(CurrentPlayer)) {
                            System.out.println("CheckMate");
                            System.out.println("Black Wins");
                            Gamecontinue = false;
                            DeleteSave();
                        }

                    } else {
                        System.out.println("Black King in Check");
                        if (Checkmate(CurrentPlayer)) {
                            System.out.println("CheckMate");
                            System.out.println("White Wins");
                            Gamecontinue = false;
                            DeleteSave();

                        }

                    }

                }

                if (Stalemate(CurrentPlayer) == true) {
                    System.out.println(" The game has ended in a draw");
                    Gamecontinue = false;
                    DeleteSave();

                }

            }

        } while (Gamecontinue);


    }


    public void Printboard(Board game) {

        for (int i = 0; i < 8; i++) {
            for (int n = 0; n < 8; n++) {

                if (game.tiles[i][n].isOccupied() == true) {

                    System.out.print(game.tiles[i][n].pieceoccuping.getPiecename() + " ");

                } else {

                    System.out.print(game.tiles[i][n].getNameofSpace() + " ");
                }
            }


            System.out.println();
        }


    }

    public boolean movemenu(String input, Board game, Player CurrentPlayer) {
        String Space;
        Piece PieceGotten;
        Piece KingForCastle = new Piece();
        Piece RookForCastle = new Piece();


        int kingfound = -1;
        int rookfound = -1;

        int x = -1;
        int y = -1;
        Space = input;
        String Spaceentered;

        int Originalx;
        int Originaly;
        boolean OriginalHasMoved;
        boolean taken = false;

        Player Opposingplayer;
        Piece Opposingpiece = null;

        int Opposingplayersize;

        if (CurrentPlayer == White) {
            Opposingplayer = Black;

        } else {
            Opposingplayer = White;

        }

        Opposingplayersize = Opposingplayer.Pieces.size();

        if (Space.length() < 2) {
            return false;

        }

        if (Space.charAt(0) == 'k') {
            return false;

        } else if (Space.charAt(0) == 'q') {
            return false;

        } else if (Space.charAt(0) == 'n') {
            return false;

        } else if (Space.charAt(0) == 'r') {
            return false;

        } else if ((Space.charAt(0) == 'b') && (Space.length() == 3)) {
            return false;

        }


        if (Space.equals("O-O")) {
            for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {

                PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                if (PieceGotten.piecename.contains("K") && (PieceGotten.hasmoved == false)) {

                    KingForCastle = PieceGotten;
                    kingfound = i;

                }

            }

            if (kingfound == -1) {
                return false;

            }

            for (int second = KingForCastle.getY() + 1; second < 7; second++) {
                if (game.tiles[KingForCastle.getX()][second].isOccupied()) {
                    return false;

                }

            }


            for (int third = 0; third < CurrentPlayer.Pieces.size(); third++) {

                PieceGotten = (Piece) CurrentPlayer.Pieces.get(third);

                if (CurrentPlayer.color.equals("White")) {
                    if (PieceGotten.piecename.contains("R") && (PieceGotten.getHasmoved() == false) && (PieceGotten.getX() == 7) && (PieceGotten.getY() == 7)) {

                        RookForCastle = PieceGotten;
                        rookfound = third;


                    }

                } else {
                    if (PieceGotten.piecename.contains("R") && (PieceGotten.getHasmoved() == false) && (PieceGotten.getX() == 0) && (PieceGotten.getY() == 7)) {

                        RookForCastle = PieceGotten;
                        rookfound = third;


                    }


                }


            }

            if (rookfound == -1) {
                return false;


            }


            if (check(CurrentPlayer) == true) {
                return false;

            }


            KingForCastle.castle(game, KingForCastle.getX(), 6);
            ((Piece) CurrentPlayer.Pieces.get(kingfound)).setX(KingForCastle.getX());
            ((Piece) CurrentPlayer.Pieces.get(kingfound)).setY(6);
            ((Piece) CurrentPlayer.Pieces.get(kingfound)).setHasmoved(true);

            RookForCastle.castle(game, RookForCastle.getX(), 5);
            ((Piece) CurrentPlayer.Pieces.get(rookfound)).setX(RookForCastle.getX());
            ((Piece) CurrentPlayer.Pieces.get(rookfound)).setY(5);
            ((Piece) CurrentPlayer.Pieces.get(rookfound)).setHasmoved(true);
            return true;

        } else if (Space.equals("O-O-O")) {
            for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {

                PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                if (PieceGotten.piecename.contains("K") && (PieceGotten.hasmoved == false)) {

                    KingForCastle = PieceGotten;
                    kingfound = i;

                }

            }

            if (kingfound == -1) {
                return false;

            }

            for (int second = KingForCastle.getY() - 1; second > 0; second--) {
                if (game.tiles[KingForCastle.getX()][second].isOccupied()) {
                    return false;

                }

            }


            for (int third = 0; third < CurrentPlayer.Pieces.size(); third++) {

                PieceGotten = (Piece) CurrentPlayer.Pieces.get(third);

                if (CurrentPlayer.color.equals("White")) {
                    if (PieceGotten.piecename.contains("R") && (PieceGotten.getHasmoved() == false) && (PieceGotten.getX() == 7) && (PieceGotten.getY() == 0)) {

                        RookForCastle = PieceGotten;
                        rookfound = third;


                    }

                } else {
                    if (PieceGotten.piecename.contains("R") && (PieceGotten.getHasmoved() == false) && (PieceGotten.getX() == 0) && (PieceGotten.getY() == 0)) {

                        RookForCastle = PieceGotten;
                        rookfound = third;


                    }


                }


            }

            if (rookfound == -1) {
                return false;


            }


            if (check(CurrentPlayer) == true) {
                return false;

            }


            KingForCastle.castle(game, KingForCastle.getX(), 2);
            ((Piece) CurrentPlayer.Pieces.get(kingfound)).setX(KingForCastle.getX());
            ((Piece) CurrentPlayer.Pieces.get(kingfound)).setY(2);
            ((Piece) CurrentPlayer.Pieces.get(kingfound)).setHasmoved(true);

            RookForCastle.castle(game, RookForCastle.getX(), 3);
            ((Piece) CurrentPlayer.Pieces.get(rookfound)).setX(RookForCastle.getX());
            ((Piece) CurrentPlayer.Pieces.get(rookfound)).setY(3);
            ((Piece) CurrentPlayer.Pieces.get(rookfound)).setHasmoved(true);
            return true;

        }


        if (Space.charAt(Space.length() - 1) == '+') {
            Spaceentered = Character.toString(Space.charAt(Space.length() - 3)) + Character.toString(Space.charAt(Space.length() - 2));
        } else {
            Spaceentered = Character.toString(Space.charAt(Space.length() - 2)) + Character.toString(Space.charAt(Space.length() - 1));
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                if (game.tiles[i][j].nameofSpace.equals(Spaceentered)) {
                    x = i;
                    y = j;


                }

            }

        }


        if (x == -1) {
            System.out.println("That is not a valid move");
            return false;


        }


        if (Space.charAt(0) == 'K') {
            if (checkspaceinput(Space, "K", x, y, Opposingplayer) == true) {

                return true;

            } else {
                return false;

            }


        } else if (Space.charAt(0) == 'Q') {
            if (checkspaceinput(Space, "Q", x, y, Opposingplayer) == true) {

                return true;

            } else {
                return false;

            }


        } else if (Space.charAt(0) == 'B') {

            if (CurrentPlayer == Black) {

                if (checkspaceinput(Space, "BB", x, y, Opposingplayer) == true) {

                    return true;

                } else {
                    return false;

                }

            } else if (checkspaceinput(Space, "B", x, y, Opposingplayer) == true) {

                return true;

            } else {
                return false;

            }


        } else if (Space.charAt(0) == 'N') {
            if (checkspaceinput(Space, "N", x, y, Opposingplayer) == true) {

                return true;

            } else {
                return false;

            }


        } else if (Space.charAt(0) == 'R') {
            if (checkspaceinput(Space, "R", x, y, Opposingplayer) == true) {

                return true;

            } else {
                return false;

            }


        } else if ((Space.charAt(1) == 'x') && (Space.length() == 4)) {
            if (Space.charAt(0) == 'a') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);


                    if (PieceGotten.getY() == 0) {

                        if (PieceGotten.piecename.contains("P")) {

                            if (PieceGotten.calculate(game, x, y) == true) {

                                Originalx = PieceGotten.getX();
                                Originaly = PieceGotten.getY();
                                OriginalHasMoved = PieceGotten.hasmoved;


                                if (check(CurrentPlayer) == true) {
                                    return false;

                                }

                                if (game.tiles[x][y].isOccupied() == true) {
                                    for (int n = 0; n < Opposingplayersize; n++) {

                                        Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                        if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                            Opposingplayer.Pieces.remove(n);
                                            n--;
                                            Opposingplayersize = Opposingplayersize - 1;
                                            taken = true;


                                        }

                                    }

                                }
                                PieceGotten.move(game, x, y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                if (check(CurrentPlayer) == true) {
                                    PieceGotten.reset(game, Originalx, Originaly);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                     ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                    return false;

                                }


                                return true;


                            }

                        }
                    }
                }


            } else if (Space.charAt(0) == 'b') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                    if (PieceGotten.getY() == 1) {

                        if (PieceGotten.piecename.contains("P")) {

                            if (PieceGotten.calculate(game, x, y) == true) {

                                Originalx = PieceGotten.getX();
                                Originaly = PieceGotten.getY();
                                OriginalHasMoved = PieceGotten.hasmoved;

                                if (game.tiles[x][y].isOccupied() == true) {
                                    for (int n = 0; n < Opposingplayersize; n++) {

                                        Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                        if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                            Opposingplayer.Pieces.remove(n);
                                            n--;
                                            Opposingplayersize = Opposingplayersize - 1;
                                            taken = true;


                                        }

                                    }

                                }

                                PieceGotten.move(game, x, y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                if (check(CurrentPlayer) == true) {
                                    PieceGotten.reset(game, Originalx, Originaly);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                     ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                    return false;

                                }
                                return true;


                            }

                        }
                    }
                }


            } else if (Space.charAt(0) == 'c') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                    if (PieceGotten.getY() == 2) {

                        if (PieceGotten.piecename.contains("P")) {

                            if (PieceGotten.calculate(game, x, y) == true) {

                                Originalx = PieceGotten.getX();
                                Originaly = PieceGotten.getY();
                                OriginalHasMoved = PieceGotten.hasmoved;
                                if (game.tiles[x][y].isOccupied() == true) {
                                    for (int n = 0; n < Opposingplayersize; n++) {

                                        Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                        if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                            Opposingplayer.Pieces.remove(n);
                                            n--;
                                            Opposingplayersize = Opposingplayersize - 1;
                                            taken = true;


                                        }

                                    }

                                }
                                PieceGotten.move(game, x, y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                if (check(CurrentPlayer) == true) {
                                    PieceGotten.reset(game, Originalx, Originaly);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                     ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                    return false;

                                }
                                return true;


                            }

                        }
                    }
                }


            } else if (Space.charAt(0) == 'd') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                    if (PieceGotten.getY() == 3) {

                        if (PieceGotten.piecename.contains("P")) {

                            if (PieceGotten.calculate(game, x, y) == true) {

                                Originalx = PieceGotten.getX();
                                Originaly = PieceGotten.getY();
                                OriginalHasMoved = PieceGotten.hasmoved;
                                if (game.tiles[x][y].isOccupied() == true) {
                                    for (int n = 0; n < Opposingplayersize; n++) {

                                        Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                        if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                            Opposingplayer.Pieces.remove(n);
                                            n--;
                                            Opposingplayersize = Opposingplayersize - 1;
                                            taken = true;


                                        }

                                    }

                                }

                                PieceGotten.move(game, x, y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                if (check(CurrentPlayer) == true) {
                                    PieceGotten.reset(game, Originalx, Originaly);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                     ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                    return false;

                                }
                                return true;


                            }

                        }
                    }
                }


            } else if (Space.charAt(0) == 'e') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                    if (PieceGotten.getY() == 4) {

                        if (PieceGotten.piecename.contains("P")) {

                            if (PieceGotten.calculate(game, x, y) == true) {

                                Originalx = PieceGotten.getX();
                                Originaly = PieceGotten.getY();
                                OriginalHasMoved = PieceGotten.hasmoved;
                                if (game.tiles[x][y].isOccupied() == true) {
                                    for (int n = 0; n < Opposingplayersize; n++) {

                                        Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                        if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                            Opposingplayer.Pieces.remove(n);
                                            n--;
                                            Opposingplayersize = Opposingplayersize - 1;
                                            taken = true;


                                        }

                                    }

                                }
                                PieceGotten.move(game, x, y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                if (check(CurrentPlayer) == true) {
                                    PieceGotten.reset(game, Originalx, Originaly);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                     ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                    return false;

                                }
                                return true;


                            }

                        }
                    }
                }


            } else if (Space.charAt(0) == 'f') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                    if (PieceGotten.getY() == 5) {

                        if (PieceGotten.piecename.contains("P")) {

                            if (PieceGotten.calculate(game, x, y) == true) {

                                Originalx = PieceGotten.getX();
                                Originaly = PieceGotten.getY();
                                OriginalHasMoved = PieceGotten.hasmoved;
                                if (game.tiles[x][y].isOccupied() == true) {
                                    for (int n = 0; n < Opposingplayersize; n++) {

                                        Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                        if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                            Opposingplayer.Pieces.remove(n);
                                            n--;
                                            Opposingplayersize = Opposingplayersize - 1;
                                            taken = true;


                                        }

                                    }

                                }
                                PieceGotten.move(game, x, y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                if (check(CurrentPlayer) == true) {
                                    PieceGotten.reset(game, Originalx, Originaly);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                     ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                    return false;

                                }
                                return true;


                            }

                        }
                    }
                }


            } else if (Space.charAt(0) == 'g') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                    if (PieceGotten.getY() == 6) {

                        if (PieceGotten.piecename.contains("P")) {

                            if (PieceGotten.calculate(game, x, y) == true) {

                                Originalx = PieceGotten.getX();
                                Originaly = PieceGotten.getY();
                                OriginalHasMoved = PieceGotten.hasmoved;
                                if (game.tiles[x][y].isOccupied() == true) {
                                    for (int n = 0; n < Opposingplayersize; n++) {

                                        Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                        if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                            Opposingplayer.Pieces.remove(n);
                                            n--;
                                            Opposingplayersize = Opposingplayersize - 1;
                                            taken = true;


                                        }

                                    }

                                }
                                PieceGotten.move(game, x, y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                if (check(CurrentPlayer) == true) {
                                    PieceGotten.reset(game, Originalx, Originaly);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                     ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                    return false;

                                }
                                return true;


                            }

                        }
                    }
                }


            } else if (Space.charAt(0) == 'h') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                    if (PieceGotten.getY() == 7) {

                        if (PieceGotten.piecename.contains("P")) {

                            if (PieceGotten.calculate(game, x, y) == true) {

                                Originalx = PieceGotten.getX();
                                Originaly = PieceGotten.getY();
                                OriginalHasMoved = PieceGotten.hasmoved;
                                if (game.tiles[x][y].isOccupied() == true) {
                                    for (int n = 0; n < Opposingplayersize; n++) {

                                        Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                        if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                            Opposingplayer.Pieces.remove(n);
                                            n--;
                                            Opposingplayersize = Opposingplayersize - 1;
                                            taken = true;


                                        }

                                    }

                                }
                                PieceGotten.move(game, x, y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                if (check(CurrentPlayer) == true) {
                                    PieceGotten.reset(game, Originalx, Originaly);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                     ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                    return false;

                                }
                                return true;


                            }

                        }
                    }
                }


            } else {
                return false;

            }

        } else {
            for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                if (PieceGotten.piecename.contains("P")) {

                    if (PieceGotten.calculate(game, x, y) == true) {

                        Originalx = PieceGotten.getX();
                        Originaly = PieceGotten.getY();
                        OriginalHasMoved = PieceGotten.hasmoved;
                        if (game.tiles[x][y].isOccupied() == true) {
                            for (int n = 0; n < Opposingplayersize; n++) {

                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                    Opposingplayer.Pieces.remove(n);
                                    n--;
                                    Opposingplayersize = Opposingplayersize - 1;
                                    taken = true;


                                }

                            }

                        }
                        PieceGotten.move(game, x, y);
                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                        if (check(CurrentPlayer) == true) {
                            PieceGotten.reset(game, Originalx, Originaly);
                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                            return false;

                        }
                        return true;


                    }

                }
            }

        }


        return false;
    }

    public void save_game() {
        try {
            FileOutputStream st = new FileOutputStream("Game.txt");
            ObjectOutputStream ot = new ObjectOutputStream(st);
            ot.writeObject(this);
            ot.close();
            //st.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public boolean check(Player CurrentPlayer) {
        int x = 0;
        int y = 0;
        Player NonCurrentPlayer;
        Piece PieceGotten;

        if (CurrentPlayer == White) {
            NonCurrentPlayer = Black;

        } else {
            NonCurrentPlayer = White;

        }

        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {

            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

            if (PieceGotten.piecename.contains("K")) {

                x = PieceGotten.getX();
                y = PieceGotten.getY();

            }
        }

        for (int i = 0; i < NonCurrentPlayer.Pieces.size(); i++) {

            PieceGotten = (Piece) NonCurrentPlayer.Pieces.get(i);


            if (PieceGotten.calculate(game, x, y) == true) {

                return true;
            }


        }


        return false;

    }

    public boolean Checkmate(Player CurrentPlayer) {


        int x = 0;
        int y = 0;
        int xmodified;
        int ymodified;
        int count = 0;
        boolean occupied;
        Piece spacepiece;


        Player NonCurrentPlayer;
        Piece PieceGotten;

        if (CurrentPlayer == White) {
            NonCurrentPlayer = Black;

        } else {
            NonCurrentPlayer = White;

        }


        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {

            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

            if (PieceGotten.piecename.contains("K")) {

                x = PieceGotten.getX();
                y = PieceGotten.getY();
                game.tiles[x][y].setOccupied(false);
            }
        }

        xmodified = x;
        ymodified = y;

        for (int j = 0; j < CurrentPlayer.Pieces.size() + 9; j++) {

            count = 0;
            for (int i = 0; i < NonCurrentPlayer.Pieces.size(); i++) {


                PieceGotten = (Piece) NonCurrentPlayer.Pieces.get(i);

                if (((xmodified < 8) && (xmodified > -1)) && ((ymodified < 8) && (ymodified > -1))) {

                    occupied = game.tiles[xmodified][ymodified].occupied;
                    spacepiece = game.tiles[xmodified][ymodified].pieceoccuping;


                    game.tiles[xmodified][ymodified].setOccupied(true);
                    game.tiles[xmodified][ymodified].setPieceoccuping(new King(CurrentPlayer.color));

                    if (PieceGotten.calculate(game, xmodified, ymodified) == false) {
                        count = count + 1;
                    }

                    game.tiles[xmodified][ymodified].setOccupied(occupied);
                    game.tiles[xmodified][ymodified].setPieceoccuping(spacepiece);
                }


            }

            if (count == NonCurrentPlayer.Pieces.size()) {
                game.tiles[x][y].setOccupied(true);
                return false;
            }

            if (j == 0) {
                xmodified = x + 1;

            } else if (j == 1) {
                xmodified = x - 1;

            } else if (j == 2) {
                xmodified = x + 1;
                ymodified = y + 1;

            } else if (j == 3) {
                xmodified = x - 1;
                ymodified = y - 1;


            } else if (j == 4) {
                xmodified = x;
                ymodified = y - 1;
            } else if (j == 5) {
                xmodified = x + 1;
                ymodified = y - 1;
            } else if (j == 6) {
                xmodified = x - 1;
                ymodified = y + 1;
            } else if (j == 7) {
                xmodified = x;
                ymodified = y + 1;
            }


        }

        return true;

    }


    public boolean checkspaceinput(String Space, String Name, int x, int y, Player Opposingplayer) {

        Piece PieceGotten;
        Piece Opposingpiece = null;
        int Originalx;
        int Originaly;
        boolean OriginalHasMoved;
        boolean taken = false;

        int Opposingplayersize = Opposingplayer.Pieces.size();


        if (Space.length() == 6) {
            if ((Space.charAt(3) == 'x') || (Space.charAt(5) == '+')) {

                if (Space.charAt(1) == 'a') {

                    if (Space.charAt(2) == '1') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 0) && (PieceGotten.getX() == 7)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '2') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 0) && (PieceGotten.getX() == 6)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }

                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '3') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 0) && (PieceGotten.getX() == 5)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '4') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 0) && (PieceGotten.getX() == 4)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '5') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 0) && (PieceGotten.getX() == 3)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '6') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 0) && (PieceGotten.getX() == 2)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '7') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 0) && (PieceGotten.getX() == 1)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '8') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 0) && (PieceGotten.getX() == 0)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else {
                        return false;

                    }


                } else if (Space.charAt(1) == 'b') {
                    if (Space.charAt(2) == '1') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 1) && (PieceGotten.getX() == 7)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '2') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 1) && (PieceGotten.getX() == 6)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '3') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 1) && (PieceGotten.getX() == 5)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '4') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 1) && (PieceGotten.getX() == 4)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '5') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 1) && (PieceGotten.getX() == 3)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '6') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 1) && (PieceGotten.getX() == 2)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '7') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 1) && (PieceGotten.getX() == 1)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '8') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 1) && (PieceGotten.getX() == 0)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else {
                        return false;

                    }


                } else if (Space.charAt(1) == 'c') {
                    if (Space.charAt(2) == '1') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 2) && (PieceGotten.getX() == 7)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '2') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 2) && (PieceGotten.getX() == 6)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '3') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 2) && (PieceGotten.getX() == 5)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '4') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 2) && (PieceGotten.getX() == 4)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '5') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 2) && (PieceGotten.getX() == 3)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '6') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 2) && (PieceGotten.getX() == 2)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '7') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 2) && (PieceGotten.getX() == 1)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '8') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 2) && (PieceGotten.getX() == 0)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else {
                        return false;

                    }


                } else if (Space.charAt(1) == 'd') {
                    if (Space.charAt(2) == '1') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 3) && (PieceGotten.getX() == 7)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '2') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 3) && (PieceGotten.getX() == 6)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '3') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 3) && (PieceGotten.getX() == 5)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '4') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 3) && (PieceGotten.getX() == 4)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '5') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 3) && (PieceGotten.getX() == 3)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '6') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 3) && (PieceGotten.getX() == 2)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '7') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 3) && (PieceGotten.getX() == 1)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '8') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 3) && (PieceGotten.getX() == 0)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else {
                        return false;

                    }


                } else if (Space.charAt(1) == 'e') {
                    if (Space.charAt(2) == '1') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 4) && (PieceGotten.getX() == 7)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '2') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 4) && (PieceGotten.getX() == 6)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '3') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 4) && (PieceGotten.getX() == 5)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '4') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 4) && (PieceGotten.getX() == 4)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '5') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 4) && (PieceGotten.getX() == 3)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '6') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 4) && (PieceGotten.getX() == 2)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '7') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 4) && (PieceGotten.getX() == 1)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '8') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 4) && (PieceGotten.getX() == 0)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else {
                        return false;

                    }
                } else if (Space.charAt(1) == 'f') {
                    if (Space.charAt(2) == '1') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 5) && (PieceGotten.getX() == 7)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '2') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 5) && (PieceGotten.getX() == 6)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '3') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 5) && (PieceGotten.getX() == 5)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '4') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 5) && (PieceGotten.getX() == 4)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '5') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 5) && (PieceGotten.getX() == 3)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '6') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 5) && (PieceGotten.getX() == 2)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '7') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 5) && (PieceGotten.getX() == 1)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '8') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 5) && (PieceGotten.getX() == 0)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else {
                        return false;

                    }

                } else if (Space.charAt(1) == 'g') {
                    if (Space.charAt(2) == '1') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 6) && (PieceGotten.getX() == 7)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '2') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 6) && (PieceGotten.getX() == 6)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '3') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 6) && (PieceGotten.getX() == 5)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '4') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 6) && (PieceGotten.getX() == 4)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '5') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 6) && (PieceGotten.getX() == 3)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '6') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 6) && (PieceGotten.getX() == 2)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '7') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 6) && (PieceGotten.getX() == 1)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '8') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 6) && (PieceGotten.getX() == 0)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else {
                        return false;

                    }
                } else if (Space.charAt(1) == 'h') {
                    if (Space.charAt(2) == '1') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 7) && (PieceGotten.getX() == 7)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '2') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 7) && (PieceGotten.getX() == 6)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '3') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 7) && (PieceGotten.getX() == 5)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '4') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 7) && (PieceGotten.getX() == 4)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '5') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 7) && (PieceGotten.getX() == 3)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '6') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 7) && (PieceGotten.getX() == 2)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '7') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 7) && (PieceGotten.getX() == 1)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else if (Space.charAt(2) == '8') {
                        for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                            PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                            if ((PieceGotten.getY() == 7) && (PieceGotten.getX() == 0)) {

                                if (PieceGotten.piecename.contains(Name)) {

                                    if (PieceGotten.calculate(game, x, y) == true) {

                                        Originalx = PieceGotten.getX();
                                        Originaly = PieceGotten.getY();
                                        OriginalHasMoved = PieceGotten.hasmoved;
                                        if (game.tiles[x][y].isOccupied() == true) {
                                            for (int n = 0; n < Opposingplayersize; n++) {

                                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                    Opposingplayer.Pieces.remove(n);
                                                    n--;
                                                    Opposingplayersize = Opposingplayersize - 1;
                                                    taken = true;


                                                }

                                            }

                                        }
                                        PieceGotten.move(game, x, y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                        if (check(CurrentPlayer) == true) {
                                            PieceGotten.reset(game, Originalx, Originaly);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                            return false;

                                        }
                                        return true;


                                    }

                                }
                            }
                        }
                    } else {
                        return false;

                    }
                } else if (Space.charAt(1) == '1') {
                    for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                        PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                        if (PieceGotten.getX() == 7) {

                            if (PieceGotten.piecename.contains(Name)) {

                                if (PieceGotten.calculate(game, x, y) == true) {

                                    Originalx = PieceGotten.getX();
                                    Originaly = PieceGotten.getY();
                                    OriginalHasMoved = PieceGotten.hasmoved;
                                    if (game.tiles[x][y].isOccupied() == true) {
                                        for (int n = 0; n < Opposingplayersize; n++) {

                                            Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                            if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                Opposingplayer.Pieces.remove(n);
                                                n--;
                                                Opposingplayersize = Opposingplayersize - 1;
                                                taken = true;


                                            }

                                        }

                                    }
                                    PieceGotten.move(game, x, y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                    if (check(CurrentPlayer) == true) {
                                        PieceGotten.reset(game, Originalx, Originaly);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                         ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                        return false;

                                    }
                                    return true;


                                }

                            }
                        }
                    }

                } else if (Space.charAt(1) == '2') {
                    for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                        PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                        if (PieceGotten.getX() == 6) {

                            if (PieceGotten.piecename.contains(Name)) {

                                if (PieceGotten.calculate(game, x, y) == true) {

                                    Originalx = PieceGotten.getX();
                                    Originaly = PieceGotten.getY();
                                    OriginalHasMoved = PieceGotten.hasmoved;
                                    if (game.tiles[x][y].isOccupied() == true) {
                                        for (int n = 0; n < Opposingplayersize; n++) {

                                            Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                            if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                Opposingplayer.Pieces.remove(n);
                                                n--;
                                                Opposingplayersize = Opposingplayersize - 1;
                                                taken = true;


                                            }

                                        }

                                    }
                                    PieceGotten.move(game, x, y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                    if (check(CurrentPlayer) == true) {
                                        PieceGotten.reset(game, Originalx, Originaly);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                         ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                        return false;

                                    }
                                    return true;


                                }

                            }
                        }
                    }

                } else if (Space.charAt(1) == '3') {
                    for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                        PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                        if (PieceGotten.getX() == 5) {

                            if (PieceGotten.piecename.contains(Name)) {

                                if (PieceGotten.calculate(game, x, y) == true) {

                                    Originalx = PieceGotten.getX();
                                    Originaly = PieceGotten.getY();
                                    OriginalHasMoved = PieceGotten.hasmoved;
                                    if (game.tiles[x][y].isOccupied() == true) {
                                        for (int n = 0; n < Opposingplayersize; n++) {

                                            Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                            if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                Opposingplayer.Pieces.remove(n);
                                                n--;
                                                Opposingplayersize = Opposingplayersize - 1;
                                                taken = true;


                                            }

                                        }

                                    }
                                    PieceGotten.move(game, x, y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                    if (check(CurrentPlayer) == true) {
                                        PieceGotten.reset(game, Originalx, Originaly);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                         ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                        return false;

                                    }
                                    return true;


                                }

                            }
                        }
                    }

                } else if (Space.charAt(1) == '4') {
                    for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                        PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                        if (PieceGotten.getX() == 4) {

                            if (PieceGotten.piecename.contains(Name)) {

                                if (PieceGotten.calculate(game, x, y) == true) {

                                    Originalx = PieceGotten.getX();
                                    Originaly = PieceGotten.getY();
                                    OriginalHasMoved = PieceGotten.hasmoved;
                                    if (game.tiles[x][y].isOccupied() == true) {
                                        for (int n = 0; n < Opposingplayersize; n++) {

                                            Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                            if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                Opposingplayer.Pieces.remove(n);
                                                n--;
                                                Opposingplayersize = Opposingplayersize - 1;
                                                taken = true;


                                            }

                                        }

                                    }
                                    PieceGotten.move(game, x, y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                    if (check(CurrentPlayer) == true) {
                                        PieceGotten.reset(game, Originalx, Originaly);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                         ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                        return false;

                                    }
                                    return true;


                                }

                            }
                        }
                    }

                } else if (Space.charAt(1) == '5') {
                    for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                        PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                        if (PieceGotten.getX() == 3) {

                            if (PieceGotten.piecename.contains(Name)) {

                                if (PieceGotten.calculate(game, x, y) == true) {

                                    Originalx = PieceGotten.getX();
                                    Originaly = PieceGotten.getY();
                                    OriginalHasMoved = PieceGotten.hasmoved;
                                    if (game.tiles[x][y].isOccupied() == true) {
                                        for (int n = 0; n < Opposingplayersize; n++) {

                                            Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                            if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                Opposingplayer.Pieces.remove(n);
                                                n--;
                                                Opposingplayersize = Opposingplayersize - 1;
                                                taken = true;


                                            }

                                        }

                                    }
                                    PieceGotten.move(game, x, y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                    if (check(CurrentPlayer) == true) {
                                        PieceGotten.reset(game, Originalx, Originaly);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                         ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                        return false;

                                    }
                                    return true;


                                }

                            }
                        }
                    }

                } else if (Space.charAt(1) == '6') {
                    for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                        PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                        if (PieceGotten.getX() == 2) {

                            if (PieceGotten.piecename.contains(Name)) {

                                if (PieceGotten.calculate(game, x, y) == true) {

                                    Originalx = PieceGotten.getX();
                                    Originaly = PieceGotten.getY();
                                    OriginalHasMoved = PieceGotten.hasmoved;
                                    if (game.tiles[x][y].isOccupied() == true) {
                                        for (int n = 0; n < Opposingplayersize; n++) {

                                            Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                            if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                Opposingplayer.Pieces.remove(n);
                                                n--;
                                                Opposingplayersize = Opposingplayersize - 1;
                                                taken = true;


                                            }

                                        }

                                    }
                                    PieceGotten.move(game, x, y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                    if (check(CurrentPlayer) == true) {
                                        PieceGotten.reset(game, Originalx, Originaly);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                         ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                        return false;

                                    }
                                    return true;


                                }

                            }
                        }
                    }

                } else if (Space.charAt(1) == '7') {
                    for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                        PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                        if (PieceGotten.getX() == 1) {

                            if (PieceGotten.piecename.contains(Name)) {

                                if (PieceGotten.calculate(game, x, y) == true) {

                                    Originalx = PieceGotten.getX();
                                    Originaly = PieceGotten.getY();
                                    OriginalHasMoved = PieceGotten.hasmoved;
                                    if (game.tiles[x][y].isOccupied() == true) {
                                        for (int n = 0; n < Opposingplayersize; n++) {

                                            Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                            if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                Opposingplayer.Pieces.remove(n);
                                                n--;
                                                Opposingplayersize = Opposingplayersize - 1;
                                                taken = true;


                                            }

                                        }

                                    }
                                    PieceGotten.move(game, x, y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                    if (check(CurrentPlayer) == true) {
                                        PieceGotten.reset(game, Originalx, Originaly);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                         ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                        return false;

                                    }
                                    return true;


                                }

                            }
                        }
                    }

                } else if (Space.charAt(1) == '8') {
                    for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                        PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                        if (PieceGotten.getX() == 0) {

                            if (PieceGotten.piecename.contains(Name)) {

                                if (PieceGotten.calculate(game, x, y) == true) {

                                    Originalx = PieceGotten.getX();
                                    Originaly = PieceGotten.getY();
                                    OriginalHasMoved = PieceGotten.hasmoved;
                                    if (game.tiles[x][y].isOccupied() == true) {
                                        for (int n = 0; n < Opposingplayersize; n++) {

                                            Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                            if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                Opposingplayer.Pieces.remove(n);
                                                n--;
                                                Opposingplayersize = Opposingplayersize - 1;
                                                taken = true;


                                            }

                                        }

                                    }
                                    PieceGotten.move(game, x, y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                    if (check(CurrentPlayer) == true) {
                                        PieceGotten.reset(game, Originalx, Originaly);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                         ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                        return false;

                                    }
                                    return true;


                                }

                            }
                        }
                    }

                } else {
                    return false;

                }


            } else {
                return false;
            }


        } else if (Space.length() == 5) {
            if ((Space.charAt(2) == 'x') || (Space.charAt(4) == '+')) {

                if (Space.charAt(1) == 'a') {
                    for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                        PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                        if (PieceGotten.getY() == 0) {

                            if (PieceGotten.piecename.contains(Name)) {

                                if (PieceGotten.calculate(game, x, y) == true) {

                                    Originalx = PieceGotten.getX();
                                    Originaly = PieceGotten.getY();
                                    OriginalHasMoved = PieceGotten.hasmoved;
                                    if (game.tiles[x][y].isOccupied() == true) {
                                        for (int n = 0; n < Opposingplayersize; n++) {

                                            Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                            if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                Opposingplayer.Pieces.remove(n);
                                                n--;
                                                Opposingplayersize = Opposingplayersize - 1;
                                                taken = true;


                                            }

                                        }

                                    }
                                    PieceGotten.move(game, x, y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                    if (check(CurrentPlayer) == true) {
                                        PieceGotten.reset(game, Originalx, Originaly);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                         ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                        return false;

                                    }
                                    return true;


                                }

                            }
                        }
                    }


                } else if (Space.charAt(1) == 'b') {
                    for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                        PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                        if (PieceGotten.getY() == 1) {

                            if (PieceGotten.piecename.contains(Name)) {

                                if (PieceGotten.calculate(game, x, y) == true) {

                                    Originalx = PieceGotten.getX();
                                    Originaly = PieceGotten.getY();
                                    OriginalHasMoved = PieceGotten.hasmoved;
                                    if (game.tiles[x][y].isOccupied() == true) {
                                        for (int n = 0; n < Opposingplayersize; n++) {

                                            Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                            if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                Opposingplayer.Pieces.remove(n);
                                                n--;
                                                Opposingplayersize = Opposingplayersize - 1;
                                                taken = true;


                                            }

                                        }

                                    }
                                    PieceGotten.move(game, x, y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                    if (check(CurrentPlayer) == true) {
                                        PieceGotten.reset(game, Originalx, Originaly);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                         ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                        return false;

                                    }
                                    return true;


                                }

                            }
                        }
                    }


                } else if (Space.charAt(1) == 'c') {
                    for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                        PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                        if (PieceGotten.getY() == 2) {

                            if (PieceGotten.piecename.contains(Name)) {

                                if (PieceGotten.calculate(game, x, y) == true) {

                                    Originalx = PieceGotten.getX();
                                    Originaly = PieceGotten.getY();
                                    OriginalHasMoved = PieceGotten.hasmoved;
                                    if (game.tiles[x][y].isOccupied() == true) {
                                        for (int n = 0; n < Opposingplayersize; n++) {

                                            Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                            if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                Opposingplayer.Pieces.remove(n);
                                                n--;
                                                Opposingplayersize = Opposingplayersize - 1;
                                                taken = true;


                                            }

                                        }

                                    }
                                    PieceGotten.move(game, x, y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                    if (check(CurrentPlayer) == true) {
                                        PieceGotten.reset(game, Originalx, Originaly);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                         ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                        return false;

                                    }
                                    return true;


                                }

                            }
                        }
                    }


                } else if (Space.charAt(1) == 'd') {
                    for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                        PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                        if (PieceGotten.getY() == 3) {

                            if (PieceGotten.piecename.contains(Name)) {

                                if (PieceGotten.calculate(game, x, y) == true) {

                                    Originalx = PieceGotten.getX();
                                    Originaly = PieceGotten.getY();
                                    OriginalHasMoved = PieceGotten.hasmoved;
                                    if (game.tiles[x][y].isOccupied() == true) {
                                        for (int n = 0; n < Opposingplayersize; n++) {

                                            Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                            if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                Opposingplayer.Pieces.remove(n);
                                                n--;
                                                Opposingplayersize = Opposingplayersize - 1;
                                                taken = true;


                                            }

                                        }

                                    }
                                    PieceGotten.move(game, x, y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                    if (check(CurrentPlayer) == true) {
                                        PieceGotten.reset(game, Originalx, Originaly);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                         ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                        return false;

                                    }
                                    return true;


                                }

                            }
                        }
                    }


                } else if (Space.charAt(1) == 'e') {
                    for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                        PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                        if (PieceGotten.getY() == 4) {

                            if (PieceGotten.piecename.contains(Name)) {

                                if (PieceGotten.calculate(game, x, y) == true) {

                                    Originalx = PieceGotten.getX();
                                    Originaly = PieceGotten.getY();
                                    OriginalHasMoved = PieceGotten.hasmoved;
                                    if (game.tiles[x][y].isOccupied() == true) {
                                        for (int n = 0; n < Opposingplayersize; n++) {

                                            Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                            if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                Opposingplayer.Pieces.remove(n);
                                                n--;
                                                Opposingplayersize = Opposingplayersize - 1;
                                                taken = true;


                                            }

                                        }

                                    }
                                    PieceGotten.move(game, x, y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                    if (check(CurrentPlayer) == true) {
                                        PieceGotten.reset(game, Originalx, Originaly);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                         ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                        return false;

                                    }
                                    return true;


                                }

                            }
                        }
                    }
                } else if (Space.charAt(1) == 'f') {
                    for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                        PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                        if (PieceGotten.getY() == 5) {

                            if (PieceGotten.piecename.contains(Name)) {

                                if (PieceGotten.calculate(game, x, y) == true) {

                                    Originalx = PieceGotten.getX();
                                    Originaly = PieceGotten.getY();
                                    OriginalHasMoved = PieceGotten.hasmoved;
                                    if (game.tiles[x][y].isOccupied() == true) {
                                        for (int n = 0; n < Opposingplayersize; n++) {

                                            Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                            if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                Opposingplayer.Pieces.remove(n);
                                                n--;
                                                Opposingplayersize = Opposingplayersize - 1;
                                                taken = true;


                                            }

                                        }

                                    }
                                    PieceGotten.move(game, x, y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                    if (check(CurrentPlayer) == true) {
                                        PieceGotten.reset(game, Originalx, Originaly);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                         ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                        return false;

                                    }
                                    return true;


                                }

                            }
                        }
                    }


                } else if (Space.charAt(1) == 'g') {
                    for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                        PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                        if (PieceGotten.getY() == 6) {

                            if (PieceGotten.piecename.contains(Name)) {

                                if (PieceGotten.calculate(game, x, y) == true) {

                                    Originalx = PieceGotten.getX();
                                    Originaly = PieceGotten.getY();
                                    OriginalHasMoved = PieceGotten.hasmoved;
                                    if (game.tiles[x][y].isOccupied() == true) {
                                        for (int n = 0; n < Opposingplayersize; n++) {

                                            Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                            if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                Opposingplayer.Pieces.remove(n);
                                                n--;
                                                Opposingplayersize = Opposingplayersize - 1;
                                                taken = true;


                                            }

                                        }

                                    }
                                    PieceGotten.move(game, x, y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                    if (check(CurrentPlayer) == true) {
                                        PieceGotten.reset(game, Originalx, Originaly);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                         ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                        return false;

                                    }
                                    return true;


                                }

                            }
                        }
                    }
                } else if (Space.charAt(1) == 'h') {
                    for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                        PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                        if (PieceGotten.getY() == 7) {

                            if (PieceGotten.piecename.contains(Name)) {

                                if (PieceGotten.calculate(game, x, y) == true) {

                                    Originalx = PieceGotten.getX();
                                    Originaly = PieceGotten.getY();
                                    OriginalHasMoved = PieceGotten.hasmoved;
                                    if (game.tiles[x][y].isOccupied() == true) {
                                        for (int n = 0; n < Opposingplayersize; n++) {

                                            Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                            if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                Opposingplayer.Pieces.remove(n);
                                                n--;
                                                Opposingplayersize = Opposingplayersize - 1;
                                                taken = true;


                                            }

                                        }

                                    }
                                    PieceGotten.move(game, x, y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                    if (check(CurrentPlayer) == true) {
                                        PieceGotten.reset(game, Originalx, Originaly);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                         ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                        return false;

                                    }
                                    return true;


                                }

                            }
                        }
                    }
                } else if (Space.charAt(1) == '1') {
                    for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                        PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                        if (PieceGotten.getX() == 7) {

                            if (PieceGotten.piecename.contains(Name)) {

                                if (PieceGotten.calculate(game, x, y) == true) {

                                    Originalx = PieceGotten.getX();
                                    Originaly = PieceGotten.getY();
                                    OriginalHasMoved = PieceGotten.hasmoved;
                                    if (game.tiles[x][y].isOccupied() == true) {
                                        for (int n = 0; n < Opposingplayersize; n++) {

                                            Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                            if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                Opposingplayer.Pieces.remove(n);
                                                n--;
                                                Opposingplayersize = Opposingplayersize - 1;
                                                taken = true;


                                            }

                                        }

                                    }
                                    PieceGotten.move(game, x, y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                    if (check(CurrentPlayer) == true) {
                                        PieceGotten.reset(game, Originalx, Originaly);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                         ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                        return false;

                                    }
                                    return true;


                                }

                            }
                        }
                    }

                } else if (Space.charAt(1) == '2') {
                    for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                        PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                        if (PieceGotten.getX() == 6) {

                            if (PieceGotten.piecename.contains(Name)) {

                                if (PieceGotten.calculate(game, x, y) == true) {

                                    Originalx = PieceGotten.getX();
                                    Originaly = PieceGotten.getY();
                                    OriginalHasMoved = PieceGotten.hasmoved;
                                    if (game.tiles[x][y].isOccupied() == true) {
                                        for (int n = 0; n < Opposingplayersize; n++) {

                                            Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                            if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                Opposingplayer.Pieces.remove(n);
                                                n--;
                                                Opposingplayersize = Opposingplayersize - 1;
                                                taken = true;


                                            }

                                        }

                                    }
                                    PieceGotten.move(game, x, y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                    if (check(CurrentPlayer) == true) {
                                        PieceGotten.reset(game, Originalx, Originaly);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                         ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                        return false;

                                    }
                                    return true;


                                }

                            }
                        }
                    }

                } else if (Space.charAt(1) == '3') {
                    for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                        PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                        if (PieceGotten.getX() == 5) {

                            if (PieceGotten.piecename.contains(Name)) {

                                if (PieceGotten.calculate(game, x, y) == true) {

                                    Originalx = PieceGotten.getX();
                                    Originaly = PieceGotten.getY();
                                    OriginalHasMoved = PieceGotten.hasmoved;
                                    if (game.tiles[x][y].isOccupied() == true) {
                                        for (int n = 0; n < Opposingplayersize; n++) {

                                            Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                            if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                Opposingplayer.Pieces.remove(n);
                                                n--;
                                                Opposingplayersize = Opposingplayersize - 1;
                                                taken = true;


                                            }

                                        }

                                    }
                                    PieceGotten.move(game, x, y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                    if (check(CurrentPlayer) == true) {
                                        PieceGotten.reset(game, Originalx, Originaly);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                         ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                        return false;

                                    }
                                    return true;


                                }

                            }
                        }
                    }

                } else if (Space.charAt(1) == '4') {
                    for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                        PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                        if (PieceGotten.getX() == 4) {

                            if (PieceGotten.piecename.contains(Name)) {

                                if (PieceGotten.calculate(game, x, y) == true) {

                                    Originalx = PieceGotten.getX();
                                    Originaly = PieceGotten.getY();
                                    OriginalHasMoved = PieceGotten.hasmoved;
                                    if (game.tiles[x][y].isOccupied() == true) {
                                        for (int n = 0; n < Opposingplayersize; n++) {

                                            Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                            if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                Opposingplayer.Pieces.remove(n);
                                                n--;
                                                Opposingplayersize = Opposingplayersize - 1;
                                                taken = true;


                                            }

                                        }

                                    }
                                    PieceGotten.move(game, x, y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                    if (check(CurrentPlayer) == true) {
                                        PieceGotten.reset(game, Originalx, Originaly);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                         ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                        return false;

                                    }
                                    return true;


                                }

                            }
                        }
                    }

                } else if (Space.charAt(1) == '5') {
                    for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                        PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                        if (PieceGotten.getX() == 3) {

                            if (PieceGotten.piecename.contains(Name)) {

                                if (PieceGotten.calculate(game, x, y) == true) {

                                    Originalx = PieceGotten.getX();
                                    Originaly = PieceGotten.getY();
                                    OriginalHasMoved = PieceGotten.hasmoved;
                                    if (game.tiles[x][y].isOccupied() == true) {
                                        for (int n = 0; n < Opposingplayersize; n++) {

                                            Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                            if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                Opposingplayer.Pieces.remove(n);
                                                n--;
                                                Opposingplayersize = Opposingplayersize - 1;
                                                taken = true;


                                            }

                                        }

                                    }
                                    PieceGotten.move(game, x, y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                    if (check(CurrentPlayer) == true) {
                                        PieceGotten.reset(game, Originalx, Originaly);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                         ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                        return false;

                                    }
                                    return true;


                                }

                            }
                        }
                    }

                } else if (Space.charAt(1) == '6') {
                    for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                        PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                        if (PieceGotten.getX() == 2) {

                            if (PieceGotten.piecename.contains(Name)) {

                                if (PieceGotten.calculate(game, x, y) == true) {

                                    Originalx = PieceGotten.getX();
                                    Originaly = PieceGotten.getY();
                                    OriginalHasMoved = PieceGotten.hasmoved;
                                    if (game.tiles[x][y].isOccupied() == true) {
                                        for (int n = 0; n < Opposingplayersize; n++) {

                                            Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                            if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                Opposingplayer.Pieces.remove(n);
                                                n--;
                                                Opposingplayersize = Opposingplayersize - 1;
                                                taken = true;


                                            }

                                        }

                                    }
                                    PieceGotten.move(game, x, y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                    if (check(CurrentPlayer) == true) {
                                        PieceGotten.reset(game, Originalx, Originaly);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                         ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                        return false;

                                    }
                                    return true;


                                }

                            }
                        }
                    }

                } else if (Space.charAt(1) == '7') {
                    for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                        PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                        if (PieceGotten.getX() == 1) {

                            if (PieceGotten.piecename.contains(Name)) {

                                if (PieceGotten.calculate(game, x, y) == true) {

                                    Originalx = PieceGotten.getX();
                                    Originaly = PieceGotten.getY();
                                    OriginalHasMoved = PieceGotten.hasmoved;
                                    if (game.tiles[x][y].isOccupied() == true) {
                                        for (int n = 0; n < Opposingplayersize; n++) {

                                            Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                            if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                Opposingplayer.Pieces.remove(n);
                                                n--;
                                                Opposingplayersize = Opposingplayersize - 1;
                                                taken = true;


                                            }

                                        }

                                    }
                                    PieceGotten.move(game, x, y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                    if (check(CurrentPlayer) == true) {
                                        PieceGotten.reset(game, Originalx, Originaly);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                         ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                        return false;

                                    }
                                    return true;


                                }

                            }
                        }
                    }

                } else if (Space.charAt(1) == '8') {
                    for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                        PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                        if (PieceGotten.getX() == 0) {

                            if (PieceGotten.piecename.contains(Name)) {

                                if (PieceGotten.calculate(game, x, y) == true) {

                                    Originalx = PieceGotten.getX();
                                    Originaly = PieceGotten.getY();
                                    OriginalHasMoved = PieceGotten.hasmoved;
                                    if (game.tiles[x][y].isOccupied() == true) {
                                        for (int n = 0; n < Opposingplayersize; n++) {

                                            Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                            if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                                Opposingplayer.Pieces.remove(n);
                                                n--;
                                                Opposingplayersize = Opposingplayersize - 1;
                                                taken = true;


                                            }

                                        }

                                    }
                                    PieceGotten.move(game, x, y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                    if (check(CurrentPlayer) == true) {
                                        PieceGotten.reset(game, Originalx, Originaly);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                         ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                        return false;

                                    }
                                    return true;


                                }

                            }
                        }
                    }

                } else {
                    return false;

                }


            } else {
                return false;
            }


        } else if (Space.length() == 4) {
            if (Space.charAt(1) == 'a') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                    if (PieceGotten.getY() == 0) {

                        if (PieceGotten.piecename.contains(Name)) {

                            if (PieceGotten.calculate(game, x, y) == true) {

                                Originalx = PieceGotten.getX();
                                Originaly = PieceGotten.getY();
                                OriginalHasMoved = PieceGotten.hasmoved;
                                if (game.tiles[x][y].isOccupied() == true) {
                                    for (int n = 0; n < Opposingplayersize; n++) {

                                        Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                        if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                            Opposingplayer.Pieces.remove(n);
                                            n--;
                                            Opposingplayersize = Opposingplayersize - 1;
                                            taken = true;


                                        }

                                    }

                                }
                                PieceGotten.move(game, x, y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                if (check(CurrentPlayer) == true) {
                                    PieceGotten.reset(game, Originalx, Originaly);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                     ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                    return false;

                                }
                                return true;


                            }

                        }
                    }
                }


            } else if (Space.charAt(1) == 'b') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                    if (PieceGotten.getY() == 1) {

                        if (PieceGotten.piecename.contains(Name)) {

                            if (PieceGotten.calculate(game, x, y) == true) {

                                Originalx = PieceGotten.getX();
                                Originaly = PieceGotten.getY();
                                OriginalHasMoved = PieceGotten.hasmoved;
                                if (game.tiles[x][y].isOccupied() == true) {
                                    for (int n = 0; n < Opposingplayersize; n++) {

                                        Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                        if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                            Opposingplayer.Pieces.remove(n);
                                            n--;
                                            Opposingplayersize = Opposingplayersize - 1;
                                            taken = true;


                                        }

                                    }

                                }
                                PieceGotten.move(game, x, y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                if (check(CurrentPlayer) == true) {
                                    PieceGotten.reset(game, Originalx, Originaly);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                     ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                    return false;

                                }
                                return true;


                            }

                        }
                    }
                }


            } else if (Space.charAt(1) == 'c') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                    if (PieceGotten.getY() == 2) {

                        if (PieceGotten.piecename.contains(Name)) {

                            if (PieceGotten.calculate(game, x, y) == true) {

                                Originalx = PieceGotten.getX();
                                Originaly = PieceGotten.getY();
                                OriginalHasMoved = PieceGotten.hasmoved;
                                if (game.tiles[x][y].isOccupied() == true) {
                                    for (int n = 0; n < Opposingplayersize; n++) {

                                        Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                        if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                            Opposingplayer.Pieces.remove(n);
                                            n--;
                                            Opposingplayersize = Opposingplayersize - 1;
                                            taken = true;


                                        }

                                    }

                                }
                                PieceGotten.move(game, x, y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                if (check(CurrentPlayer) == true) {
                                    PieceGotten.reset(game, Originalx, Originaly);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                     ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                    return false;

                                }
                                return true;


                            }

                        }
                    }
                }


            } else if (Space.charAt(1) == 'd') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                    if (PieceGotten.getY() == 3) {

                        if (PieceGotten.piecename.contains(Name)) {

                            if (PieceGotten.calculate(game, x, y) == true) {

                                Originalx = PieceGotten.getX();
                                Originaly = PieceGotten.getY();
                                OriginalHasMoved = PieceGotten.hasmoved;
                                if (game.tiles[x][y].isOccupied() == true) {
                                    for (int n = 0; n < Opposingplayersize; n++) {

                                        Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                        if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                            Opposingplayer.Pieces.remove(n);
                                            n--;
                                            Opposingplayersize = Opposingplayersize - 1;
                                            taken = true;


                                        }

                                    }

                                }
                                PieceGotten.move(game, x, y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                if (check(CurrentPlayer) == true) {
                                    PieceGotten.reset(game, Originalx, Originaly);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                     ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                    return false;

                                }
                                return true;


                            }

                        }
                    }
                }


            } else if (Space.charAt(1) == 'e') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                    if (PieceGotten.getY() == 4) {

                        if (PieceGotten.piecename.contains(Name)) {

                            if (PieceGotten.calculate(game, x, y) == true) {

                                Originalx = PieceGotten.getX();
                                Originaly = PieceGotten.getY();
                                OriginalHasMoved = PieceGotten.hasmoved;
                                if (game.tiles[x][y].isOccupied() == true) {
                                    for (int n = 0; n < Opposingplayersize; n++) {

                                        Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                        if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                            Opposingplayer.Pieces.remove(n);
                                            n--;
                                            Opposingplayersize = Opposingplayersize - 1;
                                            taken = true;


                                        }

                                    }

                                }
                                PieceGotten.move(game, x, y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                if (check(CurrentPlayer) == true) {
                                    PieceGotten.reset(game, Originalx, Originaly);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                     ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                    return false;

                                }
                                return true;


                            }

                        }
                    }
                }
            } else if (Space.charAt(1) == 'f') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                    if (PieceGotten.getY() == 5) {

                        if (PieceGotten.piecename.contains(Name)) {

                            if (PieceGotten.calculate(game, x, y) == true) {

                                Originalx = PieceGotten.getX();
                                Originaly = PieceGotten.getY();
                                OriginalHasMoved = PieceGotten.hasmoved;
                                if (game.tiles[x][y].isOccupied() == true) {
                                    for (int n = 0; n < Opposingplayersize; n++) {

                                        Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                        if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                            Opposingplayer.Pieces.remove(n);
                                            n--;
                                            Opposingplayersize = Opposingplayersize - 1;
                                            taken = true;


                                        }

                                    }

                                }
                                PieceGotten.move(game, x, y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                if (check(CurrentPlayer) == true) {
                                    PieceGotten.reset(game, Originalx, Originaly);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                     ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                    return false;

                                }
                                return true;


                            }

                        }
                    }
                }


            } else if (Space.charAt(1) == 'g') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                    if (PieceGotten.getY() == 6) {

                        if (PieceGotten.piecename.contains(Name)) {

                            if (PieceGotten.calculate(game, x, y) == true) {

                                Originalx = PieceGotten.getX();
                                Originaly = PieceGotten.getY();
                                OriginalHasMoved = PieceGotten.hasmoved;
                                if (game.tiles[x][y].isOccupied() == true) {
                                    for (int n = 0; n < Opposingplayersize; n++) {

                                        Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                        if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                            Opposingplayer.Pieces.remove(n);
                                            n--;
                                            Opposingplayersize = Opposingplayersize - 1;
                                            taken = true;


                                        }

                                    }

                                }
                                PieceGotten.move(game, x, y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                if (check(CurrentPlayer) == true) {
                                    PieceGotten.reset(game, Originalx, Originaly);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                     ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                    return false;

                                }
                                return true;


                            }

                        }
                    }
                }
            } else if (Space.charAt(1) == 'h') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                    if (PieceGotten.getY() == 7) {

                        if (PieceGotten.piecename.contains(Name)) {

                            if (PieceGotten.calculate(game, x, y) == true) {

                                Originalx = PieceGotten.getX();
                                Originaly = PieceGotten.getY();
                                OriginalHasMoved = PieceGotten.hasmoved;
                                if (game.tiles[x][y].isOccupied() == true) {
                                    for (int n = 0; n < Opposingplayersize; n++) {

                                        Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                        if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                            Opposingplayer.Pieces.remove(n);
                                            n--;
                                            Opposingplayersize = Opposingplayersize - 1;
                                            taken = true;


                                        }

                                    }

                                }
                                PieceGotten.move(game, x, y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                if (check(CurrentPlayer) == true) {
                                    PieceGotten.reset(game, Originalx, Originaly);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                     ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                    return false;

                                }
                                return true;


                            }

                        }
                    }
                }
            } else if (Space.charAt(1) == 'x') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);


                    if (PieceGotten.piecename.contains(Name)) {

                        if (PieceGotten.calculate(game, x, y) == true) {

                            Originalx = PieceGotten.getX();
                            Originaly = PieceGotten.getY();
                            OriginalHasMoved = PieceGotten.hasmoved;
                            if (game.tiles[x][y].isOccupied() == true) {
                                for (int n = 0; n < Opposingplayersize; n++) {

                                    Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                    if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                        Opposingplayer.Pieces.remove(n);
                                        n--;
                                        Opposingplayersize = Opposingplayersize - 1;
                                        taken = true;


                                    }

                                }

                            }
                            PieceGotten.move(game, x, y);
                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                            ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                            if (check(CurrentPlayer) == true) {
                                PieceGotten.reset(game, Originalx, Originaly);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                 ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                return false;

                            }
                            return true;


                        }

                    }

                }


            } else if (Space.charAt(1) == '1') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                    if (PieceGotten.getX() == 7) {

                        if (PieceGotten.piecename.contains(Name)) {

                            if (PieceGotten.calculate(game, x, y) == true) {

                                Originalx = PieceGotten.getX();
                                Originaly = PieceGotten.getY();
                                OriginalHasMoved = PieceGotten.hasmoved;
                                if (game.tiles[x][y].isOccupied() == true) {
                                    for (int n = 0; n < Opposingplayersize; n++) {

                                        Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                        if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                            Opposingplayer.Pieces.remove(n);
                                            n--;
                                            Opposingplayersize = Opposingplayersize - 1;
                                            taken = true;


                                        }

                                    }

                                }
                                PieceGotten.move(game, x, y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                if (check(CurrentPlayer) == true) {
                                    PieceGotten.reset(game, Originalx, Originaly);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                     ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                    return false;

                                }
                                return true;


                            }

                        }
                    }
                }

            } else if (Space.charAt(1) == '2') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                    if (PieceGotten.getX() == 6) {

                        if (PieceGotten.piecename.contains(Name)) {

                            if (PieceGotten.calculate(game, x, y) == true) {

                                Originalx = PieceGotten.getX();
                                Originaly = PieceGotten.getY();
                                OriginalHasMoved = PieceGotten.hasmoved;
                                if (game.tiles[x][y].isOccupied() == true) {
                                    for (int n = 0; n < Opposingplayersize; n++) {

                                        Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                        if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                            Opposingplayer.Pieces.remove(n);
                                            n--;
                                            Opposingplayersize = Opposingplayersize - 1;
                                            taken = true;


                                        }

                                    }

                                }
                                PieceGotten.move(game, x, y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                if (check(CurrentPlayer) == true) {
                                    PieceGotten.reset(game, Originalx, Originaly);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                     ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                    return false;

                                }
                                return true;


                            }

                        }
                    }
                }

            } else if (Space.charAt(1) == '3') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                    if (PieceGotten.getX() == 5) {

                        if (PieceGotten.piecename.contains(Name)) {

                            if (PieceGotten.calculate(game, x, y) == true) {

                                Originalx = PieceGotten.getX();
                                Originaly = PieceGotten.getY();
                                OriginalHasMoved = PieceGotten.hasmoved;
                                if (game.tiles[x][y].isOccupied() == true) {
                                    for (int n = 0; n < Opposingplayersize; n++) {

                                        Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                        if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                            Opposingplayer.Pieces.remove(n);
                                            n--;
                                            Opposingplayersize = Opposingplayersize - 1;
                                            taken = true;


                                        }

                                    }

                                }
                                PieceGotten.move(game, x, y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                if (check(CurrentPlayer) == true) {
                                    PieceGotten.reset(game, Originalx, Originaly);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                     ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                    return false;

                                }
                                return true;


                            }

                        }
                    }
                }

            } else if (Space.charAt(1) == '4') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                    if (PieceGotten.getX() == 4) {

                        if (PieceGotten.piecename.contains(Name)) {

                            if (PieceGotten.calculate(game, x, y) == true) {

                                Originalx = PieceGotten.getX();
                                Originaly = PieceGotten.getY();
                                OriginalHasMoved = PieceGotten.hasmoved;
                                if (game.tiles[x][y].isOccupied() == true) {
                                    for (int n = 0; n < Opposingplayersize; n++) {

                                        Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                        if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                            Opposingplayer.Pieces.remove(n);
                                            n--;
                                            Opposingplayersize = Opposingplayersize - 1;
                                            taken = true;


                                        }

                                    }

                                }
                                PieceGotten.move(game, x, y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                if (check(CurrentPlayer) == true) {
                                    PieceGotten.reset(game, Originalx, Originaly);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                     ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                    return false;

                                }
                                return true;


                            }

                        }
                    }
                }

            } else if (Space.charAt(1) == '5') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                    if (PieceGotten.getX() == 3) {

                        if (PieceGotten.piecename.contains(Name)) {

                            if (PieceGotten.calculate(game, x, y) == true) {

                                Originalx = PieceGotten.getX();
                                Originaly = PieceGotten.getY();
                                OriginalHasMoved = PieceGotten.hasmoved;
                                if (game.tiles[x][y].isOccupied() == true) {
                                    for (int n = 0; n < Opposingplayersize; n++) {

                                        Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                        if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                            Opposingplayer.Pieces.remove(n);
                                            n--;
                                            Opposingplayersize = Opposingplayersize - 1;
                                            taken = true;


                                        }

                                    }

                                }
                                PieceGotten.move(game, x, y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                if (check(CurrentPlayer) == true) {
                                    PieceGotten.reset(game, Originalx, Originaly);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                     ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                    return false;

                                }
                                return true;


                            }

                        }
                    }
                }

            } else if (Space.charAt(1) == '6') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                    if (PieceGotten.getX() == 2) {

                        if (PieceGotten.piecename.contains(Name)) {

                            if (PieceGotten.calculate(game, x, y) == true) {

                                Originalx = PieceGotten.getX();
                                Originaly = PieceGotten.getY();
                                OriginalHasMoved = PieceGotten.hasmoved;
                                if (game.tiles[x][y].isOccupied() == true) {
                                    for (int n = 0; n < Opposingplayersize; n++) {

                                        Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                        if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                            Opposingplayer.Pieces.remove(n);
                                            n--;
                                            Opposingplayersize = Opposingplayersize - 1;
                                            taken = true;


                                        }

                                    }

                                }
                                PieceGotten.move(game, x, y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                if (check(CurrentPlayer) == true) {
                                    PieceGotten.reset(game, Originalx, Originaly);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                     ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                    return false;

                                }
                                return true;


                            }

                        }
                    }
                }

            } else if (Space.charAt(1) == '7') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                    if (PieceGotten.getX() == 1) {

                        if (PieceGotten.piecename.contains(Name)) {

                            if (PieceGotten.calculate(game, x, y) == true) {

                                Originalx = PieceGotten.getX();
                                Originaly = PieceGotten.getY();
                                OriginalHasMoved = PieceGotten.hasmoved;
                                if (game.tiles[x][y].isOccupied() == true) {
                                    for (int n = 0; n < Opposingplayersize; n++) {

                                        Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                        if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                            Opposingplayer.Pieces.remove(n);
                                            n--;
                                            Opposingplayersize = Opposingplayersize - 1;
                                            taken = true;


                                        }

                                    }

                                }
                                PieceGotten.move(game, x, y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                if (check(CurrentPlayer) == true) {
                                    PieceGotten.reset(game, Originalx, Originaly);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                     ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                    return false;

                                }
                                return true;


                            }

                        }
                    }
                }

            } else if (Space.charAt(1) == '8') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {


                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                    if (PieceGotten.getX() == 0) {

                        if (PieceGotten.piecename.contains(Name)) {

                            if (PieceGotten.calculate(game, x, y) == true) {

                                Originalx = PieceGotten.getX();
                                Originaly = PieceGotten.getY();
                                OriginalHasMoved = PieceGotten.hasmoved;
                                if (game.tiles[x][y].isOccupied() == true) {
                                    for (int n = 0; n < Opposingplayersize; n++) {

                                        Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                        if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                            Opposingplayer.Pieces.remove(n);
                                            n--;
                                            Opposingplayersize = Opposingplayersize - 1;
                                            taken = true;


                                        }

                                    }

                                }
                                PieceGotten.move(game, x, y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                                if (check(CurrentPlayer) == true) {
                                    PieceGotten.reset(game, Originalx, Originaly);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                    ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                     ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                    return false;

                                }
                                return true;


                            }

                        }
                    }
                }

            } else if (Space.charAt(3) == '+') {
                for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {

                    PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                    if (PieceGotten.piecename.contains(Name)) {

                        if (PieceGotten.calculate(game, x, y) == true) {

                            Originalx = PieceGotten.getX();
                            Originaly = PieceGotten.getY();
                            OriginalHasMoved = PieceGotten.hasmoved;
                            if (game.tiles[x][y].isOccupied() == true) {
                                for (int n = 0; n < Opposingplayersize; n++) {

                                    Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                    if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                        Opposingplayer.Pieces.remove(n);
                                        n--;
                                        Opposingplayersize = Opposingplayersize - 1;
                                        taken = true;


                                    }

                                }

                            }
                            PieceGotten.move(game, x, y);
                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                            ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                            if (check(CurrentPlayer) == true) {
                                PieceGotten.reset(game, Originalx, Originaly);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                                ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                                 ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                                return false;

                            }
                            return true;


                        }

                    }
                }


            } else {
                return false;

            }
        } else if (Space.length() == 3) {
            for (int i = 0; i < CurrentPlayer.Pieces.size(); i++) {

                PieceGotten = (Piece) CurrentPlayer.Pieces.get(i);

                if (PieceGotten.piecename.contains(Name)) {

                    if (PieceGotten.calculate(game, x, y) == true) {

                        Originalx = PieceGotten.getX();
                        Originaly = PieceGotten.getY();
                        OriginalHasMoved = PieceGotten.hasmoved;
                        if (game.tiles[x][y].isOccupied() == true) {
                            for (int n = 0; n < Opposingplayersize; n++) {

                                Opposingpiece = (Piece) Opposingplayer.Pieces.get(n);

                                if ((Opposingpiece.x == x) && (Opposingpiece.y == y)) {
                                    Opposingplayer.Pieces.remove(n);
                                    n--;
                                    Opposingplayersize = Opposingplayersize - 1;
                                    taken = true;


                                }

                            }

                        }
                        PieceGotten.move(game, x, y);
                        ((Piece) CurrentPlayer.Pieces.get(i)).setX(x);
                        ((Piece) CurrentPlayer.Pieces.get(i)).setY(y);
                        ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(true);
                        if (check(CurrentPlayer) == true) {
                            PieceGotten.reset(game, Originalx, Originaly);
                            ((Piece) CurrentPlayer.Pieces.get(i)).setX(Originalx);
                            ((Piece) CurrentPlayer.Pieces.get(i)).setY(Originaly);
                             ((Piece) CurrentPlayer.Pieces.get(i)).setHasmoved(OriginalHasMoved);
                                            if(taken == true){
                                                game.tiles[x][y].setPieceoccuping(Opposingpiece);
                                                game.tiles[x][y].pieceoccuping.setX(x);
                                                game.tiles[x][y].pieceoccuping.setY(y);
                                                game.tiles[x][y].setOccupied(true);
                                                game.tiles[i][y].pieceoccuping.setPiecename(Opposingpiece.getPiecename());
                                                Opposingplayer.Pieces.add(game.tiles[x][y].pieceoccuping);
                                                
                                            }


                            return false;

                        }
                        return true;


                    }

                }
            }


        }
        return false;
    }

    public void removepiecefromarray(String Piececolor, int x, int y) {

        Piece PieceGotten;
        Player OpposingPlayer;

        if (Piececolor == "White") {
            OpposingPlayer = Black;

        } else {

            OpposingPlayer = White;
        }


        for (int i = 0; i < OpposingPlayer.Pieces.size(); i++) {

            PieceGotten = (Piece) OpposingPlayer.Pieces.get(i);

            if ((PieceGotten.getY() == y) && (PieceGotten.getX() == x)) {

                OpposingPlayer.Pieces.remove(i);

            }
        }


    }


    public static void DeleteSave() {
        File myObj = new File("Game.txt");
        if (myObj.delete()) {
            System.out.println("Deleted the file: " + myObj.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }


    }

    public boolean Stalemate(Player currentPlayer) {

        if ((White.Pieces.size() == 1) && Black.Pieces.size() == 1) {

            return true;

        }


        return false;
    }


}



















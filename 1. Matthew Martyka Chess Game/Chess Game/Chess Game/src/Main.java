
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        Scanner input = new Scanner(System.in);
        int option;
        do {
            try {


                option = MainMenu(input);

                if (option == 1) {

                    game.startgame(true);


                } else if (option == 2) {

                    game = load_game();
                    game.startgame(false);


                } else if (option == 3) {

                    System.exit(0);


                } else {

                    System.out.println("That is not a valid input");

                }
            } catch (Exception e) {
                System.out.println("That is not a valid input");
                input.nextLine();

            }

        } while (true);


    }

    public static int MainMenu(Scanner input) {
        System.out.println("1) New Game 2) Resume Game 3) Quit");

        return input.nextInt();


    }


    public static Game load_game() {
        Game myGame = new Game();
        try {
            FileInputStream st = new FileInputStream("Game.txt");
            ObjectInputStream ot = new ObjectInputStream(st);
            myGame = (Game) ot.readObject();
            ot.close();
        } catch (Exception ex) {
            System.out.println("Previous Game Does Not Exist");
        }
        return myGame;
    }

}


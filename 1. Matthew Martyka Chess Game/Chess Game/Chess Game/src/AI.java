import java.io.Serializable;

public class AI implements Serializable {

    AI(){



    }


    public String GenerateMove(){
        String move = "";
        int random;

        random = (int)(Math.random() * 8);

        if (random == 0){
            move = move + "K";
        }
        else if(random == 1){
            move = move + "Q";

        } else if(random == 2){
            move = move + "B";

        } else if(random == 3){
            move = move + "N";

        } else if(random == 4){
            move = move + "R";

        }else if(random == 5){
            return "O-O";


        }else if (random == 6){

            return "O-O-O";

        }else{


        }



        random = (int)(Math.random() * 8);
        if (random == 0){
            move = move + "a";


        }else if (random == 1){
            move = move + "b";

        }
        else if (random == 2){
            move = move + "c";


        }else if (random == 3){
            move = move + "d";


        }else if (random == 4){
            move = move + "e";


        }else if (random == 5){
            move = move + "f";


        }else if (random == 6){
            move = move + "g";

        }else{
            move = move + "h";

        }

        random = (int)(Math.random()* 8);
        if (random == 0){
            move = move + "1";


        }else if (random == 1){
            move = move + "2";

        }
        else if (random == 2){
            move = move + "3";


        }else if (random == 3){
            move = move + "4";


        }else if (random == 4){
            move = move + "5";


        }else if (random == 5){
            move = move + "6";


        }else if (random == 6){
            move = move + "7";

        }else{
            move = move + "8";

        }



        return move;

    }




}

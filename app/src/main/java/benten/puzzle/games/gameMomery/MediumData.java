package benten.puzzle.games.gameMomery;

import java.util.ArrayList;
import java.util.Random;

import benten.puzzle.games.R;



public class MediumData {
//    image size 1500 *1000


    ArrayList<Integer> numberArray =new ArrayList<>();



    int [] arraImagRtage ={R.drawable.one_medium,R.drawable.two_medium,R.drawable.three_medium,R.drawable.foure_medium};


    int range = arraImagRtage.length;
    int myImgCount = 0;


    Random random = new Random();


    public  ArrayList<Integer> getMediumData() {

        for( int i = 0; i < range; ++i)
        {
            myImgCount = random.nextInt(range);
            if(!numberArray.contains(arraImagRtage[myImgCount])){
                numberArray.add(arraImagRtage[myImgCount]);
            }
        }

        return numberArray;
    }
}

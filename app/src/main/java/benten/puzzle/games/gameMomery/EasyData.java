package benten.puzzle.games.gameMomery;

import java.util.ArrayList;
import java.util.Random;

import benten.puzzle.games.R;


/**
 * Created by falcon on 12/10/2017.
 */

public class EasyData {
//    size image 704 *704


    ArrayList<Integer> numberArray =new ArrayList<>();




    int [] arraImagRtage ={
            R.drawable.cute,
            R.drawable.batote,
            R.drawable.esy_one,
            R.drawable.boys,
            R.drawable.esy_two,
            R.drawable.garil,
            R.drawable.s
            ,R.drawable.esy_three};


    int range = arraImagRtage.length;
    int myImgCount = 0;

    Random random = new Random();



    public  ArrayList<Integer> getEasyDataArray() {

        for( int i = 0; i < range; ++i)
        {
             if(!numberArray.contains(arraImagRtage[i])){
                numberArray.add(arraImagRtage[i]);
            }
        }

        if(numberArray.size()<range+1){
            for( int i = 0; i < range; ++i)
            {
                 if(!numberArray.contains(arraImagRtage[i])){
                    numberArray.add(arraImagRtage[i]);
                }
            }

        }



        return numberArray;
    }

//
//    public  ArrayList<Integer> getEasyDataArray() {
//
//        for( int i = 0; i < range; ++i)
//        {
//            myImgCount = random.nextInt(range);
//            if(!numberArray.contains(arraImagRtage[myImgCount])){
//                numberArray.add(arraImagRtage[myImgCount]);
//            }
//        }
//
//        if(numberArray.size()<range+1){
//            for( int i = 0; i < range; ++i)
//            {
//                myImgCount = random.nextInt(range);
//                if(!numberArray.contains(arraImagRtage[myImgCount])){
//                    numberArray.add(arraImagRtage[myImgCount]);
//                }
//            }
//
//        }
//
//
//
//        return numberArray;
//    }




}

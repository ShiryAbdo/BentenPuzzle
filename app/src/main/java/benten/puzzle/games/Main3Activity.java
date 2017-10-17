package benten.puzzle.games;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import benten.puzzle.games.gamePuzzle.ImageFromYourGalayActivity;
import benten.puzzle.games.gamePuzzle.MainPuzzle;
import benten.puzzle.games.gamePuzzle.SlidePuzzle;
import benten.puzzle.games.gamePuzzle.SlidePuzzleView;

public class Main3Activity extends AppCompatActivity {
    SlidePuzzleView slidePuzzleView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        slidePuzzleView = (SlidePuzzleView)findViewById(R.id.slider);


    }
}

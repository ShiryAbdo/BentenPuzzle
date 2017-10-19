package benten.puzzle.games.gamePuzzle;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import benten.puzzle.games.R;
import benten.puzzle.games.gameMomery.DifficultData;
import benten.puzzle.games.gameMomery.EasyData;
import benten.puzzle.games.gameMomery.HardData;
import benten.puzzle.games.gameMomery.MediumData;
import benten.puzzle.games.ui.MainCircleActivity;


public class BaseActivity extends AppCompatActivity {
    Bundle bundle;
    String catogery ;
    Button showImage  ;
    ImageView next ,refresh;

    int imageSourse ;
    EasyData easyData ;
    MediumData mediumData;
    HardData hardData ;
    DifficultData difficultData ;
    ArrayList<Integer> images ;
    String NN;
    final Context context = this;
    Toolbar toolbar ;
    Random random;
    int count =0;
    boolean chhh = false;
    SharedPreferences.Editor editor ,newEditore;
    SharedPreferences sharedPref ,catogerys ;
    int nexti ;
    int score  ,countt;

    public   CountDownTimer countDownTimer;

    private boolean timerHasStarted = false;

    private Button startB;

    public TextView text;

    private final long startTime = 100 * 1000;
    int score_nu ,number ;

    private final long interval = 1 * 1000;
    TextView  scoret,numberOfImage , timerText ,total_score ,total_image ,last_time;
    FrameLayout fragment_container ;
    LinearLayout next_layout ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fragment_container=(FrameLayout)findViewById(R.id.fragment_container);
        setSupportActionBar(toolbar);
        bundle=getIntent().getExtras();
        showImage=(Button)findViewById(R.id.showImage);
        scoret=(TextView)findViewById(R.id.score);
        numberOfImage=(TextView)findViewById(R.id.numberOfImage);
        timerText=(TextView) findViewById(R.id.timerText);
        total_image= (TextView)findViewById(R.id.total_image);
        last_time=(TextView)findViewById(R.id.last_time);
        next_layout =(LinearLayout)findViewById(R.id.next_layout);
//        next_layout.animate().alpha(0.0f);
        countDownTimer = new MyCountDownTimer(startTime, interval);
        countDownTimer.start();
        timerHasStarted = true;
        timerText.setText(String.valueOf(startTime / 1000));








        if(bundle!=null) {
            catogery= bundle.getString("catogery");

        }
        sharedPref = getApplicationContext().getSharedPreferences(catogery, Context.MODE_PRIVATE);
        catogerys=getApplicationContext().getSharedPreferences("catogerys", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        newEditore=catogerys.edit();
        boolean frist_time = sharedPref.getBoolean("frist_time", true);
        countt = sharedPref.getInt("count", 0);
        score = sharedPref.getInt("score", 0);
         if(frist_time){
            count=0;
            countt=0;
//            Toast.makeText(this,"frist",Toast.LENGTH_LONG).show();
        }else{
            count=countt;
//            Toast.makeText(this,"second",Toast.LENGTH_LONG).show();

        }

        Toast.makeText(this,"couunt:  "+count,Toast.LENGTH_LONG).show();
//         scoret.setText(score+"");


        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setTitle(catogery);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setLogo(R.drawable.icone);




        images = new ArrayList<>();
        easyData= new EasyData();
        mediumData= new MediumData();
        hardData=new HardData();
        difficultData= new DifficultData();
        next= (ImageView)findViewById(R.id.next);
        refresh=(ImageView)findViewById(R.id.refresh);


        if(catogery.equals("Easy")){
            toolbar.setTitle(" "+catogery);
            images=easyData.getEasyDataArray();
            imageSourse=images.get(count);
            int number =images.size()-count;
            int range = images.size();
            numberOfImage.setText(number+"");

        }if(catogery.equals("Medium")){
            toolbar.setTitle(" "+catogery);
            images=mediumData.getMediumData();
            imageSourse=images.get(count);
            int number =images.size()-count;
            int range = images.size();
            numberOfImage.setText(number+"");

        }if(catogery.equals("Hard")){
            toolbar.setTitle(" "+catogery);
            images=hardData.getHardData();
            imageSourse=images.get(count);
            int number =images.size()-count;
            int range = images.size();
            numberOfImage.setText(number+"");
        }if(catogery.equals("Difficult")) {
            toolbar.setTitle(" "+catogery);
            images =difficultData.getDifficultData();
            imageSourse = images.get(count);
            int number =images.size()-count;
            int range = images.size();
            numberOfImage.setText(number+"");
        }


        MainPuzzle newFragment = new MainPuzzle();
        Bundle args = new Bundle();
        args.putString("catogery", catogery);
        args.putInt("image",imageSourse);
        newFragment.setArguments(args);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
         transaction.commit();


        showImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // custom dialog
                final Dialog dialog = new Dialog(context, R.style.custom_dialog_theme);
                dialog.setContentView(R.layout.custom);
                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                image.setImageResource(imageSourse);
                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

        total_score =(TextView)findViewById(R.id.total_score);
        scoret.setText(count+"");
        score_nu= ++count;
        total_score.setText(score_nu+"");
        number = images.size() - count;
        int range = images.size();
        total_image.setText("You finish:   "+count +"..."+"From:   "+range);
        last_time.setText(timerText.getText());



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                countDownTimer.cancel();
                countDownTimer.start();


                if(imageSourse==images.size()){


                }else{}
                MainPuzzle newFragment = new MainPuzzle();
                Bundle args = new Bundle();
                args.putString("catogery", catogery);
                if(count==images.size()){
                    // custom dialog
                    final Dialog dialog = new Dialog(context, R.style.custom_dialog_theme);
                    dialog.setContentView(R.layout.dialog_layout);
                    TextView text =(TextView)dialog.findViewById(R.id.text);
                    text.setText("Congratulations you finish"+" "+catogery+" "+"Level");

                    editor.putBoolean("frist_time" ,true );
                    editor.putInt("score" ,0);
                    editor.putString(catogery,"Easy");
                    editor.commit();
                    newEditore.putString(catogery,catogery);
                    newEditore.commit();


                    Button dialogButton = (Button) dialog.findViewById(R.id.backtoMenu);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(),MainCircleActivity.class);
                            startActivity(intent);
                        }
                    });

                    dialog.show();
                }else{
                    numberOfImage.setText(number+"");
                    scoret.setText(score_nu+"");
                    editor.putBoolean("frist_time" ,false );
                    editor.putInt("count" ,score_nu);
                    editor.putInt("score" ,nexti);
                    editor.commit();
                    imageSourse=images.get(nexti);
                    args.putInt("image",images.get(nexti));
                }

                newFragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back

                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
                fab.setVisibility(View.GONE);
                fragment_container.setVisibility(View.VISIBLE);


            }


        });



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.cancel();
                countDownTimer.start();


                if(imageSourse==images.size()){


                }else{}
                MainPuzzle newFragment = new MainPuzzle();
                Bundle args = new Bundle();
                args.putString("catogery", catogery);
                if(count==images.size()){
                    // custom dialog
                    final Dialog dialog = new Dialog(context, R.style.custom_dialog_theme);
                    dialog.setContentView(R.layout.dialog_layout);
                    TextView text =(TextView)dialog.findViewById(R.id.text);
                    text.setText("Congratulations you finish"+" "+catogery+" "+"Level");

                    editor.putBoolean("frist_time" ,true );
                    editor.putInt("score" ,0);
                    editor.putString(catogery,"Easy");
                    editor.commit();
                    newEditore.putString(catogery,catogery);
                    newEditore.commit();


                    Button dialogButton = (Button) dialog.findViewById(R.id.backtoMenu);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(),MainCircleActivity.class);
                            startActivity(intent);
                        }
                    });

                    dialog.show();
                }else{
 //                    scoret.setText(nexti+"");
//                    editor.putBoolean("frist_time" ,false );
//                    editor.putInt("count" ,nexti);
//                    editor.putInt("score" ,nexti);
//                    editor.commit();
                    imageSourse=images.get(nexti);
                    args.putInt("image",images.get(nexti));
                }




                newFragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back

                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

            }


        });


    }




    @Override
    public void onBackPressed() {
        countDownTimer.cancel();
        Intent intent = new Intent(BaseActivity.this,   MainCircleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {

            super(startTime, interval);

        }

        @Override

        public void onFinish() {
            if(score>0){
                editor.putInt("score" ,score-1);
            }
            if(score<0){
                editor.putInt("score" ,0);
            }

            editor.commit();

            timerText.setText("Time's up!");

            final Dialog dialog = new Dialog(context, R.style.custom_dialog_theme);
            dialog.setContentView(R.layout.time_up);
            TextView text =(TextView)dialog.findViewById(R.id.text);
            text.setText("Time's up!");
            Button dialogButton = (Button) dialog.findViewById(R.id.backtoMenu);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),BaseActivity.class);
                    intent.putExtra("catogery",catogery);
                    startActivity(intent);
                    finish();
                }
            });

            dialog.show();


        }



        @Override

        public void onTick(long millisUntilFinished) {


            long secondsRemaining = millisUntilFinished / 1000 + 1;
            last_time.setText(Long.toString(secondsRemaining));
            timerText.setText(Long.toString(secondsRemaining));
            if (secondsRemaining <= 10) {
                timerText.setTextColor(getResources().getColor(R.color.red));
            } else {
                timerText.setTextColor(Color.BLACK);

            }

        }

    }
}

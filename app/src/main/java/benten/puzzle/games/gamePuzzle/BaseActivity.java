package benten.puzzle.games.gamePuzzle;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import benten.puzzle.games.R;
import benten.puzzle.games.data.Json_data_Interface;
import benten.puzzle.games.data.RecyclerViewAdapter;
import benten.puzzle.games.data.json_data;
import benten.puzzle.games.gameMomery.DifficultData;
import benten.puzzle.games.gameMomery.EasyData;
import benten.puzzle.games.gameMomery.HardData;
import benten.puzzle.games.gameMomery.MediumData;
import benten.puzzle.games.ui.MainCircleActivity;
import benten.puzzle.games.user.SplachScreen;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BaseActivity extends AppCompatActivity   implements RewardedVideoAdListener {
    Bundle bundle;
    String catogery ;
    Button showImage  ;
     int size ;
    protected static int DEFAULT_SIZE ;
    int imageSourse  ,imagSersrShow;
    EasyData easyData ;
    MediumData mediumData;
    HardData hardData ;
    DifficultData difficultData ;
    ArrayList<Integer> images ;
     final Context context = this;
    Toolbar toolbar ;
     int count =0;
    boolean chhh = false;
    SharedPreferences.Editor editor ,newEditore ,editor_score;
    SharedPreferences sharedPref ,catogerys ;

    int  countt;
    long score ;
    TextView score_again ;

    public   CountDownTimer countDownTimer;


    private Button startB;

    public TextView text;
      public  boolean check_boolen = false ;

    private long startTime = 100 * 1000;
    long time_longe ;

    ArrayList<Long> score_saved_shared;
    int score_nu ,number  ,rang;
      Dialog dialog;
    SharedPreferences sharedPref_score;
    private final long interval = 1 * 1000;
    TextView  scoret,numberOfImage , timerText ,total_score ,total_image ,last_time;
    FrameLayout fragment_container ;
    LinearLayout next_layout ,puls ,homeLinearLayout;
    ImageView refresh ;
    long secondsRemaining  ,mTimeRemaining;
   long timer_pius = 5  ;
     AdView adView;
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mAd;
 boolean check_pluse =false ;
    Button BackTomenu;

    long Topescore;

    private RecyclerView recyclerView;
    private ArrayList<json_data> data;
    private RecyclerViewAdapter adapter ;
    LinearLayoutManager HorizontalLayout ;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final Button fab = (Button) findViewById(R.id.fab);
        BackTomenu =(Button)findViewById(R.id.BackTomenu);
        fragment_container=(FrameLayout)findViewById(R.id.fragment_container);
        homeLinearLayout=(LinearLayout)findViewById(R.id.homeLinearLayout);
        setSupportActionBar(toolbar);
         initViews();
         adView =   findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
        score_saved_shared =new ArrayList<>();
        data= new ArrayList<>();
        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);



         bundle=getIntent().getExtras();
        showImage= findViewById(R.id.showImage);
        scoret= findViewById(R.id.score);
        score_again= findViewById(R.id.score_again);
        numberOfImage= findViewById(R.id.numberOfImage);
        timerText=  findViewById(R.id.timerText);
        total_image=  findViewById(R.id.total_image);
        last_time= findViewById(R.id.last_time);
        next_layout = findViewById(R.id.next_layout);
        refresh =findViewById(R.id.refresh);
        puls= findViewById(R.id.puls);
//        next_layout.animate().alpha(0.0f);


        if(bundle!=null) {
            catogery= bundle.getString("catogery");

        }

        bundle=getIntent().getExtras();

        if(bundle!=null) {
            size=bundle.getInt("DEFAULT_SIZE");
        }
        DEFAULT_SIZE=size;
        sharedPref = getApplicationContext().getSharedPreferences(catogery, Context.MODE_PRIVATE);
        catogerys=getApplicationContext().getSharedPreferences("catogerys", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        newEditore=catogerys.edit();
        boolean frist_time = sharedPref.getBoolean("frist_time", true);
        countt = sharedPref.getInt("count", 0);
        score = sharedPref.getLong("score", 0);
          if(frist_time){
            count=0;
            countt=0;
         }else{
            count=countt;

        }



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
         refresh=(ImageView)findViewById(R.id.refresh);
        images=easyData.getEasyDataArray();
        imageSourse=images.get(count);
        int number =images.size()-count;
        int range = images.size();

        if(catogery.equals("Easy")){
            toolbar.setTitle(" "+catogery);
            DEFAULT_SIZE=3;
            time_longe =60 ;
            startTime=  60 * 1000;
            Topescore= 6000;
            sharedPref_score = getApplicationContext().getSharedPreferences(catogery, Context.MODE_PRIVATE);



        }if(catogery.equals("Medium")){
            toolbar.setTitle(" "+catogery);
            DEFAULT_SIZE=4;
            time_longe= 120;
            startTime=  120 * 1000;
             Topescore=1200;
            sharedPref_score = getApplicationContext().getSharedPreferences(catogery, Context.MODE_PRIVATE);
//            images=mediumData.getMediumData();
//            imageSourse=images.get(count);


        }if(catogery.equals("Hard")){
            toolbar.setTitle(" "+catogery);
            DEFAULT_SIZE=5;
            time_longe= 180;
            startTime=  180 * 1000;
             Topescore= 18000;
            sharedPref_score = getApplicationContext().getSharedPreferences(catogery, Context.MODE_PRIVATE);
//            images=hardData.getHardData();
//            imageSourse=images.get(count);
        }if(catogery.equals("Difficult")) {
            toolbar.setTitle(" "+catogery);
            DEFAULT_SIZE=6;
            time_longe= 240;
             Topescore= 24000;
            startTime=  240 * 1000;
//            images =difficultData.getDifficultData();
//            imageSourse = images.get(count);

        }
        editor_score = sharedPref_score.edit();
        countDownTimer = new MyCountDownTimer(startTime, interval);
        countDownTimer.start();

         if (next_layout.getVisibility() == View.VISIBLE) {
             countDownTimer.cancel();
             // Its visible
         }
         timerText.setText(String.valueOf(startTime / 1000));
        MainPuzzle newFragment = new MainPuzzle();
        Bundle args = new Bundle();
        args.putString("catogery", catogery);
        args.putInt("image",imageSourse);
        args.putInt("DEFAULT_SIZE",DEFAULT_SIZE);
        newFragment.setArguments(args);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
         transaction.commit();


        BackTomenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BaseActivity.this,MainCircleActivity.class);
                startActivity(intent);
                finish();
            }
        });


         imagSersrShow=images.get(count);

        showImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // custom dialog
                final Dialog dialog = new Dialog(context, R.style.custom_dialog_theme);
                dialog.setContentView(R.layout.custom);
                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                image.setImageResource(imageSourse);
                Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

//                showInterstitial();

            }
        });

        total_score =findViewById(R.id.total_score);
        scoret.setText( score+"");
         score_again.setText(score+"");
         total_score.setText(score_nu+"");
        number = images.size() - count;
        rang = images.size();
         numberOfImage.setText(" "+(count+1)+"/"+rang);
//        total_score.setText((co)+"");
        total_image.setText("Total:   "+(count+1)+"  "+"  / "+rang);
        last_time.setText(timerText.getText());

       refresh.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               countDownTimer.cancel();
                showDialoge();
           }
       });




 int yyy = images.size();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.start();
                showInterstitial();
                 if(count==images.size()-1){
                     countDownTimer.cancel();
                    // custom dialog
                    final Dialog dialog = new Dialog(context, R.style.custom_dialog_theme);
                    dialog.setContentView(R.layout.dialog_layout);
                    TextView text =(TextView)dialog.findViewById(R.id.text);
                    text.setText("Congratulations you finish"+" "+catogery+" "+"Level");
                    editor.putBoolean("frist_time" ,true );
                    editor.putInt("count" ,0);
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
                            dialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(),MainCircleActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                    dialog.show();
                }else{
                     imageSourse=images.get(count);
                     numberOfImage.setText( " "+(count+1)+"/"+rang);
                     MainPuzzle newFragment = new MainPuzzle();
                     Bundle args = new Bundle();
                     args.putString("catogery", catogery);
                     args.putInt("image",images.get(score_nu));
                     args.putInt("DEFAULT_SIZE",DEFAULT_SIZE);
                     newFragment.setArguments(args);
                     FragmentTransaction transaction = getFragmentManager().beginTransaction();
                     transaction.replace(R.id.fragment_container, newFragment);
                     transaction.addToBackStack(null);
                     transaction.commit();
                }


                next_layout.setVisibility(View.GONE);
                fragment_container.setVisibility(View.VISIBLE);
                refresh.setEnabled(true);
                adView.setVisibility(View.VISIBLE);


            }


        });





    }
     public  void showDialoge (){

         final Dialog custom_d = new Dialog(BaseActivity.this, R.style.custom_dialog_theme);
         custom_d.setContentView(R.layout.plus_time);


         Button dialogButton = (Button) custom_d.findViewById(R.id.yes);
         Button cancel = (Button)custom_d.findViewById(R.id.delet);
         cancel.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 custom_d.dismiss();
                 startTime=secondsRemaining * 1000;
                 countDownTimer = new MyCountDownTimer(startTime, interval);
                 countDownTimer.start();
             }
         });
         dialogButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 custom_d.dismiss();
                 countDownTimer.cancel();
                 countDownTimer = new MyCountDownTimer(startTime, interval);
                 countDownTimer.start();
                 MainPuzzle newFragment = new MainPuzzle();
                 Bundle args = new Bundle();
                 args.putString("catogery", catogery);
                 args.putInt("image",imageSourse);
                 args.putInt("DEFAULT_SIZE",DEFAULT_SIZE);
                 newFragment.setArguments(args);
                 FragmentTransaction transaction = getFragmentManager().beginTransaction();
                 transaction.replace(R.id.fragment_container, newFragment);
                 transaction.addToBackStack(null);
                 transaction.commit();

              }
         });

         custom_d.show();
     }





    private void loadInterstitial() {
        // Disable the next level button and load the ad.
         AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }

    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                }

            @Override
            public void onAdFailedToLoad(int errorCode) {
              }

            @Override
            public void onAdClosed() {
                countDownTimer.cancel();
               }
        });
        return interstitialAd;
    }

    public void showInterstitial() {
         if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
          }
    }

    private void loadRewardedVideoAd() {
        countDownTimer.cancel();
        mAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
        if (mAd.isLoaded()) {
            mAd.show();
        }
    }

 public  void newLevel (){
     refresh.setEnabled(true);
     countDownTimer.cancel();
     total_score.setText((count+5)+"");
     imageSourse=images.get(count);
     score_nu= ++count;
     count=score_nu;

     long level_sore  = 1000/mTimeRemaining;
     scoret.setText(level_sore+"");
     score_again.setText(level_sore+"");
     score_saved_shared.add(level_sore);

     editor_score.putLong("score_saved_shared_size", score_saved_shared.size());

     for(int i=0;i<score_saved_shared.size();i++) {
         editor_score.putLong("score_saved_shared_" + i, score_saved_shared.get(i));
      }
     editor_score.commit();

     ArrayList<Long>bast_SCORE= new ArrayList<>();
     long size = sharedPref_score.getLong("score_saved_shared_size", 0);

     if(size!=0){
         for(int i=0;i<size;i++) {
             bast_SCORE.add(sharedPref_score.getLong("score_saved_shared_" + i, 0));
         }
//         array[i] = prefs.getString(arrayName + "_" + i, null);
         Collections.max(bast_SCORE);
         total_score.setText("Best Score: " +Collections.max(bast_SCORE)+"");
     }

//     score_saved_shared
//     editor_score
     total_image.setText(" "+(count)+""+"/"+rang);
     editor.putBoolean("frist_time" ,false );
     editor.putInt("count" ,score_nu);
     editor.putLong("score" ,level_sore);
     editor.commit();


 }


    private void initViews(){


        HorizontalLayout = new LinearLayoutManager(BaseActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview1);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(HorizontalLayout);


        loadJSON();
    }
    private void loadJSON(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://mockmart.info/challenge/public/index.php/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Json_data_Interface request = retrofit.create(Json_data_Interface.class);
        Call<ArrayList<json_data>> call = request.getJSON();
        call.enqueue(new Callback<ArrayList<json_data>>() {
            @Override
            public void onResponse(Call<ArrayList<json_data>> call, Response<ArrayList<json_data>> response) {

                ArrayList<json_data> jsonResponse = response.body();
                data = new ArrayList<>();
                data.addAll(jsonResponse);
                 adapter = new RecyclerViewAdapter(data,BaseActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<json_data>> call, Throwable t) {
                Log.d("Error",t.getMessage());
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

    // Required to reward the user.
    @Override
    public void onRewarded(RewardItem reward) {

        // Reward the user.
    }

    // The following listener methods are optional.
    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
     }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
     }

    @Override
    public void onRewardedVideoAdLoaded() {
        if (mAd.isLoaded()) {
            mAd.show();
        }
     }

    @Override
    public void onRewardedVideoAdOpened() {
     }

    @Override
    public void onRewardedVideoStarted() {
     }


    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {


            super(startTime, interval);

        }

        @Override

        public void onFinish() {
            if(score>0){
                editor.putLong("score" ,score);
            }
            if(score<0){
                editor.putInt("score" ,0);
            }

            editor.commit();

            timerText.setText("Time's up!");
            loadRewardedVideoAd();

            dialog= new Dialog(context, R.style.custom_dialog_theme);
            dialog.setContentView(R.layout.time_up);
            TextView text =(TextView)dialog.findViewById(R.id.text);
            text.setText("Time's up!");
            Button dialogButton = (Button) dialog.findViewById(R.id.backtoMenu);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(),BaseActivity.class);
                    intent.putExtra("catogery",catogery);
                    intent.putExtra("DEFAULT_SIZE",DEFAULT_SIZE);
                    startActivity(intent);
                    finish();

                }
            });

            dialog.show();


        }



        @Override

        public void onTick(long millisUntilFinished) {
             secondsRemaining = millisUntilFinished / 1000 ;
            mTimeRemaining=time_longe-secondsRemaining;
//            if(secondsRemaining!=0)

            last_time.setText(Long.toString(time_longe-secondsRemaining)+"  "+ "Sec");
             last_time.setText(" "+String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes( mTimeRemaining*1000),
                    TimeUnit.MILLISECONDS.toSeconds(mTimeRemaining*1000) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mTimeRemaining*1000))));
            timerText.setText(" "+String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            if (secondsRemaining <= 10) {
                timerText.setTextColor(getResources().getColor(R.color.red));
            } else {
                timerText.setTextColor(Color.BLACK);


            }

        }

    }



    @Override
    protected void onResume() {

        mAd.resume(this);
        if (next_layout.getVisibility() == View.VISIBLE) {
            countDownTimer.cancel();
            // Its visible
        } else {
            countDownTimer.cancel();
            countDownTimer = new MyCountDownTimer(startTime, interval);
            countDownTimer.start();
        }

         super.onResume();
    }

    @Override
    protected void onRestart() {
         super.onRestart();
    }

    @Override
    protected void onPause() {
        mAd.pause(this);
        startTime=secondsRemaining * 1000;
         countDownTimer.cancel();
         super.onPause();

    }

    @Override
    public void onDestroy() {
         mAd.destroy(this);
        super.onDestroy();
    }
}

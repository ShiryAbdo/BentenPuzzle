package benten.puzzle.games.gamePuzzle;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    int score ;
TextView scoret ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);



        setSupportActionBar(toolbar);
        random = new Random();
        bundle=getIntent().getExtras();
        showImage=(Button)findViewById(R.id.showImage);
        scoret=(TextView)findViewById(R.id.score);


        if(bundle!=null) {
            catogery= bundle.getString("catogery");

        }
        sharedPref = getApplicationContext().getSharedPreferences(catogery, Context.MODE_PRIVATE);
        catogerys=getApplicationContext().getSharedPreferences("catogerys", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        newEditore=catogerys.edit();
        boolean frist_time = sharedPref.getBoolean("frist_time", true);
          score = sharedPref.getInt("score", 0);
        if(frist_time){
            count=0;
            score=0;
            Toast.makeText(this,"frist",Toast.LENGTH_LONG).show();
        }else{
            count=score;
            Toast.makeText(this,"second",Toast.LENGTH_LONG).show();

        }
        Toast.makeText(this,""+count,Toast.LENGTH_LONG).show();
        scoret.setText(count+"");

         toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setTitle(catogery);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbar.setTitle("shimaa");
        toolbar.setLogo(R.drawable.icone);

//        toolbar.setLogoDescription(getResources().getString(R.string.logo_desc));



        images = new ArrayList<>();
        easyData= new EasyData();
        mediumData= new MediumData();
        hardData=new HardData();
        difficultData= new DifficultData();
        next= (ImageView)findViewById(R.id.next);
        refresh=(ImageView)findViewById(R.id.refresh);

        if(catogery.equals("Easy")){
            toolbar.setTitle(catogery);
            images=easyData.getEasyDataArray();
            imageSourse=images.get(count);
        }if(catogery.equals("Medium")){
            toolbar.setTitle(catogery);
            images=mediumData.getMediumData();
            imageSourse=images.get(count);
        }if(catogery.equals("Hard")){
            toolbar.setTitle(catogery);
            images=hardData.getHardData();
            imageSourse=images.get(count);
        }if(catogery.equals("Difficult")) {
            toolbar.setTitle(catogery);
            images =difficultData.getDifficultData();
            imageSourse = images.get(count);
        }


        MainPuzzle newFragment = new MainPuzzle();
        Bundle args = new Bundle();
        args.putString("catogery", catogery);
        args.putInt("image",imageSourse);
        newFragment.setArguments(args);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
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

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                    newEditore.putString(catogery,"Easy");
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
                    nexti = ++count;
                    scoret.setText(nexti+"");
                    editor.putBoolean("frist_time" ,false );
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

            }


        });


    }




    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BaseActivity.this,   MainCircleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}

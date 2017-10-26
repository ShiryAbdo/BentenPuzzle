package benten.puzzle.games.data;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import benten.puzzle.games.R;

/**
 * Created by falcon on 24/10/2017.
 */

public class RecyclerViewAdapter   extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<json_data> androidList;
    private Context context;
    private int lastPosition=-1;
    String city_name ,factory_space, factory_title ,nameActivity;


    public RecyclerViewAdapter(ArrayList<json_data> android, Context c ) {
        this.androidList = android;
        this.context=c;
     }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.  horizontal_item, viewGroup, false);


        return new RecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder viewHolder, final int i) {
        if(androidList.get(i).getName()!=null){
            viewHolder.city_name.setText(androidList.get(i).getName());

        }else {

        }




//        final String imageUr ="https://play.google.com/store/apps/details?id="+androidList.get(i).getImage();
        Picasso.with(context).load(androidList.get(i).getImage()).error(android.R.drawable.stat_notify_error).fit().into(viewHolder.imageView2);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(androidList.get(i).getUrl()!=null){
                    Toast.makeText(context,androidList.get(i).getUrl(),Toast.LENGTH_LONG).show();
//                    Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(androidList.get(i).getUrl());
//                    context.startActivity(launchIntent);
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + androidList.get(i).getUrl())));

                }else{
                    Toast.makeText(context,androidList.get(i).getUrl(),Toast.LENGTH_LONG).show();
                }



            }
        });
        setAnimation(viewHolder.cardView, i);

    }



    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return androidList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView city_name,factory_space,factory_title;
        private CardView cardView;
        private ImageView imageView2;
        public ViewHolder(View view) {
            super(view);
            cardView=(CardView)view.findViewById(R.id.cardview);
            city_name = (TextView)view.findViewById(R.id.textview1);
            imageView2=(ImageView)view.findViewById(R.id.imageView2);


        }
    }

}
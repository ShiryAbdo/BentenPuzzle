package benten.puzzle.games.gamePuzzle;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dolby.dap.DolbyAudioProcessing;
import com.dolby.dap.OnDolbyAudioProcessingEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;

import benten.puzzle.games.R;
import benten.puzzle.games.gameMomery.EasyData;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectYourImage  extends android.app.Fragment implements MediaPlayer.OnCompletionListener,
        OnDolbyAudioProcessingEventListener {

    protected static final int MENU_SCRAMBLE = 0;
    protected static final int MENU_SELECT_IMAGE = 1;
    protected static final int MENU_TAKE_PHOTO = 2;

    protected static final int RESULT_SELECT_IMAGE = 0;
    protected static final int RESULT_TAKE_PHOTO = 1;

    protected static final String KEY_SHOW_NUMBERS = "showNumbers";
    protected static final String KEY_IMAGE_URI = "imageUri";
    protected static final String KEY_PUZZLE = "slidePuzzle";
    protected static final String KEY_PUZZLE_SIZE = "puzzleSize";

    protected static final String FILENAME_DIR = "benten.puzzle.games";
    protected static final String FILENAME_PHOTO_DIR = FILENAME_DIR + "/photo";
    protected static final String FILENAME_PHOTO = "photo.jpg";

    protected static final int DEFAULT_SIZE = 3;

    private SlidePuzzleView view;
    private SlidePuzzle slidePuzzle;
    private BitmapFactory.Options bitmapOptions;
    private int puzzleWidth = 1;
    private int puzzleHeight = 1;
    private Uri imageUri;
    private boolean portrait;
    private boolean expert;
    Bitmap bitmap ;




    MediaPlayer mPlayer;
    DolbyAudioProcessing mDolbyAudioProcessing;
    private final java.util.List<String> mActList = new ArrayList<String>();
    Bundle bundle;
    String catogery ;
    int imageSourse ;
    EasyData easyData ;

    ArrayList<Integer> images ;
    String NN;
    BaseActivity baseActivity ;
    PopupWindow popupWindow;
    View popupView;
    int mCurrentX,mCurrentY;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {






        bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;

        slidePuzzle = new SlidePuzzle();

        view = new SlidePuzzleView(getActivity(), slidePuzzle ,getActivity());



        view.post(new Runnable() {
            public void run() {
                popupView = View.inflate(getActivity(), R.layout.popup,null);
                popupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);


                mCurrentX = 20;
                mCurrentY = 50;

                popupWindow.showAtLocation(view, Gravity.CENTER, mCurrentX, mCurrentY);
                Button btnClose = (Button)popupView.findViewById(R.id.btnClose);

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });


                popupView.setOnTouchListener(new View.OnTouchListener() {
                    private float mDx;
                    private float mDy;

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        if (action == MotionEvent.ACTION_DOWN) {
                            mDx = mCurrentX - event.getRawX();
                            mDy = mCurrentY - event.getRawY();
                        } else
                        if (action == MotionEvent.ACTION_MOVE) {
                            mCurrentX = (int) (event.getRawX() + mDx);
                            mCurrentY = (int) (event.getRawY() + mDy);
                            popupWindow.update(mCurrentX, mCurrentY, -1, -1);
                        }
                        return true;
                    }
                });

            }
        });


        return view;
    }



    private void shuffle() {
        slidePuzzle.init(puzzleWidth, puzzleHeight);
        slidePuzzle.shuffle();
        view.invalidate();
        expert = view.getShowNumbers() == SlidePuzzleView.ShowNumbers.NONE;
    }

    protected void loadBitmap(Uri uri) {
        try
        {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            InputStream imageStream = getActivity().getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(imageStream, null, o);

            int targetWidth = view.getTargetWidth();
            int targetHeight = view.getTargetHeight();

            if(o.outWidth > o.outHeight && targetWidth < targetHeight)
            {
                int i = targetWidth;
                targetWidth = targetHeight;
                targetHeight = i;
            }

            if(targetWidth < o.outWidth || targetHeight < o.outHeight)
            {
                double widthRatio = (double) targetWidth / (double) o.outWidth;
                double heightRatio = (double) targetHeight / (double) o.outHeight;
                double ratio = Math.max(widthRatio, heightRatio);

                o.inSampleSize = (int) Math.pow(2, (int) Math.round(Math.log(ratio) / Math.log(0.5)));
            }
            else
            {
                o.inSampleSize = 1;
            }

            o.inScaled = false;
            o.inJustDecodeBounds = false;

            imageStream = getActivity().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream, null, o);

            if(bitmap == null)
            {
                Toast.makeText(getActivity(), getString(R.string.error_could_not_load_image), Toast.LENGTH_LONG).show();
                return;
            }

            int rotate = 0;

            Cursor cursor = getActivity().getContentResolver().query(uri, new String[] {MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

            if(cursor != null)
            {
                try
                {
                    if(cursor.moveToFirst())
                    {
                        rotate = cursor.getInt(0);

                        if(rotate == -1)
                        {
                            rotate = 0;
                        }
                    }
                }
                finally
                {
                    cursor.close();
                }
            }

            if(rotate != 0)
            {
                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);

                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }

            setBitmap(bitmap);
            imageUri = uri;
        }
        catch(FileNotFoundException ex)
        {
            Toast.makeText(getActivity(), MessageFormat.format(getString(R.string.error_could_not_load_image_error), ex.getMessage()), Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void setBitmap(Bitmap bitmap) {
        portrait = bitmap.getWidth() < bitmap.getHeight();

        view.setBitmap(bitmap);
        setPuzzleSize(Math.min(puzzleWidth, puzzleHeight), true);

//        setRequestedOrientation(portrait ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private void selectImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_SELECT_IMAGE);
    }

    private void takePicture()
    {
        File dir = getSaveDirectory();

        if(dir == null)
        {
            Toast.makeText(getActivity(), getString(R.string.error_could_not_create_directory_to_store_photo), Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(dir, FILENAME_PHOTO);
        Intent photoPickerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(photoPickerIntent, RESULT_TAKE_PHOTO);
    }


    private File getSaveDirectory()
    {
        File root = new File(Environment.getExternalStorageDirectory().getPath());
        File dir = new File(root, FILENAME_PHOTO_DIR);

        if(!dir.exists())
        {
            if(!root.exists() || !dir.mkdirs())
            {
                return null;
            }
        }

        return dir;
    }

    private float getImageAspectRatio()
    {
        Bitmap bitmap = view.getBitmap();

        if(bitmap == null)
        {
            return 1;
        }

        float width = bitmap.getWidth();
        float height = bitmap.getHeight();

        return width / height;
    }

    protected void setPuzzleSize(int size, boolean scramble)
    {
        float ratio = getImageAspectRatio();

        if(ratio < 1)
        {
            ratio = 1f /ratio;
        }

        int newWidth;
        int newHeight;

        if(portrait)
        {
            newWidth = size;
            newHeight = (int) (size * ratio);
        }
        else
        {
            newWidth = (int) (size * ratio);
            newHeight = size;
        }

        if(scramble || newWidth != puzzleWidth || newHeight != puzzleHeight)
        {
            puzzleWidth = newWidth;
            puzzleHeight = newHeight;
            shuffle();
        }
    }



    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        return onOptionsItemSelected(item);
    }


    protected SharedPreferences getPreferences()
    {
        return getActivity().getSharedPreferences(MainPuzzle.class.getName(), Activity.MODE_PRIVATE);
    }
    @Override
    public void onStop()
    {
        super.onStop();

        if (mPlayer != null) {
            mPlayer.pause();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mPlayer != null)
        {
            mPlayer.start();
        }
    }

    protected boolean loadPreferences()
    {
        SharedPreferences prefs = getPreferences();

        try
        {

            String s = prefs.getString(KEY_IMAGE_URI, null);

            if(s == null)
            {
                imageUri = null;
            }
            else
            {
                loadBitmap(Uri.parse(s));
            }

            int size = prefs.getInt(KEY_PUZZLE_SIZE, 0);
            s = prefs.getString(KEY_PUZZLE, null);

            if(size > 0 && s != null)
            {
                String[] tileStrings = s.split("\\;");

                if(tileStrings.length / size > 1)
                {
                    setPuzzleSize(size, false);
                    slidePuzzle.init(puzzleWidth, puzzleHeight);

                    int[] tiles = new int[tileStrings.length];

                    for(int i = 0; i < tiles.length; i++)
                    {
                        try
                        {
                            tiles[i] = Integer.parseInt(tileStrings[i]);
                        }
                        catch(NumberFormatException ex)
                        {
                        }
                    }

                    slidePuzzle.setTiles(tiles);
                }
            }

            return prefs.contains(KEY_SHOW_NUMBERS);
        }
        catch(ClassCastException ex)
        {
            // ignore broken settings
            return false;
        }
    }

    public void playSound()
    {
        if(mPlayer == null) {
            mPlayer = MediaPlayer.create(
                    getActivity(),
                    R.raw.slide);
            mPlayer.start();
        } else {
            mPlayer.release();
            mPlayer = null;
            mPlayer = MediaPlayer.create(
                    getActivity(),
                    R.raw.slide);
            mPlayer.start();
        }

        mDolbyAudioProcessing = DolbyAudioProcessing.getDolbyAudioProcessing(getActivity(), DolbyAudioProcessing.PROFILE.GAME, this);
        if (mDolbyAudioProcessing == null) {
            return;
        }
    }

    public void onFinish()
    {
        if(mPlayer == null) {
            mPlayer = MediaPlayer.create(
                    getActivity(),
                    R.raw.fireworks);
            mPlayer.start();
        } else {
            mPlayer.release();
            mPlayer = null;
            mPlayer = MediaPlayer.create(
                    getActivity(),
                    R.raw.fireworks);
            mPlayer.start();
        }

        mDolbyAudioProcessing = DolbyAudioProcessing.getDolbyAudioProcessing(getActivity(), DolbyAudioProcessing.PROFILE.GAME, this);
        if (mDolbyAudioProcessing == null) {
            Toast.makeText(getActivity(),
                    "Dolby Audio Processing not available on this device.",
                    Toast.LENGTH_SHORT).show();
            shuffle();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onDolbyAudioProcessingClientConnected() {
        mDolbyAudioProcessing.setEnabled(true);
    }

    @Override
    public void onDolbyAudioProcessingClientDisconnected() {
        mDolbyAudioProcessing.setEnabled(false);
    }

    @Override
    public void onDolbyAudioProcessingEnabled(boolean b) {
    }

    @Override
    public void onDolbyAudioProcessingProfileSelected(DolbyAudioProcessing.PROFILE profile) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("Dolby processing", "onDestroy()");

        // Release Media Player instance
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }

        this.releaseDolbyAudioProcessing();

    }

    @Override
    public void onResume() {
        super.onResume();
        restartSession();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Dolby processing", "The application is in background, supsendSession");
        //
        // If audio playback is not required while your application is in the background, restore the Dolby audio processing system
        // configuration to its original state by suspendSession().
        // This ensures that the use of the system-wide audio processing is sandboxed to your application.
        suspendSession();
    }

    public void releaseDolbyAudioProcessing() {
        if (mDolbyAudioProcessing != null) {
            try {
                mDolbyAudioProcessing.release();
                mDolbyAudioProcessing = null;
            } catch (IllegalStateException ex) {
                handleIllegalStateException(ex);
            } catch (RuntimeException ex) {
                handleRuntimeException(ex);
            }
        }

    }

    // Backup the system-wide audio effect configuration and restore the application configuration
    public void restartSession() {
        if (mDolbyAudioProcessing != null) {
            try{
                mDolbyAudioProcessing.restartSession();
            } catch (IllegalStateException ex) {
                handleIllegalStateException(ex);
            } catch (RuntimeException ex) {
                handleRuntimeException(ex);
            }
        }
    }

    // Backup the application Dolby Audio Processing configuration and restore the system-wide configuration
    public void suspendSession() {

        if (mDolbyAudioProcessing != null) {
            try{
                mDolbyAudioProcessing.suspendSession();
            } catch (IllegalStateException ex) {
                handleIllegalStateException(ex);
            } catch (RuntimeException ex) {
                handleRuntimeException(ex);
            }
        }
    }

    /** Generic handler for IllegalStateException */
    private void handleIllegalStateException(Exception ex)
    {
        Log.e("Dolby processing", "Dolby Audio Processing has a wrong state");
        handleGenericException(ex);
    }

    /** Generic handler for IllegalArgumentException */
    private void handleIllegalArgumentException(Exception ex)
    {
        Log.e("Dolby processing","One of the passed arguments is invalid");
        handleGenericException(ex);
    }

    /** Generic handler for RuntimeException */
    private void handleRuntimeException(Exception ex)
    {
        Log.e("Dolby processing", "Internal error occured in Dolby Audio Processing");
        handleGenericException(ex);
    }

    private void handleGenericException(Exception ex)
    {
        Log.e("Dolby processing", Log.getStackTraceString(ex));
    }


}
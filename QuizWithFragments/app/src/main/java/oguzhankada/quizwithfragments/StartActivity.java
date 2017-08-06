package oguzhankada.quizwithfragments;


        import android.content.Intent;
        import android.media.MediaPlayer;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {


    private Button StartButton;
    private User mainUser;
    ArrayList<Category> categories;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Log.v("fork","start activityyyyy");

        Intent previousIntent = getIntent();
        mainUser = (User) previousIntent.getExtras().get(NicknameActivity.EXTRA_USER);
        categories = (ArrayList<Category>) previousIntent.getExtras().get(NicknameActivity.EXTRA_CATEGORIES);

        Button b =(Button)findViewById(R.id.start_button);

        //set onClick listener to the next button
        StartButton = (Button) findViewById(R.id.start_button) ;
        StartButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //if music is not muted play the startGame sound
                if (!mainUser.IsMusicMuted()) playStartSound();


                //create an intent
                Intent startIntent = new Intent(StartActivity.this, GameActivity.class);
                startIntent.putExtra(NicknameActivity.EXTRA_USER, mainUser);
                startIntent.putExtra(NicknameActivity.EXTRA_CATEGORIES, categories);
                startIntent.putExtra(NicknameActivity.EXTRA_INDEX, 0);
                startActivity(startIntent);
            }
        });



    }

    @Override
    public void onBackPressed() {
        //toast sending part

        int messageResId = 0;


        messageResId = R.string.no_going_back;


        Toast.makeText(this, messageResId, Toast.LENGTH_LONG)
                .show();

        //end of toast
    }

    public void playStartSound() {
        try {
            MediaPlayer playSound = MediaPlayer.create(StartActivity.this, R.raw.start_sound);
            playSound.start();
        }
        catch (NullPointerException e) {

        }
    }
}

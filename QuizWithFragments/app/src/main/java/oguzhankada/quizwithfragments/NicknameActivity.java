package oguzhankada.quizwithfragments;

        import android.content.Context;
        import android.content.Intent;
        import android.content.res.Resources;
        import android.media.MediaPlayer;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.wang.avi.AVLoadingIndicatorView;

        import java.util.ArrayList;


public class NicknameActivity extends AppCompatActivity {

    static final String EXTRA_USER = "app.user";
    static final String EXTRA_INDEX = "app.categoryIndex";
    static final String EXTRA_INDEX_QUESTION = "app.questionIndex";
    static final String EXTRA_CATEGORY = "app.category";
    static final String EXTRA_CATEGORY_NAME = "app.category_name";
    static final String EXTRA_CATEGORIES = "app.categories";
    static final String EXTRA_QUESTION = "app.question";



    private EditText NicknameText;
    private Button NextButton;
    private Button MuteMusic;
    private TextView loading;
    private String Nickname;
    private boolean mIsMusicMuted = false;
    private User mainUser;

    private AVLoadingIndicatorView avi;


    //create a database referance
    private DatabaseReference mDatabase;


    //private Answer[][] Answers = new Answer[4][16][4];

    //private Question[] mQuestionBank = new Question[4][16];

    //private Category[] mCategories = new Category[4];

    ArrayList<Category> categories = new ArrayList<Category>();






    static Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);




        //get avi from view
        String indicator=getIntent().getStringExtra("indicator");
        avi= (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.setIndicator(indicator);
        //end avi view get


        this.savedInstanceState = savedInstanceState;
        NicknameText = (EditText) findViewById(R.id.Nickname_EditText);
        loading = (TextView) findViewById(R.id.loading);

        if (savedInstanceState != null) {
            Nickname = savedInstanceState.getString("Nickname");
            NicknameText.setText(Nickname);
            mIsMusicMuted = savedInstanceState.getBoolean("MusicMute");


        }



        //set onClick listener to the next button
        NextButton = (Button) findViewById(R.id.Nickname_NextButton) ;


        NextButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //get the Nickname (from EditText) upon button click

                Nickname = NicknameText.getText().toString();

                //play game start sound if not muted
                if(!mIsMusicMuted) playSound(R.raw.gameintro_sound, NicknameActivity.this);


                //check if the Nickname is valid (if not ask for a new Nick
                if(isValidNickname(Nickname)) {
                    //start loading animation
                    startAnim();



                    //if music is not muted play the startGame sound
                    //if(!mIsMusicMuted) startsoundmusicplayer.start();
                    if (!mIsMusicMuted) playStartSound();

                    //create a new user with given Nickname
                    mainUser = new User(Nickname);

                    //modify IsMusicMuted of mainUser accordingly
                    mainUser.setmIsMusicMuted(mIsMusicMuted);

//////////////////////////////////////////////////////////////////
                    //further database involvement -> create a database instance of firebase
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    //

                    //get category db (single snapshot)
                    mDatabase = mDatabase.child("categories");

                    Log.v("cheker", "aaaaaaaaaaaaaaaaaaaa  "+mDatabase.child("category1").getKey());

                    //create the category archieve
                    createCategoryArchieve(mDatabase);
/////////////////////////////////////////////////////////////////



                }else{


                    makeAToast(R.string.emptyNickname);


                }

            }
        });
        //end of onclick for next button

        //set onClick listener to the muteMusic button
        MuteMusic = (Button) findViewById(R.id.musicMute_button) ;

        //set Mute Button txt
        if(mIsMusicMuted) {
            MuteMusic.setText(R.string.musicMuted);
        }else{
            MuteMusic.setText(R.string.notMusicMuted);
        }

        MuteMusic.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                mIsMusicMuted = !mIsMusicMuted;
                if(mIsMusicMuted) {
                    MuteMusic.setText(R.string.musicMuted);
                }else{
                    MuteMusic.setText(R.string.notMusicMuted);
                }

            }
        });
        //end of onclick for muteMusic button



    }



    @Override
    public void onBackPressed() {
        makeAToast(R.string.no_going_back);
    }


    private boolean isValidNickname(String name){
        return !(name.isEmpty());
    }


    private void makeAToast(int messageResId){
        //toast sending part


        Toast.makeText(this, messageResId, Toast.LENGTH_LONG)
                .show();

        //end of toast
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        Nickname = NicknameText.getText().toString();
        savedInstanceState.putString("Nickname", Nickname);
        savedInstanceState.putBoolean("MusicMute", mIsMusicMuted);
    }

    public void playStartSound() {
        try {
            MediaPlayer playSound = MediaPlayer.create(NicknameActivity.this, R.raw.start_sound);
            playSound.start();
        }
        catch (NullPointerException e) {

        }
    }

    public void playSound(int soundResource, Context context) {
        try {
            MediaPlayer playSound = MediaPlayer.create(context, soundResource);
            playSound.start();
        }
        catch (NullPointerException e) {

        }
    }

    private void createCategoryArchieve(DatabaseReference dbCategories){




        dbCategories.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("check2","lllllllllllllllll  "+dataSnapshot.getKey()+dataSnapshot.child("category1").getKey());
                categories.add(createCategories(dataSnapshot.child("category1")));
                categories.add(createCategories(dataSnapshot.child("category2")));

                initiateStartActivity();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private Category createCategories(DataSnapshot snapshot){


        String categoryName = (String) snapshot.child("category_text").getValue();
        Log.v("sssssssssss", "abc  "+categoryName);

        Question[] categoryQuestions = new Question[2];

        for (int i =0; i<categoryQuestions.length;i++){

            String questionKey = "question"+(i+1);
            DataSnapshot currentQuestionSnap = snapshot.child(questionKey);
            String questionText = String.valueOf(currentQuestionSnap.child("text").getValue());
            int questionTime = Integer.parseInt((String) currentQuestionSnap.child("time").getValue());
            int rightAnswer = Integer.parseInt((String)currentQuestionSnap.child("rightanswer").getValue());
            int questionPoints = Integer.parseInt((String)currentQuestionSnap.child("points").getValue());
            Answer[] answers = createAnswers(currentQuestionSnap.child("answers"));
            categoryQuestions[i] = new Question(questionText, rightAnswer, answers, questionTime, questionPoints);
            Log.v("xxxxxxxlogger","yyyyyyyyyyy  "+categoryQuestions[i].getQuestionText());
        }

        Category new_category = new Category(categoryName, categoryQuestions);

        Log.v("xxxxxxxlogger","yyyyyyyyyyy  "+ new_category.getCategoryText());


        return new_category;



    }



    private Answer[] createAnswers(DataSnapshot snapshot){
        Answer[] answers = new Answer[4];

        for(int i=0;i<answers.length;i++){
            String answerKey = "answer"+(i+1);
            String answerText = (String) snapshot.child(answerKey).getValue();
            answers[i] = new Answer(answerText);
            Log.v("xxxxxxxlogger","xxxxxxxx  "+answers[i].getmAnswerText());
        }

        return answers;
    }

    ///loading animation methods start
    void startAnim(){


        avi.setVisibility(View.VISIBLE);
        avi.show();
        NicknameText.setVisibility(View.INVISIBLE);
        NextButton.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.VISIBLE);


    }

    void stopAnim(){

        avi.hide();
        avi.setVisibility(View.INVISIBLE);

        NicknameText.setVisibility(View.VISIBLE);
        NextButton.setVisibility(View.VISIBLE);
        loading.setVisibility(View.INVISIBLE);

    }
    ///loading animation methods end


    private void initiateStartActivity(){
        //create an intent
        Intent nextIntent = new Intent(NicknameActivity.this, StartActivity.class);
        nextIntent.putExtra(EXTRA_USER, mainUser);
        nextIntent.putExtra(EXTRA_CATEGORIES, categories);



        //start next activity -> startActivity
        startActivity(nextIntent);

    }
}

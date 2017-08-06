package oguzhankada.geoquizfinal;

import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class NicknameActivity extends AppCompatActivity {

    static final String EXTRA_USER = "app.user";
    static final String EXTRA_INDEX = "app.questionIndex";
    private EditText NicknameText;
    private Button NextButton;
    private Button MuteMusic;
    private String Nickname;
    private boolean mIsMusicMuted = false;
    //private MediaPlayer startsoundmusicplayer = MediaPlayer.create(this, R.raw.start_sound);


    //create the question base
//    private Answer[] mQuestion1 = new Answer[] {
//            new Answer(R.string.question1answer1),
//            new Answer(R.string.question1answer2),
//            new Answer(R.string.question1answer3),
//            new Answer(R.string.question1answer4),
//    };

    private Answer[][] Answers = new Answer[16][4];



//
//    private Question[] mQuestionBank = new Question[] {
//            new Question(R.string.question1, 4, mQuestion1, 5),
//            new Question(R.string.question1, 4, mQuestion1, 10),
////            new Question(R.string.question_oceans, true),
////            new Question(R.string.question_mideast, false),
////            new Question(R.string.question_africa, false),
////            new Question(R.string.question_americas, true),
////            new Question(R.string.question_asia, true),
//    };


    private Question[] mQuestionBank = new Question[16];


    //end question creation


    static final String EXTRA_QUESTIONS = "app.questions";

    static Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);

        createQuestionArchieve();


        this.savedInstanceState = savedInstanceState;
        NicknameText = (EditText) findViewById(R.id.Nickname_EditText);

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


                //check if the Nickname is valid (if not ask for a new Nick
                if(isValidNickname(Nickname)) {

                    //if music is not muted play the startGame sound
                    //if(!mIsMusicMuted) startsoundmusicplayer.start();
                    if (!mIsMusicMuted) playStartSound();

                    //create a new user with given Nickname
                    User mainUser = new User(Nickname);

                    //modify IsMusicMuted of mainUser accordingly
                    mainUser.setmIsMusicMuted(mIsMusicMuted);

                    //create an intent
                    Intent nextIntent = new Intent(NicknameActivity.this, StartActivity.class);
                    nextIntent.putExtra(EXTRA_USER, mainUser);
                    nextIntent.putExtra(EXTRA_QUESTIONS, mQuestionBank);

                    startActivity(nextIntent);
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
    private void createQuestionArchieve(){
        for(int y=1; y<17; y++){
            for(int x=1; x<5;x++) {
                Resources res = getResources();
                String message = "question" + y + "answer" + x;
                int id = res.getIdentifier(message, "string", getPackageName());

                Answers[y-1][x-1] = new Answer(id);

            }
        }

        for(int i=0; i<16; i++){
            Resources res = getResources();
            String question_txt = "question" + (i+1);
            int question_text_id = res.getIdentifier(question_txt, "string", getPackageName());

            String question_answer_txt = "question" + (i+1) + "answer";
            int question_answer_id = res.getIdentifier(question_answer_txt, "integer", getPackageName());

            mQuestionBank[i] = new Question(question_text_id, res.getInteger(question_answer_id), Answers[i], 20);
        }


    }
}

package oguzhankada.quizwithfragments;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionShowFragment extends Fragment implements  View.OnClickListener{
    //interface for method of list item click
    static interface backButtonClicked {
        void backButtonIsClicked();
    }

    private backButtonClicked backClicked;

    private Question selectedQuestion;
    private Category selectedCategory;
    private int qIndex;

    private boolean IsBackOverriden=false;


    private User mainUser;

    private int mLocalTime;
    private TextView score_text;
    private TextView userID_text;
    private TextView timeRemained_text;
    private boolean bundleExist=false;
    private Button[] answer_buttons = new Button[4];
    private Button answer1_button = answer_buttons[0];
    private Button answer2_button = answer_buttons[1];
    private Button answer3_button = answer_buttons[2];
    private Button answer4_button = answer_buttons[3];
    private  boolean timeUp;
    private int mAnswered = 99;
    private boolean mIsPaused = false;



    private Button mBackButton;

    private TextView mQuestionTextView;


    private Context context;

    public QuestionShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //get the Bundle data (saved Instance)
        if (savedInstanceState != null) {
            bundleExist = true;
            mainUser = (User) savedInstanceState.getSerializable("user");
            selectedCategory = (Category) savedInstanceState.getSerializable("category");
            qIndex = savedInstanceState.getInt("qIndex");

            selectedQuestion = (Question) savedInstanceState.getSerializable("question");
            Log.v("aaaa2current quest", "select "+ selectedQuestion.IsAnswered());
            mLocalTime=savedInstanceState.getInt("localTime");
            Log.v("local time" , "local time = " +mLocalTime);



        }


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question_show, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();



        //get the View
        View view = getView();

        context = view.getContext();



        //get Question Time if bundle does not exits
        if(!bundleExist) {
            mLocalTime = selectedQuestion.getmQuestionTime();
        }

        //set Views

        timeRemained_text= (TextView)  view.findViewById(R.id.textView_remainingTime);

        score_text = (TextView) view.findViewById(R.id.textView_userScore);
        score_text.setText("User score: " + mainUser.getmUserScore());

        userID_text = (TextView) view.findViewById(R.id.textView_userID);
        userID_text.setText("User: " + mainUser.getmUserNickname());

        mQuestionTextView = (TextView) view.findViewById(R.id.question_text_view);
        Answer[] answers_question = selectedQuestion.getAnswers();

        String question = selectedQuestion.getQuestionText();
        mQuestionTextView.setText(question);
        buttonAnim(mQuestionTextView, 500);

        answer1_button = (Button) view.findViewById(R.id.answer1_button);
        answer1_button.setText(answers_question[0].getmAnswerText());

        buttonAnim(answer1_button, 1000);

        answer2_button = (Button) view.findViewById(R.id.answer2_button);
        answer2_button.setText(answers_question[1].getmAnswerText());

        buttonAnim(answer2_button, 1500);

        answer3_button = (Button) view.findViewById(R.id.answer3_button);
        answer3_button.setText(answers_question[2].getmAnswerText());

        buttonAnim(answer3_button, 2000);

        answer4_button = (Button) view.findViewById(R.id.answer4_button);
        answer4_button.setText(answers_question[3].getmAnswerText());

        buttonAnim(answer4_button, 2500);

        mBackButton = (Button) view.findViewById(R.id.back_button);

        Log.v("aaaaaa3"," "+selectedQuestion.IsAnswered());
        if(!selectedQuestion.IsAnswered()){
            answer1_button.setOnClickListener(this);
            answer2_button.setOnClickListener(this);
            answer3_button.setOnClickListener(this);
            answer4_button.setOnClickListener(this);

            runTimer();

        }else{

            mAnswered = selectedQuestion.getmGivenAnswer();
            if(mAnswered==99){

                //if the time up case

                setButtonColor(1, false);
                setButtonColor(2, false);
                setButtonColor(3, false);
                setButtonColor(4, false);
            }else {

                // else -> the answered case
                setButtonColor(mAnswered, selectedQuestion.isAnswerTrue());
            }

            //toast sending part

            int messageResId = 0;


            messageResId = R.string.already_answered;


            Toast.makeText(view.getContext(), messageResId, Toast.LENGTH_LONG)
                    .show();

            //end of toast


        }


        mBackButton.setOnClickListener(this);
        //getFragmentManager().popBackStackImmediate();

    }

    //implements View.OnClickListener

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.answer1_button:
                setAnswer(1);
                break;
            case R.id.answer2_button:
                setAnswer(2);
                break;
            case R.id.answer3_button:
                setAnswer(3);
                break;
            case R.id.answer4_button:
                setAnswer(4);
                break;
            case R.id.back_button:
                goBack();

                break;
            default:
                break;


        }
        updateCategory();



    }


    public void setCategory(Category category) {
        selectedCategory = category;
    }
    public Category getCategory(){return selectedCategory;}

    public void setQuestion(Question question) {
        selectedQuestion = question;
    }
    public Question getQuestion(){return  selectedQuestion;}

    public void setqIndex (int index){qIndex =index;}

    public void setMainUser(User mUser){mainUser =mUser;}
    public User getUser(){return mainUser;}

    public void setIsBackOverriden (boolean overriden){IsBackOverriden = overriden;}

    public boolean getHasBundle (){return bundleExist;}

    //button animation methods
    private void buttonAnim(View myView, long duration){


        myView.startAnimation(inFromDownAnimation(duration));
    }
    private Animation inFromDownAnimation(long duration) {

        Animation downtoup = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        downtoup.setDuration(duration);
        downtoup.setInterpolator(new AccelerateInterpolator());
        return downtoup;
    }
    //end of button animation methods

    public void playSound(int id) {
        try {
            MediaPlayer playSound = MediaPlayer.create(context, id);
            playSound.start();
        }
        catch (NullPointerException e) {

        }
    }

    private void goBack(){
        if (!selectedQuestion.IsAnswered()) timeIsUp();
        selectedQuestion.setIsAnswered(true);
        mIsPaused=true;
        updateCategory();

        if(!selectedQuestion.IsAnswered()) selectedCategory.oneQuestionAnswered();



        if(IsBackOverriden){
            backClicked.backButtonIsClicked();
        }else {
            getFragmentManager().popBackStackImmediate();
        }
    }



    private void setAnswer(int userPressed) {

        //if question is answered dont allow to answer again
        //else answer the question
        if (selectedQuestion.IsAnswered()) {
            //toast sending part

            int messageResId = 0;


            messageResId = R.string.already_answered;


            Toast.makeText(context, messageResId, Toast.LENGTH_LONG)
                    .show();

            //end of toast
        } else {
            selectedCategory.oneQuestionAnswered();
            selectedQuestion.setIsAnswered(true);

            //check for answer
            selectedQuestion.setIsAnswerTrue(userPressed);
            boolean answerIsTrue = selectedQuestion.isAnswerTrue();

            //update given answer
            selectedQuestion.setmGivenAnswer(userPressed);

            //update the user score
            if (answerIsTrue) {
                mainUser.rightAnswer(selectedQuestion.getQuestionPoints());
            } else {
                mainUser.wrongAnswer(selectedQuestion.getQuestionPoints());
            }

            //update score text
            updateScore();

            //change the pressed button color
            setButtonColor(userPressed, answerIsTrue);



            //toast sending part & also plays sound (wrong or right) if not muted

            int messageResId = 0;

            if (answerIsTrue) {
                messageResId = R.string.correct_toast;
                if(!mainUser.IsMusicMuted()) playSound(R.raw.rightanswer_sound);
            } else {
                messageResId = R.string.incorrect_toast;
                if(!mainUser.IsMusicMuted()) playSound(R.raw.wronganswer_sound);
            }

            Toast.makeText(context, messageResId, Toast.LENGTH_LONG)
                    .show();

            //end of toast

        }
        updateCategory();
    }

    private void updateScore(){
        score_text.setText("User score: " + mainUser.getmUserScore());
    }


    private void setButtonColor(int answeredButton, boolean answerIsTrue){


        //color button in accordance with the answerIsTrue boolean -> if true answer green button, else red button

        int updatedButtonColor;
        if (answerIsTrue) {
            updatedButtonColor = Color.GREEN;

        } else {
            updatedButtonColor = Color.RED;

        }


        //update color
        switch (answeredButton) {
            case 1:
                answer1_button.setTextColor(updatedButtonColor);
                break;
            case 2:
                answer2_button.setTextColor(updatedButtonColor);
                break;
            case 3:
                answer3_button.setTextColor(updatedButtonColor);
                break;
            case 4:
                answer4_button.setTextColor(updatedButtonColor);
                break;
        }
    }



    private void runTimer() {
        timeUp = false;


        final Handler countDown_handler = new Handler();
        countDown_handler.post(new Runnable() {

            @Override
            public void run() {
                if (!timeUp) {
                    if(!selectedQuestion.IsAnswered()){
                        String remainingTime = "Remaining Time: " + mLocalTime;
                        timeRemained_text.setText(remainingTime);
                        if (mLocalTime <= 0 ) {
                            timeUp = true;


                        }

                        //before continuing the count down check if app is paused
                        if(!mIsPaused) mLocalTime--;

                        countDown_handler.postDelayed(this, 1000);
                    }else {
                        String remainingTime = "Already answered!";
                        timeRemained_text.setText(remainingTime);
                    }
                }else{
                    timeIsUp();
                }
            }

        });



    }


    private void makeAToast(int messageResId){
        //toast sending part


        Toast.makeText(context, messageResId, Toast.LENGTH_LONG)
                .show();

        //end of toast
    }

    public void timeIsUp() {
        selectedQuestion.setIsAnswered(true);
        setButtonColor(1, false);
        setButtonColor(2, false);
        setButtonColor(3, false);
        setButtonColor(4, false);

        makeAToast(R.string.timeIsUp);

        if(!selectedQuestion.IsAnswered()) selectedCategory.oneQuestionAnswered();


        //play timeUp sound if not muted
        if(!mainUser.IsMusicMuted()) playSound(R.raw.timeup_sound);

        updateCategory();



    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        updateCategory();
        savedInstanceState.putSerializable("user", mainUser);
        savedInstanceState.putSerializable("category", selectedCategory);
        savedInstanceState.putSerializable("question", selectedQuestion);
        savedInstanceState.putInt("localTime", mLocalTime);
        savedInstanceState.putInt("qIndex", qIndex);
        Log.v("aaaa2current question state", "selected "+ selectedQuestion.IsAnswered());


    }

    //on pause
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        mIsPaused=true;


    }
    //end onpause


    //on resume
    @Override
    public void onResume() {
        super.onResume();

        mIsPaused=false;
    }


    //end onresume

    private void updateCategory(){
        Question[] cQuestion = selectedCategory.getQuestions();
        cQuestion[qIndex] = selectedQuestion;
        selectedCategory.setmQuestions(cQuestion);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.backClicked = (backButtonClicked) activity;
    }






}

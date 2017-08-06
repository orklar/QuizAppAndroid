package oguzhankada.quizwithfragments;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class QuestionPointsActivity extends AppCompatActivity implements QuestionPointsScreenFragment.questionClicked {
    private User mainUser;
    private ArrayList<Category> categories;
    private int position;
    private Category currentCategory;
    private Button mEndGameButton;

    private boolean mIsPaused= false; //for total timer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_points);

        Intent previousIntent = getIntent();
        mainUser = (User) previousIntent.getExtras().get(NicknameActivity.EXTRA_USER);
        position = (int) previousIntent.getExtras().get(NicknameActivity.EXTRA_INDEX);
        categories = (ArrayList<Category>) previousIntent.getExtras().get(NicknameActivity.EXTRA_CATEGORIES);

        currentCategory = (Category) previousIntent.getExtras().get(NicknameActivity.EXTRA_CATEGORY);
        if(currentCategory!=null){
            categories.get(position).setmCompletedQuestions(currentCategory.numberOfCompletedQuestions());
            categories.get(position).setmQuestions(currentCategory.getQuestions());
        }else{
            currentCategory = categories.get(position);
        }

        //get the end game button from the view and set a listener
        mEndGameButton = (Button) findViewById(R.id.question_points_end_game);
        mEndGameButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                onEndGamePressed();
            }
        });

        //start total time timer
        totalTimeRunner();



        QuestionPointsScreenFragment showQuestionPointsFrag = (QuestionPointsScreenFragment)
                getSupportFragmentManager().findFragmentById(R.id.question_points_fragment);


        showQuestionPointsFrag.setCategory(currentCategory);
        showQuestionPointsFrag.setMainUser(mainUser);

    }




    public void questionIsClicked(Question[] questionsArray, int id, Category selectedCategory, User user){


        Log.v("jjj","jjjıksdjf");



        Intent intent = new Intent(QuestionPointsActivity.this, QuestionShowActivity.class);
        intent.putExtra(NicknameActivity.EXTRA_CATEGORIES, categories);
        intent.putExtra(NicknameActivity.EXTRA_INDEX, position);
        intent.putExtra(NicknameActivity.EXTRA_USER, user);
        intent.putExtra(NicknameActivity.EXTRA_QUESTION, questionsArray);
        intent.putExtra(NicknameActivity.EXTRA_CATEGORY, selectedCategory);
        intent.putExtra(NicknameActivity.EXTRA_INDEX_QUESTION, id);


        startActivity(intent);



    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(QuestionPointsActivity.this, GameActivity.class);
        intent.putExtra(NicknameActivity.EXTRA_USER, mainUser);
        intent.putExtra(NicknameActivity.EXTRA_CATEGORIES, categories);
        startActivity(intent);


    }

    private void onEndGamePressed(){
        String remaining="";
        //warning screen of remaining questions
        for (int i=0; i<categories.size();i++){
            if(categories.get(i).numberOfUncompletedQuestions()!=0)
            remaining = remaining + "\nYou have "+categories.get(i).numberOfUncompletedQuestions()+" uncompleted questions in "+categories.get(i).getCategoryText()+ "!";
        }
        remaining = remaining+"\n Do you want to end the game ?";

        alertBox(remaining).show();
    }

    public AlertDialog alertBox(String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("End Game?");
        builder.setMessage(message);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                //if yes -> go to the score screen


                mIsPaused=true;
                Intent intent = new Intent(QuestionPointsActivity.this, ScoreScreenActivity.class);
                intent.putExtra(NicknameActivity.EXTRA_USER, mainUser);
                startActivity(intent);

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        return alert;
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

    //total time timer :)
    //total timer
    public void totalTimeRunner(){



        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                //before increasing the time check if the app is paused
                int totalTime = mainUser.getmTotalTime();
                if(!mIsPaused) mainUser.setmTotalTime(totalTime+1);

                handler.postDelayed(this, 1000);
            }
        });
    }




}

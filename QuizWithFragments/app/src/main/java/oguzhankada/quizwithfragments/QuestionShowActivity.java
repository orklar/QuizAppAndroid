package oguzhankada.quizwithfragments;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class QuestionShowActivity extends AppCompatActivity implements QuestionShowFragment.backButtonClicked{

    private User mainUser;
    private Category selectedCategory;
    private int id;
    private Question[] questionsArray;
    private ArrayList<Category> categories;
    private int position;
    private boolean mIsPaused= false; //for total timer


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_show);

        Intent previousIntent = getIntent();
        mainUser = (User) previousIntent.getExtras().get(NicknameActivity.EXTRA_USER);
        selectedCategory = (Category) previousIntent.getExtras().get(NicknameActivity.EXTRA_CATEGORY);
        questionsArray = (Question[]) previousIntent.getExtras().get(NicknameActivity.EXTRA_QUESTION);

        categories = (ArrayList<Category>) previousIntent.getExtras().get(NicknameActivity.EXTRA_CATEGORIES);
        position =  (int) previousIntent.getExtras().get(NicknameActivity.EXTRA_INDEX);


        id = (int) previousIntent.getExtras().get(NicknameActivity.EXTRA_INDEX_QUESTION);


        //start total time timer
        totalTimeRunner();

        //start transaction for fragmentsss

        QuestionShowFragment showQFrag = (QuestionShowFragment)
                getSupportFragmentManager().findFragmentById(R.id.question_show_fragment);
        if(!showQFrag.getHasBundle()){

            showQFrag.setQuestion(questionsArray[id]);
            showQFrag.setMainUser(mainUser);
            showQFrag.setCategory(selectedCategory);
            showQFrag.setqIndex(id);
        }else{
            mainUser=showQFrag.getUser();
            selectedCategory = showQFrag.getCategory();

        }

        showQFrag.setIsBackOverriden(true);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(QuestionShowActivity.this, QuestionPointsActivity.class);
        Question[] q = selectedCategory.getQuestions();
        q[id].setIsAnswered(true);
        intent.putExtra(NicknameActivity.EXTRA_USER, mainUser);
        intent.putExtra(NicknameActivity.EXTRA_CATEGORY, selectedCategory);
        intent.putExtra(NicknameActivity.EXTRA_CATEGORIES,categories);
        intent.putExtra(NicknameActivity.EXTRA_INDEX, position);
        startActivity(intent);


    }

    @Override
    public void backButtonIsClicked() {
        onBackPressed();
    }

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
}

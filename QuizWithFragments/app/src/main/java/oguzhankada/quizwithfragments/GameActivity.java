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
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements ShowCategoriesFragment.CategoryListListener, QuestionPointsScreenFragment.questionClicked, QuestionShowFragment.backButtonClicked{

    private User mainUser;
    private ArrayList<Category> categories;
    private boolean mIsPaused= false;

    private android.support.v4.app.FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent previousIntent = getIntent();
        mainUser = (User) previousIntent.getExtras().get(NicknameActivity.EXTRA_USER);
        categories = (ArrayList<Category>) previousIntent.getExtras().get(NicknameActivity.EXTRA_CATEGORIES);


        totalTimeRunner();

        ShowCategoriesFragment categoryListFrag = (ShowCategoriesFragment)
                getSupportFragmentManager().findFragmentById(R.id.Category_list_fragment);

        categoryListFrag.setCategories(categories);

        //get the Bundle data (saved Instance)
        if (savedInstanceState != null) {

            mainUser = (User) savedInstanceState.getSerializable("user");
            categories = (ArrayList<Category>) savedInstanceState.getSerializable("categories");

        }
    }


    @Override
    public void CategoryItemClicked(int position) {
        Log.v( "CategoryItemClicked", " on list of catefories list position"+position);


        View frameLayout = findViewById(R.id.frame_layout_for_tablet);
        if(position == categories.size()){
            String remaining="";
            //warning screen of remaining questions
            for (int i=0; i<categories.size();i++){
                if(categories.get(i).numberOfUncompletedQuestions()!=0)
                    remaining = remaining + "\nYou have "+categories.get(i).numberOfUncompletedQuestions()+" uncompleted questions in "+categories.get(i).getCategoryText()+ "!";
            }
            remaining = remaining+"\n Do you want to end the game ?";

            alertBox(remaining).show();



        }else {
            if (frameLayout != null) {
                QuestionPointsScreenFragment questionPointsScreen = new QuestionPointsScreenFragment();
                ft = getSupportFragmentManager().beginTransaction();
                questionPointsScreen.setCategory(categories.get(position));
                questionPointsScreen.setMainUser(mainUser);
                ft.replace(R.id.frame_layout_for_tablet, questionPointsScreen);

                //Dont add to stack as dont want to return to old answered questions

                //ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            } else {
                Intent intent = new Intent(GameActivity.this, QuestionPointsActivity.class);
                intent.putExtra(NicknameActivity.EXTRA_USER, mainUser);
                intent.putExtra(NicknameActivity.EXTRA_CATEGORIES, categories);
                intent.putExtra(NicknameActivity.EXTRA_INDEX, position);
                startActivity(intent);
            }
        }


    }

    public AlertDialog alertBox(String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("End Game?");
        builder.setMessage(message);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                mIsPaused=true;
                Intent intent = new Intent(GameActivity.this, ScoreScreenActivity.class);
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

    //handler for total time runner
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

    @Override
    public void onBackPressed() {
        makeAToast(R.string.no_going_back);
    }

    private void makeAToast(int messageResId){
        //toast sending part


        Toast.makeText(this, messageResId, Toast.LENGTH_LONG)
                .show();

        //end of toast
    }

    public void questionIsClicked(Question[] questionsArray, int id, Category selectedCategory, User user){
        mainUser = user;
        QuestionShowFragment questionShowScreen = new QuestionShowFragment();
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        questionShowScreen.setQuestion(questionsArray[id]);
        questionShowScreen.setMainUser(user);
        questionShowScreen.setCategory(selectedCategory);
        questionShowScreen.setqIndex(id);
        ft.replace(R.id.frame_layout_for_tablet, questionShowScreen);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

    }

    @Override
    public void backButtonIsClicked() {}

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putSerializable("user", mainUser);
        savedInstanceState.putSerializable("categories", categories);


    }




}

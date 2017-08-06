package oguzhankada.quizwithfragments;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionPointsScreenFragment extends Fragment  implements  View.OnClickListener{
    //interface for method of list item click
    static interface questionClicked {
        void questionIsClicked(Question[] questionsArray, int id, Category selectedCategory, User user);
    }

    private questionClicked qClicked;


    private Category selectedCategory;
    private User mainUser;

    private TextView userID_text;
    private TextView score_text;
    private TextView category_name_Text;





    public QuestionPointsScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //get the Bundle data (saved Instance)
        if (savedInstanceState != null) {
            setMainUser((User) savedInstanceState.getSerializable("user"));
            setCategory((Category) savedInstanceState.getSerializable("category"));

        }


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question_points_screen, container, false);
    }




    @Override
    public void onStart() {
        super.onStart();
        View view = getView();




        //set Views
        score_text = (TextView) view.findViewById(R.id.textView_userScore);
        score_text.setText("User score: " + mainUser.getmUserScore());


        userID_text = (TextView) view.findViewById(R.id.textView_userID);
        userID_text.setText("User: " + mainUser.getmUserNickname());

        category_name_Text = (TextView) view.findViewById(R.id.current_category_textView);
        category_name_Text.setText("Category: " + selectedCategory.getCategoryText());


        Question[] questions = selectedCategory.getQuestions();
        int questionNumber = questions.length;
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.linear_layout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 1, 0 ,1);
        params.gravity = Gravity.CENTER_VERTICAL;


        for(int i=0;i<questionNumber;i++){
            Button btn = new Button(view.getContext());
            btn.setId(i);

            Log.v("questionID","Question "+i+"id set to "+ btn.getId());

            btn.setText(" "+ questions[i].getQuestionPoints());
            btn.setOnClickListener(this);
            btn.setLayoutParams(params);


            int color = Color.WHITE;
            int text_color = Color.BLACK;
            if (questions[i].IsAnswered()){
                if(questions[i].isAnswerTrue()){
                    color = Color.GREEN;
                }else if(questions[i].getmGivenAnswer()==99){
                    //for no answers in given time
                    color = Color.BLUE;
                    text_color =Color.WHITE;

                }else{
                    //wrong answer -> red color
                    color = Color.RED;
                    text_color =Color.WHITE;
                }
            }

            btn.setBackgroundColor(color);
            btn.setTextColor(text_color);

            layout.addView(btn);
        }





    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("user", mainUser);
        savedInstanceState.putSerializable("category", selectedCategory);
    }

    public void setCategory(Category category) {
        selectedCategory = category;
    }

    public void setMainUser(User mUser){mainUser =mUser;}

    //implements View.OnClickListener

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Question[] questionsArray = selectedCategory.getQuestions();
        if(!questionsArray[id].IsAnswered()) {

            //
            if(qClicked!=null){
                qClicked.questionIsClicked(questionsArray, id, selectedCategory, mainUser);
            }

        }else{
            makeAToast(v.getContext(), R.string.already_answered);

        }

    }

    private void makeAToast(Context c, int messageResId){
        //toast sending part


        Toast.makeText(c, messageResId, Toast.LENGTH_LONG)
                .show();

        //end of toast
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.qClicked = (questionClicked) activity;
    }


    //on pause
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first



    }
    //end onpause


    //on resume
    @Override
    public void onResume() {
        super.onResume();

        score_text.setText("User score: " + mainUser.getmUserScore());
    }

}

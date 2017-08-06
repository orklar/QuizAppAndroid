package oguzhankada.quizwithfragments;

import java.io.Serializable;

/**
 * Created by Orklar on 7/30/2017.
 */


public class Category implements Serializable {


    private String mCategoryText;
    private Question[] mQuestions;
    private int mCompletedQuestions;



    //constructor for question
    public Category(String categoryText, Question[] questions) {
        mCategoryText=categoryText;
        mQuestions=questions;
        mCompletedQuestions=0;

    }




    public String getCategoryText() {
        return mCategoryText;
    }

    public Question[] getQuestions() {
        return mQuestions;
    }

    public void setmCategoryText(String mCategoryText) {
        this.mCategoryText = mCategoryText;
    }

    public void setmQuestions(Question[] mQuestions) {
        this.mQuestions = mQuestions;
    }

    public void setmCompletedQuestions(int mCompletedQuestions) {
        this.mCompletedQuestions = mCompletedQuestions;
    }

    public void oneQuestionAnswered(){
        mCompletedQuestions++;
    }

    public boolean isCategoryCompleted (){
        return mCompletedQuestions==mQuestions.length;
    }
    public int numberOfCompletedQuestions(){return mCompletedQuestions;}

    public int numberOfUncompletedQuestions(){return  mQuestions.length-mCompletedQuestions;}
}

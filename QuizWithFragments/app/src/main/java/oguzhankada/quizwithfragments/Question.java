package oguzhankada.quizwithfragments;

import java.io.Serializable;

/**
 * Created by Orklar on 7/30/2017.
 */

public class Question implements Serializable {


    private String mQuestionText;
    private int mRightAnswer;
    private boolean mIsAnswered;
    private Answer[] mAnswers;
    private  boolean mIsAnswerTrue;
    private int mQuestionTime; //seconds
    private int mGivenAnswer;
    private int mQuestionPoints;


    //constructor for question
    public Question(String questionText, int rightAnswer, Answer[] answers, int time, int points) {
        mQuestionText = questionText;
        mRightAnswer = rightAnswer;
        mIsAnswered = false;
        mAnswers = answers;
        mIsAnswerTrue = false;
        mQuestionTime = time;
        mGivenAnswer = 99;
        mQuestionPoints = points;
    }




    public int getmRightAnswer() {
        return mRightAnswer;
    }


    public boolean IsAnswered() {
        return mIsAnswered;
    }

    public void setIsAnswered(boolean mIsAnswered) {
        this.mIsAnswered = mIsAnswered;
    }

    public Answer[] getAnswers() {
        return mAnswers;
    }

    public String getQuestionText() {
        return mQuestionText;
    }

    public boolean isAnswerTrue() {
        return mIsAnswerTrue;
    }

    public void setIsAnswerTrue(int answer) {
        mIsAnswerTrue = mRightAnswer==answer;
    }

    public int getmQuestionTime() {
        return mQuestionTime;
    }

    public void setmQuestionTime(int mQuestionTime) {
        this.mQuestionTime = mQuestionTime;
    }

    public int getmGivenAnswer() {
        return mGivenAnswer;
    }

    public void setmGivenAnswer(int mGivenAnswer) {
        this.mGivenAnswer = mGivenAnswer;
    }

    public int getQuestionPoints() {
        return mQuestionPoints;
    }

    public void setQuestionPoints(int mQuestionPoints) {
        this.mQuestionPoints = mQuestionPoints;
    }
}

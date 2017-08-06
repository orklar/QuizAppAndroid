package oguzhankada.quizwithfragments;

import java.io.Serializable;

/**
 * Created by Orklar on 7/30/2017.
 */

public class Answer implements Serializable {

    private String mAnswerText;
    public Answer(String answerText) {
        mAnswerText= answerText;
    }

    public String getmAnswerText() {
        return mAnswerText;
    }


}

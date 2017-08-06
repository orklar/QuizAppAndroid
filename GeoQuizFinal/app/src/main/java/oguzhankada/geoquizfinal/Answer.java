package oguzhankada.geoquizfinal;

import java.io.Serializable;

/**
 * Created by Orklar on 7/19/2017.
 */

public class Answer implements Serializable {

    private int mAnswerTextID;
    public Answer(int answerTextID) {
        mAnswerTextID= answerTextID;
    }

    public int getmAnswerTextID() {
        return mAnswerTextID;
    }

    public void setmAnswerTextID(int mAnswerTextID) {
        this.mAnswerTextID = mAnswerTextID;
    }
}

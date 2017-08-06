package oguzhankada.geoquizfinal;

import java.io.Serializable;

/**
 * Created by Orklar on 7/19/2017.
 */

public class User implements Serializable {

    private String mUserNickname;
    private int mUserScore;
    private int mTotalTime;
    private boolean mIsMusicMuted;

    public User(String Nickname) {
        mUserNickname=Nickname;
        mUserScore=0;
        mTotalTime=0;
        mIsMusicMuted = false;
    }

    public String getmUserNickname() {
        return mUserNickname;
    }

    //right answer
    public void rightAnswer(){
        mUserScore+= 100;
    }

    //wrong answer
    public void wrongAnswer(){
        mUserScore-= 20;
    }

    //get score

    public int getmUserScore() {
        return mUserScore;
    }

    //total time getter&setter

    public int getmTotalTime() {
        return mTotalTime;
    }

    public void setmTotalTime(int mTotalTime) {
        this.mTotalTime = mTotalTime;
    }

    public boolean IsMusicMuted() {
        return mIsMusicMuted;
    }

    public void setmIsMusicMuted(boolean mIsMusicMuted) {
        this.mIsMusicMuted = mIsMusicMuted;
    }
}

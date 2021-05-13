package com.selfowner.kryptomeet;

public class JoinMeet {
    String Date;
    String MeetId;
    JoinMeet(){

    }

    public JoinMeet(String date, String meetId) {
        Date = date;
        MeetId = meetId;
    }

    public String getDate() {
        return Date;
    }

    public String getMeetId() {
        return MeetId;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setMeetId(String meetId) {
        MeetId = meetId;
    }
}

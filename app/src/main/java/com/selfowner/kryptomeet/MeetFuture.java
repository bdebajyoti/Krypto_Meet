package com.selfowner.kryptomeet;

public class MeetFuture {
    String UID;
    String Code;
    String Date;
    String Time;
    String RoomName;
    String Password;

    MeetFuture(){

    }

    public MeetFuture(String code, String date, String time, String roomName, String password) {
        Code = code;
        Date = date;
        Time = time;
        RoomName = roomName;
        Password = password;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getCode() {
        return Code;
    }

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return Time;
    }

    public String getRoomName() {
        return RoomName;
    }

    public String getPassword() {
        return Password;
    }

    public void setCode(String code) {
        Code = code;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
    }

    public void setPassword(String password) {
        Password = password;
    }
}

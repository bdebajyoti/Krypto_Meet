package com.selfowner.kryptomeet;

public class Recent_Calls {
    String Date;
    String Code;
    String RoomName;
    Recent_Calls(){

    }

    public Recent_Calls(String date, String code, String roomName) {
        Date = date;
        Code = code;
        RoomName = roomName;
    }

    public String getDate() {
        return Date;
    }

    public String getCode() {
        return Code;
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setCode(String code) {
        Code = code;
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
    }
}

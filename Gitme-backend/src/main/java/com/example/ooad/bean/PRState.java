package com.example.ooad.bean;

public enum PRState {
    UNPROCESS, ACCEPT, REJECT;

    public String toString(){
        if (this.equals(UNPROCESS)){
            return "UNPROCESS";
        }else if(this.equals(ACCEPT)){
            return "ACCEPT";
        }
        return "REJECT";
    }

    public static PRState toEnum(String PRState){
        if (PRState == null) return null;
        if (PRState.equals("UNPROCESS")){
            return UNPROCESS;
        }
        if (PRState.equals("ACCEPT")){
            return ACCEPT;
        }
        if (PRState.equals("REJECT")){
            return REJECT;
        }
        return null;
    }
}

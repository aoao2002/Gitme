package com.example.ooad.bean;

public enum RepoState {
    PUBLIC, PRIVATE;

    public String toString(){
        if (this.equals(PUBLIC)){
            return "PUBLIC";
        }
        return "PRIVATE";
    }

    public static RepoState toEnum(String repoState){
        if (repoState == null) return null;
        if (repoState.equals("PUBLIC")){
            return PUBLIC;
        }
        if (repoState.equals("PRIVATE")){
            return PRIVATE;
        }
        return null;
    }
}

package com.avi.in.miwok;

public class Word {
    private String defaultTranslatiion;
    private  String miwokTranslation;
    private int imageID = -1;
    private int audioID;


    public Word(String defaultTranslatiion , String miwokTranslation, int imageID, int audioID){

        this.defaultTranslatiion = defaultTranslatiion;
        this.miwokTranslation = miwokTranslation;
        this.imageID = imageID;
        this.audioID = audioID;
    }

    public Word(String defaultTranslatiion , String miwokTranslation, int audioID){

        this.defaultTranslatiion = defaultTranslatiion;
        this.miwokTranslation = miwokTranslation;
        this.audioID = audioID;
    }

    public String getDefaultTranslatiion(){
        return defaultTranslatiion;
    }
    public String getMiwokTranslation(){
        return miwokTranslation;
    }
    public int getImgageID(){return imageID;}
    public int getAudioID(){return audioID;}

}

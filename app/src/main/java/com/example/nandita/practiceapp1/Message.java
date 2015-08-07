package com.example.nandita.practiceapp1;

/**
 * Created by nandita on 2/17/2015.
 */
public class Message {

    private String phNum;
    private String txtBody;


    public Message(String phNum, String txtBody) {
        this.phNum = phNum;
        this.txtBody = txtBody;
    }


    public String getPhNum() {
        return phNum;
    }
    public void setPhNum(String phNum) {
        this.phNum = phNum;
    }
    public String getTxtBody() {
        return txtBody;
    }
    public void setTxtBody(String txtBody) {
        this.txtBody = txtBody;
    }


    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "Phone num: "+phNum ;
    }
}

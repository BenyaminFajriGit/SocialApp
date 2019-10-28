package com.example.socialapp;

import java.util.ArrayList;

public class CommentsData {
    public static String[][] data = new String[][]{
            {"0", "Lewis", "1", "Ipsum"},
            {"1", "Morgan", "2", "Tes"},
            {"2", "Morgan", "3", "Tes"},
            {"3", "Morgan", "1", "Tes"},
            {"4", "Morgan", "2", "Tes"}
    };
    public static ArrayList<Comment> getListData(){
        ArrayList<Comment> list = new ArrayList<>();
        for (String[] aData : data) {
            Comment comment = new Comment();
            comment.setId(Integer.parseInt(aData[0]));
            comment.setUsername(aData[1]);
            comment.setId_post(Integer.parseInt(aData[2]));
            comment.setCaption(aData[3]);
            list.add(comment);
        }
        return list;
    }
}

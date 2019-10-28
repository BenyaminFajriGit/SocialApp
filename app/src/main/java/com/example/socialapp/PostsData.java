package com.example.socialapp;

import java.util.ArrayList;

public class PostsData {
    //this class used to load dummy data of posts
    public static String[][] data = new String[][]{
            {"0", "Lewis", "Loren", "Ipsum"},
            {"1", "Morgan", "Tes", "Tes"},
            {"2", "Morgan", "Tes", "Tes"},
            {"3", "Morgan", "Tes", "Tes"},
            {"4", "Morgan", "Tes", "Tes"}
    };
    public static ArrayList<Post> getListData(){
        ArrayList<Post> list = new ArrayList<>();
        for (String[] aData : data) {
            Post post = new Post();
            post.setId(Integer.parseInt(aData[0]));
            post.setUsername(aData[1]);
            post.setTitle(aData[2]);
            post.setCaption(aData[3]);
            list.add(post);
        }
        return list;
    }
}

package com.group2.projectmanagementapi.boards;

public class BoardNotFoundException extends RuntimeException{

    public BoardNotFoundException(){
        super ("Board Not Found");
    }

}

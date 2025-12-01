package model;

import interfaces.Completable;

public class Task  implements Completable {

    private String id;
    private String title;
    private String description;
    private boolean completed;

    @Override
    public void complete(){

    }
}

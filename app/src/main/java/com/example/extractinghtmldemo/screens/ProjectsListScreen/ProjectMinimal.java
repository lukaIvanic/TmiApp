package com.example.extractinghtmldemo.screens.ProjectsListScreen;

public class ProjectMinimal {

    private String id;
    private String name;

    public ProjectMinimal(String name, String id) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

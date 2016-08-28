package com.popular_movies.app.model;

import java.io.Serializable;

/**
 * Created by Naledi Madlopha on 2016/08/09.
 */
public class Genre implements Serializable {

    private static long serialVersionUID = 1L;

    private int id;
    private String name;

    public Genre() {
    }

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

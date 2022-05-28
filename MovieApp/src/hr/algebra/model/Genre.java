/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;


import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author kezel
 */
public enum Genre {

    ANIMATED("animirani"),
    HOROR("horor"),
    DRAMA("drama"),
    ACTION("akcija"),
    THRILLER("triler"),
    SF("SF"),
    ADVENTURE("avantura"),
    ROMANTIC_DRAMA("ljubavna drama"),
    COMEDY("komedija"),
    BIOGRAPHY("biografski"),
    MUSICAL("mjuzikl"),
    WESTERN("vestern"),
    CRIME("krimi"),
    ACTION_THRILLER("akcioni triler"),
    CRIMINALISTIC("kriminalistički"),
    HISTORY("istorijski"),
    NO_GENRE("no genre");

    private final String originalName;

    private Genre(String originalName) {
        this.originalName = originalName;
    }

    public static Genre fromOriginalName(String originalName) {

        for (Genre value : values()) {
            if (value.originalName.equals(originalName)) {
                return value;
            }
        }

        return NO_GENRE;
    }

    public static Genre fromName(String name) {

        for (Genre value : values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }

        return NO_GENRE;
    }

    public static Set<Genre> parse(String data) {
        Set<Genre> genreSet = new TreeSet<>();

        String[] genres = data.split(", ");

        for (String genre : genres) {
            genreSet.add(Genre.fromOriginalName(genre));
        }

        return genreSet;
    }

    public static final String ID_GENRE = "IDGenre";
    public static final String NAME = "Name";

    public static final String CREATE_GENRE = "{ CALL createGenre (?, ?) }";
    public static final String UPDATE_GENRE = "{ CALL updateGenre (?, ?) }";
    public static final String DELETE_GENRE = "{ CALL deleteGenre (?) }";
    public static final String SELECT_GENRE = "{ CALL selectGenre (?) }";
    public static final String SELECT_GENRES = "{ CALL selectGenres }";
}

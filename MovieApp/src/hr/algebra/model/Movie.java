/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author kezel
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"title", "pubDate", "description", "originalTitle", "directors", "actors", "duration", "genres", "poster", "link", "guid", "trailer", "startDate"})
public class Movie{

    public static final DateTimeFormatter PUB_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
    public static final DateFormat START_DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");
    public static final Comparator<Movie> TITLE_COMPARATOR = (Movie o1, Movie o2) -> o1.getTitle().compareTo(o2.getTitle());

    // Date date = (Date) Movie.START_DATE_FORMATTER.parse(string);
    // String string = Movie.START_DATE_FORMATTER.format(date);
    @XmlAttribute
    private int id;

    private String title;

    @XmlJavaTypeAdapter(PubDateAdapter.class)
    private LocalDateTime pubDate;

    private String description;

    private String originalTitle;

    @XmlElementWrapper
    @XmlElement(name = "director")
    private Set<Person> directors;

    @XmlElementWrapper
    @XmlElement(name = "actor")
    private Set<Person> actors;

    @XmlJavaTypeAdapter(DurationAdapter.class)
    private Optional<Integer> duration;

    @XmlElementWrapper
    @XmlElement(name = "genre")
    private Set<Genre> genres;

    private String poster;

    private String link;

    private String guid;

    private String trailer;

    @XmlJavaTypeAdapter(StartDateAdapter.class)
    private Date startDate;

    public Movie() {
    }

    public Movie(int id, String title, LocalDateTime pubDate, String description, String originalTitle, Optional<Integer> duration, String poster, String link, String guid, String trailer, Date startDate) {
        this.id = id;
        this.title = title;
        this.pubDate = pubDate;
        this.description = description;
        this.originalTitle = originalTitle;
        this.duration = duration;
        this.poster = poster;
        this.link = link;
        this.guid = guid;
        this.trailer = trailer;
        this.startDate = startDate;
    }

    public Movie(String title, LocalDateTime pubDate, String description, String originalTitle, Set<Person> directors, Set<Person> actors, Optional<Integer> duration, Set<Genre> genres, String poster, String link, String guid, String trailer, Date startDate) {
        this.title = title;
        this.pubDate = pubDate;
        this.description = description;
        this.originalTitle = originalTitle;
        this.directors = directors;
        this.actors = actors;
        this.duration = duration;
        this.genres = genres;
        this.poster = poster;
        this.link = link;
        this.guid = guid;
        this.trailer = trailer;
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getPubDate() {
        return pubDate;
    }

    public String getDescription() {
        return description;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public Set<Person> getDirectors() {
        return directors;
    }

    public Set<Person> getActors() {
        return actors;
    }

    public Optional<Integer> getDuration() {
        return duration;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public String getPoster() {
        return poster;
    }

    public String getLink() {
        return link;
    }

    public String getGuid() {
        return guid;
    }

    public String getTrailer() {
        return trailer;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPubDate(LocalDateTime pubDate) {
        this.pubDate = pubDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setDirectors(Set<Person> directors) {
        this.directors = directors;
    }

    public void setActors(Set<Person> actors) {
        this.actors = actors;
    }

    public void setDuration(Optional<Integer> duration) {
        this.duration = duration;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}

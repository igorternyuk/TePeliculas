package tepeliculas.dto;

import java.sql.Date;

/**
 *
 * @author igor
 */
public class Movie extends SimpleDTO {
    private String description;
    private int classification, genre, duration;
    private java.sql.Date date;
    private int director;
    private byte[] picture;
    private int rating;
    
    public Movie() {
    }

    public Movie(String name, String description, int classification, int genre, int duration,
            Date date, int director, byte[] picture, int rating) {
        super(name);
        this.description = description;
        this.classification = classification;
        this.genre = genre;
        this.duration = duration;
        this.date = date;
        this.director = director;
        this.picture = picture;
        this.rating = rating;
    }

    public Movie(int id, String name, String description,
            int classification, int genre, int duration,
            Date date, int director, byte[] picture, int rating) {
        super(id, name);
        this.description = description;
        this.classification = classification;
        this.genre = genre;
        this.duration = duration;
        this.date = date;
        this.director = director;
        this.picture = picture;
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getClassification() {
        return classification;
    }

    public void setClassification(int classification) {
        this.classification = classification;
    }

    public int getGenre() {
        return genre;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDirector() {
        return director;
    }

    public void setDirector(int director) {
        this.director = director;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

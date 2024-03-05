package models;

import java.awt.image.BufferedImage;
import static java.lang.Math.round;
import java.sql.Time;
import java.util.List;

public class Story {
    private Integer storyID;
    private String story, description, title;
    private Boolean hideStory,hideComment;
    private String author;
    private Integer aprrovedByID;
    private String picture;
    private List<Genre> genres;
    private Data data;
    private Integer numOfReads,rating;

    public Story(Integer storyID, String story, String description, String title, Boolean hideStory, Boolean hideComment, String author, Integer aprrovedByID, String picture, List<Genre> genres) {
        this.storyID = storyID;
        this.story = story;
        this.description = description;
        this.title = title;
        this.hideStory = hideStory;
        this.hideComment = hideComment;
        this.author = author;
        this.aprrovedByID = aprrovedByID;
        this.picture = picture;
        this.genres = genres;
    }

    public Story() {
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStoryID() {
        return storyID;
    }

    public void setStoryID(Integer storyID) {
        this.storyID = storyID;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getHideStory() {
        return hideStory;
    }

    public void setHideStory(Boolean hideStory) {
        this.hideStory = hideStory;
    }

    public Boolean getHideComment() {
        return hideComment;
    }

    public void setHideComment(Boolean hideComment) {
        this.hideComment = hideComment;
    }

    public Integer getAprrovedByID() {
        return aprrovedByID;
    }

    public void setAprrovedByID(Integer aprrovedByID) {
        this.aprrovedByID = aprrovedByID;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Integer getNumOfReads() {
        return numOfReads;
    }

    public void setNumOfReads(Integer numOfReads) {
        this.numOfReads = numOfReads;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        Long value =Math.round(rating);
        this.rating=value.intValue();
    }

    @Override
    public String toString() {
        return "Story{" +
                "storyID=" + storyID +
                ", story='" + story + '\'' +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", hideStory=" + hideStory +
                ", hideComment=" + hideComment +
                ", author='" + author + '\'' +
                ", aprrovedByID=" + aprrovedByID +
                ", picture='" + picture + '\'' +
                ", genres=" + genres +
                '}';
    }
}

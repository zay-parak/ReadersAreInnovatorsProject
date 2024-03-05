package models;

public class Genre {
    private Integer genreID;
    private String name;
    public Genre(Integer genreID, String name) {
        this.genreID = genreID;
        this.name = name;
    }
    public Genre() {
    }

    public Integer getGenreID() {
        return genreID;
    }
    public void setGenreID(Integer catergoryID) {
        this.genreID = catergoryID;
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
                "genreID=" + genreID +
                ", name='" + name + '\'' +
                '}';
    }
}

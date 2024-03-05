package models;

public class Comment {
    private Integer commentID,storyID;
    private String comment, userName;
    public Comment(Integer commentID, String comment, String userName) {
        this.commentID = commentID;
        this.comment = comment;
        this.userName = userName;
    }
    public Comment() {}

    public Integer getCommentID() {
        return commentID;
    }

    public void setCommentID(Integer commentID) {
        this.commentID = commentID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getStoryID() {
        return storyID;
    }

    public void setStoryID(Integer userID) {
        this.storyID = userID;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentID=" + commentID +
                ", comment='" + comment + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}

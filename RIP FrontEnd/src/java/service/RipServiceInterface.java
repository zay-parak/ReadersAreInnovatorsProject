package service;

import jakarta.servlet.http.Part;
import models.*;

import java.sql.Date;
import java.util.List;

public interface RipServiceInterface {
    User loginUser(String email, String password,String code);

    String register(User user);

    void addComment(Comment comment,Integer storyD);

    int rateStory(Integer rating,Integer storyID);

    int likeStory(Boolean liked,Integer storyID);
    String saveDraftAndWait(Story story,Integer type);
    void hide(Integer storyID, Integer hideWhich, Boolean visible);
    String approveWriter(Integer userID, Boolean yesNo);
    String reviewStory(Story story, Boolean acceptance);
    String blockWriter(Integer id,Character type);
    String addEditor(Integer id);
    List<List<Story>> getStories(Integer limite);
    List<Story> getAllStories(Integer limite);
    List<Story> searchGenre(Integer genreID);
    List<Story> searchName(String storyName);
    List<Story> getRecommended(Integer limite);
    List<Comment> getAllComments(Integer storyID);
    List<Genre> getGenres();
    List<User> getSpecificUser(Character type);
    User updateUserDetails(User user);
    List<Story> getAllDrafts();
    List<Story> topStories(int amount, int type, Date start, Date end);
    Double getRating(Integer storyID);
    Integer getNumReads(Integer storyID);
    List<Story> getWaitListStories();
    String deleteComment(Integer commentID);
    String deleteDraft(Integer draftID);
    List<Story> getFavourites(Integer limite);
    void addWaitApproval(Integer id ,String argument);
    List<Approval> getWaitListAppprovals();
    String downloadImage(Part imageData);
    Integer setStoryData(Integer storyID);
    List<User> getTopUsers(Integer amount, Integer type, Date start, Date end);
    List<Genre> getTopGenre(Integer amount, Date start, Date end);  
    User getUser();
    List<Story> getUserStories();
     public String sendSms(String phoneNumber,String message);
}

package service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Part;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import models.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RipServiceImplementation implements RipServiceInterface {

    /*  Will replace every methods body with
        WebTargeter to hit the end points     */
    private final Client client;
    private WebTarget webT;
    private ObjectMapper mappper;
    private final String uri;
    private Response response;
    private User userG;

    public RipServiceImplementation() {
        client = ClientBuilder.newClient();
        mappper = new ObjectMapper();
        uri = "http://localhost:8080/TestingRIPEnd/APP/";
    }

    @Override
    //Works
    public User loginUser(String email, String password, String code) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setSalt(code);//Salt is holding code

        String userUri = uri + "userController/login";
        webT = client.target(userUri);
        response = webT.request().post(Entity.json(user));
        userG = response.readEntity(User.class);
        return userG;
    }

    @Override
    //Works
    public String register(User user) {
        String userUri = uri + "userController/signup";
        webT = client.target(userUri);
        response = webT.request().post(Entity.json(user));
        return response.readEntity(String.class);
    }

    @Override
    //Works
    public void addComment(Comment comment, Integer storyID) {
        comment.setStoryID(storyID);

        String storyUri = uri + "storyController/addComment";
        webT = client.target(storyUri);
          response = webT.request().post(Entity.json(comment));
    }

    @Override
    //Works
    public int rateStory(Integer rating, Integer storyID) {
        Story story = new Story();
        story.setStoryID(storyID);
        story.setData(new Data(false, rating));

        String storyUri = uri + "storyController/rateStory";
        webT = client.target(storyUri);
        response = webT.request().post(Entity.json(story));
        return response.readEntity(int.class);
    }

    @Override
    //Works
    public int likeStory(Boolean liked, Integer storyID) {
        Story story = new Story();
        story.setStoryID(storyID);
        story.setData(new Data(liked, 0));

        String storyUri = uri + "storyController/likeStory";
        webT = client.target(storyUri);
        response = webT.request().post(Entity.json(story));
        return response.readEntity(Integer.class);
    }

    @Override
    //Works
    public String saveDraftAndWait(Story story, Integer type) {
        String storyUri = uri + "storyController/";
        if (type == 2) {
            storyUri += "saveDraft";
        } else {
            storyUri += "submitStory";
        }
        webT = client.target(storyUri);
        response = webT.request().post(Entity.json(story));
        return response.readEntity(String.class);
    }

    @Override
    //Works
    public void hide(Integer storyID, Integer hideWhich, Boolean visible) {
        Approval approval = new Approval();
        approval.setId(storyID);
        approval.setYesNo(visible);

        String storyUri = uri + "storyController/hide";
        if (hideWhich == 1) {
            storyUri += "Story";
        } else {
            storyUri += "Comment";
        }

        webT = client.target(storyUri);
        response = webT.request().post(Entity.json(approval));
    }

    @Override
    //Works
    public String approveWriter(Integer userID, Boolean yesNo) {
        Approval approval = new Approval();
        approval.setId(userID);
        approval.setYesNo(yesNo);

        String userUri = uri + "userController/approveWriter";
        webT = client.target(userUri);
        response = webT.request().post(Entity.json(approval));
        return response.readEntity(String.class);
    }

    @Override
    //Works
    public String reviewStory(Story story, Boolean acceptance) {
        String storyUri = uri + "storyController/reviewStory/{yesNo}";
        webT = client.target(storyUri).resolveTemplate("yesNo", acceptance);
        response = webT.request().post(Entity.json(story)); 
        return response.readEntity(String.class);
    }

    @Override
    //Works
    public String blockWriter(Integer id, Character type) {
        User user = new User();
        user.setUserID(id);
        user.setType(type);

        String userUri = uri + "userController/blockWriter";
        webT = client.target(userUri);
        response = webT.request().post(Entity.json(user));
        return response.readEntity(String.class);
    }

    @Override
    //Works
    public String addEditor(Integer id) {
        String userUri = uri + "userController/addEditor";
        webT = client.target(userUri);
        response = webT.request().post(Entity.json(id));
        return response.readEntity(String.class);
    }

    @Override
    //Wroks
    public List<List<Story>> getStories(Integer limite) {
        String storyUri = uri + "storyController/getStories/{limite}";
        webT = client.target(storyUri).resolveTemplate("limite", limite);
        response = webT.request().get();

        List<List<Story>> cat = new ArrayList<>();
        try {
            cat = mappper.readValue(webT.request().accept(MediaType.APPLICATION_JSON).get(String.class), new TypeReference<List<List<Story>>>() {
            });
        } catch (IOException ex) {
            Logger.getLogger(RipServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cat;
    }

    @Override
    //Wroks
    public List<Story> getAllStories(Integer limite) {
        String storyUri = uri + "storyController/getAllStories/{limite}";
        webT = client.target(storyUri).resolveTemplate("limite", limite);
        response = webT.request().get();
        try {
            return Arrays.asList(mappper.readValue(webT.request().accept(MediaType.APPLICATION_JSON).get(String.class), Story[].class));
        } catch (IOException ex) {
            Logger.getLogger(RipServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @Override
    //Works
    public List<Story> searchGenre(Integer genreID) {
        String storyUri = uri + "storyController/searchGenre/{id}";
        webT = client.target(storyUri).resolveTemplate("id", genreID);
        response = webT.request().get();
        try {
            return Arrays.asList(mappper.readValue(webT.request().accept(MediaType.APPLICATION_JSON).get(String.class), Story[].class));
        } catch (IOException ex) {
            Logger.getLogger(RipServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @Override
    //Works
    public List<Story> searchName(String storyName) {
        String storyUri = uri + "storyController/searchName/{name}";
        webT = client.target(storyUri).resolveTemplate("name", storyName);
        response = webT.request().get();
        try {
            return Arrays.asList(mappper.readValue(webT.request().accept(MediaType.APPLICATION_JSON).get(String.class), Story[].class));
        } catch (IOException ex) {
            Logger.getLogger(RipServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @Override
    //Works
    public List<Story> getRecommended(Integer limite) {
        String storyUri = uri + "storyController/getRecommended/{limite}";
        webT = client.target(storyUri).resolveTemplate("limite", limite);
        response = webT.request().get();
        try {
            return Arrays.asList(mappper.readValue(webT.request().accept(MediaType.APPLICATION_JSON).get(String.class), Story[].class));
        } catch (IOException ex) {
            Logger.getLogger(RipServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @Override
    //Works
    public List<Comment> getAllComments(Integer storyID) {
        String storyUri = uri + "storyController/getAllComments/{id}";
        webT = client.target(storyUri).resolveTemplate("id", storyID);
        response = webT.request().get();
        try {
            return Arrays.asList(mappper.readValue(webT.request().accept(MediaType.APPLICATION_JSON).get(String.class), Comment[].class));
        } catch (IOException ex) {
            Logger.getLogger(RipServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @Override
    //Works
    public List<Genre> getGenres() {
        String storyUri = uri + "storyController/getGenres";
        webT = client.target(storyUri);
        response = webT.request().get();
        try {
            return Arrays.asList(mappper.readValue(webT.request().accept(MediaType.APPLICATION_JSON).get(String.class), Genre[].class));
        } catch (IOException ex) {
            Logger.getLogger(RipServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @Override
    //Works
    public List<User> getSpecificUser(Character type) {
        String storyUri = uri + "userController/getSpercificUser/{type}";
        webT = client.target(storyUri).resolveTemplate("type", type);
        response = webT.request().get();
        try {
            return Arrays.asList(mappper.readValue(webT.request().accept(MediaType.APPLICATION_JSON).get(String.class), User[].class));
        } catch (IOException ex) {
            Logger.getLogger(RipServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @Override
    //Works
    public User updateUserDetails(User user) {
        String userUri = uri + "userController/updateUserDetails";
        webT = client.target(userUri);
        response = webT.request().post(Entity.json(user));
            userG = response.readEntity(User.class);
        return userG;
    }

    @Override
    //Works
    public List<Story> getAllDrafts() {
        String storyUri = uri + "storyController/getAlldrafts";
        webT = client.target(storyUri);
        response = webT.request().get();
        try {
            return Arrays.asList(mappper.readValue(webT.request().accept(MediaType.APPLICATION_JSON).get(String.class), Story[].class));
        } catch (IOException ex) {
            Logger.getLogger(RipServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @Override
    //Works
    public List<Story> topStories(int amount, int type, Date start, Date end) {
        String storyUri = uri + "storyController/getTopStories/{amount}/{type}/{start}/{end}";
        webT = client.target(storyUri).resolveTemplate("amount", amount).resolveTemplate("type", type).resolveTemplate("start", start).resolveTemplate("end", end);
        response = webT.request().get();
        try {
            return Arrays.asList(mappper.readValue(webT.request().accept(MediaType.APPLICATION_JSON).get(String.class), Story[].class));
        } catch (IOException ex) {
            Logger.getLogger(RipServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @Override
    //Works
    public Double getRating(Integer storyID) {
        String storyUri = uri + "storyController/getRating";
        webT = client.target(storyUri);
        response = webT.request().post(Entity.json(storyID));
        return response.readEntity(Double.class);
    }

    @Override
    //Works
    public Integer getNumReads(Integer storyID) {
        String storyUri = uri + "storyController/GetNumberReads";
        webT = client.target(storyUri);
        response = webT.request().post(Entity.json(storyID));
        return response.readEntity(Integer.class);
    }

    @Override
    //Works
    public List<Story> getWaitListStories() {
        String storyUri = uri + "storyController/getWaitListStories";
        webT = client.target(storyUri);
        response = webT.request().get();
        try {
            return Arrays.asList(mappper.readValue(webT.request().accept(MediaType.APPLICATION_JSON).get(String.class), Story[].class));
        } catch (IOException ex) {
            Logger.getLogger(RipServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @Override
    //Works
    public String deleteComment(Integer commentID) {
        String storyUri = uri + "storyController/deleteComment/{id}";
        webT = client.target(storyUri).resolveTemplate("id", commentID);
        response = webT.request().get();
        return response.readEntity(String.class);
    }

    @Override
    //Works
    public String deleteDraft(Integer draftID) {
        String storyUri = uri + "storyController/deleteDraft/{id}";
        webT = client.target(storyUri).resolveTemplate("id", draftID);
        response = webT.request().get();
        return response.readEntity(String.class);
    }

    @Override
    //Works
    public List<Story> getFavourites(Integer limite) {
        String storyUri = uri + "storyController/getFavourites/{limite}";
        webT = client.target(storyUri).resolveTemplate("limite", limite);
        response = webT.request().get();
        try {
            return Arrays.asList(mappper.readValue(webT.request().accept(MediaType.APPLICATION_JSON).get(String.class), Story[].class));
        } catch (IOException ex) {
            Logger.getLogger(RipServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @Override
    //Works
    public void addWaitApproval(Integer id, String argument) {
        Approval wA = new Approval(id, "", argument);

        String userUri = uri + "userController/addWaitApproval";
        webT = client.target(userUri);
        response = webT.request().post(Entity.json(wA));
    }

    @Override
    //Works
    public List<Approval> getWaitListAppprovals() {
        String userUri = uri + "userController/getWaitListApprovals";
        webT = client.target(userUri);
        response = webT.request().get();
        try {
            return Arrays.asList(mappper.readValue(webT.request().accept(MediaType.APPLICATION_JSON).get(String.class), Approval[].class));
        } catch (IOException ex) {
            Logger.getLogger(RipServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @Override
    //Works
    public String downloadImage(Part imageData) {
        FileOutputStream fis = null;
        InputStream fileContent = null;
        String fileName = Paths.get(imageData.getSubmittedFileName()).getFileName().toString();
        try {
            fileContent = imageData.getInputStream();
            fis = new FileOutputStream("C:\\Users\\Zayaan\\OneDrive\\Desktop\\RIP FrontEnd\\web\\books\\" + fileName);
            byte[] buffer = new byte[1024];
            int count;
            while ((count = fileContent.read(buffer, 0, 1024)) != -1) {
                fis.write(buffer, 0, count);
            }
        } catch (IOException ex) {
            Logger.getLogger(RipServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fileContent != null) {
                    fileContent.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(RipServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return fileName;
    }

    @Override
    //Works
    public Integer setStoryData(Integer storyID) {
        String storyUri = uri + "storyController/setStoryData/{id}";
        webT = client.target(storyUri).resolveTemplate("id", storyID);
        response = webT.request().get();
        return response.readEntity(Integer.class);
    }

    @Override
    //Test
    public List<Genre> getTopGenre(Integer amount, Date start, Date end) {
        String userUri = uri + "topController/topGenre/{amount}/{start}/{end}";
        webT = client.target(userUri).resolveTemplate("amount", amount).resolveTemplate("start", start).resolveTemplate("end", end);
        response = webT.request().get();
        try {
            return Arrays.asList(mappper.readValue(webT.request().accept(MediaType.APPLICATION_JSON).get(String.class), Genre[].class));
        } catch (IOException ex) {
            Logger.getLogger(RipServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @Override
    //Test
    public List<User> getTopUsers(Integer amount, Integer type, Date start, Date end) {
        String userUri = uri + "topController/topUsers/{amount}/{type}/{start}/{end}";
        webT = client.target(userUri).resolveTemplate("amount", amount).resolveTemplate("type", type).resolveTemplate("start", start).resolveTemplate("end", end);
        response = webT.request().get();
        try {
            return Arrays.asList(mappper.readValue(webT.request().accept(MediaType.APPLICATION_JSON).get(String.class), User[].class));
        } catch (IOException ex) {
            Logger.getLogger(RipServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @Override
    public User getUser() {
        return userG;
    }

    @Override
    //Wroks
    public List<Story> getUserStories() {
        String storyUri = uri + "storyController/getUserStories";
        webT = client.target(storyUri);
        response = webT.request().get();
        try {
            return Arrays.asList(mappper.readValue(webT.request().accept(MediaType.APPLICATION_JSON).get(String.class), Story[].class));
        } catch (IOException ex) {
            Logger.getLogger(RipServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    @Override
    public String sendSms(String phoneNumber,String message){
        String smsUri=uri+"storyController/sendSMS/{phoneNumber}/{message}";
        webT = client.target(smsUri).resolveTemplate( "phoneNumber",phoneNumber).resolveTemplate("message",message);
        response = webT.request().get();
        return response.readEntity(String.class);
    }


}

package service;

import com.fasterxml.jackson.databind.ObjectMapper;;
import daos.RipDaoImplementation;
import daos.RipDaoInterface;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.System.Logger.Level;
import java.util.logging.Logger;
import models.*;
import passwordEncryption.PassBasedEnc;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RipServiceImplementation implements RipServiceInterface{
    private RipDaoInterface rdi= new RipDaoImplementation();
    private final Integer NUMBER=60; 
     private final Client client;
    private WebTarget webT;
    private ObjectMapper mappper;
    private final String smsURI;
    private Response response;

    public RipServiceImplementation(){
        rdi = new RipDaoImplementation();
        client = ClientBuilder.newClient();
        mappper = new ObjectMapper();
        smsURI = "http://196.41.180.157:8080/sms/sms_request;";
    }
    //Checks password, returns User if password correct
    @Override
    public User loginUser(String email, String password,String code) {
        return rdi.login(email, password,code);
    }

    @Override
    public String register(User user) {
        User newUser = user;
        String salt = PassBasedEnc.getSaltValue(30);
        String securePassword= PassBasedEnc.generateSecurePassword(user.getPassword(),salt);
        newUser.setPassword(securePassword);
        newUser.setSalt(salt);
        return rdi.register(newUser);
    }

    @Override
    public void addComment(Comment comment,Integer storyID){
        rdi.addComment(comment,storyID);
    }

    @Override
    public int rateStory(Integer rating,Integer storyID){
        return rdi.rateStory(rating,storyID);
    }

    @Override
    public int likeStory(Boolean liked,Integer storyID){
        return rdi.likeStory(liked,storyID);
    }

    @Override
    public String saveDraftAndWait(Story story, Integer type) {
        return rdi.saveDraftAndWait(story,type);
    }

    @Override
    public void hide(Integer storyID, Integer hideWhich, Boolean visible) {
        rdi.hide(storyID,hideWhich,visible);
    }

    @Override
    public String approveWriter(Integer userID, Boolean yesNo) {
        return rdi.approveWriter(userID,yesNo);
    }

    @Override
    public String reviewStory(Story story, Boolean acceptance) {
            String phone=rdi.getPhoneNumber(story.getAuthor());
            String message="Your story with the title of:  "+story.getTitle()+" has been";
            if(acceptance){message+=" accepted";}else{message+=" denied";}
            sendSms(phone, message);
        
        return rdi.reviewStory(story,acceptance);
    }

    @Override
    public String blockWriter(Integer id, Character type) {
        return rdi.blockWriter(id,type);
    }

    @Override
    public String addEditor(Integer id) {
        return rdi.addEditor(id);
    }

    @Override
    public List<Story> getAllStories(Integer limite) {
        Integer start=(limite-1)*NUMBER,end=limite*NUMBER;
        List<Story> stories=rdi.getAllStories();
        if(start>stories.size()){return new ArrayList<>();}
        if(end>stories.size()){end=stories.size();}
        return stories.subList(start,end);
    }

    @Override
    public List<Story> searchGenre(Integer genreID) {
        return rdi.searchGenre(genreID);
    }

    @Override
    public List<Story> searchName(String storyName){
        return rdi.searchName(storyName);
    }

    @Override
    public List<Story> getRecommended(Integer limite) {
        Integer start=(limite-1)*NUMBER,end=limite*NUMBER;
        List<Story> stories=rdi.getRecommended();
        if(start>stories.size()){return new ArrayList<>();}
        if(end>stories.size()){end=stories.size();}
        return stories.subList(start,end);
    }

    @Override
    public List<Comment> getAllComments(Integer storyID) {
        return rdi.getAllComments(storyID);
    }

    @Override
    public List<Genre> getGenres() {
        return rdi.getGenres();
    }

    @Override
    public List<User> getSpecificUser(Character type) {
        return rdi.getSpecificUser(type);
    }

    @Override
    public User updateUserDetails(User user) {
        User newUser = user;
        String salt = PassBasedEnc.getSaltValue(30);
        String securePassword= PassBasedEnc.generateSecurePassword(user.getPassword(),salt);
        newUser.setPassword(securePassword);
        newUser.setSalt(salt);
        return rdi.updateUserDetails(newUser);
    }

    @Override
    public List<Story> getAllDrafts() {
        return rdi.getAllDrafts();
    }

    @Override
    public List<Story> topStories(int amount, int type, Date start, Date end) {
        return rdi.topStories(amount,type,start,end);
    }

    @Override
    public Double getRating(Integer storyID) {
        return rdi.getRating(storyID);
    }

    @Override
    public Integer getNumReads(Integer storyID) {
        return rdi.getNumReads(storyID);
    }

    @Override
    public List<Story> getWaitListStories() {
        List<Story> stories=rdi.getWaitListStories();

        return stories;
    }

    @Override
    public String deleteComment(Integer commentID) {
        return rdi.deleteComment(commentID);
    }

    @Override
    public String deleteDraft(Integer draftID) {
        return rdi.deleteDraft(draftID);
    }

    @Override
    public List<Story> getFavourites(Integer limite){
        Integer start=(limite-1)*NUMBER,end=limite*NUMBER;
        List<Story> stories=rdi.getFavourites();
        if(start>stories.size()){return new ArrayList<>();}
        if(end>stories.size()){end=stories.size();}
        return stories.subList(start,end);
    }

    @Override
    public void addWaitApproval(Integer id, String argument) {
        rdi.addWaitApproval(id, argument);
    }

    @Override
    public List<Approval> getWaitListAppprovals() {
        return rdi.getWaitListAppprovals();
    }

    @Override
    public Integer setStoryData(Integer storyID){
    return rdi.setStoryData(storyID);
    }

    @Override
    public List<User> topUsers(Integer amount, Integer type, Date start, Date end) {
    return rdi.topUsers(amount, type, start, end);
    }

    @Override
    public List<Genre> topGenre(Integer amount, Date start, Date end) {
    return rdi.topGenre(amount, start, end);
    }
    
    @Override
    public List<Story> getUserStories(){
    return rdi.getUserStories();}
    
@Override
    public String sendSms(String phoneNumber,String message){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd, HH:mm:ss");
        SMSSend request = new SMSSend(java.time.LocalDateTime.now().format(formatter),"GROUP2","2group",phoneNumber,message);
        webT =client.target(smsURI);
        response = webT.request(MediaType.APPLICATION_XML).post(Entity.xml(jaxbObjectToXML(request)));
        
        if (response.getStatus() != Response.Status.OK.getStatusCode()){
            return "SMS did not send ";
        }
        
        //xml response to sms object
        
        SMSReceive smsR = xmlToJaxbObj(response.readEntity(String.class));
        response.close();
        if(smsR.getCode().equals("0000")){
            return "SMS sent!!";
        }else {
            return "SMS not sent error" + smsR.getResponse()    ;
        }
    }
    
    private static String jaxbObjectToXML(SMSSend request) {
    String xmlString = "";
    try {
        JAXBContext context = JAXBContext.newInstance(SMSSend.class);
        Marshaller m = context.createMarshaller();

        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML
        m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        StringWriter sw = new StringWriter();
        m.marshal(request, sw);
        xmlString = sw.toString();

    } catch (JAXBException e) {
        e.printStackTrace();
    }

    return xmlString;
}
    
    private static SMSReceive xmlToJaxbObj(String request){
        SMSReceive sr = null;
        
            JAXBContext context;
        try {
            context = JAXBContext.newInstance(SMSReceive.class);
       
            Unmarshaller marshal = context.createUnmarshaller();
            
            StringReader reader = new StringReader(request);
            sr =  (SMSReceive) marshal.unmarshal(reader);
        
            } catch (JAXBException ex) {
            Logger.getLogger(RipServiceImplementation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return sr;
    }
}

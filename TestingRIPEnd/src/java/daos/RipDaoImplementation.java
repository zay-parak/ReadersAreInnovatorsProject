package daos;

import connection.DBManager;
import connection.sendEmail;
import models.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static passwordEncryption.PassBasedEnc.verifyUserPassword;

public class RipDaoImplementation implements RipDaoInterface {

    private User userG = new User();

    // Allows for methods to place data into the different intercept table
    private String selectionType(Integer choice) {
        switch (choice) {
            case 1:
                return "story";
            case 2:
                return "draft";
            case 3:
                return "wait";
            case 4:
                return "user";
            default:
                return "Not a choice";
        }
    }

    // Allows for the getting of userIDs from only their username
    private Integer getUserIDFromUsername(String username) throws SQLException {
        Connection connection = DBManager.getConn();
        String query = "Select userID from user where username=?";
        PreparedStatement st = connection.prepareStatement(query);
        st.setString(1, username);
        ResultSet rs = st.executeQuery();
        rs.next();
        Integer in = rs.getInt(1);
        rs.close();
        st.close();
        connection.close();
        return in;
    }
    // Allows for the getting of storyID from only its title
    private Integer getStoryIDFromTitle(String title, Integer type) throws SQLException {
        Connection connection = DBManager.getConn();
        String typeO = selectionType(type);
        String query = "Select " + typeO + "ID from " + typeO + " where title=?";
        PreparedStatement st = connection.prepareStatement(query);
        st.setString(1, title);
        ResultSet rs = st.executeQuery();
        rs.next();
        Integer in = rs.getInt(1);
        rs.close();
        st.close();
        return in;

    }

    // Get data for stories to be on the story for the specific user
    private Data getData(Integer storyID) {
        Connection connection = DBManager.getConn();
        String query = "select d.liked,d.rating from data d where d.storyID=? and d.userID=?";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement(query);
            st.setInt(1, storyID);
            st.setInt(2, userG.getUserID());
            rs = st.executeQuery();
            if (rs.next()) {
                return new Data(rs.getBoolean(1), rs.getInt(2));
            }
        } catch (SQLException ex) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    // OverLoaded method to get genres from all intercept genre tables
    private List<Genre> getAllGenres(Integer type, Integer id) {
        Connection connection = DBManager.getConn();
        String intercept = selectionType(type);

        List<Genre> genreList = new ArrayList<>();
        String query = "SELECT g.genreID,g.genre from genre g," + intercept + "genre a where a." + intercept + "id=? and a.genreID=g.genreID";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();
            while (rs.next()) {
                Genre genre = new Genre(rs.getInt(1), rs.getString(2));
                genreList.add(genre);
            }

            return genreList;
        } catch (SQLException e) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return genreList;
    }

    // OverLoaded method to  set genres from all intercept genre tables
    private List<Genre> setAllGenres(Integer type, Integer id, List<Genre> genres) {
        Connection connection = DBManager.getConn();
        String typeO = selectionType(type);
        String query = "Delete from " + typeO + "genre where " + typeO + "ID=? ";
        PreparedStatement delete = null;
        try {
            delete = connection.prepareStatement(query);
            delete.setInt(1, id);
            delete.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (delete != null) {
                try {
                    delete.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        connection = DBManager.getConn();
        PreparedStatement st = null;
        try {
            if (genres != null) {
                for (Genre g : genres) {
                    query = "Insert into " + typeO + "genre (" + typeO + "ID,genreID) values (?,?)";
                    st = connection.prepareStatement(query);
                    st.setInt(1, id);
                    st.setInt(2, g.getGenreID());
                    st.executeUpdate();
                }
            }
        } catch (SQLException e) {
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return null;
    }

    private boolean verifyChecking(Integer userID, String code) {
        Connection connection = DBManager.getConn();
        String query = "select v.verificationCode, v.freeOfVerify from userverification v where v.userID=?";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement(query);
            st.setInt(1, userID);
            rs = st.executeQuery();
            //See if user has not verified
            if (rs.next()) {
                // If not verified and code matches
                if (code.equals(rs.getString(1))) {
                    query = "delete from userverification where userID=?";
                    st = connection.prepareStatement(query);
                    st.setInt(1, userID);
                    st.executeUpdate();
                    return true;
                }

                //See if they have login in without verifing before
                if (rs.getBoolean(2) == false) {
                    query = "Update userverification set freeOfVerify=true where userID=?";
                    st = connection.prepareStatement(query);
                    st.setInt(1, userID);
                    st.executeUpdate();
                    return true;
                }
                return false;
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return true;
    }

    @Override
    //Allows for the login of a user
    public User login(String email, String password, String code) {
        Connection connection = DBManager.getConn();
        userG = null;
        String query = "Select * from user where email=?";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement(query);
            st.setString(1, email);
            rs = st.executeQuery();
            while (rs.next()) {
                //Verify if password matches password in database
                //if it does then only a user is created else will return null
                if (verifyUserPassword(password, rs.getString("password"), rs.getString("salt"))) {

                    if (verifyChecking(rs.getInt(1), code) == true) {
                        userG = new User(rs.getInt(1), rs.getString(3), rs.getString(4), rs.getString(2), rs.getString(5), rs.getString(7).charAt(0), rs.getString(6));
                        userG.setFavourites(getAllGenres(4, userG.getUserID()));
                    }
                }
            }
        } catch (SQLException e) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return userG;
    }

    private void sendEmailVerification(Integer id, String email) {
        sendEmail send = new sendEmail(email, 1, "", "");
        String code = send.emailSender();

        Connection connection = DBManager.getConn();
        PreparedStatement st = null;
        String query = "insert into userverification (userID,verificationCode) values (?,?)";
        try {
            if (!code.equals("Email not sent")) {
                st = connection.prepareStatement(query);
                st.setInt(1, id);
                st.setString(2, code);
                st.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    @Override
    //Allows for a user to be added to the database
    public String register(User user) {
        Connection connection = DBManager.getConn();
        PreparedStatement st = null;

        try {
            //Adding user to database
            String query = "INSERT INTO user (name, surname, userName, email, password, phoneNumber, salt) VALUES (?,?,?,?,?,?,?)";
            st = connection.prepareStatement(query);
            st.setString(1, user.getName());
            st.setString(2, user.getSurname());
            st.setString(3, user.getUsername());
            st.setString(4, user.getEmail());
            st.setString(5, user.getPassword());
            st.setString(6, user.getPhoneNumber());
            st.setString(7, user.getSalt());
            st.executeUpdate();
            setAllGenres(4, getUserIDFromUsername(user.getUsername()), user.getFavourites());
            sendEmailVerification(getUserIDFromUsername(user.getUsername()), user.getEmail());
            return "Registration successful";
        } catch (SQLException ignored) {
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return "Registration unsuccessful";
    }

    @Override
    //Allows for a user to write a comment to a story they have read
    public void addComment(Comment comment, Integer storyID) {
        Connection connection = DBManager.getConn();
        PreparedStatement st = null;
        try {
            String sql = "Insert into comment (comment,userID,storyID) values(?,?,?)";
            st = connection.prepareStatement(sql);
            st.setString(1, comment.getComment());
            st.setInt(2, userG.getUserID());
            st.setInt(3, storyID);
            st.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    @Override
    //Allows a user to add/change their rating to a story they have read
    public Integer rateStory(Integer rating, Integer storyID) {
        Connection connection = DBManager.getConn();
        PreparedStatement st = null;
        try {
            String sql = "Update data SET rating = ?, timeRated=current_timestamp where userID=? and storyID=?";

            st = connection.prepareStatement(sql);
            st.setInt(2, userG.getUserID());
            st.setInt(3, storyID);
            st.setInt(1, rating);
            //returns 0 if person has not read the book first
            return st.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return -1;
    }

    @Override
    //Allows a user to add/remove their life from a story they have read
    public Integer likeStory(Boolean liked, Integer storyID) {
        Connection connection = DBManager.getConn();
        PreparedStatement st = null;
        try {
            String query = "Update data SET liked = ?, timeLiked=current_timestamp where userID=? and storyID=?";
            st = connection.prepareStatement(query);
            st.setBoolean(1, liked);
            st.setInt(2, userG.getUserID());
            st.setInt(3, storyID);

            return st.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return -1;
    }

    @Override
    //Allows user to save a draft or submit a story to a waiting List
    public String saveDraftAndWait(Story story, Integer type) {
        Connection connection = DBManager.getConn();
        String typeO = selectionType(type);
        PreparedStatement st = null;
        if (type == 2) {
            String delete = "Delete from draft where draftID=?";
            try {
                st = connection.prepareStatement(delete);
                st.setInt(1, story.getStoryID());
                st.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        String query = "INSERT INTO " + typeO + "(title, story, description, picture, authorID) VALUES (?,?,?,?,?)";
        try {
            st = connection.prepareStatement(query);
            st.setString(1, story.getTitle());
            st.setString(2, story.getStory());
            st.setString(3, story.getDescription());
            st.setString(4, story.getPicture());
            st.setInt(5, userG.getUserID());
            st.executeUpdate();

            setAllGenres(type, getStoryIDFromTitle(story.getTitle(), type), story.getGenres());
            return typeO + " did save";
        } catch (SQLException ignored) {
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return typeO + " did not save";
    }

    @Override
    // Allows a user the ability to hide/unhide their comments or story
    public void hide(Integer storyID, Integer hideWhich, Boolean visible) {
        Connection connection = DBManager.getConn();
        PreparedStatement st = null;
        try {
            String choice;
            switch (hideWhich) {
                case 1:
                    choice = "hideStory";
                    break;
                default:
                    choice = "hideComment";
            }
            String sql = "UPDATE story SET " + choice + " = ? WHERE storyID = ?";
            st = connection.prepareStatement(sql);
            st.setBoolean(1, visible);
            st.setInt(2, storyID);
            st.executeUpdate();

        } catch (SQLException ignored) {
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void sendApproveEmail(Integer id, String subject, String text) {
        Connection connection = DBManager.getConn();
        String query = "SELECT email FROM rip.user where userID=?";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                new sendEmail(rs.getString(1), 2, subject, text).emailSender();
            }
        } catch (SQLException ex) {
            Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    @Override
    // Allows Editors to approve/deny a user request be become Writer
    public String approveWriter(Integer userID, Boolean yesNo) {
        Connection connection = DBManager.getConn();
        String subject = "Rejected of premotion to Writer";
        String text = "Editor: " + userG.getUsername() + " has reviewed your details and has concluded that you do not meet the requirments to become a Writer."
                + "\n We appologise for any inconvenience caused. Hope you still stay with us as a Reader ";
        String query = "UPDATE user SET type = 'W' WHERE userID  = ?";
        String query2 = "DELETE FROM waitapproval WHERE userID = ?";
        PreparedStatement st = null;
        PreparedStatement st2 = null;
        try {
            st = connection.prepareStatement(query);
            st2 = connection.prepareStatement(query2);

            st2.setInt(1, userID);
            st2.executeUpdate();

            if (yesNo) {
                st.setInt(1, userID);
                st.executeUpdate();
                subject = "Approval to become a Writer";
                text = "Editor: " + userG.getUsername() + " has reviewed your details and found you meet the requirments to become a Writer."
                        + "\n Hope to recieve some interesting stories from you in the future.";
                sendApproveEmail(userID, subject, text);
                return "User Approval Successful";
            }
            sendApproveEmail(userID, subject, text);
            return "User Denied Successful";
        } catch (SQLException ignored) {
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st2 != null) {
                try {
                    st2.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return "User Approval/Denied Unsuccessful";
    }

    @Override
    // Allows Editors to approve/deny a story
    public String reviewStory(Story story, Boolean acceptance) {
        Connection connection = DBManager.getConn();
        PreparedStatement st2 = null;
        PreparedStatement st = null;
        try {
            Integer authorID = getUserIDFromUsername(story.getAuthor());

            String query2 = "DELETE from wait WHERE waitID = ? ";
            st2 = connection.prepareStatement(query2);
            st2.setInt(1, getStoryIDFromTitle(story.getTitle(), 3));
            st2.executeUpdate();

            if (acceptance) {
                String query = "INSERT INTO story(title,story,description,authorID,picture,approvedByID) values(?,?,?,?,?,?)";

                st = connection.prepareStatement(query);
                st.setString(1, story.getTitle());
                st.setString(2, story.getStory());
                st.setString(3, story.getDescription());
                st.setInt(4, authorID);
                st.setString(5, story.getPicture());
                st.setInt(6, userG.getUserID());
                st.executeUpdate();

                setAllGenres(1, getStoryIDFromTitle(story.getTitle(), 1), story.getGenres());
                return "Added story successfully";
            }
            return "Denied story";

        } catch (SQLException ignored) {
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st2 != null) {
                try {
                    st2.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return "Unexpected error";
    }

    @Override
    // Allows editors to block/ unblock writers
    public String blockWriter(Integer id, Character type) {
        Connection connection = DBManager.getConn();
        PreparedStatement st = null;
        try {

            String sql = "UPDATE user SET type = ? WHERE userID = ?";
            String returns = "Unblocked";

            st = connection.prepareStatement(sql);
            st.setString(1, String.valueOf(type));
            st.setInt(2, id);
            st.executeUpdate();
            if (type.equals('B')) {
                returns = "Blocked";
            }
            return returns;
        } catch (SQLException ignored) {
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return "Not blocked";
    }

    @Override
    // Allow admin editor to add editors
    public String addEditor(Integer id) {
        Connection connection = DBManager.getConn();
        String query = "UPDATE user SET type = 'E' WHERE userID  = ?";
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(query);
            st.setInt(1, id);
            st.executeUpdate();

            return "Editor added";
        } catch (SQLException ignored) {
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return "Editor not added";
    }

    @Override
    //Gets all stories for readers to explore new genres
    public List<Story> getAllStories() {
        Connection connection = DBManager.getConn();
        List<Story> storyList = new ArrayList<>();
        String query = "SELECT s.*, u.username from story s, user u where u.userID=s.authorID";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                Story story = new Story(rs.getInt(1), rs.getString(3),
                        rs.getString(4), rs.getString(2), rs.getBoolean(6),
                        rs.getBoolean(7), rs.getString(11),
                        rs.getInt(10), rs.getString(5), getAllGenres(1, rs.getInt(1)));
                story.setData(getData(story.getStoryID()));
                story.setNumOfReads(getNumReads(story.getStoryID()));
                story.setRating(getRating(story.getStoryID()));
                storyList.add(story);
            }
        } catch (SQLException ignored) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return storyList;
    }

    @Override
    // Allow users to search by genre
    public List<Story> searchGenre(Integer genreID) {
        Connection connection = DBManager.getConn();
        List<Story> storyList = new ArrayList<>();
        String query = "SELECT s.*,u.username from story s ,storygenre g ,user u where u.userID=s.authorID and s.storyID=g.storyID and g.genreID=? ";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement(query);
            st.setInt(1, genreID);
            rs = st.executeQuery();
            while (rs.next()) {
                Story story = new Story(rs.getInt(1), rs.getString(3),
                        rs.getString(4), rs.getString(2), rs.getBoolean(6),
                        rs.getBoolean(7), rs.getString(11),
                        rs.getInt(10), rs.getString(5), getAllGenres(1, rs.getInt(1)));
                story.setData(getData(story.getStoryID()));
                story.setNumOfReads(getNumReads(story.getStoryID()));
                story.setRating(getRating(story.getStoryID()));
                storyList.add(story);
            }
        } catch (SQLException ignored) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return storyList;
    }

    @Override
    // Allow users to do a search based on a portion of the title
    public List<Story> searchName(String storyName) {
        Connection connection = DBManager.getConn();
        List<Story> stories = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            storyName = storyName.replaceAll(" ", "_");
            String query = "SELECT s.*,u.username FROM story s,user u WHERE u.userID=s.authorID and title LIKE ?";
            st = connection.prepareStatement(query);
            st.setString(1, "%" + storyName + "%");
            rs = st.executeQuery();

            while (rs.next()) {
                Story story = new Story(rs.getInt(1), rs.getString(3),
                        rs.getString(4), rs.getString(2), rs.getBoolean(6),
                        rs.getBoolean(7), rs.getString(11),
                        rs.getInt(10), rs.getString(5), getAllGenres(1, rs.getInt(1)));
                story.setData(getData(story.getStoryID()));
                story.setNumOfReads(getNumReads(story.getStoryID()));
                story.setRating(getRating(story.getStoryID()));
                stories.add(story);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return stories;
    }

    @Override
    // Readers get their favourite stories based on genres they like
    public List<Story> getRecommended() {
        Connection connection = DBManager.getConn();
        List<Story> stories = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT distinct(s.storyID),s.*,u.username FROM story s,storygenre g, user u where s.storyID=g.storyID and s.authorID=u.userID and (";
            StringBuilder search = new StringBuilder();
            int count = 0;
            for (Genre g : userG.getFavourites()) {
                if (count == 0) {
                    search.append("g.genreID=").append(g.getGenreID());
                    count++;
                } else {
                    search.append(" or g.genreID=").append(g.getGenreID());
                }
            }
            sql += search + ")";
            st = connection.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                Story story = new Story(rs.getInt(2), rs.getString(4),
                        rs.getString(5), rs.getString(3), rs.getBoolean(7),
                        rs.getBoolean(8), rs.getString(12),
                        rs.getInt(11), rs.getString(6), getAllGenres(1, rs.getInt(1)));
                story.setData(getData(story.getStoryID()));
                story.setNumOfReads(getNumReads(story.getStoryID()));
                story.setRating(getRating(story.getStoryID()));
                stories.add(story);
            }
            return stories;
        } catch (SQLException ignored) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return stories;
    }

    @Override
    // Get comments for a story
    public List<Comment> getAllComments(Integer storyID) {
        Connection connection = DBManager.getConn();
        List<Comment> commentList = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;
        String query = "SELECT c.commentID, c.comment,u.username from comment c,user u where c.userID=u.userID and c.storyID = ?";
        try {
            st = connection.prepareStatement(query);
            st.setInt(1, storyID);
            rs = st.executeQuery();

            while (rs.next()) {
                commentList.add(new Comment(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }

        } catch (SQLException ignored) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return commentList;
    }

    @Override
    // Get a specific User
    public List<User> getSpecificUser(Character type) {
        Connection connection = DBManager.getConn();
        List<User> userList = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;
        String query = "SELECT * from user WHERE type  = ?";
        try {
            st = connection.prepareStatement(query);
            st.setString(1, String.valueOf(type));

            rs = st.executeQuery();
            while (rs.next()) {
                userList.add(new User(rs.getInt(1), rs.getString(3),
                        rs.getString(4), rs.getString(2), rs.getString(5),
                        rs.getString(7).charAt(0), rs.getString(6)));
            }
        } catch (SQLException ignored) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return userList;
    }

    @Override
    // Update a users details
    public User updateUserDetails(User user) {
        Connection connection = DBManager.getConn();
        PreparedStatement st = null;
        ResultSet rs = null;
        String update = "UPDATE user SET name =?,surname=?,username=?,email=?,password=?,salt=?,phonenumber=? where UserID = ?";
        try {
            st = connection.prepareStatement(update);

            st.setString(1, user.getName());
            st.setString(2, user.getSurname());
            st.setString(3, user.getUsername());
            st.setString(4, user.getEmail());
            st.setString(5, user.getPassword());
            st.setString(6, user.getSalt());
            st.setString(7, user.getPhoneNumber());
            st.setInt(8, userG.getUserID());
            st.executeUpdate();
            setAllGenres(4, userG.getUserID(), user.getFavourites());
        } catch (SQLException ignored) {
        }
        String query = "Select * from user where userID=?";
        try {
            st = connection.prepareStatement(query);
            st.setInt(1, userG.getUserID());
            rs = st.executeQuery();
            if (rs.next()) {
                userG = new User(rs.getInt(1), rs.getString(3), rs.getString(4),
                        rs.getString(2), rs.getString(5),
                        rs.getString(7).charAt(0), rs.getString(6));
                userG.setFavourites(getAllGenres(4, userG.getUserID()));
            }
        } catch (SQLException ignored) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return userG;
    }

    @Override
    // Get all user drafts
    public List<Story> getAllDrafts() {
        Connection connection = DBManager.getConn();
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Story> drafts = new ArrayList<>();
        String query = "SELECT d.* from draft d where authorID=?";
        try {
            st = connection.prepareStatement(query);
            st.setInt(1, userG.getUserID());
            rs = st.executeQuery();
            while (rs.next()) {
                Story draft = new Story(rs.getInt(1), rs.getString(3),
                        rs.getString(4), rs.getString(2), false,
                        false, null, null,
                        rs.getString(5), getAllGenres(1, rs.getInt(1)));
                draft.setGenres(getAllGenres(2, draft.getStoryID()));
                drafts.add(draft);

            }
        } catch (SQLException ignored) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return drafts;
    }

    @Override
    // Overloaded to Get all tops of stories
    public List<Story> topStories(Integer amount, Integer type, Date start, Date end) {
        Connection connection = DBManager.getConn();
        List<Story> stories = new ArrayList<>();
        String search = "", count = "";
        String time = " between '" + start + "' and '" + end + "'";
        switch (type) {
            case 1:
                count = "count(d.liked)";
                search = "d.liked=true and d.timeLiked" + time;
                break;
            case 2:
                count = "avg(d.rating)";
                search = "d.timeRated" + time;
                break;
            case 3:
                count = "count(d.read)";
                search = "d.read" + time;
                break;
        }
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            String query = "SELECT distinct(s.storyID),s.story,s.description,s.title,s.hideStory,s.hideComment,u.username,s.approvedByID,s.picture," + count + " "
                    + "FROM story s,data d,user u "
                    + "where s.storyID=d.storyID and s.authorID=u.userID and " + search + " "
                    + "group by s.storyID order by " + count + " desc";
            st = connection.prepareStatement(query);
            rs = st.executeQuery();
            int counter = 0;
            while ((rs.next()) && (counter < amount)) {
                Story story = new Story(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getBoolean(5), rs.getBoolean(6), rs.getString(7), rs.getInt(8), rs.getString(9), getAllGenres(1, rs.getInt(1)));
                story.setData(getData(story.getStoryID()));
                story.setNumOfReads(getNumReads(story.getStoryID()));
                story.setRating(getRating(story.getStoryID()));
                stories.add(story);
                counter++;
            }
            return stories;
        } catch (SQLException e) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return stories;
    }

    @Override
    //Get average rating for a story
    public Double getRating(Integer storyID) {
        Connection connection = DBManager.getConn();
        PreparedStatement st = null;
        ResultSet rs = null;
        String query = "select avg(rating) from data where storyID = ?";
        Double rating;
        try {
            st = connection.prepareStatement(query);
            st.setInt(1, storyID);
            rs = st.executeQuery();
            if (rs.next()) {
                rating = rs.getDouble(1);
                return rating;
            }
        } catch (SQLException ignored) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return 0.0;
    }

    @Override
    // Get number of times a story has been read
    public Integer getNumReads(Integer storyID) {
        Connection connection = DBManager.getConn();
        PreparedStatement st = null;
        String query = "SELECT count(storyID) from data WHERE storyID = ? ";
        try {
            st = connection.prepareStatement(query);
            st.setInt(1, storyID);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException ignored) {
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return 0;
    }

    @Override
    // Get stories awaiting approval
    public List<Story> getWaitListStories() {
        Connection connection = DBManager.getConn();
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Story> waitList = new ArrayList<>();
        try {

            String sql = "SELECT w.*,u.username FROM wait w,user u where u.userID=w.authorID";
            st = connection.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                waitList.add(new Story(rs.getInt(1), rs.getString(3),
                        rs.getString(4), rs.getString(2), false,
                        false, rs.getString(7), null,
                        rs.getString(5), getAllGenres(3, rs.getInt(1))));
            }
        } catch (SQLException ignored) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return waitList;
    }

    @Override
    //Ability to delete a comment
    public String deleteComment(Integer commentID) {
        Connection connection = DBManager.getConn();
        String query = "Delete from comment where commentID=?";
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(query);
            st.setInt(1, commentID);
            int num = st.executeUpdate();
            if (num > 0) {
                return "Comment deleted";
            } else {
                return "No comment with that id";
            }
        } catch (SQLException e) {
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return "Comment not deleted";
    }

    @Override
    // Allow writers the ability to delete drafts
    public String deleteDraft(Integer draftID) {
        Connection connection = DBManager.getConn();
        PreparedStatement st = null;
        String query = "Delete from draft where draftID=?";
        try {
            st = connection.prepareStatement(query);
            st.setInt(1, draftID);
            int num = st.executeUpdate();
            if (num > 0) {
                return "Draft deletion successful";
            } else {
                return "No draft with that id to delete";
            }
        } catch (SQLException e) {
            return "Draft deletion unsuccessful";
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    //NEEDS TO BE REVIEWED
    @Override
    public List<Story> getFavourites() {
        Connection connection = DBManager.getConn();
        List<Story> stories = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            String query = "select s.*,u.username from story s, data d, user u where d.userID=u.userID and d.userID = ? and d.liked=true and d.storyID= s.storyID";

            st = connection.prepareStatement(query);
            st.setInt(1, userG.getUserID());
            rs = st.executeQuery();
            while (rs.next()) {
                Story story = new Story(rs.getInt(1), rs.getString(3),
                        rs.getString(4), rs.getString(2), rs.getBoolean(6),
                        rs.getBoolean(7), rs.getString(11),
                        rs.getInt(10), rs.getString(5), getAllGenres(1, rs.getInt(1)));
                story.setData(getData(story.getStoryID()));
                story.setNumOfReads(getNumReads(story.getStoryID()));
                story.setRating(getRating(story.getStoryID()));
                stories.add(story);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return stories;
    }

    @Override
    public void addWaitApproval(Integer id, String argument) {
        Connection connection = DBManager.getConn();
        String query = "Insert into waitApproval (userID,reason) values(?,?)";
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(query);
            st.setInt(1, id);
            st.setString(2, argument);
            st.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    @Override
    public List<Approval> getWaitListAppprovals() {
        List<Approval> waitList = new ArrayList<>();
        Connection connection = DBManager.getConn();
        ResultSet rs = null;
        PreparedStatement st = null;
        try {
            String query = "Select w.userID,u.username,w.reason from waitapproval w, user u where w.userID=u.userID";

            st = connection.prepareStatement(query);
            rs = st.executeQuery();

            while (rs.next()) {
                waitList.add(new Approval(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
            return waitList;
        } catch (SQLException ex) {
            Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return waitList;
    }

    @Override
    // get All genres for selecting genres
    public List<Genre> getGenres() {
        PreparedStatement st = null;
        ResultSet rs = null;
        Connection connection = DBManager.getConn();
        try {

            String query = "Select * from genre";
            List<Genre> genres = new ArrayList<>();
            st = connection.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                genres.add(new Genre(rs.getInt(1), rs.getString(2)));
            }
            return genres;
        } catch (SQLException ex) {
            Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    @Override
    // Add data to a story per user
    public Integer setStoryData(Integer storyID) {
        if (userG == null) {
            return -1;
        }
        Connection connection = DBManager.getConn();
        PreparedStatement st = null;
        String query = "Insert into data(userID,storyID) values (?,?)";
        try {
            st = connection.prepareStatement(query);
            st.setInt(1, userG.getUserID());
            st.setInt(2, storyID);
            Integer value = st.executeUpdate();
            return value;
        } catch (SQLException ex) {
            Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return 0;
    }

    //Need to test
    @Override
    // Overloaded to Get all tops of Users
    public List<User> topUsers(Integer amount, Integer type, Date start, Date end) {
        Connection connection = DBManager.getConn();
        List<User> users = new ArrayList<>();
        String query = "";
        String time = " between '" + start + "' and '" + end + "'";
        switch (type) {
            case 1://Most accepting editor
                query = "select s.approvedByID, u.username, count(s.approvedByID)  from user u, story s where u.userID=s.approvedByID and s.timeApproved" + time + " group by s.approvedByID order by count(s.approvedByID) desc";
                break;
            case 2:// Most view user based on book views
                query = "select u.userID,u.username, count(d.read) from user u, data d where u.userID=d.userID and d.read" + time + " group by u.userID order by count(d.read) desc";
                break;
        }
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement(query);
            rs = st.executeQuery();
            int counter = 0;
            while ((rs.next()) && (counter < amount)) {
                User user = new User();
                user.setUserID(rs.getInt(1));
                user.setUsername(rs.getString(2));
                user.setName(String.valueOf(rs.getInt(3)));
                // user is userID , username , then in name is total count amount
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return users;
    }

    @Override
    // Get all tops of stories
    public List<Genre> topGenre(Integer amount, Date start, Date end) {
        Connection connection = DBManager.getConn();
        List<Genre> genres = new ArrayList<>();
        String time = " between '" + start + "' and '" + end + "'";
        // Get most read genre from books read in a certain period   
        String query = "select count(g.genreID), g.genre from genre g,data d, storygenre sg where d.storyID=sg.storyID and sg.genreID=g.genreID and d.read" + time + " group by g.genre order by count(g.genreID) desc";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement(query);
            rs = st.executeQuery();
            int counter = 0;
            while ((rs.next()) && (counter < amount)) {
                //Genre is        total count(genre), name of genre
                Genre genre = new Genre(rs.getInt(1), rs.getString(2));
                genres.add(genre);
            }
            return genres;
        } catch (SQLException e) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return genres;

    }

    @Override
    //Gets all stories for readers to explore new genres
    public List<Story> getUserStories() {
        Connection connection = DBManager.getConn();
        List<Story> storyList = new ArrayList<>();
        String query = "SELECT s.*, u.username from story s, user u where s.authorID=? and u.userID=s.authorID";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement(query);
            st.setInt(1, userG.getUserID());
            rs = st.executeQuery();
            while (rs.next()) {
                Story story = new Story(rs.getInt(1), null,
                        rs.getString(4), rs.getString(2), rs.getBoolean(6),
                        rs.getBoolean(7), rs.getString(11),
                        rs.getInt(10), rs.getString(5), getAllGenres(1, rs.getInt(1)));
                story.setNumOfReads(getNumReads(story.getStoryID()));
                story.setRating(getRating(story.getStoryID()));
                storyList.add(story);
            }
        } catch (SQLException ignored) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return storyList;
    }
    
    @Override
    public String getPhoneNumber(String username){
        String query="Select phoneNumber from user where userID=?";
        Connection connection = DBManager.getConn();
         PreparedStatement st = null;
        ResultSet rs = null;
        try {
            Integer id=getUserIDFromUsername(username);
            st=connection.prepareStatement(query);
            st.setInt(1, id);
            rs=st.executeQuery();
            if(rs.next()){
            return rs.getString(1);
            }
        } catch (SQLException ex) {}
        finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RipDaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }
}

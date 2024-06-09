package com.api.geoshot.apigeoshot.classes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Class that interfaces between Application's Users and MariaDB database. It uses Users Class.
 * @see User
 */
public class usersDAO {

//        public static void main(String[] args) {
//            usersDAO teste = new usersDAO();
//            teste.changePhotoFromUser("laplace","default-photo");
//        }
    private Connection dbconnection;

    /**
     * Constructor of userDAO. It creates a connection between ours Java Backend and MariaDB database.
     */
    public usersDAO() {
        ConnectionFactory factory = null;
        try {
            CredentialsReader cred = new CredentialsReader("/home/laplace/Projects/Geoshot/geoshot-web/misc/Credentials");
            factory = new ConnectionFactory(cred.getServer(),cred.getPort(),cred.getDB(),cred.getUser(), cred.getPlainPassword());
            this.dbconnection = factory.Connect();
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        } catch(ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     *
     * @param user Method that inserts a user into the database. SignupServlet uses this method.
     * @return An integer value that represents the insert query status.
     * @see User
     * @see com.geoshot.geoshotweb.SignupServlet
     */
    public int insertUser(User user) {
        try {
            String queryString = "INSERT INTO users (name, username, email, password, photo)";
            queryString += " VALUES (?,?,?,?,?)";

            PreparedStatement stmt = this.dbconnection.prepareStatement(queryString);

            stmt.setString(1,user.getName());
            stmt.setString(2,user.getUsername());
            stmt.setString(3,user.getEmail());
            stmt.setString(4,user.getPassword());
            stmt.setString(5,user.getPhoto());

            return stmt.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
            return -1;
        }
    }

    /**
     * Second Version of insertUser which does not need a User Object to be executed. It uses its parameters to create a
     * new User Object, without userId, attempts and accuracy, and inserts this User using the first version.
     * @param name User name.
     * @param username User username.
     * @param email User email.
     * @param password User SHA256 password hash.
     * @param photo User Base64 encoded photo
     * @return An integer value that represents the insert query status.
     */
    public int insertUser(String name, String username, String email, String password, String photo) {
        return this.insertUser(new User(0,0, name, username, email, password, photo,0.));
    }

    /**
     * Method that checks if a Column (fieldToCheck) has a Value (checker)
     * @param fieldToCheck String that represents a column in the database.
     * @param checker String that represents a value to check.
     * @return A boolean value that indicates if there is a value (checker) in column (fieldToCheck).
     */
    public boolean check(String fieldToCheck, String checker) {
        try {
            String queryString = String.format("SELECT users.%s FROM users WHERE users.%s = \"%s\"",fieldToCheck,fieldToCheck,checker);

            PreparedStatement stmt = this.dbconnection.prepareStatement(queryString);

            ResultSet result = stmt.executeQuery();

            result.next();

            if(checker.equalsIgnoreCase(result.getString(fieldToCheck))) {
                return true;
            } else {
                return false;
            }

        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    /**
     * Method that checks if user exists in the database, returns the String username if this user exists and returns 'null'
     * if not.
     * @param username User username
     * @param hashPassword User SHA256 password hash
     * @return String username if this user exists and 'null' if not. It also returns 'null' if a SQLException occurs.
     */
    public String checkUser(String username, String hashPassword) {
        try {
            String queryString = String.format("SELECT users.username, users.password FROM users WHERE users.username = \"%s\" AND users.password = \"%s\"", username, hashPassword);

            PreparedStatement stmt = this.dbconnection.prepareStatement(queryString);

            ResultSet userpass = stmt.executeQuery();

            userpass.next();

            if(username.equalsIgnoreCase(userpass.getString("username"))) {
                return userpass.getString("username");
            } else {
                return null;
            }

        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }

    }

    /**
     * Method that gets user from the DB using its username. If the user does not exist, this method returns 'null'.
     * @param username User username.
     * @return User that has this username. Returns 'null' if there is no User with this username or a SQLException occurs.
     */
    public User getUser(String username) {
        if(!this.check("username",username)) {
            return null;
        } else {

            User thisUser = null;

            try {

                String queryString = String.format("SELECT * FROM users WHERE users.username = \"%s\"",username);

                PreparedStatement stmt = dbconnection.prepareStatement(queryString);

                ResultSet result = stmt.executeQuery();

                result.next();

                thisUser = new User(
                        result.getInt("usr_id"),
                        result.getInt("attempts"),
                        result.getString("name"),
                        result.getString("username"),
                        result.getString("email"),
                        result.getString("password"),
                        result.getString("photo"),
                        result.getDouble("accuracy")
                );


            } catch(SQLException ex) {
                System.out.println(ex.getMessage());
            }

            return thisUser;

        }
    }

    /**
     * Method that updates the SHA256 password hash of a user.
     * @param username User password hash that will be changed has this username
     * @param newPassword the new plain password.
     * @see HashGeneretor
     */
    public void changePasswordFromUser(String username, String newPassword) {

        String newHashPassword = HashGeneretor.getHash("SHA256",newPassword);

        try {

            String queryString = String.format("UPDATE users SET users.password=\"%s\" " +
                    "WHERE users.username=\"%s\"", newHashPassword, username);

            PreparedStatement stmt = this.dbconnection.prepareStatement(queryString);

            stmt.executeQuery();

        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Method that updates the Base64 encoded photo of a user.
     * @param username User photo that will be changed has this username
     * @param encodedPhoto the new Base64 encoded photo.
     */
    public void changePhotoFromUser(String username, String encodedPhoto) {

        try {

            String queryString = String.format("UPDATE users SET users.photo=\"%s\" " +
                    "WHERE users.username=\"%s\"", encodedPhoto, username);

            PreparedStatement stmt = this.dbconnection.prepareStatement(queryString);

            stmt.executeQuery();

        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    /**
     * Method that verify if a user follows another user.
     * @param usernameFollower Username of the user that follows (or not).
     * @param usernameFollowed Username of the user that is followed (or is not).
     * @return Returns boolean 'true' if usernameFollower follows usernameFollowed, else returns boolean 'false'.
     */
    public boolean follows(String usernameFollower, String usernameFollowed) {
        try {
            String queryString = String.format("SELECT * FROM friends f WHERE " +
                    "f.usr_id1 = (SELECT usr_id FROM users u WHERE u.username = \"%s\") AND " +
                    "f.usr_id2 = (SELECT usr_id FROM users u WHERE u.username = \"%s\")", usernameFollower, usernameFollowed);

            PreparedStatement stmt = this.dbconnection.prepareStatement(queryString);

            ResultSet result = stmt.executeQuery();

            if(result.next()) return true;
            else return false;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    /**
     * Method that makes a user follow another user.
     * @param usernameFollower username of the user that will follow.
     * @param usernameFollowed username of the user that will be followed.
     */
    public void insertFollow(String usernameFollower, String usernameFollowed) {
        try {
            String queryString = String.format("INSERT INTO friends (usr_id1, usr_id2) VALUES ((SELECT usr_id FROM users " +
                    "WHERE users.username=\"%s\"),(SELECT usr_id FROM users WHERE users.username=\"%s\"));", usernameFollower, usernameFollowed);

            PreparedStatement stmt = this.dbconnection.prepareStatement(queryString);

            stmt.executeQuery();

        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Method that undo what insertFollow do. It unmakes a user follow another.
     * @param usernameFollower username of the user that will not follow (anymore).
     * @param usernameFollowed username of the user that will not be followed (anymore).
     */
    public  void removeFollow(String usernameFollower, String usernameFollowed) {
        try {
            String queryString = String.format("DELETE FROM friends WHERE " +
                    "friends.usr_id1=(SELECT usr_id FROM users WHERE users.username=\"%s\") AND " +
                    "friends.usr_id2=(SELECT usr_id FROM users WHERE users.username=\"%s\")", usernameFollower, usernameFollowed);

            PreparedStatement stmt = this.dbconnection.prepareStatement(queryString);

            stmt.executeQuery();

        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Destructor. Important because it finalizes the connection with the database.
     * @throws SQLException
     * @throws Throwable
     */
    @Override
    @SuppressWarnings("FinalizeDeclaration")
    protected void finalize() throws SQLException, Throwable {
        try {
            this.dbconnection.close();
        } finally {
            super.finalize();
        }
    }
}

package com.api.geoshot.apigeoshot.classes;

/**
 * Class that represents the User entity.
 */
public class User {
    private int userId, attempts;
    private String username, name, email, password, photo;
    private double accuracy;


    /**
     * Construtor of User
     * @param userId Integer that represents usr_id in MariaDB Database.
     * @param attempts Number of attempts of a user.
     * @param name User name.
     * @param username User username.
     * @param email User e-mail.
     * @param password SHA256 password hash.
     * @param photo Base64 encoded photo String.
     * @param accuracy Double that represents the user accuracy throughout the GeoShot Game.
     */
    public User(int userId, int attempts, String name, String username, String email, String password, String photo, double accuracy) {
        this.userId     =   userId;
        this.username   = username;
        this.attempts   = attempts;
        this.name       =     name;
        this.email      =    email;
        this.password   = password;
        this.photo      =    photo;
        this.accuracy   = accuracy;
    }

    /**
     *
     * @return usr_id from MariaDB database.
     */
    public int getUserId() {
        return this.userId;
    }

    /**
     *
     * @return Number of attempts of a user.
     */
    public int getAttempts() {
        return this.attempts;
    }

    /**
     *
     * @return A user name.
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @return A user email.
     */
    public String getEmail() {
        return this.email;
    }


    /**
     *
     * @return SHA256 password hash of a user.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     *
     * @return Base64 encoded photo String.
     */
    public String getPhoto() {
        return this.photo;
    }

    /**
     *
     * @return A user username.
     */
    public String getUsername() { return this.username; }

    /**
     *
     * @return A user accuracy.
     */
    public double getAccuracy() {
        return this.accuracy;
    }
}

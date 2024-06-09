package com.api.geoshot.apigeoshot.classes;

/**
 * Class that represents each Feed element (Challenge) that appears in a user Feed. It gets information from Users and Publications
 * to generate a FeedList. There is a method in publicationsDAO that generates this feedList.
 * @see publicationsDAO
 */
public class Feed {
    private int pubId;
    private String photo, userPhoto, dateOfCreation, username;

    /**
     * Feed Constructor.
     * @param pubId Integer thar represents the pub_id in the MariaDB database.
     * @param username Username of the User that publicated.
     * @param photo Photo which was publicated.
     * @param dateOfCreation OBS.: This is a String. String that represents the date of creation of the challenge.
     * @param userPhoto Photo of the user who publicated.
     */
    public Feed(int pubId, String username, String photo, String dateOfCreation, String userPhoto) {
        this.pubId          = pubId;
        this.userPhoto      = userPhoto;
        this.photo          = photo;
        this.dateOfCreation = dateOfCreation;
        this.username       = username;
    }

    /**
     *
     * @return This publication id.
     */
    public int getPubId() { return this.pubId; }

    /**
     *
     * @return Base64 encoded String of user photo.
     */
    public String getUserPhoto() { return this.userPhoto; }

    /**
     *
     * @return Base64 encoded String of the challenge photo.
     */
    public String getPhoto() { return this.photo; }

    /**
     *
     * @return String representation of the date of creation.
     */
    public String getDateOfCreation() { return this.dateOfCreation; }

    /**
     *
     * @return Username of the user ho created the challenge.
     */
    public  String getUsername() { return this.username; }

}

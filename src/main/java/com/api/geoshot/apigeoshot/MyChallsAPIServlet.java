package com.api.geoshot.apigeoshot;

import com.api.geoshot.apigeoshot.classes.Publication;
import com.api.geoshot.apigeoshot.classes.publicationsDAO;
import com.api.geoshot.apigeoshot.classes.usersDAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name="my-challs",value="/api/my-challs")
public class MyChallsAPIServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = request.getParameter("username");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse = new JsonObject();

        if(username == null) {

            jsonResponse.addProperty("status","error");

        } else {

            jsonResponse.addProperty("status","success");

            jsonResponse.addProperty("username",username);

            publicationsDAO PublicationManager = new publicationsDAO();

            List<Publication> publicationlist = PublicationManager.getMyPublications(username);

            usersDAO UserManager = new usersDAO();

            Gson gson = new Gson();

            jsonResponse.addProperty("user", gson.toJson(UserManager.getUser(username)));
            jsonResponse.add("publicationlist",gson.toJsonTree(publicationlist));

        }


        response.getWriter().write(jsonResponse.toString());

    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = request.getParameter("username");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse = new JsonObject();

        if(username == null) {

            jsonResponse.addProperty("status","error");

        } else {

            jsonResponse.addProperty("status","success");

            int pubId = Integer.parseInt((String) request.getParameter("pub-id"));

            publicationsDAO PublicationManager = new publicationsDAO();

            PublicationManager.removePublicationById(pubId);

        }

        response.getWriter().write(jsonResponse.toString());
    }

}

package com.api.geoshot.apigeoshot;

import com.api.geoshot.apigeoshot.classes.User;
import com.api.geoshot.apigeoshot.classes.usersDAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "search", value = "/api/search")
public class SearchAPIServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = request.getParameter("username");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse = new JsonObject();

        if(username == null) {
            jsonResponse.addProperty("status","error");
        } else {

            jsonResponse.addProperty("status","success");

            String searchedUsername = request.getParameter("searched-username");
            usersDAO UserManager = new usersDAO();
            User searchedUser = UserManager.getUser(searchedUsername);
            jsonResponse.addProperty("username",username);

            if(searchedUser == null) {
                // não tem esse usuário!
                jsonResponse.addProperty("user-not-found","true");

            } else {

                jsonResponse.addProperty("user-not-found","false");
                boolean userFollows = UserManager.follows(username,searchedUsername);
                jsonResponse.addProperty("followship-state",userFollows?"true":"false");
                Gson gson = new Gson();
                jsonResponse.addProperty("user", gson.toJson(searchedUser));


            }

            // Atentar para o nome do username, Se varar ele, ainda vai ser sucess

            response.getWriter().write(jsonResponse.toString());

        }

    }

}

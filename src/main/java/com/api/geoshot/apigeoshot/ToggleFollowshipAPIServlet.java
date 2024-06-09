package com.api.geoshot.apigeoshot;

import com.api.geoshot.apigeoshot.classes.RequestBodyGetter;
import com.api.geoshot.apigeoshot.classes.User;
import com.api.geoshot.apigeoshot.classes.usersDAO;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name="toggle-followship",value="/api/toggle-followship")
public class ToggleFollowshipAPIServlet extends HttpServlet {

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JsonObject body = RequestBodyGetter.getJsonBody(request);

        String username = body.get("username").getAsString();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse = new JsonObject();

        if(username == null) {

            jsonResponse.addProperty("status", "error");

        } else {

            String searchedUsername = body.get("searched-username").getAsString();

            usersDAO UserManager = new usersDAO();

            User searchedUser = UserManager.getUser(searchedUsername);

            if(searchedUser == null) {
                // Usuário não existe!!
                jsonResponse.addProperty("status", "error");
            } else {

                jsonResponse.addProperty("status", "success");

                if(UserManager.follows(username,searchedUsername)) {
                    // Usuário segue o pesquisado;
                    UserManager.removeFollow(username,searchedUsername);
                } else {
                    // Usuário não segue o pesquisado;
                    UserManager.insertFollow(username,searchedUsername);
                }

            }

        }

        response.getWriter().write(jsonResponse.toString());

    }

}

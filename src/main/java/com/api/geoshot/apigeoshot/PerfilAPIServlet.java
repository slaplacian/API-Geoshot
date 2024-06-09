package com.api.geoshot.apigeoshot;

import com.api.geoshot.apigeoshot.classes.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.api.geoshot.apigeoshot.classes.usersDAO;
import java.io.IOException;

@WebServlet(name="perfil", value="/api/perfil")
public class PerfilAPIServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse = new JsonObject();

        if(username == null) {

            jsonResponse.addProperty("status","error");

        } else {

            usersDAO UserManager = new usersDAO();
            User thisUser = UserManager.getUser(username);
            Gson gson = new Gson();
            String stringThisUser = gson.toJson(thisUser);
            jsonResponse.addProperty("status","success");
            jsonResponse.addProperty("user", stringThisUser);
            jsonResponse.addProperty("username",username);

        }

        response.getWriter().write(jsonResponse.toString());

    }

}

package com.api.geoshot.apigeoshot;

import com.api.geoshot.apigeoshot.classes.HashGeneretor;
import com.api.geoshot.apigeoshot.classes.RequestBodyGetter;
import com.api.geoshot.apigeoshot.classes.usersDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;

import java.io.IOException;

@WebServlet(name="login", value="/api/login")
public class LoginAPIServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        JsonObject body = RequestBodyGetter.getJsonBody(request);

        String username = body.get("username").getAsString();
        String password = body.get("password").getAsString();

        String hashPassword = HashGeneretor.getHash("SHA256",password);

        usersDAO verifyUserDAO = new usersDAO();
        String realUsername = verifyUserDAO.checkUser(username,hashPassword);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse = new JsonObject();

        if(realUsername!=null) {
            jsonResponse.addProperty("status", "success");
            jsonResponse.addProperty("username", realUsername);
            System.out.println("Usuario logou");
        } else {
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "invalid username or password");
            System.out.println("Usuario nao logou");
        }

        response.getWriter().write(jsonResponse.toString());
    }

    public void destroy() {}
}

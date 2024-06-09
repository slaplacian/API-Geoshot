package com.api.geoshot.apigeoshot;

import com.api.geoshot.apigeoshot.classes.HashGeneretor;
import com.api.geoshot.apigeoshot.classes.RequestBodyGetter;
import com.api.geoshot.apigeoshot.classes.usersDAO;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name="signup", value="/api/signup")
public class SignupAPIServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        JsonObject body = RequestBodyGetter.getJsonBody(request);

        String confirmPassword  = body.get("confirm-password").getAsString();
        String password         = body.get("password").getAsString();
        String username         = body.get("username").getAsString();
        String email            = body.get("email").getAsString();
        String name             = body.get("name").getAsString();

        boolean mustRepeat = false;

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse = new JsonObject();

        if(password.isEmpty()) {
            jsonResponse.addProperty("no-password","true");
            mustRepeat = true;
        } else jsonResponse.addProperty("no-password","false");

        if(!confirmPassword.equals(password)) {
            jsonResponse.addProperty("non-equals-password","true");
            mustRepeat = true;
        } else jsonResponse.addProperty("non-equals-password","false");

        usersDAO userManager = new usersDAO();

        if(userManager.check("email",email)) {
            jsonResponse.addProperty("already-email","true");
            mustRepeat = true;
        } else jsonResponse.addProperty("already-email","false");

        if(userManager.check("username", username)) {
            jsonResponse.addProperty("already-username","true");
            mustRepeat = true;
        } else jsonResponse.addProperty("already-username","false");

        if(mustRepeat) {
            jsonResponse.addProperty("status", "error");
        } else {
            userManager.insertUser(name, username, email, HashGeneretor.getHash("SHA256",password), "default-photo");
            jsonResponse.addProperty("username", username);
            jsonResponse.addProperty("status", "success");
        }

        response.getWriter().write(jsonResponse.toString());
    }

    public void destroy() {}
}

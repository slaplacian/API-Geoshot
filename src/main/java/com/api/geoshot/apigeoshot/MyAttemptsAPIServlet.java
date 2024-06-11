package com.api.geoshot.apigeoshot;

import com.api.geoshot.apigeoshot.classes.MyAttempt;
import com.api.geoshot.apigeoshot.classes.publicationsDAO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name="my-attempts",value="/api/my-attempts")
public class MyAttemptsAPIServlet  extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String username = request.getParameter("username");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse = new JsonObject();

        if(username == null) {
            jsonResponse.addProperty("status","error");
        } else {

            jsonResponse.addProperty("status","success");

            Gson gson = new Gson();

            publicationsDAO PublicationsManager = new publicationsDAO();
            List<MyAttempt> attemptslist = PublicationsManager.getMyAttempts(username);

            JsonElement Attemptslist = gson.toJsonTree(attemptslist);

            jsonResponse.addProperty("username",username);
            jsonResponse.add("attemptslist", Attemptslist);

        }

        // Atenção ao bug de não ter username.......

        response.getWriter().write(jsonResponse.toString());

    }
}

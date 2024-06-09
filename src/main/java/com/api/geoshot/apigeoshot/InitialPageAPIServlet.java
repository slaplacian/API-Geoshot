package com.api.geoshot.apigeoshot;

import java.io.*;
import com.api.geoshot.apigeoshot.classes.publicationsDAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "intial-page", value = "/api/initial-page")
public class InitialPageAPIServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse = new JsonObject();

        if(username == null) {
            jsonResponse.addProperty("status","error");
        } else {
            jsonResponse.addProperty("status","success");
            publicationsDAO feedGenerator = new publicationsDAO();

            Gson gson = new Gson();
            String stringFeedList = gson.toJson(feedGenerator.getFeedFromUser(username));

            jsonResponse.addProperty("feedlist",stringFeedList);
            jsonResponse.addProperty("username",username);
        }

        response.getWriter().write(jsonResponse.toString());
    }

    public void destroy() {}

}
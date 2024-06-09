package com.api.geoshot.apigeoshot;

import com.api.geoshot.apigeoshot.classes.*;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="chall",value = "/api/chall")
public class ChallAPIServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JsonObject body = RequestBodyGetter.getJsonBody(request);

        String username = body.get("username").getAsString();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse = new JsonObject();

        if (username == null) {
            jsonResponse.addProperty("status","error");
        } else {
            jsonResponse.addProperty("status","success");
            jsonResponse.addProperty("username",username);
            int pubId = Integer.parseInt((String) body.get("pub-id").getAsString());
            String userAnswerString = ((String) body.get("user-answer").getAsString());
            publicationsDAO PublicationManager = new publicationsDAO();
            Publication thisPublication = PublicationManager.getPublicationById(pubId);
            String correctValue = thisPublication.getCorrectValue();
            double accuracy = CalculateAccuracy.getAccuracy(correctValue, userAnswerString);
            attemptsDAO AttemptManager = new attemptsDAO();
            AttemptManager.insertAttempt(pubId, username, accuracy);
        }

        //Lembrar do erro do Usuário e do Pub-id.

        response.getWriter().write(jsonResponse.toString());
    }

}
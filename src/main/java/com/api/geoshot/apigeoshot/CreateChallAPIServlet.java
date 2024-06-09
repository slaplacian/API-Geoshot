package com.api.geoshot.apigeoshot;

// import com.api.geoshot.apigeoshot.classes.PhotoConverter;
import com.api.geoshot.apigeoshot.classes.RequestBodyGetter;
import com.api.geoshot.apigeoshot.classes.publicationsDAO;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
// import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
// import java.io.InputStream;

@WebServlet(name="create-chall", value = "/api/create-chall")
// @MultipartConfig
public class CreateChallAPIServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws  IOException {

        JsonObject body = RequestBodyGetter.getJsonBody(request);

        String username = body.get("username").getAsString();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse = new JsonObject();


        if(username == null) {

            jsonResponse.addProperty("status","error");

        } else {

            String encodedPhoto = body.get("photo").getAsString();

            String correctValue = body.get("answer").getAsString();

            if(encodedPhoto.isEmpty() || correctValue.isEmpty()) {

                jsonResponse.addProperty("status","error");
                jsonResponse.addProperty("missing","true");

            } else {

                jsonResponse.addProperty("status","success");
                jsonResponse.addProperty("username",username);

                jsonResponse.addProperty("missing","false");

                publicationsDAO PublicationManager = new publicationsDAO();

                PublicationManager.insertPublication(encodedPhoto,username,correctValue);
            }
        }

        response.getWriter().write(jsonResponse.toString());

    }
}


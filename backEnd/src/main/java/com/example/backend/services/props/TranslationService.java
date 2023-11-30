package com.example.backend.services.props;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TranslationService {

    @Value("${application.translation.token}")
    private String IAM_TOKEN;
    @Value("${application.translation.folder}")
    private String FOLDER_ID;

    public List<String> translateTexts(List<String> texts, String targetLanguage) {
        OkHttpClient client = new OkHttpClient();

        Gson gson = new Gson();
        List<String> translatedTexts = new ArrayList<>();

        JsonObject body = new JsonObject();
        body.addProperty("targetLanguageCode", targetLanguage);
        body.add("texts", gson.toJsonTree(texts));
        body.addProperty("folderId", FOLDER_ID);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), body.toString());

        Request request = new Request.Builder()
                .url("https://translate.api.cloud.yandex.net/translate/v2/translate")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + IAM_TOKEN)
                .build();

        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

                JsonArray translations = jsonResponse.getAsJsonArray("translations");
                for (int i = 0; i < translations.size(); i++) {
                    String translatedText = translations.get(i).getAsJsonObject().getAsJsonPrimitive("text").getAsString();
                    translatedTexts.add(translatedText);
                }
            } else {
                System.out.println("Error: " + response.code() + " - " + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return translatedTexts;
    }

}

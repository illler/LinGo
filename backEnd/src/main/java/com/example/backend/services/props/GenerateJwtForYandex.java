package com.example.backend.services.props;

import com.example.backend.model.KeyInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import okhttp3.*;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GenerateJwtForYandex {

    public void getNewJwt() throws NoSuchAlgorithmException, InvalidKeySpecException {
        ObjectMapper objectMapper = new ObjectMapper();
        KeyInfo keyInfo = null;
        try {
             keyInfo = objectMapper.readValue(new File(
                    "backEnd/src/main/java/com/example/backend/services/settings/authKey.json"),
                    KeyInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert keyInfo != null;
        String privateKeyContent = keyInfo.getPrivate_key();
        PemObject pemObject = convertStringToPemObject(privateKeyContent);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(pemObject.getContent()));

        String serviceAccountId = keyInfo.getService_account_id();
        String keyId = keyInfo.getId();

        Instant now = Instant.now();

        getNewIAMToken(Jwts.builder()
                .setHeaderParam("kid", keyId)
                .setIssuer(serviceAccountId)
                .setAudience("https://iam.api.cloud.yandex.net/iam/v1/tokens")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(360)))
                .signWith(privateKey, SignatureAlgorithm.PS256)
                .compact());
    }

    public void getNewIAMToken(String token) {
        OkHttpClient client = new OkHttpClient();

        Gson gson = new Gson();

        JsonObject body = new JsonObject();
        body.addProperty("jwt", token);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(body));

        Request request = new Request.Builder()
                .url("https://iam.api.cloud.yandex.net/iam/v1/tokens")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JsonObject jsonResponse = gson.fromJson(response.body().string(), JsonObject.class);

                setToPropFile(jsonResponse.get("iamToken").getAsString());
            } else {
                System.out.println("Request failed with code: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setToPropFile(String iamToken) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("backEnd/src/main/java/com/example/backend/services/settings/iAmToken"))) {
            bw.write(iamToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static PemObject convertStringToPemObject(String pemString) {
        try (StringReader stringReader = new StringReader(pemString);
             PemReader pemReader = new PemReader(stringReader)) {
            return pemReader.readPemObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

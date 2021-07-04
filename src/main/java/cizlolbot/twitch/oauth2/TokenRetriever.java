package cizlolbot.twitch.oauth2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class TokenRetriever {
    private static final String clientId = "a9aakq6k3jo34vexd5zfndghpant72";

    private static final String clientSecret = System.getenv("CLIENT_SECRET");
    private static final String refreshToken = System.getenv("REFRESH_TOKEN");

    private OauthResponse cachedOauthResponse;
    private static TokenRetriever instance;

    // singleton
    private TokenRetriever() {

    }

    public static TokenRetriever getInstance() {
        if (instance == null) {
            instance = new TokenRetriever();
        }
        return instance;
    }


    public String getToken() {
        if (cachedOauthResponse != null) {
            long now = System.currentTimeMillis();
            if (now < cachedOauthResponse.getExpiresAt()) {
                return cachedOauthResponse.getAccessToken();
            }
            return getNewToken();
        }

        return getNewToken();
    }

    private String getNewToken() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(getAuthorizeUrl());
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                String responseString = EntityUtils.toString(response.getEntity());
                OauthResponse oauthResponse = unmarshallOauthResponse(responseString);

                long expiresAt = System.currentTimeMillis() + oauthResponse.getExpiresIn()*1000;
                oauthResponse.setExpiresAt(expiresAt);

                cachedOauthResponse = oauthResponse;
                return oauthResponse.getAccessToken();
            }

        } catch (IOException e) {
            System.out.println("Failed to create http client");
            throw new RuntimeException(e);
        }
    }

    private OauthResponse unmarshallOauthResponse(String response) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(response, OauthResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private URI getAuthorizeUrl() {
        try {
            URIBuilder uriBuilder = new URIBuilder("https://id.twitch.tv");
            uriBuilder.setPath("/oauth2/token");
            uriBuilder.setParameter("client_id", clientId);
            uriBuilder.setParameter("client_secret", clientSecret);
            uriBuilder.setParameter("grant_type", "refresh_token");
            uriBuilder.setParameter("scope", "chat:read chat:edit channel:moderate whispers:read whispers:edit channel_editor");
            uriBuilder.setParameter("refresh_token", refreshToken);
            return uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}

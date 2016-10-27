package com.manywho.services.huddle.api;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.manywho.services.huddle.api.entities.Document;
import com.manywho.services.huddle.api.entities.Folder;
import com.manywho.services.huddle.api.entities.User;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static com.manywho.sdk.services.providers.ObjectMapperProvider.getObjectMapper;

public class HuddleClient {
    private final String accessToken;
    private final HttpClient httpClient;

    public HuddleClient(String accessToken) {
        this.accessToken = accessToken;
        this.httpClient = HttpClients.createDefault();
    }

    public User getCurrentUser() {
        HttpGet request = new HttpGet("https://api.huddle.net/entry");
        request.addHeader("Accept", "application/vnd.huddle.data+json");
        request.addHeader("Authorization", "OAuth2 " + accessToken);

        try {
            return executeWithResponse(request, User.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to get the current user from Huddle", e);
        }
    }

    public Document createDocument(String folder, String contentType, long contentLength, String title, String description, String extension) {
        JSONObject body = new JSONObject();
        body.put("title", title);
        body.put("description", description);
        body.put("extension", extension);

        HttpEntity entity = new StringEntity(body.toString(), Charsets.UTF_8);

        HttpPost request = new HttpPost("https://api.huddle.net/files/folders/" + folder + "/documents");
        request.addHeader("Accept", "application/vnd.huddle.data+json");
        request.addHeader("Authorization", "OAuth2 " + accessToken);
        request.addHeader("Content-Type", "application/vnd.huddle.data+json");
        request.addHeader("X-Upload-Content-Type", contentType);
        request.addHeader("X-Upload-Content-Length", String.valueOf(contentLength));
        request.setEntity(entity);

        try {
            return executeWithResponse(request, Document.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create a new document on Huddle", e);
        }
    }

    public Folder createFolder(String parent, String title, String description) {
        JSONObject body = new JSONObject();
        body.put("title", title);
        body.put("description", description);

        HttpEntity entity = new StringEntity(body.toString(), Charsets.UTF_8);

        HttpPost request = new HttpPost("https://api.huddle.net/files/folders/" + parent);
        request.addHeader("Authorization", "OAuth2 " + accessToken);
        request.addHeader("Content-Type", "application/vnd.huddle.data+json");
        request.setEntity(entity);

        try {
            return executeWithResponse(request, Folder.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create a new folder on Huddle", e);
        }
    }

    public Folder loadFolder(String workspace, String id) {
        HttpGet request;

        if (id.equals("0")) {
            request = new HttpGet("https://api.huddle.net/files/workspaces/" + workspace + "/folders/root");
        } else {
            request = new HttpGet("https://api.huddle.net/files/folders/" + id);
        }

        request.addHeader("Accept", "application/vnd.huddle.data+json");
        request.addHeader("Authorization", "OAuth2 " + accessToken);

        try {
            return executeWithResponse(request, Folder.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to get the folder from Huddle", e);
        }
    }

    public void moveDocument(String id, String folder) {
        HttpGet initialGet = new HttpGet("https://api.huddle.net/files/documents/" + id + "/edit");
        initialGet.addHeader("Accept", "application/vnd.huddle.data+json");
        initialGet.addHeader("Authorization", "OAuth2 " + accessToken);

        Map map;
        try {
            map = executeWithResponse(initialGet, Map.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load the document to edit from Huddle");
        }

        JSONObject link = new JSONObject();
        link.put("rel", "parent");
        link.put("href", "https://api.huddle.net/files/folders/" + folder);

        JSONArray links = new JSONArray(Lists.newArrayList(link));

        JSONObject body = new JSONObject();
        body.put("title", map.get("title"));
        body.put("description", map.get("description"));
        body.put("links", links);

        HttpEntity entity = new StringEntity(body.toString(), Charsets.UTF_8);

        HttpPut request = new HttpPut("https://api.huddle.net/files/documents/" + id + "/edit");
        request.addHeader("Accept", "application/vnd.huddle.data+json");
        request.addHeader("Authorization", "OAuth2 " + accessToken);
        request.addHeader("Content-Type", "application/vnd.huddle.data+json");
        request.setEntity(entity);

        try {
            httpClient.execute(request);
        } catch (IOException e) {
            throw new RuntimeException("Unable to move the document on Huddle", e);
        }
    }

    public void uploadFile(String url, InputStream content) {
        HttpEntity entity = MultipartEntityBuilder.create()
                .addBinaryBody("content", content)
                .build();

        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        httpPost.setHeader("Authorization", "OAuth2 " + accessToken);

        try {
            HttpResponse response = HttpClients.createDefault()
                    .execute(httpPost);

            if (!isSuccessStatusCode(response.getStatusLine().getStatusCode())) {
                throw new RuntimeException("Unable to upload the file to Huddle: " + response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to upload the file to Huddle", e);
        }
    }

    private <T> T executeWithResponse(HttpRequestBase request, Class<T> tClass) throws IOException {
        return httpClient.execute(request, response -> parseResponse(response, tClass));
    }

    private static <T> T parseResponse(HttpResponse response, Class<T> responseClass) throws IOException {
        if (!isSuccessStatusCode(response.getStatusLine().getStatusCode())) {
            throw new RuntimeException("The request to Huddle failed: " + response.getStatusLine().getReasonPhrase());
        }

        return getObjectMapper().readValue(response.getEntity().getContent(), responseClass);
    }

    private static boolean isSuccessStatusCode(int statusCode) {
        return statusCode >= 200 && statusCode <= 299;
    }
}

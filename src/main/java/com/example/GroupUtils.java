package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GroupUtils
{

    @Autowired
    private IdentityProviderProperties identityProviderProperties;

    public static ConcurrentHashMap<String,String> GROUPID_GROUPNAME_CACHE = new ConcurrentHashMap<>();

    public  String getGroupName(Object groupId){


        String groupName = null;

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id="+identityProviderProperties.getClientId()+"&client_secret="+identityProviderProperties.getClientSecret()+"&scope="+identityProviderProperties.getScope());
        Request request = new Request.Builder()
                .url("https://login.microsoftonline.com/"+identityProviderProperties.getTenantId()+"/oauth2/v2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String responseString = responseBody.string(); // Get response body as string
                // Process the response string

                ObjectMapper om = new ObjectMapper();
                TokenResponse tokenResponse = om.readValue(responseString, TokenResponse.class);

                responseBody.close(); // Close the response body

                if(tokenResponse!=null) {

                        OkHttpClient client2 = new OkHttpClient().newBuilder()
                                .build();
                        Request request2 = new Request.Builder()
                                .url("https://graph.microsoft.com/v1.0/groups/" + String.valueOf(groupId))
                                .get()
                                .addHeader("Authorization", "Bearer " + tokenResponse.getAccessToken())
                                .build();
                        Response response2 = client2.newCall(request2).execute();

                        ResponseBody responseBody2 = response2.body();

                        if (responseBody2 != null) {

                            om = new ObjectMapper();

                            GroupQueryResponse groupQueryResponse = om.readValue(responseBody2.string(), GroupQueryResponse.class);

                            groupName  = groupQueryResponse.getDisplayName();

                        responseBody2.close();
                    }
                }

            } else {
                // Handle null response body
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Add to cache to avoid further calls
        GROUPID_GROUPNAME_CACHE.put(String.valueOf(groupId), (groupName==null?String.valueOf(groupId):groupName));


        return  (groupName==null?String.valueOf(groupId):groupName);
    }

}

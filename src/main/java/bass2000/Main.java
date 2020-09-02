package bass2000;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.OAuthException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.users.Fields;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main {
    private final static String REDIRECT_URI = "";
    private final static Logger logger = LogManager.getLogger(Main.class);
    private static Integer APP_ID;
    private static String CLIENT_SECRET;
    private static String code;
    private static UserAuthResponse authResponse;

    public static void main(String[] args) throws Exception {

        Properties prop = new Properties();
        try {
            prop.load(Main.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException ex) {
            logger.error("Отсутствует файл properties");
            return;
        }

        APP_ID = Integer.valueOf((String) prop.get("APP_ID"));
        CLIENT_SECRET = (String) prop.get("CLIENT_SECRET");
        System.out.println(APP_ID);
        System.out.println(CLIENT_SECRET);

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        String url = "https://oauth.vk.com/authorize?client_id=" + APP_ID + "&display=page&redirect_uri=&scope=photos&response_type=code&v=5.122";
        System.out.println("А теперь надо сходить по ссылке " + url);
        System.out.println("Введите полученный code и нажмите ENTER:");
        code = reader.readLine();
        reader.close();


        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);

        //Auth
        try {
            authResponse = vk.oAuth()
                    .userAuthorizationCodeFlow(APP_ID, CLIENT_SECRET, REDIRECT_URI, code)
                    .execute();
        } catch (OAuthException e) {
            e.getRedirectUri();
        }
        UserActor actor = new UserActor(authResponse.getUserId(), authResponse.getAccessToken());

        //Get friends with fields(
        String execute = vk.friends().get(actor).count(9999).fields(Fields.BDATE, Fields.SCREEN_NAME, Fields.FRIEND_STATUS).executeAsString();

        FileHelper.printToFile("friends.json", execute);
        logger.debug("Json saved");

        //parsing response
        JsonElement friendsJson = new JsonParser().parse(execute).getAsJsonObject().get("response");
        int count = friendsJson.getAsJsonObject().get("count").getAsInt();
        logger.info("Friends total count:" + count);
        List<Friend> res = new ArrayList<>();
        JsonArray friendsArray = friendsJson.getAsJsonObject().get("items").getAsJsonArray();
        for (JsonElement friend : friendsArray) {
            Friend cusFriend = new Friend();
            JsonObject jsonObject = friend.getAsJsonObject();
            cusFriend.setFirstName(jsonObject.get("first_name").getAsString());
            cusFriend.setLastName(jsonObject.get("last_name").getAsString());
            cusFriend.setBday(jsonObject.get("bdate") != null ? jsonObject.get("bdate").getAsString() : "");
            cusFriend.setUrl("https://vk.com/" + (jsonObject.get("screen_name") != null ? jsonObject.get("screen_name").getAsString() : "deleted"));
            res.add(cusFriend);
            logger.debug(cusFriend);
        }

        FileHelper.printCSV(res);
    }

}




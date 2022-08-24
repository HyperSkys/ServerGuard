package dev.hyperskys.serverguard.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.model.Filters;
import dev.hyperskys.serverguard.utils.LanguageUtils;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.Objects;

public class MongoDB {

    private static final MongoClient mongoClient = MongoClients.create(LanguageUtils.mongoURI);

    public static boolean addWhitelist(Player player) {
        if (mongoClient.getDatabase("ServerGuard").getCollection("storage").find(Filters.eq("uuid", player.getUniqueId().toString())).first() == null) {
            Document document = new Document("uuid", player.getUniqueId().toString());
            document.put("username", player.getName());
            mongoClient.getDatabase("ServerGuard").getCollection("storage").insertOne(document);
            return true;
        }

        return false;
    }

    public static boolean addWhitelist(String uuid, String username) {
        if (mongoClient.getDatabase("ServerGuard").getCollection("storage").find(Filters.eq("uuid", uuid)).first() == null) {
            Document document = new Document("uuid", uuid);
            document.put("username", username);
            mongoClient.getDatabase("ServerGuard").getCollection("storage").insertOne(document);
            return true;
        }

        return false;
    }

    public static boolean removeWhitelist(Player player) {
        if (mongoClient.getDatabase("ServerGuard").getCollection("storage").find(Filters.eq("uuid", player.getUniqueId().toString())).first() != null) {
            mongoClient.getDatabase("ServerGuard").getCollection("storage").deleteOne(Filters.eq("uuid", player.getUniqueId()));
            return true;
        }

        return false;
    }

    public static boolean removeWhitelist(String uuid) {
        if (mongoClient.getDatabase("ServerGuard").getCollection("storage").find(Filters.eq("uuid", uuid)).first() != null) {
            mongoClient.getDatabase("ServerGuard").getCollection("storage").deleteOne(Filters.eq("uuid", uuid));
            return true;
        }

        return false;
    }

    public static boolean checkWhitelist(Player player) {
        return mongoClient.getDatabase("ServerGuard").getCollection("storage").find(Filters.eq("uuid", player.getUniqueId().toString())).first() == null;
    }

    public static boolean setPassword(String password) {
        Document document = new Document("plugin", "ServerGuard");
        document.put("password", password);

        if (mongoClient.getDatabase("ServerGuard").getCollection("privacy").find(Filters.eq("plugin", "ServerGuard")).first() == null) {
            mongoClient.getDatabase("ServerGuard").getCollection("privacy").insertOne(document);
        } else {
            if (!(Objects.equals(Objects.requireNonNull(mongoClient.getDatabase("ServerGuard").getCollection("privacy").find(Filters.eq("plugin", "ServerGuard")).first()).getString("password"), password))) {
                mongoClient.getDatabase("ServerGuard").getCollection("privacy").replaceOne(Filters.eq("plugin", "ServerGuard"), document);
            } else {
                return false;
            }
            return true;
        }

        return false;
    }

    public static boolean verifyPassword(String input) {
        if (mongoClient.getDatabase("ServerGuard").getCollection("privacy").find(Filters.eq("plugin", "ServerGuard")).first() != null) {
            Document document = mongoClient.getDatabase("ServerGuard").getCollection("privacy").find(Filters.eq("plugin", "ServerGuard")).first();
            return Objects.requireNonNull(document).getString("password").equals(input);
        }

        return true;
    }
}

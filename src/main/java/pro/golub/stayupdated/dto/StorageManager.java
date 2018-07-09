package pro.golub.stayupdated.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StorageManager {

    private static final Map<Integer, Set<String>> users = new HashMap<>();

    public static void addUser(int userId, Set<String> hashtags) {
        users.put(userId, hashtags);
    }

    public static void removeUser(int userId) {
        users.remove(userId);
    }

    public static boolean isUserExists(int userId) {
        return users.containsKey(userId);
    }
}

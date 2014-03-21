package util;

/**
 * Class of constants used in this project.
 */
public class Constants {

    /**
     * Session attribute key for the selected game group.
     */
    public final static String SESSION_KEY_GAME_GROUP = "gameGroup";

    /**
     * Session attribute key for signaling an authenticated user.
     */
    public static final String SESSION_KEY_SECURITY = "SECKEY";


    /**
     * Private constructor that only throws a {@link RuntimeException}
     * to prevent instances of this class to be created.
     */
    private Constants() {
        throw new RuntimeException("No constructor for you!");
    }

}

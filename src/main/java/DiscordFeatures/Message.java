package DiscordFeatures;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;

/**
 * A message is the data sent between users in chats. These messages each contain the
 * text content as well as the time and date it was sent. These messages are saved in chats
 * and are serializable and saved. These messages are sent from client to server and vise versa.
 */
public class Message implements Serializable {
    private final String content;
    private final LocalDateTime timeSent;
    private HashSet<String> laughs;
    private HashSet<String> likes;
    private HashSet<String> dislikes;

    /**
     * Creates message with String parameter and local time
     *
     * @param content String text of message
     */
    public Message(String content) {
        this.content = content;
        timeSent = LocalDateTime.now();
        laughs = new HashSet<>();
        likes = new HashSet<>();
        dislikes = new HashSet<>();
    }

    public String getDislikes() {
        String s = "";
        for (String username : dislikes) {
            s = s.concat(username + "\n");
        }
        return s;
    }

    public String getLikes() {
        String s = "";
        for (String username : likes) {
            s = s.concat(username + "\n");
        }
        return s;
    }

    public String getLaughs() {
        String s = "";
        for (String username : laughs) {
            s = s.concat(username + "\n");
        }
        return s;
    }

    public void addReaction(String reaction, String username) {
        int react = Integer.parseInt(reaction);
        if (react == 1)
            laughs.add(username);
        else if (react == 2)
            likes.add(username);
        else if (react == 3)
            dislikes.add(username);
    }

    /**
     * returns the content of the message
     *
     * @return String text content
     */
    public String getContent() {
        return content;
    }

    /**
     * returns the content we want to print everytime we print message
     *
     * @return String content of message
     */
    @Override
    public String toString() {
        return content;
    }
}

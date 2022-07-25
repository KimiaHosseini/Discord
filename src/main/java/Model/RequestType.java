package Model;

/**
 * This enum contains all the different requests that the client can send to the server
 * in order to receive data needed for the program to function.
 * <p>
 * The request type determines how the server will respond to the request
 */
public enum RequestType {

    // signing up
    CHECK_USERNAME,
    SIGN_UP,
    SIGN_IN,

    // user configurations
    PROFILE_PHOTO,
    SET_STATUS,
    CHANGE_PASSWORD,

    // friends and friend requests
    FRIEND_REQUEST,
    ACCEPT_FRIEND_REQUEST,
    DELETE_FRIEND_REQUEST,
    PRINT_FRIEND_REQUESTS,
    PRINT_FRIENDS,

    // blocking users
    PRINT_BLOCKED_FRIENDS,
    BLOCK_FRIEND,
    IS_BLOCKED,
    UNBLOCK_USER,

    // private chat and sending files
    PRINT_PRIVATE_CHATS_USERNAMES,
    PRIVATE_CHAT_MESSAGES,
    SEND_MESSAGE,
    CLOSE_THREAD,
    ADD_FILE_TO_CHAT,
    PRINT_FILE_NAMES,
    DOWNLOAD_FILE,
    SET_CURRENT_CHAT,
    CHECK_USERNAME_FOR_CHAT,

    // server
    NEW_SERVER,
    PRINT_SERVERS,
    PRINT_CHANNELS,
    INVITE_TO_SERVER,
    ENTER_CHANNEL,
    SEND_MESSAGE_TO_CHANNEL,
    ADD_FILE_TO_CHANNEL,
    DOWNLOAD_FILE_IN_CHANNEL,

    //server accessibility and role configurations
    NEW_CHANNEL,
    DELETE_CHANNEL,
    CHANGE_SERVER_NAME,
    DELETE_SERVER,
    GET_SERVER,
    GET_SERVER_MEMBERS,
    GET_MEMBER_ROLES,
    VIEW_PINNED,
    PIN_MESSAGE,
    LEAVE_SERVER,
    VIEW_MEMBERS,

    //info
    GET_EMAIL,
    GET_PHONE,
    GET_PFP,
    GET_STATUS,

    CHANGE_EMAIL,
    CHANGE_PHONE,
    CHANGE_USER,

    REACT_MESSAGE,
    GET_LAUGHS,
    GET_LIKES,
    GET_DISLIKES,
    GET_SENT_REQUESTS,
    REMOVE_SENT_REQUEST,

    REACT_MESSAGE_SERVER,
    GET_LAUGHS_SERVER,
    GET_LIKES_SERVER,
    GET_DISLIKE_SERVER,
    CAN_DELETE_CHANNEL,
}

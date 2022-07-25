package Client;

import DiscordFeatures.*;
import Model.ResponseStatus;
import Model.Request;
import Model.RequestType;
import Model.Response;
import UserFeatures.Status;
import UserFeatures.User;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

/**
 * The class Client is in direct communication with the console. It invokes
 * methods that send requests to the server and receives responses to give
 * back output to those requests.
 */
public class Client {
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private User user;

    /**
     * Creates new Client and connection with the server socket
     *
     * @param socket give socket
     */
    public Client(Socket socket) {
        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * gets response from input Stream from server
     *
     * @return Response from server
     */
    synchronized public Response getResponse() {
        Response response = null;
        try {
            response = (Response) ois.readUnshared();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Sends request to output Stream
     *
     * @param request request to be sent out to server
     */
    private void sendRequest(Request<?> request) {
        try {
            oos.writeUnshared(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String printServers() {
        sendRequest(new Request<>(RequestType.PRINT_SERVERS));
        return (String) getResponse().getData();
    }

    /**
     * removes the client from the server and they no longer have access to the server
     * prompted to ensure they want to leave before exiting
     *
     * @param requestedServer given server
     */
    private void leaveServer(String requestedServer) {
        Request<String> leaveServer = new Request<>(RequestType.LEAVE_SERVER);
        leaveServer.addData("username", user.getUsername());
        leaveServer.addData("serverIndex", requestedServer);
        sendRequest(leaveServer);
        getResponse();
        System.out.println("You have successfully left the server. Enter 0 to go back.");
    }

    public String getRoles(String serverIndex, String chosenMember) {
        Request<String> getMemberRoles = new Request<>(RequestType.GET_MEMBER_ROLES);
        getMemberRoles.addData("serverIndex", serverIndex);
        getMemberRoles.addData("username", chosenMember);
        sendRequest(getMemberRoles);
        Response response = getResponse();
        return (String) response.getData();

    }

    /**
     * sends request that changes server name for all members of the server
     *
     * @param requestedServer given server
     */
    public void changeServerName(String newName, String requestedServer) {
        Request<String> changeName = new Request<>(RequestType.CHANGE_SERVER_NAME);
        changeName.addData("serverIndex", requestedServer);
        changeName.addData("newName", newName);
        sendRequest(changeName);
        getResponse();
    }

    /**
     * prompts user to choose a channel to delete for all members of the given server
     *
     * @param requestedServer given server
     */
    public void deleteChannel(String channelIndex, String requestedServer) {
        Request<String> deleteChannel = new Request<>(RequestType.DELETE_CHANNEL);
        deleteChannel.addData("serverIndex", requestedServer);
        deleteChannel.addData("channelIndex", channelIndex);
        sendRequest(deleteChannel);
        getResponse();
    }

    public boolean canDeleteChannel(String requestedServer){
        Request<String> request = new Request<>(RequestType.CAN_DELETE_CHANNEL);
        request.addData("serverIndex", requestedServer);
        sendRequest(request);
        return getResponse().getResponseStatus() == ResponseStatus.VALID_STATUS;
    }
    /**
     * creates and adds channel to given server that is accessible by all members of the given server
     *
     * @param requestedServer given server
     */
    public void createChannel(String requestedServer, String channelName) {
        Request<String> newChannel = new Request<>(RequestType.NEW_CHANNEL);
        newChannel.addData("serverIndex", requestedServer);
        newChannel.addData("name", channelName);
        sendRequest(newChannel);
        getResponse();
    }

    /**
     * sends request to get all accessible channels to the client in the given server as a String
     *
     * @param requestedServer given server
     * @return String of channels
     */
    public String viewChannels(String requestedServer) {
        Request<String> printChannels = new Request<>(RequestType.PRINT_CHANNELS);
        printChannels.addData("requestedServer", requestedServer);
        sendRequest(printChannels);
        Response response = getResponse();

        // return response.getData().equals("This server is no longer accessible.\n");
        return (String)response.getData();
    }

    public String getMembers(String requestedServer) {
        Request<String> getMembers = new Request<>(RequestType.GET_SERVER_MEMBERS);
        getMembers.addData("serverIndex", requestedServer);
        sendRequest(getMembers);
        return (String)getResponse().getData();
    }


    /**
     * prints all pinned messages of the given channel in the given server
     *
     * @param requestedServer given server
     * @param channelIndex    given channel
     */
    public String viewPinnedMessages(String requestedServer, String channelIndex) {
        Request<String> viewPinned = new Request<>(RequestType.VIEW_PINNED);
        viewPinned.addData("serverIndex", requestedServer);
        viewPinned.addData("channelIndex", channelIndex);
        sendRequest(viewPinned);

        return (String) getResponse().getData();
    }

    /**
     * prints all messages in the channel and prompts client to choose which
     * message they want to pin and sends request to pin that message
     *
     * @param serverIndex given server
     * @param channelIndex    given channel
     */
    public void pinMessage(String serverIndex, String channelIndex, String messageIndex) {
        System.out.println("client");
        Request<String> pinMessage = new Request<>(RequestType.PIN_MESSAGE);
        pinMessage.addData("serverIndex", serverIndex);
        pinMessage.addData("channelIndex", channelIndex);
        pinMessage.addData("messageIndex", messageIndex);
        System.out.println("send req");
        sendRequest(pinMessage);
        System.out.println("get response");
        getResponse();
    }

    /**
     * sends request to return get the server of the given serverIndex
     *
     * @param requestedServer given server index
     * @return DiscordServer of the index
     */
    public DiscordServer getServer(String requestedServer) {
        Request<String> getServer = new Request<>(RequestType.GET_SERVER);
        getServer.addData("serverIndex", requestedServer);
        sendRequest(getServer);
        return (DiscordServer) getResponse().getData();
    }



    /**
     * sends request to receive messages in the channel
     *
     * @param requestedServer given server
     * @param chosenChannel   given channel
     * @return string of messages
     */
    public String printChannelMessages(String requestedServer, String chosenChannel) {
        Request<String> enterChannel = new Request<>(RequestType.ENTER_CHANNEL);
        enterChannel.addData("channelIndex", chosenChannel);
        enterChannel.addData("serverIndex", requestedServer);
        sendRequest(enterChannel);
        Response response = getResponse();
       return (String)response.getData();
    }

    /**
     * prompts user to invite friend to user and the chosen user is then added to the server
     *
     * @param requestedServer given server
     */
    public Response inviteFriendToServer(String requestedServer, String friendInvite) {
        Request<String> serverInviteRequest = new Request<>(RequestType.INVITE_TO_SERVER);
        serverInviteRequest.addData("username", friendInvite);
        serverInviteRequest.addData("serverIndex", requestedServer);
        sendRequest(serverInviteRequest);
        return getResponse();
    }


    public Response enterPrivateChat(String username) {
        Request<String> request = new Request<>(RequestType.PRIVATE_CHAT_MESSAGES);
        request.addData("username", username);
        sendRequest(request);
        return getResponse();
    }

    /**
     * get private chat
     * @param username String username
     * @return Response
     */
    public Response getPrivateChat(String username) {
        Request<String> request = new Request<>(RequestType.CHECK_USERNAME_FOR_CHAT);
        request.addData("username", username);
        sendRequest(request);
        return getResponse();
    }


    /**
     * prompts client to create new server
     */
    public void createServer(String name) {
        Request<String> newServerRequest = new Request<>(RequestType.NEW_SERVER);
        newServerRequest.addData("name", name);
        sendRequest(newServerRequest);
        DiscordServer server = (DiscordServer) getResponse().getData();
        user.addServer(server);
    }

    /**
     * sends request to print all privateChat usernames
     */
    public String PrivateChatUsernames() {
        Request<String> print = new Request<>(RequestType.PRINT_PRIVATE_CHATS_USERNAMES);
        print.addData("username", user.getUsername());
        sendRequest(print);
        Response response = getResponse();

        return (String) response.getData();
    }

    /**
     * sends message to user with given username
     *
     * @param username String username of the private chat we want to send a message to
     * @param text     String content of the message
     */
    public void sendPrivateChatMessage(String username, String text) {
        Request<String> request = new Request<>(RequestType.SEND_MESSAGE);
        text = user.getUsername() + ":" + text;
        request.addData("message", text);
        request.addData("username", username);
        sendRequest(request);
    }


    /**
     * sends message to given channel in given server
     *
     * @param channelIndex given channel
     * @param serverIndex  given server
     * @param text         String content of the message
     */
    public void sendChannelMessage(String channelIndex, String serverIndex, String text) {
        Request<String> request = new Request<>(RequestType.SEND_MESSAGE_TO_CHANNEL);
        request.addData("message", text);
        request.addData("channelIndex", channelIndex);
        request.addData("serverIndex", serverIndex);
        sendRequest(request);
    }

    /**
     * prompts user to send a file to the chat that the user is currently in
     *
     * @param username username of the private chat
     */
    public void sendFile(String username, File file) {
        Request<String> addFileRequest = new Request<>(RequestType.ADD_FILE_TO_CHAT);
        addFileRequest.addData("file", file.getAbsolutePath());
        addFileRequest.addData("username", username);
        sendRequest(addFileRequest);
    }

    /**
     * prompts user to send a file to the given channel in the given server
     *
     * @param channelIndex given channel
     * @param serverIndex  given server
     */
    public void sendFile(String channelIndex, String serverIndex, File file) {
        Request<String> addFileRequest = new Request<>(RequestType.ADD_FILE_TO_CHANNEL);
        addFileRequest.addData("file", file.getAbsolutePath());
        addFileRequest.addData("channelIndex", channelIndex);
        addFileRequest.addData("serverIndex", serverIndex);
        sendRequest(addFileRequest);
    }

    private long downloadFile(Request<String> downloadFileRequest){
        sendRequest(downloadFileRequest);
        Response response = getResponse();
        if (response.getResponseStatus() == ResponseStatus.INVALID_FILE_NAME)
            return 0;
        File file = (File) response.getData();
        try {
            return Files.size(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * prompts user to download a file from the chat that the user is currently in
     *
     * @param username username of the private chat
     */
    public long downloadFile(String fileName, String username) {
        Request<String> downloadFileRequest = new Request<>(RequestType.DOWNLOAD_FILE);
        downloadFileRequest.addData("fileName", fileName);
        downloadFileRequest.addData("username", username);
        return downloadFile(downloadFileRequest);
    }

    /**
     * prompts user to download a file from the given channel in the given server
     *
     * @param channelIndex given channel
     * @param serverIndex  given server
     */
    public long downloadFile(String fileName, String channelIndex, String serverIndex) {
        Request<String> downloadFileRequest = new Request<>(RequestType.DOWNLOAD_FILE_IN_CHANNEL);
        downloadFileRequest.addData("fileName", fileName);
        downloadFileRequest.addData("channelIndex", channelIndex);
        downloadFileRequest.addData("serverIndex", serverIndex);
        return downloadFile(downloadFileRequest);
    }

    /**
     * sends request to accept friend request to given username
     *
     * @param requester given username
     */
    public void acceptFriendRequest(String requester) {
        Request<String> acceptFriendRequest = new Request<>(RequestType.ACCEPT_FRIEND_REQUEST);
        acceptFriendRequest.addData("receiver", user.getUsername());
        acceptFriendRequest.addData("requester", requester);
        sendRequest(acceptFriendRequest);
        getResponse();
    }

    /**
     * sends request to delete friend request to given username
     *
     * @param requester given username
     */
    public void deleteFriendRequest(String requester) {
        Request<String> deleteFriendRequest = new Request<>(RequestType.DELETE_FRIEND_REQUEST);
        deleteFriendRequest.addData("receiver", user.getUsername());
        deleteFriendRequest.addData("requester", requester);
        sendRequest(deleteFriendRequest);
        getResponse();
    }

    /**
     * sends request to send friend request to given username
     *
     * @param receiver given username
     * @return whether the request was successful or not
     */
    public ResponseStatus sendFriendRequest(String receiver) {
        Request<String> request = new Request<>(RequestType.FRIEND_REQUEST);
        request.addData("receiver", receiver);
        request.addData("requester", user.getUsername());
        sendRequest(request);

        return getResponse().getResponseStatus();
    }

    public String getSentRequests() {
        Request<String> request = new Request<>(RequestType.GET_SENT_REQUESTS);
        String username = getUsername();
        request.addData("username", username);
        sendRequest(request);
        Response response = getResponse();
        return (String)response.getData();
    }

    public void removeRequest(String receiver) {
        Request<String> request = new Request<>(RequestType.REMOVE_SENT_REQUEST);
        request.addData("receiver", receiver);
        request.addData("requester", getUsername());
        sendRequest(request);
        getResponse();
    }

    /**
     * sends request to unblock given username
     *
     * @param unblock given username
     */
    public void unblockUser(String unblock) {
        Request<String> unblockUser = new Request<>(RequestType.UNBLOCK_USER);
        unblockUser.addData("unblocked", unblock);
        unblockUser.addData("requester", user.getUsername());
        sendRequest(unblockUser);
        getResponse();
    }

    /**
     * sends request to block given username
     *
     * @param blocked given username
     */
    public void blockFriend(String blocked) {
        Request<String> request = new Request<>(RequestType.BLOCK_FRIEND);
        request.addData("blocked", blocked);
        request.addData("requester", user.getUsername());
        sendRequest(request);
        getResponse();
    }

    public boolean isBlocked(String username){
        Request<String> request = new Request<>(RequestType.IS_BLOCKED);
        request.addData("blocked", username);
        request.addData("requester", user.getUsername());
        sendRequest(request);
        Response response = getResponse();
        return response.getResponseStatus() == ResponseStatus.DUPLICATE_BLOCK;
    }

    /**
     * sends request to check whether the entered username is correct
     *
     * @param username String entered username
     * @return Response to the request
     */
    public Response checkUsername(String username) {
        Request<String> request = new Request<>(RequestType.CHECK_USERNAME);
        request.addData("username", username);
        sendRequest(request);
        return getResponse();
    }

    /**
     * sends request to sign in the client with given information from console and gets response
     * to check the validity of the sign in
     *
     * @param username given username
     * @param pass     given pass
     * @param email    given email
     * @param phoneNum given phoneNum
     */
    public boolean completeSignUp(String username, String pass, String email, String phoneNum) {
        makeSignUpRequest(username, pass, email, phoneNum);
        Response response = getResponse();
        if (response.getResponseStatus() == ResponseStatus.SIGNUP_VALID) {
            System.out.println("successfully signed up");
        }


        Request<String> request2 = new Request<>(RequestType.SIGN_IN);
        request2.addData("username", username);
        request2.addData("pass", pass);
        sendRequest(request2);

        Response response2 = getResponse();
        this.user = (User) response2.getData();
        user.setChatToNull();
        return true;
    }

    /**
     * sends request to sign in the client with given information from console
     *
     * @param username given username
     * @param pass     given pass
     * @param email    given email
     * @param phoneNum given phoneNum
     */
    private void makeSignUpRequest(String username, String pass, String email, String phoneNum) {
        Request<String> request = new Request<>(RequestType.SIGN_UP);
        request.addData("username", username);
        request.addData("password", pass);
        request.addData("email", email);
        request.addData("phoneNum", phoneNum);
        sendRequest(request);
    }

    /**
     * sends request to set the client's status to the given status
     *
     * @param statusIndex given status
     */
    public void setStatus(int statusIndex) {
        Request<String> request = new Request<>(RequestType.SET_STATUS);
        request.addData("status", Integer.toString(statusIndex - 1));
        sendRequest(request);
        getResponse();
    }

    /**
     * sends request to print the clients friends list and prints
     */
    public String printFriends() {
        Request<String> printList = new Request<>(RequestType.PRINT_FRIENDS);
        printList.addData("username", user.getUsername());
        sendRequest(printList);
        Response response = getResponse();
        return (String)response.getData();
    }


    public boolean changePassword(String newPass) {
        Request<String> request = new Request<>(RequestType.CHANGE_PASSWORD);
        request.addData("username", this.user.getUsername());
        request.addData("pass", newPass);
        sendRequest(request);
        Response response = getResponse();
        return response.getResponseStatus() != ResponseStatus.SAME_PASSWORD;
    }

    /**
     * prompts user to choose a file as their pfp and sends request to save that file
     *
     */
    public void setPFP(File profilePhoto) {
        if (!profilePhoto.exists()) {
            return;
        }

        Request<File> request = new Request<>(RequestType.PROFILE_PHOTO);
        request.addData("profilePhoto", profilePhoto);
        sendRequest(request);
    }

    public File getPFP(String username) {
        Request<String> request = new Request<>(RequestType.GET_PFP);
        request.addData("username", username);
        sendRequest(request);

        return (File)getResponse().getData();
    }

    /**
     * prompts user to log in and sends request with log in information
     *
     * @return whether the login was successful or not
     */
    public Boolean logIn(String username, String pass) {
        Request<String> request;
        request = new Request<>(RequestType.SIGN_IN);
        request.addData("username", username);
        request.addData("pass", pass);
        sendRequest(request);
        Response response = getResponse();

        if (response.getResponseStatus() == ResponseStatus.SIGN_IN_INVALID)
            return false;
        else{
            this.user = (User) response.getData();
            user.setChatToNull();
            return true;
        }
    }

    /**
     * sends request to get all users the client has blocked
     *
     * @return Response from request of getting blocked users
     */
    public String printBlockedUsers() {
        Request<String> printBlocked = new Request<>(RequestType.PRINT_BLOCKED_FRIENDS);
        printBlocked.addData("username", user.getUsername());
        sendRequest(printBlocked);
        Response response = getResponse();
        return ((String) response.getData());
    }


    public String getFriendRequests() {
        Request<String> printFriendRequests = new Request<>(RequestType.PRINT_FRIEND_REQUESTS);
        printFriendRequests.addData("username", user.getUsername());
        sendRequest(printFriendRequests);
        Response response = getResponse();
        return (String) response.getData();
    }

    public String getDislikes(String messageIndex, String username) {
        Request<String> request = new Request<>(RequestType.GET_DISLIKES);
        request.addData("username", username);
        request.addData("messageIndex", messageIndex);
        sendRequest(request);
        return (String)getResponse().getData();
    }

    public String getLikes(String messageIndex, String username) {
        Request<String> request = new Request<>(RequestType.GET_LIKES);
        request.addData("username", username);
        request.addData("messageIndex", messageIndex);
        sendRequest(request);
        return (String)getResponse().getData();
    }

    public String getLaughs(String messageIndex, String username) {
        Request<String> request = new Request<>(RequestType.GET_LAUGHS);
        request.addData("username", username);
        request.addData("messageIndex", messageIndex);
        sendRequest(request);
        return (String)getResponse().getData();
    }

    public String getServerDislikes(String messageIndex, String serverIndex, String channelIndex) {
        Request<String> request = new Request<>(RequestType.GET_DISLIKE_SERVER);
        request.addData("serverIndex", serverIndex);
        request.addData("messageIndex", messageIndex);
        request.addData("channelIndex", channelIndex);
        sendRequest(request);
        return (String)getResponse().getData();
    }

    public String getServerLikes(String messageIndex, String serverIndex, String channelIndex) {
        Request<String> request = new Request<>(RequestType.GET_LIKES_SERVER);
        request.addData("serverIndex", serverIndex);
        request.addData("messageIndex", messageIndex);
        request.addData("channelIndex", channelIndex);
        sendRequest(request);
        return (String)getResponse().getData();
    }

    public String getServerLaughs(String messageIndex, String serverIndex, String channelIndex) {
        Request<String> request = new Request<>(RequestType.GET_LAUGHS_SERVER);
        request.addData("serverIndex", serverIndex);
        request.addData("messageIndex", messageIndex);
        request.addData("channelIndex", channelIndex);
        sendRequest(request);
        return (String)getResponse().getData();
    }

    public void reactToServerMessage(String reaction, String messageIndex, String serverIndex, String channelIndex) {
        Request<String> request = new Request<>(RequestType.REACT_MESSAGE_SERVER);
        request.addData("serverIndex", serverIndex);
        request.addData("channelIndex", channelIndex);
        request.addData("reaction", reaction);
        request.addData("messageIndex", messageIndex);
        sendRequest(request);
        getResponse();
    }

    public void reactToMessage(String reaction, String messageIndex, String username) {
        Request<String> request = new Request<>(RequestType.REACT_MESSAGE);
        request.addData("username", username);
        request.addData("reaction", reaction);
        request.addData("messageIndex", messageIndex);
        sendRequest(request);
        getResponse();
    }

    public String getUsername() {
        return user.getUsername();
    }

    public String getEmail() {
        Request<String> request = new Request<>(RequestType.GET_EMAIL);
        request.addData("username", getUsername());
        sendRequest(request);
        return (String)getResponse().getData();
    }

    public String getPhone() {
        Request<String> request = new Request<>(RequestType.GET_PHONE);
        request.addData("username", getUsername());
        sendRequest(request);
        return (String)getResponse().getData();
    }

    public Status getStatus() {
        Request<String> request = new Request<>(RequestType.GET_STATUS);
        request.addData("username", getUsername());
        sendRequest(request);
        return (Status)getResponse().getData();
    }

    public Status getStatus(String username) {
        Request<String> request = new Request<>(RequestType.GET_STATUS);
        request.addData("username", username);
        sendRequest(request);
        return (Status)getResponse().getData();
    }


    public boolean changeUsername(String newUsername) {
        Request<String> request = new Request<>(RequestType.CHANGE_USER);
        request.addData("oldUsername", getUsername());
        request.addData("newUsername", newUsername);
        sendRequest(request);
        user.setUsername(newUsername);
        return getResponse().getResponseStatus().equals(ResponseStatus.VALID_STATUS);
    }


    public boolean changeEmail(String newEmail) {
        Request<String> request = new Request<>(RequestType.CHANGE_EMAIL);
        request.addData("username", getUsername());
        request.addData("email", newEmail);
        sendRequest(request);
        return getResponse().getResponseStatus().equals(ResponseStatus.VALID_STATUS);
    }

    public boolean changePhone(String newPhone) {
        Request<String> request = new Request<>(RequestType.CHANGE_PHONE);
        request.addData("username", getUsername());
        request.addData("phone", newPhone);
        sendRequest(request);
        return getResponse().getResponseStatus().equals(ResponseStatus.VALID_STATUS);
    }

    public User getUser() {
        return user;
    }
}
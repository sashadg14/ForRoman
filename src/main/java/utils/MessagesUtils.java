package utils;

/**
 * Created by Alex on 15.02.2018.
 */
public class MessagesUtils {

    public boolean isSignInUserMessage(String s){
        return s.matches("\\/register\\s*user\\s*[a-z,A-Z,\\d]+");
    }

    public boolean isSignInAgentMessage(String s){
        return s.matches("\\/register\\s*agent\\s*[a-z,A-Z,\\d]+");
    }

    public boolean isSignInMessage(String s){
        return s.matches("\\/register\\s*(agent|user)\\s*[a-z,A-Z,\\d]+");
    }
    public boolean isExitMessage(String s){
        return s.matches("\\/exit");
    }
    public boolean isLeaveMessage(String s){
        return s.matches("\\/leave");
    }

    public String getNameFromMessage(String s){
        return s.replaceAll("\\/register|[\\s]|user|agent","");
    }

    public String getMessageType(String message){
        if (isSignInMessage(message))
            return Constants.MESSAGE_TYPE_REGISTER;
        else if (isExitMessage(message))
            return Constants.MESSAGE_TYPE_EXIT;
        else if(isLeaveMessage(message))
            return Constants.MESSAGE_TYPE_LEAVE;
        else return Constants.MESSAGE_TYPE_SMS;
    }

    public String createMessageToAgent(String message, long userId){
        return "{ \"id\":"+userId+", \"message\":\""+message+"\"}";
    }
    public String createInitialMessageToAgent(String userName, long userId){
        return "{ \"id\":"+userId+", \"newUserName\":\""+userName+"\"}";
    }
}

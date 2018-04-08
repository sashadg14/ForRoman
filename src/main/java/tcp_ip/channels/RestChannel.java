package tcp_ip.channels;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class RestChannel implements AbstractSocket {

    private List<String> cachedMessages;
    public RestChannel(){
        cachedMessages=new LinkedList<>();
    }

    @Override
    public void sendMessage(String message){
        synchronized (this) {
            cachedMessages.add(message);
            System.out.println("message to rest client--> " + message);
        }
    }

    public LinkedList getCachedMessages(){
        synchronized (this) {
            LinkedList temp= (LinkedList) ((LinkedList)cachedMessages).clone();
            cachedMessages.clear();
            return temp;
        }
    }

    @Override
    public void close() throws IOException {
        cachedMessages.clear();
    }
}

package tcp_ip.channels;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class RestEntity implements AbstractSocket {

    @Override
    public void sendMessage(String message){
        System.out.println("message to rest client--> "+message);
    }

    @Override
    public void close() throws IOException {

    }
}

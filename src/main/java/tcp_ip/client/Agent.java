package tcp_ip.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tcp_ip.channels.AbstractSocket;

import java.util.concurrent.atomic.AtomicInteger;

public class Agent extends Client{
    private AtomicInteger usersCount;
    private int maxChatsCount;
    public Agent(long id, AbstractSocket abstractSocket, String name, int maxChatsCount) {
        super(id, abstractSocket, name);
        this.usersCount = usersCount;
        this.maxChatsCount = maxChatsCount;
        usersCount=new AtomicInteger();
    }

    public AtomicInteger getUsersCount() {
        return usersCount;
    }

    public int getMaxChatsCount() {
        return maxChatsCount;
    }
}

package tcp_ip.client;

import tcp_ip.channels.AbstractSocket;

public class User extends Client{

    public User(long id, AbstractSocket abstractSocket, String name) {
        super(id, abstractSocket, name);
    }
}

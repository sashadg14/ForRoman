package tcp_ip.client;

import tcp_ip.channels.AbstractSocket;

public abstract class Client {
    private long id;
    private AbstractSocket abstractSocket;
    private String name;

    public Client(long id, AbstractSocket abstractSocket, String name) {
        this.id = id;
        this.abstractSocket = abstractSocket;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public AbstractSocket getAbstractSocket() {
        return abstractSocket;
    }

    public String getName() {
        return name;
    }
}

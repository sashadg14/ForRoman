package web;

import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import tcp_ip.AllClientsBase;
import tcp_ip.ServerCommunication;
import tcp_ip.channels.AbstractSocket;
import tcp_ip.client.Client;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.lang.reflect.Method;

@Component
public class RestSessionListener implements HttpSessionListener {
    private final
    AllClientsBase allClientsBase;
    private final
    ServerCommunication serverCommunication;
    org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(RestSessionListener.class);

    @Autowired
    public RestSessionListener(AllClientsBase allClientsBase, ServerCommunication serverCommunication) {
        this.allClientsBase = allClientsBase;
        this.serverCommunication = serverCommunication;
    }

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        //System.out.println("CREATE");
        httpSessionEvent.getSession().setMaxInactiveInterval(300);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
       // System.out.println("DESTROY");
        Client client=allClientsBase.getClientById((Long)httpSessionEvent.getSession().getAttribute("id"));
        if(allClientsBase.doesItsAgentChannel(client.getAbstractSocket()))
        logger.log(Level.INFO, "Rest Agent id=" + client.getId() + " disconnect");
        else
        logger.log(Level.INFO, "Rest User id=" + client.getId() + " disconnect");
        serverCommunication.handlingClientDisconnecting(client.getAbstractSocket());
    }
}

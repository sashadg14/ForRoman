package web.controllers;

import messages_entities.AgentAuthMessage;
import messages_entities.AgentMessage;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tcp_ip.AllClientsBase;
import tcp_ip.ServerCommunication;
import tcp_ip.channels.RestChannel;
import tcp_ip.client.Agent;
import tcp_ip.client.User;
import utils.Constants;
import utils.IdCounter;
import utils.UsersSMSCache;
import web.RestServiseCommunication;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
public class RestControl {

    final private AllClientsBase allClientsBase;
    final private UsersSMSCache usersSMSCache;
    private Logger logger = Logger.getLogger(RestControl.class);

    ServerCommunication serverCommunication;

    @Autowired
    public RestControl(AllClientsBase allClientsBase, ServerCommunication serverCommunication) {
        this.allClientsBase = allClientsBase;
        usersSMSCache = new UsersSMSCache();
        this.serverCommunication = serverCommunication;
    }

    @RequestMapping(value = "agents", method = RequestMethod.GET)
    public List<Long> getAgentsList() {
        return allClientsBase.getAgentList().stream().map(Agent::getId).collect(Collectors.toList());
    }

    @RequestMapping(value = "agents/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getConcreteAgent(@PathVariable("id") long id) {
        try {
            Agent a = allClientsBase.getAgentList().stream().filter(agent -> agent.getId() == id).findFirst().get();
            return new ResponseEntity<>(new AgentEntity(a), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "agents", method = RequestMethod.POST)
    public ResponseEntity<?> createAgent(@RequestBody AgentAuthMessage agentAuthMessage, HttpServletRequest request) {
        Long integer = (Long) request.getSession().getAttribute("id");
        if (integer != null) {
            return new ResponseEntity<>(("Unable to create. Already created."), HttpStatus.CONFLICT);
        }
        Agent agent = new Agent(IdCounter.getAndIncrement(), new RestChannel(), agentAuthMessage.getName(), agentAuthMessage.getChats());
        allClientsBase.addNewAgent(agent);
        logger.log(Level.INFO, "Registered Rest agent id=" + agent.getId());
        request.getSession().setAttribute("id", agent.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/agents/" + agent.getId());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "agents/{id}/messages", method = RequestMethod.GET)
    public ResponseEntity<?> getMessageFromAgent(@PathVariable("id") long id,HttpServletRequest request) {
        Agent a;
        try {
            a = allClientsBase.getAgentList().stream().filter(agent -> agent.getId() == id).findFirst().get();
          /*  if (((Long) request.getSession().getAttribute("id")) != a.getId())
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);*/
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(((RestChannel)a.getAbstractSocket()).getCachedMessages(), HttpStatus.OK);
    }

    @RequestMapping(value = "agents/{id}/messages",method = RequestMethod.POST)
    public ResponseEntity<?> sendMessageFromAgent(@PathVariable("id") long id, @RequestBody AgentMessage agentMessage, HttpServletRequest request){
        Agent a;
        try{
            a=allClientsBase.getAgentList().stream().filter(agent->agent.getId()==id).findFirst().get();
            if(((Long)request.getSession().getAttribute("id"))!=a.getId())
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!allClientsBase.doesClientHaveInterlocutor(a.getAbstractSocket()))
            return new ResponseEntity<>(Constants.WAIT_USER,HttpStatus.BAD_REQUEST);
        serverCommunication.sendMessageToInterlocutorOfAgent(a.getAbstractSocket(),agentMessage);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /////////////////////////////////////////////////////////////////
    @RequestMapping(value = "users", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody Map<String, String> userAuthMessage, HttpServletRequest request) {
        Long integer = (Long) request.getSession().getAttribute("id");
        if (integer != null) {
            return new ResponseEntity<>(("Unable to create. Already created."), HttpStatus.CONFLICT);
        } else if (!userAuthMessage.containsKey("name"))
            return new ResponseEntity<>("For register as user you must send {\"name\":\"Alex\"}", HttpStatus.BAD_REQUEST);

        User user = new User(IdCounter.getAndIncrement(), new RestChannel(), userAuthMessage.get("name"));
        allClientsBase.addNewUser(user);
        logger.log(Level.INFO, "Registered Rest user id=" + user.getId());
        request.getSession().setAttribute("id", user.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/users/" + user.getId());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "users", method = RequestMethod.GET)
    public List<Long> getUsersList() {
        return allClientsBase.getUserList().stream().map(User::getId).collect(Collectors.toList());
    }

    @RequestMapping(value = "users/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getConcreteUser(@PathVariable("id") long id) {
        try {
            User u = allClientsBase.getUserList().stream().filter(user -> user.getId() == id).findFirst().get();
            return new ResponseEntity<>(new UserEntity(u), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /*@GetMapping("/viewSessionData")                     // it will handle all request for /welcome
    public java.util.Map<String,Integer> start(HttpServletRequest request) {
        Integer integer =(Integer) request.getSession().getAttribute("id");
        if(integer==null){
            integer= i++;
            request.getSession().setAttribute("id",integer);
        }*//*else{
            integer++;
            request.getSession().setAttribute("hitCounter",integer);
        }*//*
        java.util.Map<String,Integer> hitCounter=new HashMap<>();
        hitCounter.put("id",integer);
        return hitCounter;
    }*/

    public class AgentEntity {
        String name;
        long id;
        int chatsCount;
        int maxChatsCount;

        public AgentEntity() {
        }

        ;

        AgentEntity(tcp_ip.client.Agent agent) {
            name = agent.getName();
            id = agent.getId();
            chatsCount = agent.getUsersCount().get();
            maxChatsCount = agent.getMaxChatsCount();
        }

        ;

        public String getName() {
            return name;
        }

        public long getId() {
            return id;
        }

        public int getChatsCount() {
            return chatsCount;
        }

        public int getMaxChatsCount() {
            return maxChatsCount;
        }
    }

    public class UserEntity {
        String name;
        long id;

        public UserEntity() {
        }

        ;

        UserEntity(tcp_ip.client.User user) {
            name = user.getName();
            id = user.getId();
        }

        ;

        public String getName() {
            return name;
        }

        public long getId() {
            return id;
        }
    }
}

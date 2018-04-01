package messages_entities;

public class AgentAuthMessage {
    private String name;
    private int chats;
    public AgentAuthMessage(){};
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChats() {
        return chats;
    }

    public void setChats(int chats) {
        this.chats = chats;
    }
}

public class User {
    private String name;
    private String secondName;
    private String command;

    public User(String name) {
        this.name = name;
        this.secondName = "";
        this.command = "";
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}

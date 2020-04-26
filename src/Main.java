import com.google.inject.internal.cglib.reflect.$FastMember;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.HashMap;

public class Main extends TelegramLongPollingBot {

    public HashMap<Long, User> users = new HashMap<>();

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botapi = new TelegramBotsApi();
        try {
            botapi.registerBot(new Main());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String text = message.getText();
        long id = message.getChatId();
        System.out.println("" + id + ": " + text);

        if (text.equals("/new_user")) {
            sendMessage(message, "Привет, как тебя зовут?");
            User user = new User();
            users.put(id, user);
            return;
        }
        if (!users.containsKey(id)){
            sendMessage(message, "Мы незнакомы");
            return;
        }
        if (conditionHandler(message)) return;
        if (commandHandler(message)) return;
        sendMessage(message, users.get(id).getName() + " сказал: " + text);
    }

    private boolean commandHandler(Message message){
        String text = message.getText();
        long id = message.getChatId();
        User user = users.get(id);
        if (text.equals("/my_family")) {
            if (user.getSecondName().equals(""))
                sendMessage(message, "Я не знаю, вашей фамилии");
            else
                sendMessage(message, "Ваша фамилия: " + user.getSecondName());
            return true;
        }
        if (text.equals("/change_family")) {
            sendMessage(message, "Введите вашу фамилию");
            user.setCommand("family");
            return true;
        }
        if (text.equals("/change_name")) {
            sendMessage(message, "Введите ваше новое имя");
            user.setCommand("name");
            return true;
        }
        return false;
    }

    private boolean conditionHandler(Message message){
        String text = message.getText();
        long id = message.getChatId();
        User user = users.get(id);
        String command = user.getCommand();
        if (command.equals(""))
            return false;

        if(command.equals("family")){
            user.setSecondName(text);
            sendMessage(message, "Ваша фамилия сохранена успешно!");
            user.setCommand("");
            return true;
        }
        if(command.equals("name")) {
            user.setName(text);
            sendMessage(message, "Ваше новое имя сохранено сохранена успешно!");
            user.setCommand("");
            return true;
        }
        if(command.equals("newUser")) {
            user.setName(text);
            sendMessage(message, "Приятно познакомится, " + text);
            user.setCommand("");
            return true;
        }

        sendMessage(message, "Что-то пошло не так");
        user.setCommand("");
        return true;
    }


    private void sendMessage(Message m, String text){
        SendMessage message = new SendMessage();
        message.setChatId(m.getChatId());
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "java_2_bot";
    }

    @Override
    public String getBotToken() {
        return "1226704230:AAFgksjADyi8hMORsT7K9OoOt2WOhN4BJ9s";
    }
}

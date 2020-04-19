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
        System.out.println(text);
        long id = message.getChatId();
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        if (!users.containsKey(id)){
            users.put(id, new User(text));
            sendMessage(message, "Приятно познакомится, " + text);
            return;
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        User user = users.get(id);
        if (!user.getCommand().equals("")){
            switch (user.getCommand()){
                case "family":
                    user.setSecondName(text);
                    sendMessage(message, "Ваша фамилия сохранена успешно!");
                    user.setCommand("");
                    break;
                case "name":
                    user.setName(text);
                    sendMessage(message, "Ваше новое имя сохранено сохранена успешно!");
                    user.setCommand("");
                    break;
            }
            return;
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        if (text.equals("/new_user")) {
            if (users.containsKey(id))
                users.remove(id);
            sendMessage(message, "Привет, как тебя зовут?");
            return;
        }
        if (text.equals("/my_family")) {
            if (user.getSecondName().equals(""))
                sendMessage(message, "Я не знаю, вашей фамилии");
            else
                sendMessage(message, "Ваша фамилия: " + user.getSecondName());
            return;
        }
        if (text.equals("/change_family")) {
                sendMessage(message, "Введите вашу фамилию");
                user.setCommand("family");
            return;
        }
        if (text.equals("/change_name")) {
            sendMessage(message, "Введите ваше новое имя");
            user.setCommand("name");
            return;
        }

        sendMessage(message, users.get(id).getName() + " сказал: " + text);
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

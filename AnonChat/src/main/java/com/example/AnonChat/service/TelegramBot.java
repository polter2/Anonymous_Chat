package com.example.AnonChat.service;

import com.example.AnonChat.commands.TelegramBotCommands;
import com.example.AnonChat.config.BotConfig;
import com.example.AnonChat.model.UserReposetory;
import com.example.AnonChat.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;


@Component
public class TelegramBot extends TelegramLongPollingBot {

    private static Random random = new Random();
    private Set<Long> waiting = new HashSet<>();
    final BotConfig config;
    @Autowired
    private UserReposetory userReposetory;
    private ArrayList<Users> x = new ArrayList<>();
    private Users user = new Users();
    private Map<Long, Long> connections = new HashMap<>();
    long foundId = 0L;


    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        TelegramBotCommands commands = new TelegramBotCommands(config);

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();


            switch (messageText) {

                case "/start":
                    commands.buttonsForMenu(chatId, "Welcome to anonymous chat!");
                    registerUser(update.getMessage());
                    break;

                case "Search new chat":
                    search(chatId);
                    break;

                case "Stop conversation":

                    stop(chatId);
                    break;

                default:

                    if (update.hasMessage() && update.getMessage().hasText() && connections.containsKey(chatId)) {
                        sendMessage(connections.get(chatId), update.getMessage().getText());
                    }
            }
        }
    }


    private void search(long chatId) {
        if (connections.containsKey(chatId)) {
            connections.remove(chatId);
            connections.remove(foundId);
            sendMessage(chatId, "You stopped the chat!");
            sendMessage(foundId, "Your companion has stopped the chat.");
        }
        long find = 0L;
        synchronized (waiting) {
            if (waiting.size() > 0) {
                find = waiting.stream().skip(random.nextInt(waiting.size())).findFirst().orElse(null);
                waiting.remove(find);
            } else {
                waiting.add(chatId);
            }
        }
        sendMessage(chatId, "People are searching chat: " + waiting.size());
        if (find == 0 || find == chatId) {
            sendMessage(chatId, "Searching... Companion not found, waiting list is empty. You have been added to the waiting list");
        } else {
            connections.put(chatId, find);
            connections.put(find, chatId);
            sendMessage(chatId, "Companion found. Created a new chat");
            sendMessage(find, "Companion found. Created a new chat");
            foundId = find;
        }
    }


    private void stop(long chatId){
        connections.remove(chatId);
        connections.remove(foundId);
        sendMessage(chatId, "You stopped the chat!");
        sendMessage(foundId, "Your companion has stopped the chat.");
    }

    private void registerUser(Message msg) {
        if (userReposetory.findById(msg.getChatId()).isEmpty()) {
            var chatId = msg.getChatId();
            user.setChatId(chatId);
            userReposetory.save(user);
            sendMessage(chatId, "You are successfully registered in Anonymous Chat!✅  " + "\n" +
                    "‼️Anonymous chat is in beta testing, " +
                    "you may encounter some bugs and errors while chat is running!" +
                    "\n" );
        } else {
            sendMessage(msg.getChatId(), "You are alredy registered in our Anonymous Chat! " + "\n" +
                    "‼️Just a reminder that our Chat is still in beta testing!‼️");
        }
    }


    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
        }
    }
}
package com.example.AnonChat.commands;

import com.example.AnonChat.config.BotConfig;
import com.example.AnonChat.service.TelegramBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class TelegramBotCommands extends TelegramBot {

    public TelegramBotCommands(BotConfig config) {
        super(config);
    }

    public void buttonsForMenu (long chatId, String textToSend) {

        SendMessage messageForMenu = new SendMessage();
        messageForMenu.setChatId(String.valueOf(chatId));
        messageForMenu.setText(textToSend);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("Search new chat");
        row.add("Stop conversation");
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
        messageForMenu.setReplyMarkup(keyboardMarkup);

        try {
            execute(messageForMenu);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }


}

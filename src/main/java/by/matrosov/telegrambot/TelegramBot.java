package by.matrosov.telegrambot;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

public class TelegramBot extends TelegramLongPollingBot{
    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message!=null && message.hasText()){

            String a = message.getText();
            a = a.replace(" ", "+");

            try {
                String result = "";
                JsonObject jsonObject = new Gson().fromJson(readStringFromUrl("http://api.giphy.com/v1/gifs/search?q=" + a + "&api_key=&limit=25&lang=ru"), JsonObject.class);
                String gif = jsonObject.getAsJsonArray("data").get(0).getAsJsonObject().get("url").toString();
                result += gif;

                SendMessage s = new SendMessage()
                        .setChatId(message.getChatId())
                        .setText(result);

                try {
                    execute(s);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public String getBotUsername() {
        return "";
    }

    @Override
    public String getBotToken() {
        return "";
    }

    private String readStringFromUrl(String url) throws IOException {
        try(InputStream in = new URL(url).openStream()){
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = reader.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }
    }

}

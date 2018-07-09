package pro.golub.stayupdated.sender;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class StayUpdatedBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId());
            if (update.getMessage().getText().equals("/start")) {
                message.setText("Hello. To subscribe for twitter updates, use command /update followed by list of hashtags. F.e.: /update java scala blockchain");
            } else if (update.getMessage().getText().equals("/update")) {

            }
            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "stay_updated_bot";
    }

    @Override
    public String getBotToken() {
        return "409949167:AAH4a-R_vOAbHrHvMZYTevzi-A0Ya3-2Now";
    }
}

package pro.golub.stayupdated.sender.command;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;
import pro.golub.stayupdated.dto.StorageManager;

/**
 * This command simply replies with a hello to the users command and
 * sends them the 'kind' words back, which they send via command parameters
 *
 * @author Timo Schulz (Mit0x2)
 */
public class UpdateCommand extends BotCommand {

    private static final String LOGTAG = "HELLOCOMMAND";

    public UpdateCommand() {
        super("hello", "Say hallo to this bot");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        if (StorageManager.isUserExists(user.getId())) {
            return;
        }

        String userName = chat.getUserName();
        if (userName == null || userName.isEmpty()) {
            userName = user.getFirstName() + " " + user.getLastName();
        }

        //StorageManager.addUser(user.getId(), arguments);

        StringBuilder messageTextBuilder = new StringBuilder("Hello ").append(userName);
        if (arguments != null && arguments.length > 0) {
            messageTextBuilder.append("\n");
            messageTextBuilder.append("You are subscribed for following hashtags:\n");
            messageTextBuilder.append(String.join(" ", arguments));
        }

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        answer.setText(messageTextBuilder.toString());

        try {
            absSender.sendMessage(answer);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}
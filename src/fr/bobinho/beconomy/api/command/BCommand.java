package fr.bobinho.beconomy.api.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Syntax;
import fr.bobinho.beconomy.api.color.BColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

/**
 * Bobinho command library
 */
public class BCommand extends BaseCommand {

    @Retention(RetentionPolicy.RUNTIME)
    protected @interface OpCommand {}

    /**
     * Unitilizable constructor (utility class)
     */
    protected BCommand() {
    }

    /**
     * Sends the command help message to the player
     *
     * @param commandClass the command class
     * @param sender       the sender
     * @param command      the command name
     */
    protected void sendCommandHelp(Class<? extends BCommand> commandClass, Player sender, String command) {
        sender.sendMessage(BColor.GOLD + "===================== " + BColor.GREEN + command + BColor.GOLD + " ======================");

        for (Method method : commandClass.getDeclaredMethods()) {

            //Checks if the command is not an op command
            if (method.getAnnotation(OpCommand.class) != null || method.getAnnotation(Syntax.class) == null) {
                continue;
            }

            //Sends the command informations
            sender.sendMessage(BColor.GOLD + method.getAnnotation(Syntax.class).value() + BColor.AQUA + " - " +
                    BColor.GREEN + method.getAnnotation(Description.class).value());
        }

        sender.sendMessage(BColor.GOLD + StringUtils.repeat("=", command.length() + 44));
    }

}

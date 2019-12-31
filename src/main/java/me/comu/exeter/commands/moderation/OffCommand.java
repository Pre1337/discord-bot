package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class OffCommand implements ICommand {

    public static String userID;
    public static boolean shouldDelete = false;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify a user to turn off").queue();
            return;
        }

        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (!args.isEmpty() && mentionedMembers.isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                event.getChannel().sendMessage("Couldn't find the user " + args.get(0)).queue();
                return;
            } else if (targets.size() > 1) {
                event.getChannel().sendMessage("Multiple users found! Try mentioning the user instead.").queue();
                return;
            }
            shouldDelete = true;
            userID = targets.get(0).getId();
            event.getChannel().sendMessage("Ok, Turned off **" + targets.get(0).getAsMention() + "**.").queue();
            return;
        } else if (!args.isEmpty() && !mentionedMembers.isEmpty()) {
            shouldDelete = true;
            userID = mentionedMembers.get(0).getId();
            event.getChannel().sendMessage("Ok, Turned off **" + mentionedMembers.get(0).getAsMention() + "**.").queue();
        }
    }

    @Override
    public String getHelp() {
        return "Deletes all messages the off'd user tries to send\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "off";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"disable"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
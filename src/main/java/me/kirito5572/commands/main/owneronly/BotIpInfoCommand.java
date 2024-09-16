package me.kirito5572.commands.main.owneronly;

import me.kirito5572.objects.main.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class BotIpInfoCommand implements ICommand {
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        if(!Objects.requireNonNull(event.getMember()).getId().equals("284508374924787713")) {
            event.reply("해당 명령어를 사용할수 없습니다.").setEphemeral(true).queue();
            return;
        }
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("인터페이스 정보");
        String ip;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (networkInterface.isLoopback() || !networkInterface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    ip = addr.getHostAddress();
                    builder.addField(networkInterface.getDisplayName(), ip, true);
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        event.replyEmbeds(builder.build()).setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(30, TimeUnit.SECONDS));
    }

    @Override
    public String getHelp() {
        return "봇이 구동되고 있는 컴퓨터의 IP를 알아냅니다.";
    }

    @Override
    public String getInvoke() {
        return "내아이피";
    }
}

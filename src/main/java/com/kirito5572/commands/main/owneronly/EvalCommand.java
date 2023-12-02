package com.kirito5572.commands.main.owneronly;

import com.kirito5572.objects.main.ICommand;
import groovy.lang.GroovyShell;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class EvalCommand implements ICommand {
    private final @NotNull GroovyShell engine;
    private final @NotNull String imports;
    public EvalCommand() {
        this.engine = new GroovyShell();
        this.imports = """
                import java.io.*
                import java.lang.*
                import java.util.*
                import java.util.Arrays.*
                import java.util.concurrent.*
                import java.sql.*
                import net.dv8tion.jda.api.*
                import net.dv8tion.jda.api.entities.*
                import net.dv8tion.jda.api.managers.*
                import net.dv8tion.jda.api.utils.*
                import me.duncte123.botcommons.messaging.*
                import net.dv8tion.jda.api.events.message.guild.*
                import net.dv8tion.jda.api.exceptions.*
                import net.dv8tion.jda.api.audio.*
                import net.dv8tion.jda.api.events.*
                import net.dv8tion.jda.api.events.channel.category.update.*
                import net.dv8tion.jda.api.events.channel.category.*
                import net.dv8tion.jda.api.events.channel.priv.*
                import net.dv8tion.jda.api.events.channel.store.update.*
                import net.dv8tion.jda.api.events.channel.store.*
                import net.dv8tion.jda.api.events.channel.text.update.*
                import net.dv8tion.jda.api.events.channel.text.*
                import net.dv8tion.jda.api.events.channel.voice.update.*
                import net.dv8tion.jda.api.events.channel.voice.*
                import net.dv8tion.jda.api.events.emote.update.*
                import net.dv8tion.jda.api.events.emote.*
                import net.dv8tion.jda.api.events.guild.update.*
                import net.dv8tion.jda.api.events.guild.voice.*
                import net.dv8tion.jda.api.events.guild.member.*
                import net.dv8tion.jda.api.events.guild.invite.*
                import net.dv8tion.jda.api.events.guild.override.*
                import net.dv8tion.jda.api.events.guild.*
                import net.dv8tion.jda.api.events.http.*
                import net.dv8tion.jda.api.events.message.guild.*
                import net.dv8tion.jda.api.events.message.guild.react.*
                import net.dv8tion.jda.api.events.message.react.*
                import net.dv8tion.jda.api.events.message.priv.react.*
                import net.dv8tion.jda.api.events.message.priv.*
                import net.dv8tion.jda.api.events.message.*
                import net.dv8tion.jda.api.events.role.*
                import net.dv8tion.jda.api.events.role.update.*
                import net.dv8tion.jda.api.events.self.*
                import net.dv8tion.jda.api.events.role.*
                import net.dv8tion.jda.api.events.role.update.*
                import net.dv8tion.jda.api.events.*
                import net.dv8tion.jda.api.managers.*
                import me.duncte123.botcommons.*
                import me.duncte123.botcommons.text.*
                import me.duncte123.botcommons.commands.*
                import me.duncte123.botcommons.config.*
                import me.duncte123.botcommons.messaging.*
                import me.duncte123.botcommons.web.*
                import bot.listener.*
                import bot.commands.*
                import bot.commands.admin.*
                import bot.commands.music.*;
                import bot.objects.*
                import bot.*
                import com.google.gson.*
                """;
    }
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        if(!Objects.requireNonNull(event.getMember()).getId().equals("284508374924787713")) {
            return;
        }
        String command = event.getOption("실행코드", OptionMapping::getAsString);
        if (command == null) {
            event.reply("입력된 명령어 없음").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
            return;
        }
        if(command.contains("jda.shutdown()")) {
            event.reply("봇이 종료됩니다.").setEphemeral(true).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
            System.exit(0);
        }
        try {
            engine.setProperty("event", event);
            engine.setProperty("channel", event.getChannel());
            engine.setProperty("jda", event.getJDA());
            engine.setProperty("guild", event.getGuild());
            engine.setProperty("member", event.getMember());

            String script = imports + command.split("\\s+", 2)[1];
            Object out = engine.evaluate(script);

            event.reply(out == null ? "에러 없이 실행이 완료되었습니다." : out.toString()).queue(v -> v.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
        } catch (Exception e) {
            event.reply(e.getMessage()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "개발용 실행형 명령어";
    }

    @Override
    public String getInvoke() {
        return "eval";
    }
}

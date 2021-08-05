package cizlolbot;

import cizlolbot.twitch.irc.IrcBot;
import cizlolbot.twitch.model.CommandTriggerType;
import cizlolbot.twitch.module.GuiceModules;
import cizlolbot.twitch.service.ChannelConfigService;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Application {

    public static void main(String[] args) throws InterruptedException {
        Injector injector = Guice.createInjector(GuiceModules.all());
        ChannelConfigService channelConfigService = injector.getInstance(ChannelConfigService.class);

        channelConfigService.addStaticReply("cizuss", "hi", "/mods", CommandTriggerType.CONTAINS);
        channelConfigService.addStaticReplyToSender("cizuss", "hello", "hi", CommandTriggerType.CONTAINS);
        channelConfigService.addSetLolRegion("cizuss", "!setregion");
        channelConfigService.addSetLolName("cizuss", "!setsummoner");
        channelConfigService.addOpGGCommand("cizuss", "!opgg");
        channelConfigService.addRankCommand("cizuss", "!rank");
        channelConfigService.addSetCommand("cizuss", "!setcommand");

        IrcBot ircBot = injector.getInstance(IrcBot.class);

        ircBot.run("cizuss");
    }
}

package cizlolbot;

import cizlolbot.twitch.factory.ChannelConfigItemDaoFactory;
import cizlolbot.twitch.factory.CommandResponseDaoFactory;
import cizlolbot.twitch.handlers.ChannelConfigBasedHandlerService;
import cizlolbot.twitch.handlers.HandlerService;
import cizlolbot.twitch.irc.IrcBot;
import cizlolbot.twitch.model.CommandTriggerType;
import cizlolbot.twitch.service.ChannelConfigService;
import cizlolbot.twitch.service.TwitchService;

public class Application {
    public static void main(String[] args) throws InterruptedException {
        ChannelConfigService channelConfigService = ChannelConfigService.getInstance(ChannelConfigItemDaoFactory.createDao(),
                CommandResponseDaoFactory.createDao());
        TwitchService twitchService = TwitchService.getInstance();

        HandlerService handlerService = ChannelConfigBasedHandlerService.getInstance(channelConfigService, twitchService);

        channelConfigService.addStaticReply("lck", "KEKW", "LULW", CommandTriggerType.CONTAINS);
        IrcBot ircBot = new IrcBot(twitchService, handlerService);

        ircBot.run("lck");
    }
}

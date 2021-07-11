package cizlolbot.twitch.module;

import cizlolbot.twitch.dao.ChannelConfigItemDao;
import cizlolbot.twitch.dao.CommandResponseDao;
import cizlolbot.twitch.dao.InMemoryChannelConfigItemDao;
import cizlolbot.twitch.dao.InMemoryCommandResponseDao;
import cizlolbot.twitch.handlers.ChannelConfigBasedHandlerService;
import cizlolbot.twitch.handlers.HandlerService;
import com.google.inject.AbstractModule;

/**
 * Guice module that provides implementations for all abstract classes/interfaces
 * Would be nice to refactor and have multiple guice modules with less responsibility on each one
 */
public class GodModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(HandlerService.class).to(ChannelConfigBasedHandlerService.class);
        bind(ChannelConfigItemDao.class).to(InMemoryChannelConfigItemDao.class);
        bind(CommandResponseDao.class).to(InMemoryCommandResponseDao.class);
    }
}

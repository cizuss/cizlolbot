package cizlolbot.twitch.factory;

import cizlolbot.twitch.dao.CommandResponseDao;
import cizlolbot.twitch.dao.InMemoryCommandResponseDao;

public class CommandResponseDaoFactory {
    public static CommandResponseDao createDao() {
        return InMemoryCommandResponseDao.getInstance();
    }
}

package cizlolbot.twitch.dao;

import cizlolbot.twitch.model.CommandResponse;

import java.util.Map;
import java.util.Set;

public interface CommandResponseDao {
    Map<String, CommandResponse> findByIds(Set<String> ids);
    void insert(CommandResponse commandResponse);
}

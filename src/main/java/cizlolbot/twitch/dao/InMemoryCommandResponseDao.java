package cizlolbot.twitch.dao;

import cizlolbot.twitch.model.CommandResponse;
import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class InMemoryCommandResponseDao implements CommandResponseDao {
    private Map<String, CommandResponse> db = new HashMap<>();

    @Override
    public Map<String, CommandResponse> findByIds(Set<String> ids) {
        return db.entrySet().stream().filter(entry -> ids.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void insert(CommandResponse commandResponse) {
        db.put(commandResponse.getId(), commandResponse);
    }
}

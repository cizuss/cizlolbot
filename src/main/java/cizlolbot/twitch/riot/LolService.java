package cizlolbot.twitch.riot;

import cizlolbot.twitch.cache.ExpiringCache;
import cizlolbot.twitch.cache.ExpiringUnboundedCache;
import com.google.inject.Singleton;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class LolService {
    private Map<String, String> summonerNames = new HashMap<>();
    private Map<String, Region> regions = new HashMap<>();
    private ExpiringCache<String, String> ranksCache = new ExpiringUnboundedCache<>(Duration.ofMinutes(15).toMillis());

    public void setSummonerName(String channel, String summonerName) {
        summonerNames.put(channel, summonerName);
    }

    public String getSummonerName(String channel) {
        return summonerNames.get(channel);
    }

    public void setRegion(String channel, Region region) {
        regions.put(channel, region);
    }

    public Region getRegion(String channel) {
        return regions.get(channel);
    }

    public String constructOpGGLink(String channel) throws ConstructOpGgException {
        String summonerName = summonerNames.get(channel);

        if (summonerName == null) {
            throw new ConstructOpGgException("No summoner name found, make sure to set the summoner name first");
        }

        Region region = regions.get(channel);

        if (region == null) {
            throw new ConstructOpGgException("No region found, make sure to set the region first");
        }

        return String.format("https://%s.op.gg/summoner/userName=%s", region.toString(), summonerName);
    }

    // hack, should use riot api instead; if opgg changes their html code we're screwed
    public String getRank(String channel) throws ConstructOpGgException, IOException {
        String rank = ranksCache.get(channel);
        if (rank != null) {
            return rank;
        }

        String opGgLink = constructOpGGLink(channel);
        Document document = Jsoup.connect(opGgLink).get();
        Elements elements = document.getElementsByClass("TierRank");

        if (elements == null || elements.size() == 0) {
            throw new ConstructOpGgException(String.format("Summoner %s on region %s does not exist", summonerNames.get(channel), regions.get(channel).toString()));
        }

        rank = elements.get(0).text();
        ranksCache.set(channel, rank);
        return rank;
    }
}

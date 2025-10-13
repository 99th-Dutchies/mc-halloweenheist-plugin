package nl._99th_dutchies.halloween_heist.util;

import nl._99th_dutchies.halloween_heist.HalloweenHeistPlugin;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class RealTimeCycle implements Runnable {
    private final HalloweenHeistPlugin plugin;

    public RealTimeCycle(HalloweenHeistPlugin plugin) {
        this.plugin = plugin;
    }

    public void run() {
        final LocalDateTime time = LocalDateTime.now(ZoneId.of(this.plugin.config.getString("timezone", "UTC")));
        final int hours = time.getHour();
        final int minutes = hours * 60 + time.getMinute();
        final int seconds = minutes * 60 + time.getSecond();
        final int sunSet = this.plugin.config.getInt("realTimeCycle.sunset");
        final int sunRise = this.plugin.config.getInt("realTimeCycle.sunrise");

        final long value = this.mapToRealtime(seconds,
                (sunRise/60.0D),
                (sunSet/60.0D));

        this.plugin.mainWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        this.plugin.mainWorld.setTime(value);
        this.plugin.mainWorld.setDifficulty(difficultyCheck(value, (sunSet/60.0D)));
    }

    public Difficulty difficultyCheck(final long value, final double sunSet){
        if (value/1000.0D < sunSet) {
            return Difficulty.HARD;
        } else {
            return Difficulty.NORMAL;
        }
    }

    public long mapToRealtime(final long seconds, final double sunRise, final double sunSet) {
        final long sunRiseS = (long) (sunRise * 3600);
        final long sunSetS  = (long) (sunSet * 3600);

        long mapped;

        if(seconds < sunRiseS) {
            mapped = map(seconds, 0, sunRiseS, -6000, -1000);
        } else if (seconds < sunSetS) {
            mapped = map(seconds, sunRiseS, sunSetS, -1000, 12000);
        } else {
            mapped = map(seconds, sunSetS, 86400, 12000, 18000);
        }

        return (mapped % 24000) + (mapped < 0 ? 24000 : 0);
    }

    public long map(final long x, final long in_min, final long in_max, final long out_min, final long out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}


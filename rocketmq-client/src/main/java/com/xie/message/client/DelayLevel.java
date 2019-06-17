package com.xie.message.client;

/**
 * 延时常量
 */
public enum DelayLevel {
    // "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
    ONE_SECOND(1, 1000 * 1),
    FIVE_SECOND(2, 1000 * 5),
    TEN_SECOND(3, 1000 * 10),
    THIRTY_SECOND(4, 1000 * 30),

    ONE_MINUTE(5, 1000 * 60),
    TWO_MINUTE(6, 1000 * 60 * 1),
    THREE_MINUTE(7, 1000 * 60 * 3),
    FOUR_MINUTE(8, 1000 * 60 * 4),
    FIVE_MINUTE(9, 1000 * 60 * 5),
    SIX_MINUTE(10, 1000 * 60 * 6),
    SEVEN_MINUTE(11, 1000 * 60 * 7),
    EIGHT_MINUTE(12, 1000 * 60 * 8),

    NINE_MINUTE(13, 1000 * 60 * 9),
    TEN_MINUTE(14, 1000 * 60 * 10),
    TWENTY_MINUTE(15, 1000 * 60 * 20),
    THIRTY_MINUTE(16, 1000 * 60 * 30),

    ONE_HOUR(17, 1000 * 60 * 60),
    TWO_OUR(18, 1000 * 60 * 60 * 2);

    private int levelValue;
    /** 此Level表示的延时时长(毫秒单位) */
    private long levelTimeInMillis;

    private DelayLevel(int levelValue, long levelTimeInMillis) {
        this.levelValue = levelValue;
        this.levelTimeInMillis = levelTimeInMillis;
    }

    public int getLevelValue() {
        return levelValue;
    }

    public long getLevelTimeInMillis() {
        return levelTimeInMillis;
    }

    public static DelayLevel getLevelByNumber(int level) {
        DelayLevel[] values = DelayLevel.values();
        for (DelayLevel l : values) {
            if (level == l.getLevelValue()) {
                return l;
            }
        }
        return null;
    }

    public static int getLevelSize() {
        return DelayLevel.values().length;
    }
}

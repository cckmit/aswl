package com.aswl.as.metadata.common;

/**
 * 全局常量
 */
public class Globals {

    public interface WebSocketConsts {
        String TOPIC = "/topic/greetings";

        String ENDPOINT = "/as-push";

        String APP_PREFIX = "/app";
    }

    /**
     * 告警方式
     */
    public interface AlarmMethodConsts {
        /** 弹窗 */
        String POPUP_WINDOW = "PopupWindow";
        /** 语音 */
        String VOICE = "Voice";
        /** 短信 */
        String MESSAGE = "Message";
        /** 声光 */
        String SOUND_LIGHT = "SoundLight";
        /** 通知 */
        String NOTICE = "Notice";
    }

    /**
     * 事件告警类型分隔符
     */
    public interface EventAlarmSeparateConsts {
        /** 逗号 */
        String COMMA = ",";
        /** 分号 */
        String SEMICOLON = ";";
    }

    public interface AlarmLevelConsts {
        /** 1级 */
        Integer level1 = 1;
        /** 2级 */
        Integer level2 = 2;
        /** 3级 */
        Integer level3 = 3;
    }
    
    
    public interface DeviceIsOnlineConsts {
        /** 在线 */
        Integer ON_LINE = 1;
        /** 离线 */
        Integer OFF_LINE = 0;
        /** 掉电离线 */
        Integer IONIZATION_LINE = 2;
        /** 异常离线 */
        Integer ABNORMAL_OFFLINE = 3;
    }
}

package com.aswl.as.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 码流类型
 *
 * @author dingfei
 * @date 2019-12-20 15:46
 * @Version V1
 */
@Getter
@AllArgsConstructor
public enum StreamType {
    HK("HK"),
    DH("DH"),
    XM("XM");

    /**
     * 类型
     */
    private String type;

}

package com.aswl.as.metadata.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 移动端推送Data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushMobilePhoneDataVO {

    private String alert;
    private String title;
    private Map<String, String> extras;
}

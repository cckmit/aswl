package com.aswl.as.metadata.websocket.push;

import java.io.Serializable;

/**
 *
 */
public class PushDeviceNetwork implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private Long gatherTime;
    private Boolean isOnline;

    public PushDeviceNetwork(String id, Long gatherTime, Boolean isOnline) {
        this.id = id;
        this.gatherTime = gatherTime;
        this.isOnline = isOnline;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getGatherTime() {
        return gatherTime;
    }
    public void setGatherTime(Long gatherTime) {
        this.gatherTime = gatherTime;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }
    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }
}

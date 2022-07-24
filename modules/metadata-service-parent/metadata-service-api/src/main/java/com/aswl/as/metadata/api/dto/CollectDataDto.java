package com.aswl.as.metadata.api.dto;

public class CollectDataDto {

    private String statusNames;
    private String alarmTypes;
    private String alarmTypesDes;
    private String alarmLevels;
    private String alarmDates;
    private String uEventIds;
    private String promptTypes;
    private String changeTypes;
    private String changeTypesDes;
    private String regionName;

    public CollectDataDto() {
    }

    public CollectDataDto(String statusNames, String alarmTypes, String alarmTypesDes, String alarmLevels, String alarmDates, String uEventIds, String promptTypes, String changeTypes, String changeTypesDes, String regionName) {
        this.statusNames = statusNames;
        this.alarmTypes = alarmTypes;
        this.alarmTypesDes = alarmTypesDes;
        this.alarmLevels = alarmLevels;
        this.alarmDates = alarmDates;
        this.uEventIds = uEventIds;
        this.promptTypes = promptTypes;
        this.changeTypes = changeTypes;
        this.changeTypesDes = changeTypesDes;
        this.regionName = regionName;
    }

    public String getStatusNames() {
        return statusNames;
    }

    public void setStatusNames(String statusNames) {
        this.statusNames = statusNames;
    }

    public String getAlarmTypes() {
        return alarmTypes;
    }

    public void setAlarmTypes(String alarmTypes) {
        this.alarmTypes = alarmTypes;
    }

    public String getAlarmTypesDes() {
        return alarmTypesDes;
    }

    public void setAlarmTypesDes(String alarmTypesDes) {
        this.alarmTypesDes = alarmTypesDes;
    }

    public String getAlarmLevels() {
        return alarmLevels;
    }

    public void setAlarmLevels(String alarmLevels) {
        this.alarmLevels = alarmLevels;
    }

    public String getAlarmDates() {
        return alarmDates;
    }

    public void setAlarmDates(String alarmDates) {
        this.alarmDates = alarmDates;
    }

    public String getuEventIds() {
        return uEventIds;
    }

    public void setuEventIds(String uEventIds) {
        this.uEventIds = uEventIds;
    }

    public String getPromptTypes() {
        return promptTypes;
    }

    public void setPromptTypes(String promptTypes) {
        this.promptTypes = promptTypes;
    }

    public String getChangeTypes() {
        return changeTypes;
    }

    public void setChangeTypes(String changeTypes) {
        this.changeTypes = changeTypes;
    }

    public String getChangeTypesDes() {
        return changeTypesDes;
    }

    public void setChangeTypesDes(String changeTypesDes) {
        this.changeTypesDes = changeTypesDes;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}

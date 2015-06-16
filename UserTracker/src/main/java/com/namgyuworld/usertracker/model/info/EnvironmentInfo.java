package com.namgyuworld.usertracker.model.info;

/**
 * Created by danielpark on 6/16/15.
 */
public class EnvironmentInfo {

    private String country;
    private String language;
    private String networkProvider;
    private String currentTime;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getNetworkProvider() {
        return networkProvider;
    }

    public void setNetworkProvider(String networkProvider) {
        this.networkProvider = networkProvider;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("country: ");
        stringBuilder.append(getCountry());
        stringBuilder.append("\n");
        stringBuilder.append("language: ");
        stringBuilder.append(getLanguage());
        stringBuilder.append("\n");
        stringBuilder.append("networkProvider: ");
        stringBuilder.append(getNetworkProvider());
        stringBuilder.append("\n");
        stringBuilder.append("currentTime: ");
        stringBuilder.append(getCurrentTime());
        return stringBuilder.toString();
    }
}

package com.namgyuworld.usertracker.model.info;

/**
 * Created by danielpark on 6/16/15.
 */
public class DeviceInfo {

    private String deviceName;
    private String deviceManufacture;
    private String osVersion;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceManufacture() {
        return deviceManufacture;
    }

    public void setDeviceManufacture(String deviceManufacture) {
        this.deviceManufacture = deviceManufacture;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("deviceName: ");
        stringBuilder.append(getDeviceName());
        stringBuilder.append("\n");
        stringBuilder.append("deviceManufacture: ");
        stringBuilder.append(getDeviceManufacture());
        stringBuilder.append("\n");
        stringBuilder.append("osVersion: ");
        stringBuilder.append(getOsVersion());
        return stringBuilder.toString();
    }
}

package org.unyde.mapintegrationlib.cluster.model;

import android.bluetooth.le.ScanResult;
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.List_avg;

public class ClustorBeacon {

    public int rssi;
    ScanResult result;
    byte[] uidServiceData;
    public long timestamp = System.currentTimeMillis();
    public long clusterFirstSeentimestamp = System.currentTimeMillis();
    public List_avg my_avg_val;
    public String uid;
    public String nodeId;
    String deviceAddress;
    public int enter=0;
    // Used to remove devices from the listview when they haven't been seen in a while.
    public long lastSeenTimestamp = System.currentTimeMillis();
    long firstSeenTImeStamp;
    public Boolean isFirstTimeCaptured=false;
    long beaconLastSeenTimestamp=0;
    public Boolean getFirstTimeCaptured() {
        return isFirstTimeCaptured;
    }



    public void setFirstTimeCaptured(Boolean firstTimeCaptured) {
        isFirstTimeCaptured = firstTimeCaptured;
    }



    public ClustorBeacon(byte[] uidServiceData, ScanResult result) {
        this.result = result;
        this.uidServiceData = uidServiceData;
    }
    public ClustorBeacon(String deviceAddress, int rssi) {
        this.deviceAddress = deviceAddress;
        this.rssi = rssi;
        my_avg_val = new List_avg();
    }


    public ClustorBeacon(String uid, long lastSeenTimestamp, long firstSeenTImeStamp, Boolean isFirstTimeCaptured, String nodeId) {
        this.uid=uid;
        this.beaconLastSeenTimestamp=lastSeenTimestamp;
        this.firstSeenTImeStamp=firstSeenTImeStamp;
        this.isFirstTimeCaptured=isFirstTimeCaptured;
        this.nodeId=nodeId;
    }

    public ScanResult getResult() {
        return result;
    }

    public void setResult(ScanResult result) {
        this.result = result;
    }

    public int getRssi() {

        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public byte[] getUidServiceData() {
        return uidServiceData;
    }

    public void setUidServiceData(byte[] uidServiceData) {
        this.uidServiceData = uidServiceData;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List_avg getMy_avg_val() {
        return my_avg_val;
    }

    public void setMy_avg_val(List_avg my_avg_val) {
        this.my_avg_val = my_avg_val;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public long getFirstSeenTImeStamp() {
        return firstSeenTImeStamp;
    }

    public void setFirstSeenTImeStamp(long firstSeenTImeStamp) {
        this.firstSeenTImeStamp = firstSeenTImeStamp;
    }

    public long getBeaconLastSeenTimestamp() {
        return beaconLastSeenTimestamp;
    }

    public void setBeaconLastSeenTimestamp(long beaconLastSeenTimestamp) {
        this.beaconLastSeenTimestamp = beaconLastSeenTimestamp;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
}

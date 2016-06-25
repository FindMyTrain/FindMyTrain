package iitb.mtp.aurobindo.findmytrain;


import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import libsvm.svm_model;

class Data {
    public String operator;
    public int cellID, RSSI;
    public double lat, lon;
    public long timeStamp;

    public Data(String operator, int cellID, int RSSI, double lat, double lon, long timeStamp) {
        this.operator = operator;
        this.cellID = cellID;
        this.RSSI = RSSI;
        this.lat = lat;
        this.lon = lon;
        this.timeStamp = timeStamp;
    }

    public static ArrayList<Data> location = new ArrayList<>();
}

class MotionData {
    public double x;
    public double y;
    public double z;

    public MotionData(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static CopyOnWriteArrayList<MotionData> motion = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<MotionData> reorientedMotion = new CopyOnWriteArrayList<>();
}

class FinalData {
    double lat, lon, dist;
    long timeStamp;

    public FinalData(double lat, double lon, double dist, long timeStamp) {
        this.lat = lat;
        this.lon = lon;
        this.dist = dist;
        this.timeStamp = timeStamp;
    }

    public static ArrayList<FinalData> finalData = new ArrayList<>();
}

class StationMap    {
    int routeID, stnID;
    String stnName;
    double lat, lon, distOrigin, nxtStnDist;

    public StationMap(int routeID, int stnID, String stnName, double lat,
                      double lon, double nxtStnDist, double distOrigin) {
        this.routeID = routeID;
        this.stnID = stnID;
        this.stnName = stnName;
        this.lat = lat;
        this.lon = lon;
        this.distOrigin = distOrigin;
        this.nxtStnDist = nxtStnDist;
    }

    public static ArrayList<StationMap> mapping = new ArrayList<>();
    public static double[][] terminalStnGPS = {{18.935147,72.827214,19.991363,72.743594},
                                               {18.941591,72.835881,19.235318,73.13092},
                                               {18.941591,72.835881,18.990857,73.120988}};
    public static int[] numStations = {36,26,26};
    public static double[] routeLength = {122538.654300734, 51004.8662463721, 46542.2721501698};
}

class SvmModel  {
    static svm_model model;
}

class CurrentGPS {
    public static double GPSLat, GPSLong;
}
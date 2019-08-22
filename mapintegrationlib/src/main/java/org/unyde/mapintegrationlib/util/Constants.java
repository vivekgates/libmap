// Copyright 2015 Google Inc. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.unyde.mapintegrationlib.util;

import android.bluetooth.BluetoothManager;
import android.os.ParcelUuid;
import org.unyde.mapintegrationlib.InternalNavigation.model.Object3DData;
import org.unyde.mapintegrationlib.cluster.model.ClustorBeacon;
import org.unyde.mapintegrationlib.cluster.model.Device;
import org.unyde.mapintegrationlib.cluster.model.NearBeacon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Constants {




  public static String APP_DATE_TIME="2019-06-07 16:13:25";



  public static BluetoothManager mBluetoothManager;




 // public static ArrayList<FloorCountPojo> distict_floor_store_count_array = new ArrayList<FloorCountPojo>();

  public static Map<String, Device> deviceToBeaconMap = new ConcurrentHashMap<>();
  public static Map<String, Device> nearBeaconMap = new ConcurrentHashMap<>();
  public static Map<String, NearBeacon> storeBeaconMap = new ConcurrentHashMap<>();
  public static Map<String, ClustorBeacon> clustorBeaconMap = new ConcurrentHashMap<>();
  public  static Map<String, String> beaconCommandMap = new ConcurrentHashMap<>();




  public  static List<Object3DData> floor_model=new ArrayList<Object3DData>();
  public  static Object3DData floor_bg=null;
  public  static Object3DData dbuilding=null;
  public  static Object3DData compass=null;
  public  static Object3DData i_m_here_marker=null;
  public  static Object3DData compass_obj=null;
  public static HashMap<String, byte[]> char_byte_data=new HashMap<String, byte[]>();
  public static Object3DData popupbg = null;

 public static final int TAKE_STRAIGHT = 0;
 public static final int TAKE_SLIGHT_LEFT = 1;
 public static final int TURN_LEFT = 2;
 public static final int TAKE_SLIGHT_RIGHT = 3;
 public static final int TURN_RIGHT = 4;
 public static final int HEAD_TOWARDS_SOUTH = 5;
 public static final int HEAD_TOWARDS_SOUTH_EAST = 6;
 public static final int HEAD_TOWARDS_EAST = 7;
 public static final int HEAD_TOWARDS_NORTH_EAST = 8;
 public static final int HEAD_TOWARDS_NORTH = 9;
 public static final int HEAD_TOWARDS_NORTH_WEST = 10;
 public static final int HEAD_TOWARDS_WEST = 11;
 public static final int HEAD_TOWARDS_SOUTH_WEST = 12;
 public static final int DESTINATION = 13;
 public static final int LIFT = 14;

 public static final int NOTIFICATION_ID = 100;
 public static final int NOTIFICATION_ID_BIG_IMAGE = 101;


}

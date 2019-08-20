package org.unyde.mapintegrationlib.permission;

import java.util.List;

public interface PermissionListener {

  void onPermissionGranted();

  void onPermissionDenied(List<String> deniedPermissions);

}

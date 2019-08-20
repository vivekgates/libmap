package org.unyde.mapintegrationlib.permission;

import org.unyde.mapintegrationlib.permission.util.ObjectUtils;

import java.util.List;

public class TedPermissionResult {

  private boolean granted;
  private List<String> deniedPermissions;

  public TedPermissionResult(List<String> deniedPermissions) {
    this.granted = ObjectUtils.isEmpty(deniedPermissions);
    this.deniedPermissions = deniedPermissions;
  }

  public boolean isGranted() {
    return granted;
  }

  public List<String> getDeniedPermissions() {
    return deniedPermissions;
  }
}

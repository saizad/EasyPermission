package sa.zad.easypermission;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Sa-ZAD on 2017-11-05.
 */

public interface AppPermission {

  void onRequestingPermission(Activity activity);

  boolean onForceRequestingPermission(Activity activity);

  AppPermission onPermissionRequested(Activity activity);

  int getPermissionStatus(Activity activity);

  int getPermissionCode();

  AppPermission getPermission(String permission);

  boolean isPermission(String checkPermission);

  String[] getPermissions();

  boolean isPermissionGranted(Context context);

  int getMaxRequest();

  int getRequestRemaining();

  boolean isRequestPermissionMaxOut();

  int getFailedRequestCount();
}

package sa.zad.easypermission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;

/**
 * Created by Sa-ZAD on 2017-11-05.
 */

public class PermissionUtil {

  public static final int GRANTED = 0;
  public static final int DENIED = 1;
  public static final int BLOCKED = 2;

  public static boolean isPermGranted(Context context, String permsCheck) {
    return ActivityCompat.checkSelfPermission(context, permsCheck)
        == PackageManager.PERMISSION_GRANTED;
  }

  public static int getPermissionStatus(Activity activity, String androidPermissionName, AppPermission appPermission) {
    if (!isPermGranted(activity, androidPermissionName)) {
      if (appPermission.getFailedRequestCount() > 0 && !ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName)) {
        return BLOCKED;
      }
      return DENIED;
    }
    return GRANTED;
  }
}

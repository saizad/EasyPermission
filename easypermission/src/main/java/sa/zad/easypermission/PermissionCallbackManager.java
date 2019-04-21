package sa.zad.easypermission;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * Created by Sa-ZAD on 2017-10-31.
 */

public interface PermissionCallbackManager {
  void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions,
                                  @NonNull int[] grantResults);
}

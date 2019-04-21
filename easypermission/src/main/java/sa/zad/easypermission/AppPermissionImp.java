package sa.zad.easypermission;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static sa.zad.easypermission.PermissionUtil.BLOCKED;
import static sa.zad.easypermission.PermissionUtil.isPermGranted;


public class AppPermissionImp implements AppPermission {

  private final int mPermissionCode;
  private final String[] mPermissions;
  private final int mMaxRequest;
  private SharedPreferences mSharedPreferences;

  public AppPermissionImp(int permissionCode,
                          @NonNull String[] perms, int maxRequest, @NonNull SharedPreferences sharedPreferences) {
    mPermissionCode = permissionCode;
    mPermissions = perms;
    mMaxRequest = maxRequest;
    mSharedPreferences = sharedPreferences;
  }

  @Override
  public AppPermission onPermissionRequested(Activity activity) {
    if (isPermissionGranted(activity)) {
      reset();
    }
    return this;
  }

  @Override
  public int getPermissionStatus(Activity activity) {
    return PermissionUtil.getPermissionStatus(activity, getPermissions()[0], this);
  }

  @Override
  public int getPermissionCode() {
    return mPermissionCode;
  }

  @Nullable
  @Override
  public AppPermission getPermission(@NonNull String permission){
    if(isPermission(permission)){
      return this;
    }
    return null;
  }

  @Override
  public boolean isPermission(@NonNull String checkPermission) {
    for (String permission : mPermissions) {
      if (permission.equalsIgnoreCase(checkPermission)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String[] getPermissions() {
    return mPermissions;
  }

  @Override
  public void onRequestingPermission(Activity activity) {
    incrementRequest();
  }

  @Override
  public boolean onForceRequestingPermission(Activity activity) {
    return !isPermissionGranted(activity);
  }

  public boolean isPermissionGranted(Context context) {
    return isPermGranted(context, getPermissions()[0]);
  }

  public int getMaxRequest() {
    return mMaxRequest;
  }

  public int getRequestRemaining() {
    return mMaxRequest - getFailedRequestCount();
  }

  public int getFailedRequestCount() {
    return mSharedPreferences.getInt(getPermissions()[0], 0);
  }

  private boolean canRequestPermission(Activity activity) {
    return PermissionUtil.getPermissionStatus(activity, getPermissions()[0], this) != BLOCKED
        || getFailedRequestCount() == 0;
  }

  public boolean isRequestPermissionMaxOut() {
    return mSharedPreferences.getInt(getPermissions()[0], 0) >= mMaxRequest;
  }

  private void incrementRequest() {
    mSharedPreferences.edit()
        .putInt(getPermissions()[0], getFailedRequestCount() + 1)
        .apply();
  }

  private void reset() {
    mSharedPreferences.edit().putInt(getPermissions()[0], 0).apply();
  }
}

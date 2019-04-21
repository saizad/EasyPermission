package sa.zad.easypermission;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AppPermissionRequest {

  public static final int ALL_READY_GRANTED = 1;
  public static final int DENIED = 2;
  public static final int DENIED_RATIONAL = 3;
  public static final int REQUEST_CANCELED = 4;
  public static final int REQUEST_BLOCKED = 5;
  public static final int GRANTED = 6;

  private final AppPermission appPermission;
  private final int requestStatus;

  public AppPermissionRequest(AppPermission appPermission, @PermissionRequestStatus int requestStatus) {
    this.appPermission = appPermission;
    this.requestStatus = requestStatus;
  }

  public AppPermission getAppPermission() {
    return appPermission;
  }

  public int getRequestStatus() {
    return requestStatus;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @IntDef({
      ALL_READY_GRANTED, DENIED, DENIED_RATIONAL, REQUEST_BLOCKED, REQUEST_CANCELED, GRANTED
  })
  public @interface PermissionRequestStatus {}
}

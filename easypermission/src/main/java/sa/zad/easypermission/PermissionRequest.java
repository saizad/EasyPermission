package sa.zad.easypermission;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class PermissionRequest
    implements Function<AppPermission, ObservableSource<AppPermissionRequest>> {

  private final Object parent;
  private Observable<AppPermission> requestResponse;

  public PermissionRequest(Object parent) {
    this.parent = parent;
  }

  public PermissionRequest(Object parent, Observable<AppPermission> requestResponse) {
    this.parent = parent;
    this.requestResponse = requestResponse;
  }

  public Object getParent() {
    return parent;
  }

  void setRequestResponse(Observable<AppPermission> requestResponse) {
    this.requestResponse = requestResponse;
  }

  public static Activity getActivity(Object parent) {
    final Activity activity;
    if (parent instanceof Activity) {
      activity = ((Activity) parent);
    } else {
      activity = ((Fragment) parent).getActivity();
    }
    return activity;
  }

  @Override
  public final ObservableSource<AppPermissionRequest> apply(AppPermission appPermission) {
    /*if(appPermission.getPermissionStatus(getActivity(parent)) == PermissionUtil.BLOCKED){
      return Observable.just(new AppPermissionRequest(appPermission, AppPermissionRequest.REQUEST_BLOCKED));
    }*/
    Log.d("dasfasdfApply", appPermission.getPermissionCode() + "");

    if(appPermission.isPermissionGranted(getActivity(parent))){
      return Observable.just(new AppPermissionRequest(appPermission, AppPermissionRequest.ALL_READY_GRANTED));
    }
    if(!onRequesting(appPermission)){
      return Observable.just(new AppPermissionRequest(appPermission, AppPermissionRequest.REQUEST_CANCELED));
    }
    return Observable.just(appPermission).flatMap(permission -> {
          if (showRational(permission)) {
            return showRationalDialog(permission.getPermissions()[0]);
          }
          return Observable.just(true);
        })
        .flatMap(request -> {
          if (!request){
            return Observable.just(new AppPermissionRequest(appPermission,AppPermissionRequest.DENIED_RATIONAL));
          }
          return requestPerm(parent, appPermission).map(perm -> {
            if(!perm.isPermissionGranted(getActivity(parent)))
              return new AppPermissionRequest(appPermission, AppPermissionRequest.DENIED);
            return new AppPermissionRequest(appPermission, AppPermissionRequest.GRANTED);
          });
        });
  }

  protected boolean onRequesting(AppPermission appPermission) {
    return !appPermission.isRequestPermissionMaxOut();
  }

  protected boolean showRational(AppPermission appPermission) {
    return appPermission.getFailedRequestCount() >= 1;
  }

  private Observable<Boolean> showRationalDialog(String rationalMessage) {
    return LibUtils.showDialog(getActivity(parent), rationalMessage);
  }

  public Observable<AppPermission> requestPerm(final @NonNull Object object,
                                               AppPermission appPermission) {
    appPermission.onRequestingPermission(getActivity(object));
    return requestPerm(object, appPermission.getPermissions());
  }

  public Observable<AppPermission> requestPerm(final @NonNull Object object,
                                               String... permissions) {
    if (permissions.length > 0) {
      if (object instanceof Activity) {
        ActivityCompat.requestPermissions(((Activity) object), permissions,
            PermissionManager.PERM_REQUEST_CODE);
      } else {
        Fragment fragment = ((Fragment) object);
        fragment.requestPermissions(permissions, PermissionManager.PERM_REQUEST_CODE);
      }
    }
    return requestResponse.take(1);
  }
}
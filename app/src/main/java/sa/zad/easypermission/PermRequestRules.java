package sa.zad.easypermission;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class PermRequestRules extends PermissionRequest {

  public PermRequestRules(Object parent) {
    super(parent);
  }

  @Override
  protected boolean showRational(AppPermission appPermission) {
    if(appPermission.getPermissionCode() == PermModule.LOCATION_PERMISSION_REQUEST_CODE){
      return appPermission.getFailedRequestCount() >= 1;
    }else if(appPermission.getPermissionCode() == PermModule.CAMERA_PERMISSION_REQUEST_CODE){
      return appPermission.getFailedRequestCount() >= 2;
    }else if(appPermission.getPermissionCode() == PermModule.STORAGE_PERMISSION_REQUEST_CODE){
      return appPermission.getFailedRequestCount() >= 3;
    }
    return super.showRational(appPermission);
  }

  @Override
  protected boolean onRequesting(AppPermission appPermission) {
    if(appPermission.getPermissionCode() == PermModule.CAMERA_PERMISSION_REQUEST_CODE){
      //keep requesting
      return true;
    }
    return true;
  }

  @Override
  protected Observable<Boolean> showRationalDialog(AppPermission appPermission) {
    if(appPermission.getPermissionCode() == PermModule.LOCATION_PERMISSION_REQUEST_CODE) {
      PublishSubject<Boolean> callbackSubject = PublishSubject.create();
      final Activity activity = getActivity(getParent());
      final TextView textView = new TextView(activity);
      textView.setText("Custom Rational View");
      textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
      textView.setTextSize(40);
      LibUtils.defaultAlertDialogBuilder(activity, callbackSubject::onNext).setView(textView).create().show();
      return callbackSubject.take(1);
    }
    return super.showRationalDialog(appPermission);
  }
}

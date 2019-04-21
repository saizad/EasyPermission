package sa.zad.easypermission;

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
    return super.onRequesting(appPermission);
  }
}

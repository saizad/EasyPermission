package sa.zad.easypermission;

import android.Manifest;
import android.content.SharedPreferences;

public class PermModule {

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 2;
    public static final int STORAGE_PERMISSION_REQUEST_CODE = 3;
    private static final int MAX_REQUEST = 2;

    private final PermissionManager permissionManager;
    private final SharedPreferences sharedPreferences;

    public PermModule(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        permissionManager = providePermissionManager(locationPermission(), cameraPermission(), storagePermission());

    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    PermissionManager providePermissionManager(AppPermission locationAppPermission, AppPermission cameraAppPermission, AppPermission storagePermission) {
        return new PermissionManager(locationAppPermission, cameraAppPermission, storagePermission);
    }

    public AppPermission locationPermission() {
        return new AppPermissionImp(LOCATION_PERMISSION_REQUEST_CODE, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        }, MAX_REQUEST, sharedPreferences);
    }

    public AppPermission cameraPermission() {
        return new AppPermissionImp(CAMERA_PERMISSION_REQUEST_CODE, new String[]{
                Manifest.permission.CAMERA
        }, MAX_REQUEST, sharedPreferences);
    }

    public AppPermission storagePermission() {
        return new AppPermissionImp(STORAGE_PERMISSION_REQUEST_CODE, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
        }, MAX_REQUEST, sharedPreferences);
    }
}

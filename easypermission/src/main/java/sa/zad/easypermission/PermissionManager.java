package sa.zad.easypermission;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sa-ZAD on 2017-10-31.
 */

public class PermissionManager implements PermissionCallbackManager {

    public static final int PERM_REQUEST_CODE = 3000;
    private SparseArray<AppPermission> mAppPermissions = new SparseArray<>();
    private PublishSubject<AppPermission> permissionPublishSubject = PublishSubject.create();

    public PermissionManager(AppPermission... appPermissions) {
        for (AppPermission appPermission : appPermissions) {
            mAppPermissions.put(appPermission.getPermissionCode(), appPermission);
        }
    }

    @NonNull
    public AppPermission getAppPermission(int permissionCode) {
        return mAppPermissions.get(permissionCode);
    }

    @NonNull
    public Observable<AppPermission> requestPermission(Object parent, int... permissionCodes) {
        return requestPermission(new PermissionRequest(parent), permissionCodes);
    }

    @NonNull
    public Observable<AppPermission> requestPermission(final @NonNull PermissionRequest permissionRequest, int... permissionCodes) {
        permissionRequest.setRequestResponse(permissionPublishSubject);
        return Observable.just(Arrays.asList(getAppPermissions(permissionCodes)))
                .flatMapIterable(permCodes -> permCodes)
                .concatMap(permissionRequest)
                .doOnNext(appPermissionRequest -> {
                    Log.d("dasfasdf", appPermissionRequest.getAppPermission().getPermissionCode() + "");
                    Log.d("dasfasdfStatus", appPermissionRequest.getAppPermission().getPermissionStatus(PermissionRequest.getActivity(permissionRequest.getParent())) + "");
                })
                .map(AppPermissionRequest::getAppPermission)
                .toList()
                .toObservable()
                .flatMapIterable(appPermissions -> appPermissions);
    }

    private AppPermission[] getAppPermissions(
            int... permissionCodes) {
        List<AppPermission> list = new ArrayList<>();
        for (int codes : permissionCodes) {
            AppPermission appPerm = getAppPermission(codes);
            list.add(appPerm);
        }
        return list.toArray(new AppPermission[list.size()]);
    }

    @Override
    public void onRequestPermissionsResult(Activity activity, int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int i = 0; i < mAppPermissions.size(); i++) {
            AppPermission appPermission = mAppPermissions.valueAt(i).onPermissionRequested(activity);
            for (String permission : permissions) {
                if (appPermission.isPermission(permission)) {
                    permissionPublishSubject.onNext(appPermission);
                    break;
                }
            }
        }
    }
}

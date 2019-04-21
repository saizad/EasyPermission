package sa.zad.easypermission;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class LibUtils {
    public static Observable<Boolean> showDialog(Activity activity, String message) {
        PublishSubject<Boolean> callbackSubject = PublishSubject.create();
        defaultAlertDialogBuilder(activity, callbackSubject::onNext).setMessage(message).show();
        return callbackSubject.take(1);
    }

    public static AlertDialog.Builder defaultAlertDialogBuilder(Activity activity, CallBack<Boolean> callBack) {
        return new AlertDialog.Builder(activity)
                .setPositiveButton("OK", (dialog, which) -> callBack.call(true))
                .setNegativeButton("Cancel", (dialog, which) -> callBack.call(false))
                .setOnCancelListener(dialog -> callBack.call(false));
    }

    public interface CallBack<T> {
        void call(T t) ;
    }
}

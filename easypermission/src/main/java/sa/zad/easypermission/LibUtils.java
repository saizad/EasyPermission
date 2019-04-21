package sa.zad.easypermission;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class LibUtils {
    public static Observable<Boolean> showDialog(Activity activity, String message) {
        PublishSubject<Boolean> callbackSubject = PublishSubject.create();
        new AlertDialog.Builder(activity).setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> callbackSubject.onNext(true))
                .setNegativeButton("Cancel", (dialog, which) -> callbackSubject.onNext(false))
                .setOnCancelListener(dialog -> callbackSubject.onNext(false))
                .create()
                .show();
        return callbackSubject.take(1);
    }
}

package sa.zad.easypermission

import android.annotation.SuppressLint
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var permModule: PermModule? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permModule = PermModule(PreferenceManager.getDefaultSharedPreferences(application))

        storage_perms.setOnClickListener {
            req(PermRequestRules(this), PermModule.STORAGE_PERMISSION_REQUEST_CODE)
        }

        camera_perms.setOnClickListener {
            req(PermRequestRules(this), PermModule.CAMERA_PERMISSION_REQUEST_CODE)
        }

        location_perms.setOnClickListener {
            req(PermRequestRules(this), PermModule.LOCATION_PERMISSION_REQUEST_CODE)
        }

        request_all.setOnClickListener {
            req(PermRequestRules(this), PermModule.LOCATION_PERMISSION_REQUEST_CODE,
                PermModule.CAMERA_PERMISSION_REQUEST_CODE, PermModule.STORAGE_PERMISSION_REQUEST_CODE)
        }
    }

    private fun req(request: PermissionRequest, vararg permCode: Int){
        permModule?.permissionManager?.requestPermission(request, *permCode)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe ({
                Toast.makeText(this, it.appPermission.permissionCode.toString(), Toast.LENGTH_LONG).show()
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permModule?.permissionManager?.onRequestPermissionsResult(this,requestCode, permissions, grantResults )
    }
}

package framework.viewmodel

import android.app.Application

open class BaseDialogVM(application : Application) : BaseViewModel(application){

    var title: String? = null
    var message: String? = null
    var positiveButton: String? = null
    var negativeButton: String? = null
    var neutralButton: String? = null

}

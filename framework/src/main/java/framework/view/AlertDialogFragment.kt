package framework.view

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import framework.viewmodel.BaseDialogVM
import com.androidxtend.kit.R


class AlertDialogFragment : BaseDialogFragment<ViewDataBinding, BaseDialogVM>(), DialogInterface.OnClickListener {

    override val layoutId = R.layout.base_dialog
    override val viewModelClass = BaseDialogVM::class

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!)

        builder.setTitle(viewModel.title)
        builder.setMessage(viewModel.message)

        if (viewModel.positiveButton != null) {
            builder.setPositiveButton(arguments?.getInt(ARG_POSITIVE_BUTTON_RES)!!, this)
        }
        if (viewModel.negativeButton != null) {
            builder.setNegativeButton(arguments?.getInt(ARG_NEGATIVE_BUTTON_RES)!!, this)
        }
        if (viewModel.neutralButton != null) {
            builder.setNeutralButton(arguments?.getInt(ARG_NEUTRAL_BUTTON_RES)!!, this)
        }

        return builder.show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return null
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        dispatchResult(which)
    }

    class Builder : BaseDialogFragment.Builder() {
        override fun newInstance(): AlertDialogFragment {
            return AlertDialogFragment()
        }
    }
}
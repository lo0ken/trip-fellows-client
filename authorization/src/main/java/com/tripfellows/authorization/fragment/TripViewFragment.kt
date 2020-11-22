package com.tripfellows.authorization.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tripfellows.authorization.R

class TripViewFragment : Fragment() {

    companion object {
        private val DIGIT_KEY = "digit"
        fun newInstance(param: Int): TripViewFragment {
            val fragment = TripViewFragment()
            val bundle = Bundle()
            bundle.putString(DIGIT_KEY, param.toString())
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Добавляем layout к фрагменту
        return inflater.inflate(R.layout.trip_view_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button:Button = view.findViewById(R.id.buttonACS)
        button.setOnClickListener(View.OnClickListener {
            fun myFun(context: Context) {
                context.toast("approve")
            }
        })
    }


    fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}




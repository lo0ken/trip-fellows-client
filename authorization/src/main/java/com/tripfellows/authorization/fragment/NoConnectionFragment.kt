package com.tripfellows.authorization.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.tripfellows.authorization.R
import com.tripfellows.authorization.listeners.ConnectionRouter

class NoConnectionFragment : Fragment() {

    private lateinit var connectionRouter : ConnectionRouter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        connectionRouter = context as ConnectionRouter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.no_connection_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val reloadButton = view.findViewById<Button>(R.id.reload_button)

        reloadButton.setOnClickListener { connectionRouter.reload() }
    }
}

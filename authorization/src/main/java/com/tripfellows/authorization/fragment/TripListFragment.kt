import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tripfellows.authorization.R
import com.tripfellows.authorization.listeners.MainRouter
import com.tripfellows.authorization.triplist.TripListAdapter
import com.tripfellows.authorization.triplist.TripListService

class TripListFragment : Fragment() {

    private lateinit var fragmentAccessListener:MainRouter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentAccessListener = context as MainRouter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.trip_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        val adapter = TripListAdapter(
            TripListService.tripList(),
            fragmentAccessListener
        )
        recyclerView.adapter = adapter
    }
}
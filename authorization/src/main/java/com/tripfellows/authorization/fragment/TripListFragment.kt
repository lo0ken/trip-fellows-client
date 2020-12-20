import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tripfellows.authorization.R
import com.tripfellows.authorization.listeners.MainRouter
import com.tripfellows.authorization.model.Trip
import com.tripfellows.authorization.triplist.TripListAdapter
import com.tripfellows.authorization.viewmodel.TripListViewModel
import kotlinx.android.synthetic.main.trip_list_fragment.*
import java.util.*

class TripListFragment : Fragment() {

    private lateinit var mainRouter: MainRouter
    private lateinit var tripListViewModel: TripListViewModel

    private lateinit var adapter: TripListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainRouter = context as MainRouter
        adapter = TripListAdapter(Collections.emptyList(), mainRouter)
    }

    override fun onStart() {
        super.onStart()
        tripListViewModel.refresh()
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

        tripListViewModel = ViewModelProvider(activity!!, ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)).get(
            TripListViewModel::class.java)

        tripListViewModel.getTrips().observe(viewLifecycleOwner, TripsObserver())

        swipeRefresh.setOnRefreshListener {
                tripListViewModel.refresh()
                swipeRefresh.isRefreshing = false
        }
        swipeRefresh.setColorSchemeColors(Color.GRAY)
        recyclerView.adapter = adapter
    }

    inner class TripsObserver: Observer<List<Trip>> {
        override fun onChanged(trips: List<Trip>?) {
            if (trips != null) {
                adapter.setTrips(trips)
            }
        }
    }
}
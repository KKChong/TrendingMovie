package my.com.trendingmovies.ui.landing

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_landing.*
import kotlinx.android.synthetic.main.layout_error.*
import my.com.trendingmovies.R
import timber.log.Timber

@AndroidEntryPoint
class LandingFragment : Fragment(R.layout.fragment_landing) {

    private lateinit var movieAdapter: MovieAdapter

    private val landingViewModel by viewModels<LandingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter = MovieAdapter()

        rvMovie.layoutManager = LinearLayoutManager(requireContext())
        rvMovie.adapter = movieAdapter.withLoadStateFooter(
            MovieFooterStateAdapter {
                movieAdapter.retry()
            }
        )

        movieAdapter.addLoadStateListener { loadStates ->
            Timber.d(loadStates.toString())
            srl.isRefreshing = loadStates.source.refresh is LoadState.Loading
            llErrorContainer.isVisible = loadStates.source.refresh is LoadState.Error
            rvMovie.isVisible = !llErrorContainer.isVisible

            if (loadStates.source.refresh is LoadState.Error) {
                btnRetry.setOnClickListener {
                    movieAdapter.retry()
                }

                llErrorContainer.isVisible = loadStates.source.refresh is LoadState.Error

                val errorMessage = (loadStates.source.refresh as LoadState.Error).error.message
                tvErrorMessage.text = errorMessage
            }
        }

        srl.setOnRefreshListener {
            landingViewModel.onRefresh()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        landingViewModel.trendingMovies.observe(viewLifecycleOwner, Observer {
            movieAdapter.submitData(lifecycle, it)
        })
    }

}
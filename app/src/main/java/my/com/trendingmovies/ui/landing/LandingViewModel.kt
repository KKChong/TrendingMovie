package my.com.trendingmovies.ui.landing

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import my.com.trendingmovies.model.Movie
import my.com.trendingmovies.repository.MovieRepository
import timber.log.Timber

class LandingViewModel @ViewModelInject constructor(
    movieRepository: MovieRepository
) : ViewModel() {
    private val _trendingMovies = MutableLiveData<List<Movie>>()
    val trendingMovies: LiveData<List<Movie>>
        get() = _trendingMovies

    init {
        movieRepository.getTrendingMovie()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ movies -> _trendingMovies.value = movies.results },
                { t -> Timber.e(t) })
    }
}
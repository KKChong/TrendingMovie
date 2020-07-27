package my.com.trendingmovies.ui.landing

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import my.com.trendingmovies.model.Movie
import my.com.trendingmovies.model.Resource
import my.com.trendingmovies.repository.MovieRepository
import timber.log.Timber

class LandingViewModel @ViewModelInject constructor(
    movieRepository: MovieRepository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val _trendingMovies = MutableLiveData<Resource<List<Movie>>>()
    val trendingMovies: LiveData<Resource<List<Movie>>>
        get() = _trendingMovies

    init {
        compositeDisposable.add(
            movieRepository.getTrendingMovie()
                .doOnSubscribe { _trendingMovies.value = Resource.Loading(null) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { movies -> _trendingMovies.value = Resource.Success(movies.results) },
                    { t ->
                        Timber.e(t)
                        _trendingMovies.value = Resource.Error(t.message!!, null)
                    })
        )
    }

    override fun onCleared() {
        // To avoid memory leak
        compositeDisposable.clear()
        super.onCleared()
    }
}
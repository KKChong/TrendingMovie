package my.com.trendingmovies.ui.landing

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import io.reactivex.rxjava3.disposables.CompositeDisposable
import my.com.trendingmovies.model.Movie
import my.com.trendingmovies.repository.MovieRepository

class LandingViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val _trendingMovies = MutableLiveData<PagingData<Movie>>()
    val trendingMovies: LiveData<PagingData<Movie>>
        get() = _trendingMovies

    init {
        onGetTrendingMovie()
    }

    override fun onCleared() {
        // To avoid memory leak
        compositeDisposable.clear()
        super.onCleared()
    }

    fun onRefresh() {
        onGetTrendingMovie()
    }

    private fun onGetTrendingMovie() {
        compositeDisposable.add(
            movieRepository.getTrendingMovie()
                .cachedIn(viewModelScope)
                .subscribe {
                    _trendingMovies.value = it
                }
        )
    }
}
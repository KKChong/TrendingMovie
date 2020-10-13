package my.com.trendingmovies.ui.cast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import my.com.trendingmovies.model.Cast
import my.com.trendingmovies.model.Resource
import my.com.trendingmovies.repository.MovieRepository

class AboutViewModel @AssistedInject constructor(
    movieRepository: MovieRepository,
    @Assisted private val castId: Long
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val _cast = MutableLiveData<Resource<Cast>>()
    val cast: LiveData<Resource<Cast>>
        get() = _cast

    init {
        compositeDisposable.addAll(
            movieRepository.getCastDetails(castId)
                .doOnSubscribe { _cast.value = Resource.Loading() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response -> _cast.value = Resource.Success(response) },
                    { error -> _cast.value = Resource.Error(error.message!!) }
                )
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    @AssistedInject.Factory
    interface AssistedFactory {
        fun create(castId: Long): AboutViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            castId: Long
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(castId) as T
            }

        }
    }
}
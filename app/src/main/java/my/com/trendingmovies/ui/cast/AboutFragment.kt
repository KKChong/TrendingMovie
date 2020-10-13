package my.com.trendingmovies.ui.cast

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_about.*
import my.com.trendingmovies.R
import my.com.trendingmovies.model.Status
import my.com.trendingmovies.utils.toAge
import my.com.trendingmovies.utils.toStandardDateFormat
import javax.inject.Inject
import kotlin.properties.Delegates

private const val CAST_ID = "castId"

@AndroidEntryPoint
class AboutFragment : Fragment(R.layout.fragment_about) {
    private var castId by Delegates.notNull<Long>()

    @Inject
    lateinit var aboutViewModelFactory: AboutViewModel.AssistedFactory

    private val aboutViewModel: AboutViewModel by viewModels {
        AboutViewModel.provideFactory(
            aboutViewModelFactory,
            castId
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            castId = it.getLong(CAST_ID)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        srl.isEnabled = false

        aboutViewModel.cast.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { cast ->
                        tvBirthday.text =
                            if (!cast.birthday?.toStandardDateFormat()
                                    .isNullOrEmpty()
                            ) cast.birthday?.toStandardDateFormat() else getString(R.string.dash)

                        tvAge.text =
                            if (!cast.birthday?.toAge()
                                    .isNullOrEmpty()
                            ) cast.birthday?.toAge() else getString(R.string.dash)

                        tvPlaceOfBirth.text =
                            if (!cast.placeOfBirth.isNullOrEmpty()) cast.placeOfBirth else getString(
                                R.string.dash
                            )

                        tvDepartment.text =
                            if (!cast.department.isNullOrEmpty()) cast.department else getString(R.string.dash)

                        tvBiography.text =
                            if (!cast.biography.isNullOrEmpty()) cast.biography else getString(R.string.dash)

                        tvKnownAs.text =
                            if (!cast.knownAs.isNullOrEmpty()) cast.knownAs.joinToString(", ") else getString(
                                R.string.dash
                            )

                        cast.deathday?.let { deathday ->
                            tvDeathday.text = deathday.toStandardDateFormat()
                            llDeathDay.visibility = View.VISIBLE
                        }
                    }

                    showLoading(false)
                }
                Status.ERROR -> {
                    showLoading(false)
                    Snackbar.make(requireView(), it.message!!, Snackbar.LENGTH_SHORT).show()
                }
                Status.LOADING -> showLoading(true)
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        srl.isRefreshing = isLoading

        if (!isLoading) {
            nsv.visibility = View.VISIBLE
        }
    }

    companion object {
        fun newInstance(castId: Long) =
            AboutFragment().apply {
                arguments = Bundle().apply {
                    putLong(CAST_ID, castId)
                }
            }
    }
}
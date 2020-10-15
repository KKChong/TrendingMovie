package my.com.trendingmovies.ui.cast

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_photo.*
import my.com.trendingmovies.R
import my.com.trendingmovies.model.Status
import javax.inject.Inject
import kotlin.properties.Delegates

private const val ARG_CAST_ID = "castId"

@AndroidEntryPoint
class PhotoFragment : Fragment(R.layout.fragment_photo) {
    private var castId by Delegates.notNull<Long>()

    private lateinit var photoAdapter: PhotoAdapter

    @Inject
    lateinit var photoViewModelFactory: PhotoViewModel.AssistedFactory

    private val photoViewModel: PhotoViewModel by viewModels {
        PhotoViewModel.provideFactory(
            photoViewModelFactory,
            castId
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            castId = it.getLong(ARG_CAST_ID)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        srl.isEnabled = false

        photoAdapter = PhotoAdapter()

        rvPhoto.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = photoAdapter
        }

        photoViewModel.photo.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    photoAdapter.submitList(it.data)
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
    }

    companion object {
        @JvmStatic
        fun newInstance(castId: Long) =
            PhotoFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_CAST_ID, castId)
                }
            }
    }
}
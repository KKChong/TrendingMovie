package my.com.trendingmovies.ui.cast

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_cast.*
import kotlinx.android.synthetic.main.item_cast.view.*
import my.com.trendingmovies.R
import my.com.trendingmovies.glide.GlideApp

class CastFragment : Fragment(R.layout.fragment_cast) {

    private val args: CastFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cast = args.cast

        ibBack.setOnClickListener {
            it.findNavController().popBackStack()
        }

        GlideApp.with(ivCast)
            .load("https://image.tmdb.org/t/p/original${cast.profilePath}")
            .placeholder(R.drawable.ic_user_placeholder)
            .error(R.drawable.ic_user_placeholder)
            .transform(CircleCrop())
            .into(ivCast)

        tvName.text = cast.name

        viewPager2.adapter = ViewPagerAdapter(this, cast.id)
        viewPager2.offscreenPageLimit = 2

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.about)
                }
                else -> {
                    tab.text = getString(R.string.photo)
                }
            }
        }.attach()

    }
}

class ViewPagerAdapter(fragment: Fragment, private val castId: Long) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                AboutFragment.newInstance(castId)
            }
            else -> {
                PhotoFragment.newInstance(castId)
            }
        }
    }
}
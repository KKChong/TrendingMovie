package my.com.trendingmovies.ui.moviedetails

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.item_cast.view.*
import my.com.trendingmovies.R
import my.com.trendingmovies.glide.GlideApp
import my.com.trendingmovies.model.Cast
import my.com.trendingmovies.utils.toPx

class CastAdapter : ListAdapter<Cast, CastAdapter.CastViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val cast = getItem(position)
        holder.bind(cast)
    }

    class CastViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(cast: Cast) {
            itemView.apply {

                setOnClickListener {
                    it.findNavController().navigate(
                        MovieDetailsFragmentDirections.actionMovieDetailsFragmentToCastFragment(cast)
                    )
                }

                // Always show 2.5 items on the screen
                val paddingSize = 8f.toPx()
                val itemWidthSize = Resources.getSystem().displayMetrics.widthPixels / 2.75

                val params = RecyclerView.LayoutParams(
                    itemWidthSize.toInt(),
                    RecyclerView.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginStart = paddingSize.toInt()
                    marginEnd = paddingSize.toInt()
                }

                layoutParams = params

                GlideApp.with(ivCast)
                    .load("https://image.tmdb.org/t/p/original${cast.profilePath}")
                    .placeholder(R.drawable.ic_user_placeholder)
                    .error(R.drawable.ic_user_placeholder)
                    .transform(CircleCrop())
                    .into(ivCast)

                tvName.text = cast.name
            }
        }

        companion object {
            fun from(parent: ViewGroup): CastViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val itemView = layoutInflater.inflate(R.layout.item_cast, parent, false)
                return CastViewHolder(itemView)
            }
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Cast>() {
            override fun areItemsTheSame(oldItem: Cast, newItem: Cast) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Cast, newItem: Cast) = oldItem == newItem
        }
    }
}
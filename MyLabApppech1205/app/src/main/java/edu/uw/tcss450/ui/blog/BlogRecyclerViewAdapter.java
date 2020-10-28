package edu.uw.tcss450.ui.blog;

import android.graphics.drawable.Icon;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.uw.tcss450.ui.blog.BlogPost;
import edu.uw.tcss450.R;
import edu.uw.tcss450.databinding.FragmentBlogCardBinding;

public class BlogRecyclerViewAdapter extends RecyclerView.Adapter<BlogRecyclerViewAdapter.BlogViewHolder>{


    //Store the explanded state for each list item
    // true -> expanded, false -> not
    private final Map<BlogPost, Boolean> mExpandedFlags;

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BlogViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.fragment_blog_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        holder.setBlog(mBlogs.get(position));
    }

    @Override
    public int getItemCount() {
        return mBlogs.size();
    }

    //Store all of the blogs to present
    private final List<BlogPost> mBlogs;

    public BlogRecyclerViewAdapter(List<BlogPost> items) {
        this.mBlogs = items;
        mExpandedFlags = mBlogs.stream().collect(Collectors.toMap(Function.identity(), blog -> false));
    }

    /** * Objects from this class represent an Individual row View from the List
     * of rows in the Blog Recycler View.
     */
    public class BlogViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public FragmentBlogCardBinding binding;
        private BlogPost mBlog;

        public BlogViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentBlogCardBinding.bind(view);
            binding.buittonMore.setOnClickListener(this::handleMoreOrLess);
        }
        /** * When the button is clicked in the more state, expand the card to display
         * * the blog preview and switch the icon to the less state. When the button
         * * is clicked in the less state, shrink the card and switch the icon to the
         * * more state.
         * * @param button the button that was clicked */
        private void handleMoreOrLess(final View button) {
            mExpandedFlags.put(mBlog, !mExpandedFlags.get(mBlog));
            displayPreview();
        }

        /** * Helper used to determine if the preview should be displayed or not.
         * */
        private void displayPreview() {

            if (mExpandedFlags.get(mBlog)) {
                if (binding.textPreview.getVisibility() == View.GONE) {
                    binding.textPreview.setVisibility(View.VISIBLE);
                    binding.buittonMore.setImageIcon(Icon.createWithResource( mView.getContext(), R.drawable.ic_less_grey_24));
                } else {
                    binding.textPreview.setVisibility(View.GONE);
                    binding.buittonMore.setImageIcon(Icon.createWithResource(mView.getContext(), R.drawable.ic_more_grey_24));
                }
            }
        }

        void setBlog(final BlogPost blog) {
            mBlog = blog;
            binding.buttonFullPost.setOnClickListener(view -> {
                        Navigation.findNavController(mView).navigate(BlogListFragmentDirections
                                .actionNavigationBlogsToBlogPostFragment(blog));
                    }
            );
            binding.textTitle.setText(blog.getTitle());
            binding.textPubdate.setText(blog.getPubDate());

            //Use methods in the HTML class to format the HTML found in the text
            final String preview = Html.fromHtml(blog.getTeaser(),
                    Html.FROM_HTML_MODE_COMPACT) .toString().substring(0,100) //just a preview of the teaser
                    + "..."; binding.textPreview.setText(preview); displayPreview();
        }
    }
}

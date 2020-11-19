package edu.uw.tcss450.team2.friend;

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

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragemntFriendCardBinding;

public class FriendRecycleViewAdapter extends RecyclerView.Adapter<FriendRecycleViewAdapter.FriendViewHolder> {

    //Store all of the blogs to present
    private final List<FriendListUserModel> mBlogs;
    private final Map<FriendListUserModel, Boolean> mExpandedFlags;

    public FriendRecycleViewAdapter(List<FriendListUserModel> mBlogs, Map<FriendListUserModel, Boolean> mExpandedFlags) {
        this.mBlogs = mBlogs;
        this.mExpandedFlags = mExpandedFlags;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragemnt_friend_card, parent, false));
    }



    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        holder.setFriend(mBlogs.get(position));
    }

    @Override
    public int getItemCount() {
        return mBlogs.size() ;
    }




    public FriendRecycleViewAdapter(List<FriendListUserModel> items) {
        this.mBlogs = items;
        mExpandedFlags = mBlogs.stream().collect(Collectors.toMap(Function.identity(), blog -> false));
    }


    /** * Objects from this class represent an Individual row View from the List
     * of rows in the Blog Recycler View.
     */
    public class FriendViewHolder extends RecyclerView.ViewHolder {

        private final View mView;
        private FragemntFriendCardBinding binding;
        private FriendListUserModel mBlog;

        public FriendViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragemntFriendCardBinding.bind(view);
            //binding.buittonMore.setOnClickListener(this::handleMoreOrLess);
        }

        /**
         * When the button is clicked in the more state, expand the card to display
         * * the blog preview and switch the icon to the less state. When the button
         * * is clicked in the less state, shrink the card and switch the icon to the
         * * more state.
         * * @param button the button that was clicked
         */
        private void handleMoreOrLess(final View button) {
            mExpandedFlags.put(mBlog, !mExpandedFlags.get(mBlog));
            //displayPreview();
        }


        /**
         * Helper used to determine if the preview should be displayed or not.
         */


//        private void displayPreview() {
//
//            if (mExpandedFlags.get(mBlog)) {
//                if (binding.textPreview.getVisibility() == View.GONE) {
//                    binding.textPreview.setVisibility(View.VISIBLE);
//                    //binding.buittonMore.setImageIcon(Icon.createWithResource(mView.getContext(), R.drawable.ic_less_grey_24dp));
//                } else {
//                    binding.textPreview.setVisibility(View.GONE);
//                    //binding.buittonMore.setImageIcon(Icon.createWithResource(mView.getContext(), R.drawable.ic_more_grey_24dp));
//                }
//            }
//        }

        void setFriend(final FriendListUserModel blog) {
            mBlog = blog;
//            binding.buttonFullView.setOnClickListener(view -> {
//                        Navigation.findNavController(mView).navigate(FriendListFragmentDirections
//                                .actionNavigationContactToFriend());
//                    }
//            );
            binding.textUsername.setText(blog.getUserName());
            //binding.text.setText(blog.getFriendFirstName());
            //displayPreview();

            //Use methods in the HTML class to format the HTML found in the text
//            final String preview = Html.fromHtml(blog.getTeaser(),
//                    Html.FROM_HTML_MODE_COMPACT) .toString().substring(0,100) //just a preview of the teaser
//                    + "...";
//           binding.textPreview.setText(blog.getFriendFirstName() + "\n" + blog.getFriendLastName() + "\n" +
//           blog.getFriendEmail());; displayPreview();
        }
    }
}

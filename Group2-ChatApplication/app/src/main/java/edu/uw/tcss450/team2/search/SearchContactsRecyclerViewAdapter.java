package edu.uw.tcss450.team2.search;

import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentSearchCardBinding;
import edu.uw.tcss450.team2.friend.FriendContacts;
import edu.uw.tcss450.team2.model.UserInfoViewModel;
import me.pushy.sdk.lib.jackson.annotation.ObjectIdGenerator;

public class SearchContactsRecyclerViewAdapter extends RecyclerView.Adapter<SearchContactsRecyclerViewAdapter.SearchViewHolder> {

    private List<SearchContacts> searchContacts;
    private final Map<SearchContacts, Boolean> mAddRemove;
    private AddSendRequestViewModel addSendRequestViewModel;
    private UserInfoViewModel userInfoViewModel;
    private CancelFreindRequestViewModel cancelFreindRequestViewModel;


    public SearchContactsRecyclerViewAdapter(List<SearchContacts> item,
                                             AddSendRequestViewModel addSendRequestViewModel,
                                             UserInfoViewModel userInfoViewModel, CancelFreindRequestViewModel cancelFreindRequestViewModel) {

        searchContacts = item;
        mAddRemove = searchContacts.stream().collect(Collectors.toMap(Function.identity(), search -> false));
        this.addSendRequestViewModel = addSendRequestViewModel;
        this.userInfoViewModel = userInfoViewModel;
        this.cancelFreindRequestViewModel = cancelFreindRequestViewModel;
    }


    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_search_card, parent, false);
        SearchViewHolder svh = new SearchViewHolder(view);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.setSearchContact(searchContacts.get(position));

    }

    @Override
    public int getItemCount() {
        return searchContacts.size();
    }


    public void filterList(List<SearchContacts> filteredList) {
        searchContacts = filteredList;
        notifyDataSetChanged();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        private final View mView;
        private FragmentSearchCardBinding binding;
        private SearchContacts searchContact;

        public SearchViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentSearchCardBinding.bind(view);

        }


        void setSearchContact(final SearchContacts mSearch) {
            searchContact = mSearch;
            binding.searchTextUsername.setText(mSearch.getmSearchUsername());
            binding.searachTextEmail.setText(mSearch.getmSearchEmail());

            binding.addButton.setOnClickListener(button -> {
                if(mSearch.getMemberid() > 0) {
                    addSendRequestViewModel.getAddSendRequest(userInfoViewModel.getJwt(),
                            userInfoViewModel.getEmail(), mSearch.getmSearchEmail(), mSearch.getMemberid());
                }
                binding.removeButton.setVisibility(View.VISIBLE);
            });

            binding.removeButton.setOnClickListener(button -> {
                if (mSearch.getMemberid() > 0) {
                    cancelFreindRequestViewModel.getCancelSendRequest(userInfoViewModel.getJwt(), userInfoViewModel.getEmail(), mSearch.getmSearchEmail());
                }
                binding.removeButton.setVisibility(View.GONE);
            });

        }

    }
}

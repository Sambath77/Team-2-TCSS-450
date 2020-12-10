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

public class SearchContactsRecyclerViewAdapter extends RecyclerView.Adapter<SearchContactsRecyclerViewAdapter.SearchViewHolder> implements Filterable {

    private List<SearchContacts> searchContacts;
    private List<SearchContacts> copySearchContacts;
    private OnItemClickListener mListener;
    private final Map<SearchContacts, Boolean> mAddRemove;

    private AddSendRequestViewModel addSendRequestViewModel;
    private UserInfoViewModel userInfoViewModel;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onAddClick(int position);
        void onDeleteClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }



    public SearchContactsRecyclerViewAdapter(List<SearchContacts> item,
                                             AddSendRequestViewModel addSendRequestViewModel,
                                             UserInfoViewModel userInfoViewModel) {

        searchContacts = item;
        copySearchContacts = new ArrayList<>(searchContacts);
        mAddRemove = searchContacts.stream().collect(Collectors.toMap(Function.identity(), search -> false));
        this.addSendRequestViewModel = addSendRequestViewModel;
        this.userInfoViewModel = userInfoViewModel;
    }
    @Override
    public Filter getFilter() {
        return exampleFiler;
    }

    private Filter exampleFiler = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SearchContacts> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(copySearchContacts);

            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (SearchContacts item : copySearchContacts) {
                    if (item.getmSearchUsername().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            searchContacts.clear();
            searchContacts.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };




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
        //holder.controllButton(position);
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

            /*
            binding.addButton.setOnClickListener(button -> {
                if (binding.removeButton.getVisibility() == View.GONE) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onAddClick(position);

                        }
                    }
                    binding.removeButton.setVisibility(View.VISIBLE);

                }
            });
            binding.removeButton.setOnClickListener(cancel -> {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onDeleteClick(position);

                        }
                    }
                    binding.removeButton.setVisibility(View.GONE);
            });


            */

            view.setOnClickListener(v -> {
//                if (mListener != null) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        mListener.onItemClick(position);
//                    }
//                }
            });
        }

//        void controllButton() {
//            if (mAddRemove.get())
//            binding.addButton.setOnClickListener(button -> {
//                if (binding.removeButton.getVisibility() == View.GONE) {
//                    if (mListener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            mListener.onAddClick(position);
//
//                        }
//                    }
//                    binding.removeButton.setVisibility(View.VISIBLE);
//
//                }
//
//                binding.removeButton.setOnClickListener(cancel -> {
//                    if (mListener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            mListener.onDeleteClick(position);
//
//                        }
//                    }
//                    binding.removeButton.setVisibility(View.GONE);
//                });
//
//
//            });
//
//        }

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

        }

        private void handleDeleteAdd(final View button) {
            mAddRemove.put(searchContact, !mAddRemove.get(searchContact));
            addDeleteFriend();

        }


         private void addDeleteFriend() {
            if (mAddRemove.get(searchContact))
                if (binding.removeButton.getVisibility() == View.GONE) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onAddClick(position);

                        }
                    }
                    binding.removeButton.setVisibility(View.VISIBLE);

                } else {
                    binding.removeButton.setOnClickListener(view -> {
                        binding.removeButton.setVisibility(View.GONE);
                        if (mListener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                mListener.onDeleteClick(position);

                            }
                        }
                    });

                }
//            if (binding.removeButton.getVisibility() == View.GONE) {
//                if (mListener != null) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        mListener.onAddClick(position);
//
//                    }
//                }
//                binding.removeButton.setVisibility(View.VISIBLE);
//
//            } else {
//                if (mListener != null) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        mListener.onDeleteClick(position);
//
//                    }
//                }
//            }
        }



//        void setSearchContact(final SearchContacts mSearch) {
//            searchContact = mSearch;
//            binding.searchTextUsername.setText(mSearch.getmSearchUsername());
//            binding.searachTextEmail.setText(mSearch.getmSearchEmail());
//
//        }
    }
}

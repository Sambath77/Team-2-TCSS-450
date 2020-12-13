package edu.uw.tcss450.team2.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.team2.R;
import edu.uw.tcss450.team2.databinding.FragmentSearchCardBinding;

public class SearchContactsRecyclerViewAdapter extends RecyclerView.Adapter<SearchContactsRecyclerViewAdapter.SearchViewHolder> {

    private List<SearchContacts> searchContacts;
    private List<SearchContacts> copySearchContacts;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        //void onAddClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }




    public SearchContactsRecyclerViewAdapter(List<SearchContacts> searchContacts) {

        this.searchContacts = searchContacts;
        copySearchContacts = new ArrayList<>(searchContacts);
    }


    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_search_card, parent, false);
        SearchViewHolder svh = new SearchViewHolder(view, mListener);
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
//    @Override
//    public Filter getFilter() {
//        return exampleFiler;
//    }
//
//    private Filter exampleFiler = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<SearchContacts> filteredList = new ArrayList<>();
//
//            if (constraint == null || constraint.length() == 0) {
//                filteredList.addAll(copySearchContacts);
//
//            } else {
//                String filterPattern = constraint.toString().toLowerCase().trim();
//
//                for (SearchContacts item : copySearchContacts) {
//                    if (item.getmSearchUsername().toLowerCase().contains(filterPattern)) {
//                        filteredList.add(item);
//                    }
//                }
//            }
//            FilterResults results = new FilterResults();
//            results.values = filteredList;
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            searchContacts.clear();
//            searchContacts.addAll((List) results.values);
//            notifyDataSetChanged();
//        }
//    };

    public static class SearchViewHolder extends RecyclerView.ViewHolder {

        private final View mView;
        private FragmentSearchCardBinding binding;

        public SearchViewHolder(@NonNull View view, final OnItemClickListener listener) {
            super(view);
            this.mView = view;
            binding = FragmentSearchCardBinding.bind(view);

            view.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });

            binding.addButton.setOnClickListener(v -> {
//                if (listener != null) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        listener.onAddClick(position);
//                    }
//                }
            });
        }

        void setSearchContact(final SearchContacts mSearch) {
            binding.searchTextUsername.setText(mSearch.getmSearchUsername());
            binding.searachTextEmail.setText(mSearch.getmSearchEmail());

        }
    }
}

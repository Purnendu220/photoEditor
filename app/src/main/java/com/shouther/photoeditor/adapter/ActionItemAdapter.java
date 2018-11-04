package com.shouther.photoeditor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shouther.photoeditor.R;
import com.shouther.photoeditor.data.ActionItemData;
import com.shouther.photoeditor.data.ListLoader;
import com.shouther.photoeditor.viewholder.ActionItemViewHolder;
import com.shouther.photoeditor.viewholder.EmptyViewHolder;
import com.shouther.photoeditor.viewholder.LoaderViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ActionItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_UNKNOWN = -1;
    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_LOADER = 2;

    private final AdapterCallbacks<ActionItemData> adapterCallbacks;

    private List<Object> list;
    private Context context;

    private int shownInScreen;

    private boolean showLoader;
    private ListLoader listLoader;

    public ActionItemAdapter(Context context,boolean showLoader, AdapterCallbacks<ActionItemData> adapterCallbacks) {
        this.adapterCallbacks = adapterCallbacks;
        this.context = context;
        list = new ArrayList<>();
      //  this.shownInScreen = shownInScreen;
        this.showLoader = showLoader;

        listLoader = new ListLoader(true, "No more classes");
    }

    public List<Object> getList() {
        return list;
    }

    public void addData(ActionItemData model) {
        list.add(model);
        addLoader();
    }

    public void addAllData(List<ActionItemData> models) {
        clearAll();
        list.addAll(models);
        addLoader();
    }

    public void clearAll() {
        list.clear();
    }

    public void loaderDone() {
        listLoader.setFinish(true);
        try {
            notifyItemChanged(list.indexOf(listLoader));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loaderReset() {
        listLoader.setFinish(false);
    }

    private void addLoader() {
        if (showLoader) {
            list.remove(listLoader);
            list.add(listLoader);
        }
    }

    @Override
    public int getItemViewType(int position) {

        int itemViewType = VIEW_TYPE_UNKNOWN;

        Object item = getItem(position);
        if (item instanceof ActionItemData) {
            itemViewType = VIEW_TYPE_ITEM;
        } else if (item instanceof ListLoader) {
            itemViewType = VIEW_TYPE_LOADER;
        }

        return itemViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_action_item, parent, false);
            return new ActionItemViewHolder(view);

        } else if (viewType == VIEW_TYPE_LOADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_progress, parent, false);
            return new LoaderViewHolder(view);
        }
        return new EmptyViewHolder(new LinearLayout(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ActionItemViewHolder) {
            ((ActionItemViewHolder) holder).bind(getItem(position), adapterCallbacks, position);
        } else if (holder instanceof LoaderViewHolder) {
            ((LoaderViewHolder) holder).bind(listLoader, adapterCallbacks);
            if (position == getItemCount() - 1 && !listLoader.isFinish()) {
                adapterCallbacks.onShowLastItem();
            }
        } else if (holder instanceof EmptyViewHolder) {
            ((EmptyViewHolder) holder).bind();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Object getItem(int position) {
        if (list != null && list.size() > 0)
            return list.get(position);
        else
            return null;
    }
}
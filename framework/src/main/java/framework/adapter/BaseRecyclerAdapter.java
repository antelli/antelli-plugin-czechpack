package framework.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import framework.viewmodel.BaseViewModel;
import framework.viewmodel.RecyclerItem;

import com.androidxtend.kit.BR;
import java.util.List;

/**
 * Created by Stepan on 9.11.2016.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerAdapter.BaseMvvmRecyclerViewHolder> {

    protected BaseViewModel viewModel;
    protected ObservableList<T> items;
    private LifecycleOwner lifecycleOwner;

    private ObservableList.OnListChangedCallback<ObservableList<T>> onListChangedCallback;

    protected abstract @LayoutRes
    int getLayoutId(int itemType);

    public BaseRecyclerAdapter(ObservableList<T> items) {
        this.items = items;
        initOnListChangedListener();
    }

    public BaseRecyclerAdapter(ObservableList<T> items, BaseViewModel viewModel) {
        this.viewModel = viewModel;
        this.items = items;
        initOnListChangedListener();
    }

    private void initOnListChangedListener() {
        onListChangedCallback = new ObservableList.OnListChangedCallback<ObservableList<T>>() {
            @Override
            public void onChanged(ObservableList<T> sender) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList<T> sender, int positionStart, int itemCount) {
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(ObservableList<T> sender, int positionStart, int itemCount) {
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(ObservableList<T> sender, int fromPosition, int toPosition, int itemCount) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeRemoved(ObservableList<T> sender, int positionStart, int itemCount) {
                notifyItemRangeRemoved(positionStart, itemCount);
            }
        };
        items.addOnListChangedCallback(onListChangedCallback);
    }

    private ViewDataBinding getViewHolderBinding(ViewGroup parent, @LayoutRes int itemLayoutId) {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), itemLayoutId, parent, false);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerAdapter.BaseMvvmRecyclerViewHolder holder, int position) {
        T item = items.get(position);
        holder.bind(item, holder.binder);
        holder.binder.executePendingBindings();
    }

    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(ObservableList items) {
        if (items != null && items.equals(this.items)) {
            //notifyDataSetChanged();
        } else {
            this.items = items;
            initOnListChangedListener();
            notifyDataSetChanged();
        }
    }

    @Override
    public BaseMvvmRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseMvvmRecyclerViewHolder(getViewHolderBinding(parent, getLayoutId(viewType)));
    }

    public LifecycleOwner getLifecycleOwner() {
        return lifecycleOwner;
    }

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    class BaseMvvmRecyclerViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binder;

        public BaseMvvmRecyclerViewHolder(ViewDataBinding v) {
            super(v.getRoot());
            binder = DataBindingUtil.bind(v.getRoot());
        }

        protected void bind(final T item, final ViewDataBinding binder) {
            binder.setVariable(BR.vm, viewModel);
            binder.setVariable(BR.item, item);
            if (lifecycleOwner != null) {
                binder.setLifecycleOwner(lifecycleOwner);
            }
        }

    }

}

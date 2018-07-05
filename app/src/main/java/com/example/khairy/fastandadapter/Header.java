package com.example.khairy.fastandadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Header extends AbstractItem<Header, Header.ViewHolder> {

    String title;

    public Header(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // FastAdapter AbstractItem methods

    @Override
    public int getType() {
        return R.id.header_text;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.header_item;
    }



    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    protected static class ViewHolder extends FastAdapter.ViewHolder<Header> {
        @BindView(R.id.header_text)
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindView(Header item, List<Object> payloads) {

        }

        @Override
        public void unbindView(Header item) {

        }

    }
}

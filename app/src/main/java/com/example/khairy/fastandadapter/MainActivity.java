package com.example.khairy.fastandadapter;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.fastadapter.listeners.OnLongClickListener;
import com.mikepenz.fastadapter_extensions.ActionModeHelper;
import com.mikepenz.fastadapter_extensions.drag.ItemTouchCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity  {

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private List<Header> headers;
    private FastAdapter<Header> fastAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ItemAdapter<Header> itemAdapter = new ItemAdapter();
        fastAdapter = FastAdapter.with(itemAdapter);
        recyclerView.setAdapter(fastAdapter);
         headers=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            headers.add(new Header(""+i));
        }
        itemAdapter.add(headers);
        fastAdapter.setHasStableIds(true);
        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new OnClickListener<Header>() {
            @Override
            public boolean onClick(@Nullable View v, IAdapter<Header> adapter, Header item, int position) {
                Toast.makeText(MainActivity.this, item.title, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        fastAdapter.withMultiSelect(true);
        fastAdapter.withSelectOnLongClick(true);
        fastAdapter.withOnLongClickListener(new OnLongClickListener<Header>() {
            @Override
            public boolean onLongClick(View v, IAdapter<Header> adapter, Header item, int position) {
                Toast.makeText(MainActivity.this, item.title+"longClicked", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        recyclerView.setAdapter(fastAdapter);
        fastAdapter.withSavedInstanceState(savedInstanceState);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = fastAdapter.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}
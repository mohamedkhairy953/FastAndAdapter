package com.example.khairy.fastandadapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HeaderActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private FastItemAdapter fastAdapter;
    private HeaderAdapter<Header> headerAdapter;
    private List<Mango> mangoes;

    private FooterAdapter<ProgressItem> footerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("With Header");

        mangoes = new DummyData(this).generateData(); //Populating our Mangoes list

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        fastAdapter = new FastItemAdapter<>();
        fastAdapter.setHasStableIds(true);
        fastAdapter.withSelectable(true);

        headerAdapter = new HeaderAdapter<>();

        footerAdapter = new FooterAdapter<>(); // help manage load more progress

        recyclerView.setAdapter(footerAdapter.wrap(headerAdapter.wrap(fastAdapter)));

        int half = mangoes.size() / 2;

        fastAdapter.add(0, new Header("First half").withIdentifier(1));
        fastAdapter.add(1, mangoes.subList(0, half));
        fastAdapter.add(half + 1, new Header("Second half").withIdentifier(2));
        fastAdapter.add(half + 2, mangoes.subList(0, half));


        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<IItem>() {
            @Override
            public boolean onClick(View v, IAdapter<IItem> adapter, IItem item, int position) {

                if (item instanceof Mango) {
                    Mango mango = (Mango) item;
                    // Do what you want with your Mango here
                }

                return true;
            }
        });

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore(int currentPage) {
                footerAdapter.clear();
                footerAdapter.add(new ProgressItem().withEnabled(false));

                // Faking a network load
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        footerAdapter.clear();

                        List<Mango> newMangoes = mangoes.subList(0, 3);
                        fastAdapter.add(newMangoes);
                    }
                }, 2000);
            }
        });

        fastAdapter.withSavedInstanceState(savedInstanceState);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = fastAdapter.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
}
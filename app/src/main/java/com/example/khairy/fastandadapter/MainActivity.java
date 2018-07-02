package com.example.khairy.fastandadapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements ItemAdapter.ItemFilterListener, ItemTouchCallback {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler) RecyclerView recyclerView;

    private List<Mango> mangoes;
    private FastItemAdapter<Mango> fastAdapter;

    private SimpleDragCallback dragCallback;
    private ItemTouchHelper touchHelper;

    private ActionModeHelper actionModeHelper;
    private UndoHelper undoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle("Mangoes");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        /**
         * See <a href="https://gist.github.com/alexfu/0f464fc3742f134ccd1e">DividerItemDecoration</a>
         */
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mangoes = new DummyData(this).generateData(); //Populating our Mangoes list

        fastAdapter = new FastItemAdapter<>();
        fastAdapter.setHasStableIds(true);
        fastAdapter.withSelectable(true);
        fastAdapter.withMultiSelect(true);
        fastAdapter.withSelectOnLongClick(true);

        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<Mango>() {
            @Override
            public boolean onClick(View v, IAdapter<Mango> adapter, Mango item, int position) {
                //@// TODO: Handle normal clicks: Intent to DetailActivity
                return false;
            }
        });

        fastAdapter.withFilterPredicate(new IItemAdapter.Predicate<Mango>() {
            @Override
            public boolean filter(Mango item, CharSequence constraint) {
                // Remove those which return true
                return item.getName().startsWith(String.valueOf(constraint));
            }
        });

        // Drag and drop setup
        dragCallback = new SimpleDragCallback(this);
        touchHelper = new ItemTouchHelper(dragCallback);
        touchHelper.attachToRecyclerView(recyclerView);

        // Init for multi-select CAB mode
        actionModeHelper = new ActionModeHelper(fastAdapter, R.menu.cab, new ActionBarCallBack());

        fastAdapter.withOnPreClickListener(new FastAdapter.OnClickListener<Mango>() {
            @Override
            public boolean onClick(View v, IAdapter<Mango> adapter, Mango item, int position) {
                Boolean res = actionModeHelper.onClick(item);
                return res != null ? res : false;
            }
        });

        fastAdapter.withOnPreLongClickListener(new FastAdapter.OnLongClickListener<Mango>() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public boolean onLongClick(View v, IAdapter<Mango> adapter, Mango item, int position) {
                ActionMode actionMode = actionModeHelper.onLongClick(MainActivity.this, position);

                if (actionMode != null) {
                    // Set CAB background color
                    findViewById(R.id.action_mode_bar).setBackgroundColor(Color.GRAY);
                }
                return actionMode != null;
            }
        });

        /*
         For handling multiple selected items deletion via CAB mode.
         Also for Undo delete action via SnackBar
          */
        //noinspection unchecked
        undoHelper = new UndoHelper(fastAdapter, new UndoHelper.UndoListener<Mango>() {
            @Override
            public void commitRemove(Set<Integer> positions, ArrayList<FastAdapter.RelativeInfo<Mango>> removed) {
                Log.d("remove", "removed: " + removed.size());
            }
        });


        recyclerView.setAdapter(fastAdapter); // Used for single adapter type

        fastAdapter.add(mangoes); // Always add items after setting adapter

        // Call only after adding items
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_filter:
                // Ideally call this from your SearchView's 'onQuerytextSubmit()' & 'onQueryTextChange()'
                fastAdapter.filter("K");
                return true;

            case R.id.main_menu_next:
                Intent i = new Intent(MainActivity.this, HeaderActivity.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // ItemAdapter.ItemFilterListener
    @Override
    public void itemsFiltered() {
        // Items filtering completed
        Toast.makeText(MainActivity.this, "Filtered", Toast.LENGTH_SHORT).show();
    }

    // ItemTouchCallback
    @Override
    public boolean itemTouchOnMove(int oldPosition, int newPosition) {
        Collections.swap(fastAdapter.getAdapterItems(), oldPosition, newPosition); // change position
        fastAdapter.notifyAdapterItemMoved(oldPosition, newPosition);
        return true;
    }

    // inner class for handling CAB
    private class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            undoHelper.remove(findViewById(android.R.id.content), "Item removed", "Undo", Snackbar.LENGTH_LONG, fastAdapter.getSelections());
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    }
}
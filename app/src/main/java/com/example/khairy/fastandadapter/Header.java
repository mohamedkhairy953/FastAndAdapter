package com.example.khairy.fastandadapter;

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

    @Override
    public void bindView(ViewHolder holder) {
        super.bindView(holder);

        holder.textView.setText(title);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.header_text) TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

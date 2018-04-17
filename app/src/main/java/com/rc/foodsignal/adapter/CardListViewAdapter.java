package com.rc.foodsignal.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rc.foodsignal.R;
import com.stripe.android.model.Card;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class CardListViewAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<Card> mData;
    private static LayoutInflater inflater = null;
    private String TAG = CardListViewAdapter.class.getSimpleName();

    public CardListViewAdapter(Activity activity) {
        mActivity = activity;
        mData = new ArrayList<Card>();
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<Card> getData() {
        return mData;
    }

    public void setData(ArrayList<Card> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void addData(Card card) {
//        if (getCount() > 0) {
        mData.add(card);
        notifyDataSetChanged();
//        }
    }

    public void updateData(Card card) {
        if (getCount() > 0) {
            int itemPosition = getItemPosition(card);
            mData.remove(itemPosition);
            mData.add(card);
            notifyDataSetChanged();
        }
    }

    public int getItemPosition(Card card) {
        for (int i = 0; i < mData.size(); i++) {
            if ((mData.get(i)).getId().contains(card.getId())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Card getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        long id = 0;
        try {

            id = (long) Integer.parseInt(mData.get(position).getId());
        } catch (Exception ex) {
            ex.printStackTrace();
            id = 0;
        }
        return id;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_row_card, null);

        final Card card = getItem(position);

//        ImageView ivMenu = (ImageView) vi.findViewById(R.id.iv_menu);
//        Glide
//                .with(mActivity)
//                .asBitmap()
//                .load((foodItem.getImages().size() > 0) ? foodItem.getImages().get(0).getImage() : R.drawable.ic_default_restaurant_menu)
//                .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
////                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
//                .apply(new RequestOptions().placeholder(R.drawable.ic_default_restaurant_menu))
//                .apply(new RequestOptions().error(R.drawable.ic_default_restaurant_menu))
//                .apply(new RequestOptions().circleCropTransform())
//                .into(ivMenu);
//
        TextView tvTitle = (TextView) vi.findViewById(R.id.tv_title);
        tvTitle.setText("Last 4 digit: " + card.getLast4());

        TextView tvSubtitle = (TextView) vi.findViewById(R.id.tv_subtitle);
        tvSubtitle.setText("Expire date: " + card.getExpMonth() + "/" + card.getExpYear());

        return vi;
    }
}
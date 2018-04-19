package com.rc.foodsignal.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rc.foodsignal.R;
import com.rc.foodsignal.model.realm.StripeCard;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import java.util.ArrayList;

import static com.rc.foodsignal.util.AllConstants.SESSION_SELECTED_CARD;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class CardListViewAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<StripeCard> mData;
    private static LayoutInflater inflater = null;
    private int lastSelectedPosition = -1;
    private String TAG = CardListViewAdapter.class.getSimpleName();

    public CardListViewAdapter(Activity activity) {
        mActivity = activity;
        mData = new ArrayList<StripeCard>();
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<StripeCard> getData() {
        return mData;
    }

    public void setData(ArrayList<StripeCard> data) {
        mData.clear();
        mData = data;
        notifyDataSetChanged();
    }

    public void addData(StripeCard card) {
//        if (getCount() > 0) {
        mData.add(card);
        notifyDataSetChanged();
//        }
    }

    public void updateData(StripeCard card) {
        if (getCount() > 0) {
            int itemPosition = getItemPosition(card);
            mData.remove(itemPosition);
            mData.add(card);
            notifyDataSetChanged();
        }
    }

    public int getItemPosition(StripeCard card) {
        for (int i = 0; i < mData.size(); i++) {
            if ((mData.get(i)).getCardNumber().contains(card.getCardNumber())) {
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
    public StripeCard getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        long id = 0;
        try {

            id = (long) Integer.parseInt(mData.get(position).getCardLastFourNumber());
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

        final StripeCard card = getItem(position);

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
        tvTitle.setText(card.getCardNumber().replaceAll("....(?!$)", "$0 "));

        TextView tvSubtitle = (TextView) vi.findViewById(R.id.tv_subtitle);
        tvSubtitle.setText("Expire date: " + card.getCardExpireMonth() + "/" + card.getCardExpireYear());

        //Set default card selection
        ImageView ivTick = (ImageView) vi.findViewById(R.id.iv_tick);
        if (!AllSettingsManager.isNullOrEmpty(SessionManager.getStringSetting(mActivity, SESSION_SELECTED_CARD))) {
            StripeCard selectedCard = StripeCard.getResponseObject(SessionManager.getStringSetting(mActivity, SESSION_SELECTED_CARD), StripeCard.class);

            if (selectedCard != null && selectedCard.getCardNumber().equalsIgnoreCase(card.getCardNumber())) {
                lastSelectedPosition = position;
                ivTick.setVisibility(View.VISIBLE);
            } else {
                ivTick.setVisibility(View.INVISIBLE);
            }
        } else {
            ivTick.setVisibility(View.INVISIBLE);
        }

        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save card into session
                SessionManager.setStringSetting(mActivity, SESSION_SELECTED_CARD, StripeCard.getResponseString(card));

                lastSelectedPosition = position;
                notifyDataSetChanged();
            }
        });

        return vi;
    }
}
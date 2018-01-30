package com.nex3z.flowlayout;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FlowUtil {

    public enum TEXT_TYPE {SELECTED, UNSELECTED}

    public static TextView buildLabel(final Context context, String text) {

        final TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setPadding((int) dpToPx(context, 16), (int) dpToPx(context, 8), (int) dpToPx(context, 16), (int) dpToPx(context, 8));
        textView.setBackgroundResource(R.drawable.chip_unselected);
        textView.setTag(TEXT_TYPE.UNSELECTED.name());
        textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView.getTag().toString().equalsIgnoreCase(TEXT_TYPE.SELECTED.name())) {
                    unSelectFlowView(context, textView);
                } else {
                    selectFlowView(context, textView);
                }
            }
        });

        return textView;
    }

    public static List<TextView> buildFlowView(Context context, FlowLayout flowLayout, ArrayList<String> flowData) {
        List<TextView> mFlowContents = new ArrayList<TextView>();

        for (String text : flowData) {
            TextView textView = FlowUtil.buildLabel(context, text);
            flowLayout.addView(textView);
            mFlowContents.add(textView);
        }

        return mFlowContents;
    }

    public static float dpToPx(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static TextView selectFlowView(Context context, TextView textView) {
        textView.setTag(TEXT_TYPE.SELECTED.name());
        textView.setBackgroundResource(R.drawable.chip_selected);
        textView.setTextColor(ContextCompat.getColor(context, android.R.color.white));

        return textView;
    }

    public static TextView unSelectFlowView(Context context, TextView textView) {
        textView.setTag(TEXT_TYPE.UNSELECTED.name());
        textView.setBackgroundResource(R.drawable.chip_unselected);
        textView.setTextColor(ContextCompat.getColor(context, R.color.filters_header));

        return textView;
    }

    public static TextView updateTextView(List<TextView> flowViews, TextView textView) {
        int position = getFlowViewPosition(flowViews, textView.getText().toString());
        flowViews.remove(position);
        flowViews.add(position, textView);

        return flowViews.get(position);
    }

    public static int getFlowViewPosition(List<TextView> flowViews, String value) {
        for (int i = 0; i < flowViews.size(); i++) {
            if (flowViews.get(i).getText().toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return -1;
    }
}
package com.rc.foodsignal.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.reversecoder.library.util.AllSettingsManager;

/**
 * Author: Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public abstract class BaseAlertDialog extends AlertDialog {
    protected BaseAlertDialog mBaseAlertDialog;
    public Activity mActivity;
    OnClickListener mOnClickListener;

    protected BaseAlertDialog(Activity context) {
        super(context);
        mActivity = context;
        mBaseAlertDialog = this;
    }

    public Builder createView(int title, int message, int negativeButton, int positiveButton, final OnClickListener listener) {
        Builder builder = new Builder(this.getContext());
        builder.create();
        if (title > 0) {
            builder.setTitle(title);
        }
        if (message > 0) {
            builder.setMessage(message);
        }

        if (negativeButton > -1) {
            builder.setNegativeButton(negativeButton, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.onClick(dialog, which);
                    }
                    dialog.dismiss();
                }
            });
        }

        if (positiveButton > 0) {
            builder.setPositiveButton(positiveButton, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.onClick(dialog, which);
                    }
                    dialog.dismiss();
                }
            });
        }

        return builder;
    }

    public Builder createView(String title, String message, int negativeButton, int positiveButton, final OnClickListener listener) {
        Builder builder = new Builder(this.getContext());
        builder.create();
        if (!AllSettingsManager.isNullOrEmpty(title)) {
            builder.setTitle(title);
        }
        if (!AllSettingsManager.isNullOrEmpty(message)) {
            builder.setMessage(message);
        }

        if (negativeButton > -1) {
            builder.setNegativeButton(negativeButton, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.onClick(dialog, which);
                    }
                    dialog.dismiss();
                }
            });
        }

        if (positiveButton > 0) {
            builder.setPositiveButton(positiveButton, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.onClick(dialog, which);
                    }
                    dialog.dismiss();
                }
            });
        }

        return builder;
    }

    public Builder createView(String title, int message, int negativeButton, int positiveButton, int neutralButton, final OnClickListener listener) {
        Builder builder = new Builder(this.getContext());
        builder.create();
        builder.setTitle(title);
        if (message > 0) {
            builder.setMessage(message);
        }

        if (negativeButton > -1) {
            builder.setNegativeButton(negativeButton, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.onClick(dialog, which);
                    }
                    dialog.dismiss();
                }
            });
        }

        if (positiveButton > 0) {
            builder.setPositiveButton(positiveButton, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.onClick(dialog, which);
                    }
                    dialog.dismiss();
                }
            });
        }

        if (neutralButton > 0) {
            builder.setNeutralButton(neutralButton, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.onClick(dialog, which);
                    }
                    dialog.dismiss();
                }
            });
        }

        return builder;
    }

    public abstract Builder createView();
}

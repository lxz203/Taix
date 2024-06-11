package com.example.takeataxiproject.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.example.takeataxiproject.R;

/**
 * @ClassName ToastHelper
 * @Description TODO
 * @Author xinqi
 * @Date 2019-04-30 17:10
 * @Version 1.0
 */
public class ToastHelper {

    private static Toast mToast = null;

    public ToastHelper() {
    }

    public static void showShort(int textId) {
//        showToast(ActivityUtils.getTopActivity(), textId);
        Toast.makeText(ActivityUtils.getTopActivity().getApplication(), textId, Toast.LENGTH_SHORT).show();

    }
    public static void showShortToData(int textId) {
//        showToast(ActivityUtils.getTopActivity(), textId);
        Toast t =  Toast.makeText(ActivityUtils.getTopActivity().getApplication(), textId, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER,0,400);

        t.show();

    }
    public static void showShort(String text) {
//        showToast(ActivityUtils.getTopActivity(), text);
        Toast.makeText(ActivityUtils.getTopActivity().getApplication(), text, Toast.LENGTH_SHORT).show();
    }


    public static void showLong(int textId) {
        Toast.makeText(ActivityUtils.getTopActivity().getApplication(), textId, Toast.LENGTH_LONG).show();

    }

    public static void showLong(String text) {
        Toast.makeText(ActivityUtils.getTopActivity().getApplication(), text, Toast.LENGTH_LONG).show();
    }

    public static void showToast(Context context, int textId) {
        if (context != null && textId > 0) {
            TextView textView = new TextView(context);
            textView.setText(textId);
            textView.setTextColor(-1);
            textView.setPadding(25, 15, 25, 15);
            textView.setBackgroundResource(R.drawable.public_shape_toast_bg);
            if (mToast != null) {
                mToast.setView(textView);
                mToast.setDuration(Toast.LENGTH_SHORT);
                mToast.show();
                return;
            }

            mToast = new Toast(context);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setView(textView);
            mToast.show();
        }

    }

    public static void showToast(Context context, String text) {
        if (context != null && !TextUtils.isEmpty(text)) {
            TextView textView = new TextView(context);
            textView.setText(text);
            textView.setTextColor(-1);
            textView.setPadding(35, 15, 35, 15);
            textView.setBackgroundResource(R.drawable.public_shape_toast_bg);
            if (mToast != null) {
                mToast.setView(textView);
                mToast.setDuration(Toast.LENGTH_SHORT);
                mToast.show();
                return;
            }

            mToast = new Toast(context);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setView(textView);
            mToast.show();
        }

    }

    public static void showToast(Context context, int textId, int toastDuration) {
        if (context != null && textId > 0) {
            TextView textView = new TextView(context);
            textView.setText(textId);
            textView.setTextColor(-1);
            textView.setPadding(25, 15, 25, 15);
            textView.setBackgroundResource(R.drawable.public_shape_toast_bg);
            if (mToast != null) {
                mToast.setView(textView);
                mToast.setDuration(toastDuration);
                mToast.show();
                return;
            }

            mToast = new Toast(context);
            mToast.setDuration(toastDuration);
            mToast.setView(textView);
            mToast.show();
        }

    }

    public static void showToast(Context context, String text, int toastDuration) {
        if (context != null && !TextUtils.isEmpty(text)) {
            TextView textView = new TextView(context);
            textView.setText(text);
            textView.setTextColor(-1);
            textView.setPadding(25, 15, 25, 15);
            textView.setBackgroundResource(R.drawable.public_shape_toast_bg);
            if (mToast != null) {
                mToast.setView(textView);
                mToast.setDuration(toastDuration);
                mToast.show();
                return;
            }

            mToast = new Toast(context);
            mToast.setDuration(toastDuration);
            mToast.setView(textView);
            mToast.show();
        }

    }

    public static void postInUIThread(Runnable runnable, long delayMillis) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(runnable, delayMillis);
    }

    public static void setTextViewLines(final TextView textView, final int maxLines) {
        ViewTreeObserver observer = textView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewTreeObserver obs = textView.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (textView.getLineCount() > maxLines) {
                    try {
                        String content = textView.getText().toString();
                        if (!TextUtils.isEmpty(content) && content.length() > 10) {
                            int lineEndIndex = textView.getLayout().getLineEnd(maxLines - 1);
                            if (lineEndIndex > 3) {
                                String text = content.subSequence(0, lineEndIndex - 3) + "...";
                                textView.setText(text);
                            }
                        }
                    } catch (Exception var5) {
                        ;
                    }
                }

            }
        });
    }
}

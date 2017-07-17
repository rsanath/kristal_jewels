package com.talenttakeaways.kristaljewels.others;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.talenttakeaways.kristaljewels.R;

/**
 * Created by sanath on 17/07/17.
 */

public class CommonFunctions {

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static MaterialDialog.Builder getLoadingDialog(Context context, String title, String content) {
       return new MaterialDialog.Builder(context)
               .title(title).content(content)
               .contentColor(Color.BLACK).cancelable(false)
               .autoDismiss(true).progress(true, 0);
    }

    public static MaterialDialog.Builder getLoadingDialog(Context context, int titleId, int contentId) {
        return new MaterialDialog.Builder(context)
                .title(titleId).content(contentId)
                .contentColor(Color.BLACK).cancelable(false)
                .autoDismiss(true).progress(true, 0);
    }

    public static MaterialDialog.Builder getDismissDialog(Context context, String title, String content) {
        return new MaterialDialog.Builder(context)
                .title(title).content(content)
                .contentColor(Color.BLACK).positiveText(R.string.ok);
    }

    public static MaterialDialog.Builder getDismissDialog(Context context, int titleId, int contentId) {
        return new MaterialDialog.Builder(context)
                .title(titleId).content(contentId)
                .contentColor(Color.BLACK).positiveText(R.string.ok);
    }

}

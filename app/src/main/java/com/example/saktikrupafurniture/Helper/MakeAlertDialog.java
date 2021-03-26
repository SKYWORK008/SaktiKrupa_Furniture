package com.example.saktikrupafurniture.Helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.saktikrupafurniture.R;

public final class MakeAlertDialog {
    private MakeAlertDialog(){}

    /**
     * Dialog with one button a title and a message
     *
     * @param context            current context
     * @param title              dialog title
     * @param message            dialog message
     * @param positiveButtonText positive button text
     * @param positiveListener   positive button listener
     * @return the AlertDialog
     */
    public static AlertDialog createAlertDialogWithTwoButton(Context context, String title, String message,
                                                             String positiveButtonText,
                                                             String negativeButtonText,
                                                             DialogInterface.OnClickListener positiveListener,
                                                             DialogInterface.OnClickListener negativeListener) {

        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, positiveListener)
                .setNegativeButton(negativeButtonText, negativeListener)
                .create();
        }

    public static AlertDialog createAlertDialogWithOKButton(Context context, String title, String message) {

        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog, int which) {}})
                .create();
    }
}




package com.marcowilly.organizze.framework;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.marcowilly.organizze.R;

public abstract class AlertUtil {

    public static void showSnackBarError(final Activity activity, final String message){
        AlertUtil.showSnackBar(activity, message, R.color.colorAccent, android.R.color.white);
    }

    public static void showSnackBarSuccess(final Activity activity, final String message){
        AlertUtil.showSnackBar(activity, message, android.R.color.holo_green_light, android.R.color.white);
    }

    private static void showSnackBar(final Activity activity, final String message, final int background, final int txtColor){
        Snackbar snack = Snackbar.make(activity.getWindow().getDecorView().getRootView().findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
        view.setBackgroundColor(ContextCompat.getColor(activity, background));
        tv.setTextColor(ContextCompat.getColor(activity, txtColor));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        snack.show();
    }

    public static void showToast(final Context context, final String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showAlert(final Context context, final String title,
                                 final String message, final boolean cancelable,
                                 final DialogInterface.OnClickListener onClickListenerPositive,
                                 final DialogInterface.OnClickListener onClickListenerNegative){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(cancelable);
        alertDialogBuilder.setPositiveButton("Confirmar", onClickListenerPositive);
        alertDialogBuilder.setNegativeButton("Cancelar", onClickListenerNegative);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

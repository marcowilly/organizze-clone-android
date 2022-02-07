package com.marcowilly.organizze.framework;

import android.content.Context;
import android.widget.EditText;

public abstract class EditTextUtil {

    public static String getText(EditText edtText){
        return edtText.getText().toString();
    }

    public static void showAlertEditText(final Context context, final String message, final EditText edtText){
        edtText.requestFocus();
        edtText.setError(message);
    }
}

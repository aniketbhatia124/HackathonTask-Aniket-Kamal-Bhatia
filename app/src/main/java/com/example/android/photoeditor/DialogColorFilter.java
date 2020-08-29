package com.example.android.photoeditor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import android.text.Editable;
import android.text.InputType;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;


public class DialogColorFilter extends DialogFragment {


    interface OkListener {
        void onOkPressed(String dialogValue);
    }

    private OkListener okListener;
    private EditText shortCodeField;


    void setOkListener(OkListener okListener) {
        this.okListener = okListener;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private LinearLayout getDialogLayout() {
        Context context = getContext();
        LinearLayout layout = new LinearLayout(context);
        shortCodeField = new EditText(context);

        shortCodeField.setLayoutParams(
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        shortCodeField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
        layout.addView(shortCodeField);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        return layout;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setView(getDialogLayout())
                .setTitle("Enter HexCode for Colour")
                .setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Editable shortCodeText = shortCodeField.getText();
                                if (okListener != null && shortCodeText != null && shortCodeText.length() > 0) {
                                    okListener.onOkPressed(shortCodeText.toString());
                                }
                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        return builder.create();
    }
}
package com.abg.wordfinder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class WinDialogFragment extends DialogFragment {

    public interface Listener {
        void restart();
    }

    private Listener listener;
    private String time;

    public void setTime(String time) {
        this.time = time;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = getLayoutInflater().inflate(R.layout.fragment_congratilation, null);

        TextView textView = view.findViewById(R.id.time_win);
        textView.setText(getString(R.string.your_time) + time);
        Button b = view.findViewById(R.id.restart);
        b.setOnClickListener(v -> {
            listener.restart();
            dismiss();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setView(view);

        return builder.create();
    }
}

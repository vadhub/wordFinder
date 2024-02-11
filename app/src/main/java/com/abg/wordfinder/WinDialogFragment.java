package com.abg.wordfinder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class WinDialogFragment extends DialogFragment {

    public interface Listener {
        void restart();
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = getLayoutInflater().inflate(R.layout.fragment_congratilation, null);
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

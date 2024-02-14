package com.abg.wordfinder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.abg.wordfinder.datasource.Configuration;

import java.util.Locale;

public class SettingsDialogFragment extends DialogFragment implements View.OnClickListener {

    private Configuration saveConfiguration;
    private ChangeLanguageListener listener;

    public interface ChangeLanguageListener {
        void change();
    }

    public void setListener(ChangeLanguageListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = getLayoutInflater().inflate(R.layout.dialog_settings, null);
        RadioButton radioButtonRu = view.findViewById(R.id.radioButtonRu);
        RadioButton radioButtonEn = view.findViewById(R.id.radioButtonEn);

         saveConfiguration = new Configuration(view.getContext());

        if (LocaleChange.getLocale(view.getContext()).equals("ru")) {
            radioButtonRu.setChecked(true);
        } else {
            radioButtonEn.setChecked(true);
        }

        radioButtonRu.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setView(view).setPositiveButton("Ok",((dialog, which) -> {
            listener.change();
            dialog.dismiss();
        }));

        return builder.create();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.radioButtonEn) {
            LocaleChange.setLocale(v.getContext(), Locale.ENGLISH.getLanguage());
            saveConfiguration.saveLocale("en");
        } else if (v.getId() == R.id.radioButtonRu) {
            LocaleChange.setLocale(v.getContext(), "ru");
            saveConfiguration.saveLocale("ru");
        }
    }
}

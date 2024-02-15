package com.abg.wordfinder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import com.abg.wordfinder.datasource.Configuration;

import java.util.Locale;

public class SettingsDialogFragment extends DialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Configuration saveConfiguration;
    private ChangeLanguageListener listener;
    private SettingsDisplayListener settingsDisplayListener;
    private SwitchCompat showGrid;
    private SwitchCompat showContrast;

    public interface ChangeLanguageListener {
        void change();
    }

    public interface SettingsDisplayListener {
        void contrastChange(boolean isContrast);
        void gridChange(boolean isGrid);
    }

    public void setSettingsDisplayListener(SettingsDisplayListener settingsDisplayListener) {
        this.settingsDisplayListener = settingsDisplayListener;
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
        showGrid = view.findViewById(R.id.switchGrid);
        showContrast = view.findViewById(R.id.switchContrast);

        showGrid.setOnCheckedChangeListener(this);
        showContrast.setOnCheckedChangeListener(this);

        saveConfiguration = new Configuration(view.getContext());
        showGrid.setChecked(saveConfiguration.getGrid());
        showContrast.setChecked(saveConfiguration.getContrast());

        if (LocaleChange.getLocale(view.getContext()).equals("ru")) {
            radioButtonRu.setChecked(true);
        } else {
            radioButtonEn.setChecked(true);
        }

        radioButtonRu.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setView(view).setPositiveButton("Ok",((dialog, which) -> {
            dialog.dismiss();
        }));

        return builder.create();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.radioButtonEn) {
            LocaleChange.setLocale(v.getContext(), Locale.ENGLISH.getLanguage());
            saveConfiguration.saveLocale("en");
            listener.change();
        } else if (v.getId() == R.id.radioButtonRu) {
            LocaleChange.setLocale(v.getContext(), "ru");
            saveConfiguration.saveLocale("ru");
            listener.change();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (showContrast.equals(buttonView)) {
            settingsDisplayListener.contrastChange(isChecked);
            saveConfiguration.saveContrast(isChecked);
        } else if (showGrid.equals(buttonView)) {
            settingsDisplayListener.gridChange(isChecked);
            saveConfiguration.saveGrid(isChecked);
        }
    }
}

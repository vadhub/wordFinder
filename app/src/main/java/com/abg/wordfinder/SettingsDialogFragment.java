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
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import com.abg.wordfinder.datasource.Configuration;

import java.util.Locale;
import java.util.Objects;

public class SettingsDialogFragment extends DialogFragment implements View.OnClickListener {

    private Configuration saveConfiguration;
    private ChangeLanguageListener listener;
    private SettingsDisplayListener settingsDisplayListener;
    private SwitchCompat showGrid;
    private SwitchCompat showContrast;

    public interface ChangeLanguageListener {
        void change();
    }

    public interface SettingsDisplayListener {
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

        showGrid.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SoundManager.playSwitch();
            if (settingsDisplayListener != null) {
                settingsDisplayListener.gridChange(isChecked);
                saveConfiguration.saveGrid(isChecked);
            }
        });

        showContrast.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SoundManager.playSwitch();
            if (showContrast.equals(buttonView)) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                saveConfiguration.saveContrast(isChecked);
            }
        });

        saveConfiguration = new Configuration(view.getContext());
        showGrid.setChecked(saveConfiguration.getGrid());
        showContrast.setChecked(saveConfiguration.getContrast());

        if (Objects.equals(saveConfiguration.getLocale(), "ru")) {
            radioButtonRu.setChecked(true);
        } else {
            radioButtonEn.setChecked(true);
        }

        radioButtonRu.setOnClickListener(this);
        radioButtonEn.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setView(view).setPositiveButton("Ok",((dialog, which) -> {
            dialog.dismiss();
            SoundManager.playCock();
        }));

        return builder.create();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.radioButtonEn) {
            LocaleChange.setLocale(v.getContext(), Locale.ENGLISH.getLanguage());
            saveConfiguration.saveLocale("en");
            listener.change();
        }

        if (v.getId() == R.id.radioButtonRu) {
            LocaleChange.setLocale(v.getContext(), "ru");
            saveConfiguration.saveLocale("ru");
            listener.change();
        }

        SoundManager.playSwitch();
    }
}

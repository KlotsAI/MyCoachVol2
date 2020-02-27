package com.example.mycoach.ui.traning_machines;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mycoach.R;

public class TrainingMachinesFragment extends Fragment {

    private TrainingMachinesViewModel trainingMachinesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        trainingMachinesViewModel =
                ViewModelProviders.of(this).get(TrainingMachinesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_training_machines, container, false);
        final TextView textView = root.findViewById(R.id.text_training_machines);
        trainingMachinesViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
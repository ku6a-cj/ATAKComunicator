package com.example.atakcomunicator.ui.otherfunctions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.atakcomunicator.MainActivity;
import com.example.atakcomunicator.databinding.FragmentOtherFunctionsBinding;

public class otherFunctions extends Fragment {

    private FragmentOtherFunctionsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        otherFucntionsViewModel infoViewModel =
                new ViewModelProvider(this).get(otherFucntionsViewModel.class);

        binding = FragmentOtherFunctionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.AddAtackAircraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).addAtackAircraft();
            }
        });

        binding.AddStrikeAircraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).addStrikeAircraft();
            }
        });

        binding.MarkMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setMyLocation();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
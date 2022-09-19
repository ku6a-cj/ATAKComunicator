package com.example.atakcomunicator.ui.api;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.atakcomunicator.MainActivity;
import com.example.atakcomunicator.databinding.FragmentApiBinding;

public class ApiFragment extends Fragment {

    private FragmentApiBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ApiViewModel galleryViewModel =
                new ViewModelProvider(this).get(ApiViewModel.class);

        binding = FragmentApiBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.buttonApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).ApiGetInfo();
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
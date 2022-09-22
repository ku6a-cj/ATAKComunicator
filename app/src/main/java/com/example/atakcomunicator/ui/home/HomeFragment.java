package com.example.atakcomunicator.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.atakcomunicator.MainActivity;
import com.example.atakcomunicator.R;
import com.example.atakcomunicator.databinding.FragmentHomeBinding;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edMess = binding.edMessage;
                String Mess = edMess.getText().toString().trim();
                ((MainActivity)getActivity()).sendMessage2(Mess);
                ((MainActivity)getActivity()).showMessage(Mess, Color.BLUE);
            }
        });

        binding.btnConnectServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edIP = binding.edMessage;
                String IPP = edIP.getText().toString().trim();
                ((MainActivity)getActivity()).connectToServer(IPP);
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
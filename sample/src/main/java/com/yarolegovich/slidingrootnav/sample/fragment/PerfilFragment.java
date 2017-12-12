package com.yarolegovich.slidingrootnav.sample.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yarolegovich.slidingrootnav.sample.R;

public class PerfilFragment extends Fragment {

    View inflatedView = null;
    Context mCtx;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflatedView = inflater.inflate(R.layout.perfil_fragment_layout, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Perfild de usuario");

        return inflatedView;
    }
}

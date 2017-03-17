package my.mynato.rahmatridham.mynato.LandingPageMenus;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import my.mynato.rahmatridham.mynato.Config;
import my.mynato.rahmatridham.mynato.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    TextView nama, jabatan, lokasi;

    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nama = (TextView) view.findViewById(R.id.NamaProfil);
        jabatan = (TextView) view.findViewById(R.id.JabatanProfil);
        lokasi = (TextView) view.findViewById(R.id.kantorProfil);

        SharedPreferences sharedPreferences = Profile.this.getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        nama.setText(sharedPreferences.getString(Config.NAMA_SHARED_PREF,"null"));
        jabatan.setText(sharedPreferences.getString(Config.JABATAN_SHARED_PREF,"null"));
        lokasi.setText(sharedPreferences.getString(Config.KODEPOSISI_SHARED_PREF,"null"));

        return view;
    }

}

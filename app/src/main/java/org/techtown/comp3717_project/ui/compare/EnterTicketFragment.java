package org.techtown.comp3717_project.ui.compare;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.techtown.comp3717_project.CompareActivity;
import org.techtown.comp3717_project.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EnterTicketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnterTicketFragment extends Fragment {

    Button button;
    CompareActivity compareActivity;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EnterTicketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EnterTicketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EnterTicketFragment newInstance(String param1, String param2) {
        EnterTicketFragment fragment = new EnterTicketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        compareActivity = (CompareActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        compareActivity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_enter_ticket, container, false);

        button = rootView.findViewById(R.id.submit);
        button.setOnClickListener(v -> compareActivity.fragmentChange(2));

        return rootView;
    }

}
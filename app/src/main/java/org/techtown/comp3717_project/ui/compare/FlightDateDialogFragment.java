package org.techtown.comp3717_project.ui.compare;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import org.techtown.comp3717_project.CompareActivity;

import java.util.Calendar;

public class FlightDateDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    CompareActivity compareActivity;

    public FlightDateDialogFragment() {};

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(requireContext(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        compareActivity.getEnterTicketFragment().setDate(year, month + 1, day);
        dismiss();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        compareActivity = (CompareActivity) getActivity();
    }
}
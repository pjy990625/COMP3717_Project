package org.techtown.comp3717_project.ui.compare;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.techtown.comp3717_project.R;

public class ViewTicketFragment extends Fragment {

    Image comparisonResultImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_view_ticket, container, false);
        TextView headingText = rootView.findViewById(R.id.ticketComparisonHeadingText);
        TextView resultText = rootView.findViewById(R.id.ticketComparisonResultText);
        ImageView resultImage = rootView.findViewById(R.id.ticketComparisonResultImage);
        Bundle bundle = getArguments();
        // check if there were any data retrieved
        if (bundle.getString("isCheaper").equals("unknown")) {
            headingText.setText(R.string.comparison_heading_unknown);
            resultText.setText(R.string.comparison_result_unknown);
            resultImage.setImageResource(R.drawable.image_ticket_comparison_unknown);
            return rootView;
        }
        // otherwise, given values from bundle (results of ticket price comparison), update the result page
        headingText.setText(getString(R.string.comparison_heading_message, bundle.getString("price")));
        resultText.setText(getString(R.string.comparison_result_message, bundle.getString("medium")));
        resultImage.setImageResource(bundle.getString("isCheaper").equals("true") ? R.drawable.image_ticket_comparison_cheaper : R.drawable.image_ticket_comparison_not_cheaper);
        return rootView;
    }

}
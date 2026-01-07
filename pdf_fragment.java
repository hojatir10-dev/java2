package com.example.smartstudent;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;

public class PdfFragment extends Fragment {

    private RecyclerView recyclerView;
    private PDFPageAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_pdf_fragment, container, false);
        recyclerView = view.findViewById(R.id.pdfRecyclerView);

        int spanCount = getSpanCount(getResources().getConfiguration().orientation);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));

        try {
            adapter = new PDFPageAdapter(getContext(), "Unit 1.pdf");
            recyclerView.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;
    }

    private int getSpanCount(int orientation) {
        return (orientation == Configuration.ORIENTATION_LANDSCAPE) ? 2 : 1;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adapter != null) adapter.close();
    }
}

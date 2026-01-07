package com.example.smartstudent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PDFPageAdapter extends RecyclerView.Adapter<PDFPageAdapter.PageViewHolder> {

    private final Context context;
    private final int pageCount;
    private final PdfRenderer pdfRenderer;
    private final ParcelFileDescriptor fileDescriptor;

    public PDFPageAdapter(Context context, String assetFileName) throws IOException {
        this.context = context;

        
        File file = new File(context.getFilesDir(), assetFileName);
        if (!file.exists()) {
            try (InputStream asset = context.getAssets().open(assetFileName);
                 OutputStream output = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int read;
                while ((read = asset.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }
            }
        }

        fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        pdfRenderer = new PdfRenderer(fileDescriptor);
        pageCount = pdfRenderer.getPageCount();
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pdf_page, parent, false);
        return new PageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        try {
            PdfRenderer.Page page = pdfRenderer.openPage(position);
            Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(),
                    Bitmap.Config.ARGB_8888);
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            holder.imageView.setImageBitmap(bitmap);
            page.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return pageCount;
    }

    static class PageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        PageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.pdfPageImage);
        }
    }

    public void close() {
        try {
            pdfRenderer.close();
            fileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

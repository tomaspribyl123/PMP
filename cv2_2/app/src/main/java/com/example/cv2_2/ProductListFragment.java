package com.example.cv2_2;

import android.os.Bundle;
import android.view.LayoutInflater;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends Fragment {

    private List<Product> productList = new ArrayList<>();
    private TableLayout tableLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        tableLayout = view.findViewById(R.id.product_table);
        createTableHeader();

        Button addButton = view.findViewById(R.id.button_add_product);
        Button calculateButton = view.findViewById(R.id.button_calculate);

        addButton.setOnClickListener(v -> {
            AddProductDialogFragment dialog = new AddProductDialogFragment(product -> {
                addProductToTable(product);
            });
            dialog.show(getParentFragmentManager(), "AddProductDialog");
        });

        calculateButton.setOnClickListener(v -> calculateBestDeal());

        return view;
    }

    private void calculateBestDeal() {
        if (tableLayout.getChildCount() <= 1) return;

        double bestRatio = Double.MAX_VALUE;
        TableRow bestRow = null;

        for (int i = 1; i < tableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            TextView priceView = (TextView) row.getChildAt(2);
            TextView quantityView = (TextView) row.getChildAt(1);

            double price = Double.parseDouble(priceView.getText().toString().replace(" Kč", ""));
            int quantity = Integer.parseInt(quantityView.getText().toString());

            double ratio = price / quantity;

            if (ratio < bestRatio) {
                bestRatio = ratio;
                bestRow = row;
            }
        }

        for (int i = 1; i < tableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            if (row == bestRow) {
                row.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green));
            } else {
                row.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red));
            }
        }
    }

    private void addProductToTable(Product product) {
        TableRow row = new TableRow(getContext());

        int padding = 16;

        TextView nameView = new TextView(getContext());
        nameView.setText(product.getName());
        nameView.setPadding(padding, padding, padding, padding);

        TextView quantityView = new TextView(getContext());
        quantityView.setText(String.valueOf(product.getQuantity()));
        quantityView.setPadding(padding, padding, padding, padding);

        TextView priceView = new TextView(getContext());
        priceView.setText(String.valueOf(product.getPrice()) + " Kč");
        priceView.setPadding(padding, padding, padding, padding);

        TextView deleteButton = new TextView(getContext());
        deleteButton.setText("❌");
        deleteButton.setPadding(16, 16, 16, 16);
        deleteButton.setTextSize(18);
        deleteButton.setClickable(true);
        deleteButton.setFocusable(true);
        deleteButton.setOnClickListener(v -> {
            tableLayout.removeView(row);
            productList.remove(product);
            calculateBestDeal();
        });


        row.addView(nameView);
        row.addView(quantityView);
        row.addView(priceView);
        row.addView(deleteButton);

        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        );
        rowParams.setMargins(16, 8, 16, 8);
        row.setLayoutParams(rowParams);

        tableLayout.addView(row);
    }

    private void createTableHeader() {
        if (tableLayout.getChildCount() > 0) {
            TableRow firstRow = (TableRow) tableLayout.getChildAt(0);
            TextView firstCell = (TextView) firstRow.getChildAt(0);
            if (firstCell != null && firstCell.getText().toString().equals("Název")) {
                return;
            }
        }

        TableRow headerRow = new TableRow(getContext());

        TextView headerName = new TextView(getContext());
        headerName.setText("Název");
        headerName.setPadding(16, 16, 16, 16);
        headerName.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView headerQuantity = new TextView(getContext());
        headerQuantity.setText("Množství");
        headerQuantity.setPadding(16, 16, 16, 16);
        headerQuantity.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView headerPrice = new TextView(getContext());
        headerPrice.setText("Cena");
        headerPrice.setPadding(16, 16, 16, 16);
        headerPrice.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView headerDelete = new TextView(getContext());
        headerDelete.setText("❌");
        headerDelete.setPadding(16, 16, 16, 16);
        headerDelete.setTypeface(null, android.graphics.Typeface.BOLD);

        headerRow.addView(headerName);
        headerRow.addView(headerQuantity);
        headerRow.addView(headerPrice);
        headerRow.addView(headerDelete);

        tableLayout.addView(headerRow, 0);
    }



}
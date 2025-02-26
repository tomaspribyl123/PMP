package com.example.cv2_2;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

public class AddProductDialogFragment extends DialogFragment {

    private OnProductAddedListener listener;

    public interface OnProductAddedListener {
        void onProductAdded(Product product);
    }

    public AddProductDialogFragment(OnProductAddedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_product, null);
        builder.setView(view);

        EditText nameInput = view.findViewById(R.id.edit_product_name);
        EditText priceInput = view.findViewById(R.id.edit_product_price);
        EditText quantityInput = view.findViewById(R.id.edit_product_quantity);
        Button addButton = view.findViewById(R.id.button_add);
        Button cancelButton = view.findViewById(R.id.button_cancel);

        addButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            String priceText = priceInput.getText().toString();
            String quantityText = quantityInput.getText().toString();

            if (!name.isEmpty() && !priceText.isEmpty() && !quantityText.isEmpty()) {
                double price = Double.parseDouble(priceText);
                int quantity = Integer.parseInt(quantityText);

                Product product = new Product(name, price, quantity);
                listener.onProductAdded(product);
                dismiss();
            }
        });

        cancelButton.setOnClickListener(v -> dismiss());

        return builder.create();
    }
}
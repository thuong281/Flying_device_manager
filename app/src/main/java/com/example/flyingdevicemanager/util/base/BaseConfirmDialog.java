package com.example.flyingdevicemanager.util.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.flyingdevicemanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseConfirmDialog extends Dialog {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.btn_close)
    Button btnClose;

    private String dialogTitle = "";
    private String dialogMessage = "";
    private String dialogConfirmText = "";
    private String dialogCloseText = "";

    public BaseConfirmDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public BaseConfirmDialog setTitle(String title) {
        this.dialogTitle = title;
        return this;
    }

    public BaseConfirmDialog setDialogMessage(String message) {
        this.dialogMessage = message;
        return this;
    }

    public BaseConfirmDialog setDialogConfirmText(String confirmText) {
        this.dialogConfirmText = confirmText;
        return this;
    }

    public BaseConfirmDialog setDialogCloseText(String closeText) {
        this.dialogCloseText = closeText;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupDialog();
        ButterKnife.bind(this);

        setupViews();
        initEvents();
    }


    private void setupDialog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        setContentView(R.layout.confirm_dialog);
    }

    private void setupViews() {
        tvTitle.setText(dialogTitle);
        tvMessage.setText(Html.fromHtml(dialogMessage));
        btnConfirm.setText(dialogConfirmText);
        btnClose.setText(dialogCloseText);
    }

    private void initEvents() {
        btnConfirm.setOnClickListener(view -> {
            if (listener != null) {
                listener.onConfirmDialog();
            }
        });

        btnClose.setOnClickListener(view -> dismiss());
    }

    private OnConfirmDialogListener listener;

    public void setOnConfirmDialogListener(OnConfirmDialogListener listener) {
        this.listener = listener;
    }

    public interface OnConfirmDialogListener {
        void onConfirmDialog();
    }
}

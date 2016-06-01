package com.android.goexpense;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by akanksha on 31/5/16.
 */
public class AddDialog implements View.OnClickListener {

    private Dialog mDialog;
    private Context mContext;
    private AmountDialogCallback callback;
    private EditText typeExpenseView, expenseView;

    public AddDialog(Context context, AmountDialogCallback callback) {
        this.mContext = context;
        this.callback = callback;
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.dialog_add);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        initViews();
    }

    private void initViews() {
        typeExpenseView = (EditText) mDialog.findViewById(R.id.type_expense_view);
        expenseView = (EditText) mDialog.findViewById(R.id.expense_view);
        Button doneBtn = (Button) mDialog.findViewById(R.id.done_btn);
        if (doneBtn != null) {
            doneBtn.setOnClickListener(this);
        }
    }

    public void showDialog() {
        if (mDialog == null) {
            return;
        }
        mDialog.setCanceledOnTouchOutside(false);

        if (!mDialog.isShowing())
            mDialog.show();
    }

    public void dismissDialog() {
        if (mDialog == null || !mDialog.isShowing())
            return;
        mDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done_btn:
                onDone();
        }
    }

    private void onDone() {
        String type = typeExpenseView.getText().toString();
        String expenseString = expenseView.getText().toString();
        if (TextUtils.isEmpty(type) || TextUtils.isEmpty(expenseString) || !isValidDouble(expenseString)) {
            Toast.makeText(mContext, "Are you sure you wanna exit without saving. Half info won't be saved.", Toast.LENGTH_LONG).show();
        } else {
            callback.onAmountFilled(type + " : " + expenseString + " : " + getDisplayDatePattern(new Date()));
            dismissDialog();
        }
    }

    private boolean isValidDouble(String expenseString) {
        try {
            Double.parseDouble(expenseString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getDisplayDatePattern(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d 'at' hh:mm 'hr'", Locale.getDefault());
        return formatter.format(date);
    }

}

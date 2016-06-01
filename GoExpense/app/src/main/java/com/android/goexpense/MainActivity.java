package com.android.goexpense;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by akanksha on 31/5/16.
 */
public class MainActivity extends Activity implements AmountDialogCallback {

    private final static String MONTH_LIST = "MONTH_LIST";
    private SharedPreferences mPrefs;
    private ArrayList<String> expenseList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initValues();
        initViews();
    }

    private void initValues() {
        mPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String expense = mPrefs.getString(MONTH_LIST, null);
        if (expense != null) {
            String[] expenseArr = expense.split(",");
            Collections.addAll(expenseList, expenseArr);
        }
    }

    private void initViews() {
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, expenseList);
        listView.setAdapter(adapter);

        Button addNewBtn = (Button) findViewById(R.id.add_new_btn);
        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDialog addDialog = new AddDialog(MainActivity.this, MainActivity.this);
                addDialog.showDialog();
            }
        });

        Button clearBtn = (Button) findViewById(R.id.clear_btn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expenseList.size() > 0) {
                    createPrompt("Are you sure you want to clear all data.");
                }
            }
        });
    }

    @Override
    public void onAmountFilled(String amountInfo) {
        StringBuilder expensebuilder = new StringBuilder();
        for (String expense : expenseList) {
            expensebuilder.append(expense).append(",");
        }
        expensebuilder.append(amountInfo);
        expenseList.add(amountInfo);
        mPrefs.edit().putString(MONTH_LIST, expensebuilder.toString()).apply();
        adapter.notifyDataSetChanged();
        listView.smoothScrollToPosition(expenseList.size());
    }

    public void createPrompt(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clear();
                        dialog.dismiss();
                    }

                    private void clear() {
                        expenseList.clear();
                        adapter.notifyDataSetChanged();
                        mPrefs.edit().putString(MONTH_LIST, null).apply();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}

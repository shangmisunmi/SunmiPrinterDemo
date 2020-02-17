package com.sunmi.printerhelper.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;

import com.sunmi.printerhelper.R;
import com.sunmi.printerhelper.utils.BluetoothUtil;
import com.sunmi.printerhelper.utils.BytesUtil;
import com.sunmi.printerhelper.utils.ESCUtil;
import com.sunmi.printerhelper.utils.SunmiPrintHelper;

import java.util.LinkedList;

import sunmi.sunmiui.button.ButtonRectangular;

/**
 * Example of printing table
 * @author kaltin
 */
public class TableActivity extends BaseActivity {
    ListView mListView;
    TextView mTextView;
    ButtonRectangular footView;
    TableAdapter ta;
    LinkedList<TableItem> datalist;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        setMyTitle(R.string.tab_title);
        setBack();

        mListView = findViewById(R.id.table_list);
        mTextView = findViewById(R.id.table_tips);
        if (BluetoothUtil.isBlueToothPrinter) {
            mTextView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        } else {
            initListView();
        }
    }

    private void initListView() {
        footView = new ButtonRectangular(this);
        footView.setTitleText(getResources().getString(R.string.add_line));
        footView.setTextColorEnabled(R.color.black);
        footView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOneData(datalist);
                ta.notifyDataSetChanged();
            }
        });
        mListView.addFooterView(footView);
        datalist = new LinkedList<>();
        addOneData(datalist);
        ta = new TableAdapter();
        mListView.setAdapter(ta);
    }

    public void onClick(View view) {
        if (!BluetoothUtil.isBlueToothPrinter) {
            for (TableItem tableItem : datalist) {
                SunmiPrintHelper.getInstance().printTable(tableItem.getText(),
                        tableItem.getWidth(), tableItem.getAlign());
            }
            SunmiPrintHelper.getInstance().feedPaper();
        } else {
            BluetoothUtil.sendData(ESCUtil.printBitmap(BytesUtil.initTable(6, 12)));
        }
    }

    public void addOneData(LinkedList<TableItem> data) {
        TableItem ti = new TableItem();
        data.add(ti);
    }

    private class ViewHolder implements View.OnFocusChangeListener, AdapterView.OnItemSelectedListener {
        TextView mText;
        EditText mText1, mText2, mText3;
        EditText width1, width2, width3;
        AppCompatSpinner align1, align2, align3;
        EditText view;
        int line;


        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            Log.d("kaltin", v.getId() + ">>" + hasFocus);
            if (hasFocus) {
                view = (EditText) v;
                return;
            }
            TableItem ti = datalist.get(line);
            switch (v.getId()) {
                case R.id.it_text3:
                    (ti.getText())[2] = ((EditText) v).getText().toString();
                    break;
                case R.id.it_text2:
                    (ti.getText())[1] = ((EditText) v).getText().toString();
                    break;
                case R.id.it_text1:
                    (ti.getText())[0] = ((EditText) v).getText().toString();
                    break;
                case R.id.it_width3:
                    (ti.getWidth())[2] = Integer.parseInt(((EditText) v).getText().toString());
                    break;
                case R.id.it_width2:
                    (ti.getWidth())[1] = Integer.parseInt(((EditText) v).getText().toString());
                    break;
                case R.id.it_width1:
                    (ti.getWidth())[0] = Integer.parseInt(((EditText) v).getText().toString());
                    break;
                default:
                    break;
            }
            v = null;
        }


        public void setCallback() {
            if (mText1 == null || mText2 == null || mText3 == null
                    || width1 == null || width2 == null || width3 == null
                    || align1 == null || align2 == null || align3 == null) {
                return;
            }

            mText1.setOnFocusChangeListener(this);
            mText2.setOnFocusChangeListener(this);
            mText3.setOnFocusChangeListener(this);
            width1.setOnFocusChangeListener(this);
            width2.setOnFocusChangeListener(this);
            width3.setOnFocusChangeListener(this);

            align1.setOnItemSelectedListener(this);
            align2.setOnItemSelectedListener(this);
            align3.setOnItemSelectedListener(this);
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.i("kaltin", "itemclick1");
            TableItem ti = datalist.get(line);
            switch (parent.getId()) {
                case R.id.it_align3:
                    (ti.getAlign())[2] = position;
                    break;
                case R.id.it_align2:
                    (ti.getAlign())[1] = position;
                    break;
                case R.id.it_align1:
                    (ti.getAlign())[0] = position;
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class TableAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return datalist.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder v = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(TableActivity.this).inflate(R.layout.item_table, null);
                v = new ViewHolder();
                v.mText = convertView.findViewById(R.id.it_title);
                v.mText1 = convertView.findViewById(R.id.it_text1);
                v.mText2 = convertView.findViewById(R.id.it_text2);
                v.mText3 = convertView.findViewById(R.id.it_text3);
                v.width1 = convertView.findViewById(R.id.it_width1);
                v.width2 = convertView.findViewById(R.id.it_width2);
                v.width3 = convertView.findViewById(R.id.it_width3);
                v.align1 = convertView.findViewById(R.id.it_align1);
                v.align2 = convertView.findViewById(R.id.it_align2);
                v.align3 = convertView.findViewById(R.id.it_align3);
                v.setCallback();
                convertView.setTag(v);
            } else {
                v = (ViewHolder) convertView.getTag();
            }

            v.line = position;
            v.mText.setText("Row." + (++position));
            if (v.view != null) {
                v.view.requestFocus();
            }
            return convertView;
        }
    }

    private class TableItem {
        private String[] text;
        private int[] width;
        private int[] align;

        public TableItem() {
            text = new String[]{"test","test","test"};
            width = new int[]{1,1,1};
            align = new int[]{0,0,0};
        }

        public String[] getText() {
            return text;
        }

        public void setText(String[] text) {
            this.text = text;
        }

        public int[] getWidth() {
            return width;
        }

        public void setWidth(int[] width) {
            this.width = width;
        }

        public int[] getAlign() {
            return align;
        }

        public void setAlign(int[] align) {
            this.align = align;
        }
    }
}

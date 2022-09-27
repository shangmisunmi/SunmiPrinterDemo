package com.sunmi.printerhelper.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.sunmi.printerhelper.R;
import com.sunmi.printerhelper.utils.BluetoothUtil;
import com.sunmi.printerhelper.utils.ESCUtil;
import com.sunmi.printerhelper.utils.SunmiPrintHelper;

import java.io.IOException;

import sunmi.sunmiui.dialog.DialogCreater;
import sunmi.sunmiui.dialog.ListDialog;

/**
 * Example of printing text
 */
public class TextActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    private TextView mTextView, mTextView1, mTextView2;
    private CheckBox mCheckBox1, mCheckBox2;
    private EditText mEditText;
    private LinearLayout mLayout, mLinearLayout;
    private int record;
    //Font usage variables
    private String testFont;
    private boolean isBold, isUnderLine;

    private String[] mStrings = new String[]{"CP437", "CP850", "CP860", "CP863", "CP865", "CP857", "CP737", "CP928", "Windows-1252", "CP866", "CP852", "CP858", "CP874", "Windows-775", "CP855", "CP862", "CP864", "GB18030", "BIG5", "KSC5601", "utf-8"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        setMyTitle(R.string.text_title);
        setBack();

        testFont = null;
        record = 17;
        isBold = false;
        isUnderLine = false;
        mTextView = findViewById(R.id.text_text_font);
        mTextView1 = findViewById(R.id.text_text_character);
        mTextView2 = findViewById(R.id.text_text_size);
        mCheckBox1 = findViewById(R.id.text_bold);
        mCheckBox2 = findViewById(R.id.text_underline);
        mEditText = findViewById(R.id.text_text);

        mLinearLayout = findViewById(R.id.text_all);
        mLayout = findViewById(R.id.text_set);

        mLinearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                mLinearLayout.getWindowVisibleDisplayFrame(r);
                if(r.bottom < 800){
                    mLayout.setVisibility(View.GONE);
                }else{
                    mLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        mCheckBox1.setOnCheckedChangeListener(this);
        mCheckBox2.setOnCheckedChangeListener(this);

        findViewById(R.id.text_font).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] fontStrings = new String[]{getResources().getString(R.string.sunmi_font),
                        getResources().getString(R.string.custom_font)};
                final ListDialog listDialog = DialogCreater.createListDialog(TextActivity.this,
                        getResources().getString(R.string.type_text),
                        getResources().getString(R.string.cancel),
                        fontStrings);
                listDialog.setItemClickListener(new ListDialog.ItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        mTextView.setText(fontStrings[position]);
                        if(position > 0){
                            testFont = "test.ttf";
                        } else {
                            testFont = null;
                        }
                        listDialog.cancel();
                    }
                });
                listDialog.show();
            }
        });


        findViewById(R.id.text_character).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ListDialog listDialog = DialogCreater.createListDialog(TextActivity.this, getResources().getString(R.string.characterset), getResources().getString(R.string.cancel), mStrings);
                listDialog.setItemClickListener(new ListDialog.ItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        mTextView1.setText(mStrings[position]);
                        record = position;
                        listDialog.cancel();
                    }
                });
                listDialog.show();
            }
        });

        findViewById(R.id.text_size).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSeekBarDialog(TextActivity.this, getResources().getString(R.string.size_text), 12, 36, mTextView2);
            }
        });
    }

    public void onClick(View view) {
        String content = mEditText.getText().toString();

        float size = Integer.parseInt(mTextView2.getText().toString());
        if (!BluetoothUtil.isBlueToothPrinter) {
            SunmiPrintHelper.getInstance().printText(content, size, isBold, isUnderLine, testFont);
            SunmiPrintHelper.getInstance().feedPaper();
        } else {
            printByBluTooth(content);
        }
    }

    private void printByBluTooth(String content) {
        try {
            if (isBold) {
                BluetoothUtil.sendData(ESCUtil.boldOn());
            } else {
                BluetoothUtil.sendData(ESCUtil.boldOff());
            }

            if (isUnderLine) {
                BluetoothUtil.sendData(ESCUtil.underlineWithOneDotWidthOn());
            } else {
                BluetoothUtil.sendData(ESCUtil.underlineOff());
            }

            if (record < 17) {
                BluetoothUtil.sendData(ESCUtil.singleByte());
                BluetoothUtil.sendData(ESCUtil.setCodeSystemSingle(codeParse(record)));
            } else {
                BluetoothUtil.sendData(ESCUtil.singleByteOff());
                BluetoothUtil.sendData(ESCUtil.setCodeSystem(codeParse(record)));
            }

            BluetoothUtil.sendData(content.getBytes(mStrings[record]));
            BluetoothUtil.sendData(ESCUtil.nextLine(3));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte codeParse(int value) {
        byte res = 0x00;
        switch (value) {
            case 0:
                res = 0x00;
                break;
            case 1:
            case 2:
            case 3:
            case 4:
                res = (byte) (value + 1);
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
                res = (byte) (value + 8);
                break;
            case 12:
                res = 21;
                break;
            case 13:
                res = 33;
                break;
            case 14:
                res = 34;
                break;
            case 15:
                res = 36;
                break;
            case 16:
                res = 37;
                break;
            case 17:
            case 18:
            case 19:
                res = (byte) (value - 17);
                break;
            case 20:
                res = (byte) 0xff;
                break;
            default:
                break;
        }
        return (byte) res;
    }

    /**
     * 自定义的seekbar dialog
     *
     * @param context
     * @param title
     * @param min
     * @param max
     * @param set
     */
    private void showSeekBarDialog(Context context, String title, final int min, final int max, final TextView set) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.widget_seekbar, null);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        TextView tv_title = view.findViewById(R.id.sb_title);
        TextView tv_start = view.findViewById(R.id.sb_start);
        TextView tv_end = view.findViewById(R.id.sb_end);
        final TextView tv_result = view.findViewById(R.id.sb_result);
        TextView tv_ok = view.findViewById(R.id.sb_ok);
        TextView tv_cancel = view.findViewById(R.id.sb_cancel);
        SeekBar sb = view.findViewById(R.id.sb_seekbar);
        tv_title.setText(title);
        tv_start.setText(min + "");
        tv_end.setText(max + "");
        tv_result.setText(set.getText());
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set.setText(tv_result.getText());
                dialog.cancel();
            }
        });
        sb.setMax(max - min);
        sb.setProgress(Integer.parseInt(set.getText().toString()) - min);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int rs = min + progress;
                tv_result.setText(rs + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        dialog.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.text_bold:
                isBold = isChecked;
                break;
            case R.id.text_underline:
                isUnderLine = isChecked;
                break;
            default:
                break;
        }
    }
}

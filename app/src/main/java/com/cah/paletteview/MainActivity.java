package com.cah.paletteview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cah.paletteview.view.PaletteView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.palette_view)
    PaletteView mPaletteView;
    @BindView(R.id.bezier_btn)
    RadioButton mBezierBtn;
    @BindView(R.id.eraser_btn)
    RadioButton mEraserBtn;
    @BindView(R.id.clear_btn)
    RadioButton mClearBtn;
    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;

    private PaletteView.Mode mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int id) {
                switch (id) {
                    case R.id.bezier_btn:
                        if (mPaletteView.getMode() == PaletteView.Mode.BEZIER)
                            return;
                        mPaletteView.setMode(PaletteView.Mode.BEZIER);
                        mPaletteView.setPaintColor("#FFFFFFFF");
                        mPaletteView.setPaintWidth(30);
                        break;
                    case R.id.eraser_btn:
                        if (mPaletteView.getMode() == PaletteView.Mode.ERASER)
                            return;
                        mPaletteView.setMode(PaletteView.Mode.ERASER);
//                        mPaletteView.setEraserColor("#FFFFFFFF");
//                        mPaletteView.setEraserWidth(30);
                        break;
                    case R.id.clear_btn:
                        mode = mPaletteView.getMode();
                        mPaletteView.clear();
                        mClearBtn.setChecked(false);
                        if (mode == PaletteView.Mode.BEZIER)
                            mBezierBtn.setChecked(true);
                        if (mode == PaletteView.Mode.ERASER)
                            mEraserBtn.setChecked(true);
                        break;
                }
            }
        });
    }

}

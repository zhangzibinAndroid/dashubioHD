package com.returnlive.dashubiohd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.returnlive.dashubiohd.R;
import com.returnlive.dashubiohd.base.BaseActivity;
import com.zhy.autolayout.AutoLinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HelpActivity extends BaseActivity {

    @BindView(R.id.header_title)
    TextView headerTitle;
    @BindView(R.id.tv_helptitle)
    TextView tvHelptitle;
    @BindView(R.id.tv_helpcontext)
    TextView tvHelpcontext;
    @BindView(R.id.header_left_container)
    AutoLinearLayout header_left_container;
    private Unbinder unbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        unbinder = ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        headerTitle.setText(getResources().getString(R.string.use_help));
        header_left_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        String title = intent.getStringExtra("helpTitle");
        String content = intent.getStringExtra("helpContent");
        tvHelptitle.setText(title);
        tvHelpcontext.setText(content);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}

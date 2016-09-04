package com.qwwuyu.recite.fragment;

import android.view.View;
import android.widget.TextView;

import com.qwwuyu.recite.R;
import com.qwwuyu.recite.bean.EventBean;
import com.qwwuyu.recite.config.FinalConfig;
import com.qwwuyu.recite.config.ServerConfig;
import com.qwwuyu.recite.config.TApplication;
import com.qwwuyu.recite.ui.dialog.PickerDialog;
import com.qwwuyu.recite.utils.CommUtil;
import com.qwwuyu.recite.utils.SpUtil;
import com.qwwuyu.recite.utils.SystemUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 我的
 * Created by qw on 2016/8/21.
 */
public class MineFragment extends BaseFragment {
    /** 最小单词数量和最大单词数量 */
    private int minNum = 10, maxNum = 30;
    /** 每天学习的单词量 */
    @BindView(R.id.mine_txt_num4day)
    TextView txt_num4day;
    /** 每天学习的单词量 */
    @BindView(R.id.mine_txt_appNum)
    TextView txt_appNum;

    @Override
    protected int getContentViewId() {
        return R.layout.f_mine;
    }

    @Override
    public void getData() {
        if (isInited) {
            return;
        }
        isInited = true;
        initContent();
    }

    @Override
    protected void setData() {
        txt_num4day.setText(getString(R.string.mine_txt_num, ServerConfig.num4Day));
        txt_appNum.setText(getString(R.string.mine_appNum, SystemUtil.getVersionName(context)));
    }

    @OnClick({R.id.mine_ll_num4day, R.id.mine_ll_appNum})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_ll_num4day://每天学习的单词量
                selectNum();
                break;
            case R.id.mine_ll_appNum://检查版本
                CommUtil.go2Market(context, true);
                break;
            default:
                break;
        }
    }

    private void selectNum() {
        ArrayList<String> nums = new ArrayList<>();
        for (int i = minNum; i <= maxNum; i++) {
            nums.add(getString(R.string.mine_txt_num, i));
        }
        PickerDialog pickerDialog = new PickerDialog(context, nums, new PickerDialog.OnSelectListener() {
            @Override
            public void onSelect(int oldPosition, int position) {
                if (position != oldPosition) {
                    ServerConfig.num4Day = position + 10;
                    txt_num4day.setText(getString(R.string.mine_txt_num, ServerConfig.num4Day));
                    SpUtil.getSpUtil().putSPValue(SpUtil.SP_WORD4DAY, ServerConfig.num4Day);
                    int days = (TApplication.indexWords.size() + ServerConfig.num4Day - 1) / ServerConfig.num4Day;
                    if (days < ServerConfig.nowDay) {
                        ServerConfig.nowDay = days;
                        SpUtil.getSpUtil().putSPValue(SpUtil.SP_TODAY, ServerConfig.nowDay);
                        EventBus.getDefault().post(new EventBean(FinalConfig.EVENT_NOW_DAY_CHANGES));
                    }
                }
            }
        }, ServerConfig.num4Day - 10);
        pickerDialog.show();
    }
}

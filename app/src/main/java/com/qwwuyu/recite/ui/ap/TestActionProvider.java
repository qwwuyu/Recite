package com.qwwuyu.recite.ui.ap;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.SubMenu;
import android.view.View;

import com.qwwuyu.recite.R;


/**
 * 自定义ActionProvider,使用在Menu布局中
 */
public class TestActionProvider extends ActionProvider {
    public TestActionProvider(Context context) {
        super(context);
    }

    @Override
    public View onCreateActionView() {
        return null;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
        subMenu.clear();
        subMenu.add(0, R.id.menu_item3, 0, R.string.search).setIcon(R.drawable.img_menu);
        subMenu.add(0, R.id.menu_item4, 0, R.string.search).setIcon(R.drawable.img_menu);
    }

    @Override
    public boolean hasSubMenu() {
        return true;
    }
}
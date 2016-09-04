package com.qwwuyu.recite.ui.ap;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.SubMenu;
import android.view.View;

import com.qwwuyu.recite.R;


/**
 * Menu的Divider
 */
public class DividerActionProvider extends ActionProvider {
    private Context context;

    public DividerActionProvider(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View onCreateActionView() {
        return View.inflate(context, R.layout.divider_menu_horizontal, null);
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
    }

    @Override
    public boolean hasSubMenu() {
        return false;
    }
}
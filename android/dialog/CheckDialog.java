package com.dcop.jx.components.android.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ListView;

import com.dcop.jx.*;
import com.dcop.jx.frameworks.object.*;


/**
 * 多选项对话框
 */
public class CheckDialog
{
    private IObject m_iAct = null;

    /**
     * 创建对话框
     * @param int objIDAct Activity对象
     * @param String strTitle 标题
     * @param String[] strOption 选项列表
     * @param String strYes Yes按钮名
     * @param String strNo No按钮名
     */
    public CheckDialog(int objIDAct, String strTitle, String[] strOption, String strYes, String strNo)
    {
        m_iAct = ObjectManager.root.getObject(objIDAct);
        if (m_iAct == null) return;

        Activity theAct = (Activity)m_iAct;
        if (theAct == null) return;

        final String[] itemOption = strOption;

        new AlertDialog.Builder(theAct)
            .setTitle(strTitle)
            .setMultiChoiceItems(strOption, null, null)
            .setPositiveButton(strYes, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    ListView listView = ((AlertDialog)dialog).getListView();
                    boolean[] checked = new boolean[itemOption.length];
                    for (int i = 0; i < itemOption.length; i++)
                    {
                        if (listView.getCheckedItemPositions().get(i))
                        {
                            checked[i] = true;
                        }
                        else
                        {
                            checked[i] = false;
                        }
                    }
                    choice(itemOption, checked);
                }
            })
            .setNegativeButton(strNo, null)
            .show();

    }

    /**
     * choice被选择，需要覆盖
     * @param String[] strOption 对应项描述信息
     * @param boolean[] checked 对应项是否被选中
     */
    public void choice(String[] strOption, boolean[] checked)
    {
        
    }

}




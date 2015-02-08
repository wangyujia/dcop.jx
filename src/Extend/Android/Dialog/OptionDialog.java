package JX.Extend.Android.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import JX.*;
import JX.Framework.Object.*;


/**
 * 单选项对话框
 */
public class OptionDialog
{
    private IObject m_iAct = null;

    /**
     * 创建对话框
     * @param int objIDAct Activity对象
     * @param String strTitle 标题
     * @param String[] strOption 选项列表
     * @param String strNo No按钮名
     */
    public OptionDialog(int objIDAct, String strTitle, String[] strOption, String strNo)
    {
        m_iAct = ObjectManager.root.getObject(objIDAct);
        if (m_iAct == null) return;

        Activity theAct = (Activity)m_iAct;
        if (theAct == null) return;

        new AlertDialog.Builder(theAct)
            .setTitle(strTitle)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setSingleChoiceItems(strOption, 0, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) 
                {
                    option(which);
                    dialog.dismiss();
                }
            })
            .setNegativeButton(strNo, null)
            .show();

    }

    /**
     * option被选择，需要覆盖
     */
    public void option(int which)
    {
        
    }

}



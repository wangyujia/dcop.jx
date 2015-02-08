package JX.Extend.Android.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import JX.*;
import JX.Framework.Object.*;



/**
 * 输入对话框
 */
public class InputDialog
{
    private IObject m_iAct = null;

    /**
     * 创建对话框
     * @param int objIDAct Activity对象
     * @param String strTitle 标题
     * @param String strYes Yes按钮名
     * @param String strNo No按钮名
     */
    public InputDialog(int objIDAct, String strTitle, String strYes, String strNo)
    {
        m_iAct = ObjectManager.root.getObject(objIDAct);
        if (m_iAct == null) return;

        Activity theAct = (Activity)m_iAct;
        if (theAct == null) return;

        new AlertDialog.Builder(theAct)
            .setTitle(strTitle)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setView(new EditText(theAct))
            .setPositiveButton(strYes, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    yes();
                }
            })
            .setNegativeButton(strNo, null)
            .show();

    }

    /**
     * Yes被选择，需要覆盖
     */
    public void yes()
    {
        
    }

}


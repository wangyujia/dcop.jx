package JX.Extend.Android.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import JX.*;
import JX.Framework.Object.*;


/**
 * ȷ�϶Ի���
 */
public class ConfirmDialog
{
    private IObject m_iAct = null;

    /**
     * �����Ի���
     * @param int objIDAct Activity����
     * @param String strTitle ����
     * @param String strYes Yes��ť��
     * @param String strNo No��ť��
     */
    public ConfirmDialog(int objIDAct, String strTitle, String strYes, String strNo)
    {
        m_iAct = ObjectManager.root.getObject(objIDAct);
        if (m_iAct == null) return;

        Activity theAct = (Activity)m_iAct;
        if (theAct == null) return;

        new AlertDialog.Builder(theAct)
            .setTitle(strTitle)
            .setIcon(android.R.drawable.ic_dialog_info)
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
     * yes��ѡ����Ҫ����
     */
    public void yes()
    {
        
    }

}


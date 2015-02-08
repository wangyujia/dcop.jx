package JX.Extend.Android.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import JX.*;
import JX.Framework.Object.*;


/**
 * ��ѡ��Ի���
 */
public class OptionDialog
{
    private IObject m_iAct = null;

    /**
     * �����Ի���
     * @param int objIDAct Activity����
     * @param String strTitle ����
     * @param String[] strOption ѡ���б�
     * @param String strNo No��ť��
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
     * option��ѡ����Ҫ����
     */
    public void option(int which)
    {
        
    }

}



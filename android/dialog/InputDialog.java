package com.dcop.jx.components.android.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;

import com.dcop.jx.*;
import com.dcop.jx.frameworks.object.*;



/**
 * ����Ի���
 */
public class InputDialog
{
    private IObject m_iAct = null;

    /**
     * �����Ի���
     * @param int objIDAct Activity����
     * @param String strTitle ����
     * @param String strYes Yes��ť��
     * @param String strNo No��ť��
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
     * Yes��ѡ����Ҫ����
     */
    public void yes()
    {
        
    }

}


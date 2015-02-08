package JX.Extend.Android.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import JX.*;
import JX.Framework.Object.*;


/**
 * �򵥶Ի���
 */
public class SimpleDialog
{
    private IObject m_iAct = null;

    /**
     * �����Ի���
     * @param int objIDAct Activity����
     * @param String strTitle ����
     * @param String strYes Yes��ť��
     */
    public SimpleDialog(int objIDAct, String strTitle, String strYes)
    {
        m_iAct = ObjectManager.root.getObject(objIDAct);
        if (m_iAct == null) return;

        Activity theAct = (Activity)m_iAct;
        if (theAct == null) return;

        new AlertDialog.Builder(theAct)
            .setTitle(strTitle)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setPositiveButton(strYes, null)
            .show();

    }

}


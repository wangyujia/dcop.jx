package JX.Extend.Android.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ListView;
import JX.*;
import JX.Framework.Object.*;


/**
 * ��ѡ��Ի���
 */
public class CheckDialog
{
    private IObject m_iAct = null;

    /**
     * �����Ի���
     * @param int objIDAct Activity����
     * @param String strTitle ����
     * @param String[] strOption ѡ���б�
     * @param String strYes Yes��ť��
     * @param String strNo No��ť��
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
     * choice��ѡ����Ҫ����
     * @param String[] strOption ��Ӧ��������Ϣ
     * @param boolean[] checked ��Ӧ���Ƿ�ѡ��
     */
    public void choice(String[] strOption, boolean[] checked)
    {
        
    }

}




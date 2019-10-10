package com.dcop.jx.components.android.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ImageView;

import com.dcop.jx.*;
import com.dcop.jx.frameworks.object.*;


/**
 * ͼƬ�Ի���
 */
public class ImageDialog
{
    private IObject m_iAct = null;

    /**
     * �����Ի���
     * @param int objIDAct Activity����
     * @param String strTitle ����
     * @param int idImg ͼƬ��ԴID
     * @param String strYes Yes��ť��
     */
    public ImageDialog(int objIDAct, String strTitle, int idImg, String strYes)
    {
        m_iAct = ObjectManager.root.getObject(objIDAct);
        if (m_iAct == null) return;

        Activity theAct = (Activity)m_iAct;
        if (theAct == null) return;
        
        ImageView img = new ImageView(theAct);
        img.setImageResource(idImg);

        new AlertDialog.Builder(theAct)
            .setTitle(strTitle)
            .setView(img)
            .setPositiveButton(strYes, null)
            .show();

    }

}


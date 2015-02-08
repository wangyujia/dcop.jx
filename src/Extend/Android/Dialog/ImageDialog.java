package JX.Extend.Android.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ImageView;
import JX.*;
import JX.Framework.Object.*;


/**
 * 图片对话框
 */
public class ImageDialog
{
    private IObject m_iAct = null;

    /**
     * 创建对话框
     * @param int objIDAct Activity对象
     * @param String strTitle 标题
     * @param int idImg 图片资源ID
     * @param String strYes Yes按钮名
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


package pf.java.pfHelperSwt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class PFSwtHelper {
	public static void Alert(Shell shell,String msg) {
		if(SGDataHelper.StringIsNullOrWhiteSpace(msg)) {return;}
		MessageBox dialog=new MessageBox(shell,SWT.NONE);
        dialog.setText("提示");
        dialog.setMessage(msg);
        dialog.open();		
	}
}

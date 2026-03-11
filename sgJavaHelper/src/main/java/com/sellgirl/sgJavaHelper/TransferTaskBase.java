package com.sellgirl.sgJavaHelper;

import com.sellgirl.sgJavaHelper.sql.SGSqlTransferItem;

public abstract class TransferTaskBase implements ITransferTask
//,java.io.Serializable //spark里用到的类都要继承Serializable接口
{

	@Override
	public SGSqlTransferItem get() {
		SGSqlTransferItem r = new SGSqlTransferItem();
		String hashId=this.getClass().getSimpleName();
		r.setHashId(hashId);
		return r;
	}

}

package com.sellgirl.sgJavaHelper;

public class TreeListItem 
    extends TreeListItemT<TreeListItem> 
    implements ITreeListItem {

    private Object _data ;
	@Override
	public Object GetData() {
		return _data;
	}

	@Override
	public void SetData(Object data) {
		_data=data;
	}

}

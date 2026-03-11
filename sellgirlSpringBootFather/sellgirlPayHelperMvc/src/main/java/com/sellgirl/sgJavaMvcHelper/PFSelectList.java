package com.sellgirl.sgJavaMvcHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.sellgirl.sgJavaHelper.PFDataRow;
import com.sellgirl.sgJavaHelper.SGDataTable;

public class PFSelectList extends ArrayList<PFSelectListItem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1535963156860316539L;

//	public static <T,TKey,TValue> PFSelectList Init(List<T> list,Function<T,TKey> key,Function<T,TValue> value) {
//		PFSelectList r=new PFSelectList();
//		for(T i : list) {
//			r.add(new PFSelectListItem() {{
//				Value=key.apply(i).toString();
//				Text=value.apply(i).toString();
//			}});
//		}
//	} 

	public static <T> PFSelectList Init(List<T> list,Function<T,String> key,Function<T,String> value) {
		PFSelectList r=new PFSelectList();
		for(T i : list) {
			r.add(new PFSelectListItem() {{
				Value=key.apply(i);
				Text=value.apply(i);
			}});
		}
		return r;
	} 
	public static <T> PFSelectList Init(SGDataTable dt,String key,String value) {
		PFSelectList r=new PFSelectList();
      if (dt != null)
      {
          for(PFDataRow row : dt.Rows)
          {        
			r.add(new PFSelectListItem() {{
				Value=row.getStringColumn(key);
				Text=row.getStringColumn(value);
			}});
          }
      }
		return r;
	} 
    public PFSelectList TClone()
    {
    	PFSelectList r=new PFSelectList();
    	for(PFSelectListItem i :this) {
    		r.add(i);
    	}
        return r;
    }
}

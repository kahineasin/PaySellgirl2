package com.sellgirl.sgJavaHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class PFKeyValueCollection<T> extends ArrayList<PFKeyValue<T>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8452023796527777441L;

    public PFKeyValueCollection(Collection<PFKeyValue<T>> list)
    {
    	super(list);
    }
    //private string _keys = ",";
    public String[] GetKeys(){
    	List<?> list=this.stream().map(a->a.Key).collect(Collectors.toList());
    	return list.toArray(new String[list.size()]); 
    	}
    public Boolean ContainsKey(String key){
    	for(PFKeyValue<T> i : this) {
    		if(i.Key.equals(key)) {
    			return true;
    		}
    	} 
    	return false;
    	}
    public T Get(String name)
    {
            //var item = this.FirstOrDefault(a => a.Key == name);
            PFKeyValue<T> item=SGDataHelper.ListFirst(this,a->name.equals(a.Key));
            //return item == null ? null : item.Value;
            return item == null ? null : item.Value;
    }
    public void Set(String name,T value)
    {
        //var item = this.FirstOrDefault(a => a.Key == name);
        PFKeyValue<T> item=SGDataHelper.ListFirst(this,a->name.equals(a.Key));
        if (item == null) { this.Add(name,value); }
        else { item.Value = value; }
    }
    public PFKeyValueCollection()
    { }
    //public new void Add(PFKeyValue<TValue> item){
    //    base.Add(item);
    //    //AddKey(item.Key);
    //}
    public void Add(String key, T value)
    {
        super.add(new PFKeyValue<T>() { {Key = key; Value = value ;}});
        //AddKey(key);
    }
    public void Remove(String key)
    {
		this.removeIf(a->a.Key.equals(key));
//    	for(PFKeyValue<T> i : this) {
//    		if(i.Key.equals(key)) {
//    		}
//    	} 
    }
    //public bool ContainsKey(string key) {
    //    return _keys.IndexOf("," + key + ",") > -1;
    //}
    //private void AddKey(string key)
    //{
    //    if (!ContainsKey(key))
    //    {
    //        _keys += key + ",";
    //    }
    //}
}

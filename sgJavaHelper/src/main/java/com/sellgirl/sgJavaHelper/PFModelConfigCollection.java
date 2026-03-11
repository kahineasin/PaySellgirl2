package com.sellgirl.sgJavaHelper;
import java.util.HashMap;

public class PFModelConfigCollection extends HashMap<String, PFModelConfig>
implements ISGDisposable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public PFModelConfig Get(String key) {
        String low = key.toLowerCase();
        if (this.containsKey(low))
        {
            return super.get(low);
        }
        return null;
	}
	public void Set(String key,PFModelConfig value) {
		String low = key.toLowerCase();
		super.put(low, value);
	}
	@Override
    public boolean containsKey(Object key) {
        String low = key.toString().toLowerCase();
        return super.containsKey(low);
    }
	@Override
    public PFModelConfig get(Object key) {
        return this.Get(key.toString());
    }
	@Override
    public void dispose()
    {
        this.clear();
    }
//    public new PFModelConfig this[string key]
//    {
//        get
//        {
//            var low = key.ToLower();
//            if (this.ContainsKey(low))
//            {
//                return base[low];
//            }
//            return null;
//        }
//        set
//        {
//            var low = key.ToLower();
//            base[low] = value;
//        }
//    }
}

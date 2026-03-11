package com.sellgirl.sgJavaHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public abstract class TreeListItemT<T extends TreeListItemT<T>> implements ITreeListItem{

    //protected List<T> _children;
	/**
	 * 这里要用public,如果用protected的话,json序列化时会忽略
	 */
    public List<T> Children=null;
    //public Object Data;
    
    //public List<T> Children { get { return _children ?? (_children = new List<T>()); } set { _children = value; } }

    /// <summary>
    /// 深度优先递归
    /// </summary>
    /// <param name="action">参数:T子项,int深度</param>
    public List<T> GetChildren() {
    	if(Children==null) {Children = new ArrayList<T>();}
		return Children;
	}

	public void SetChildren(List<T> children) {
		this.Children = children;
	}
//    public Object GetData() {
//		return Data;
//	}
    public Object GetData() {
		return this;
	}

	public void SetData(Object data) {
		throw new RuntimeException("不实现此方法");
		//this.Data = data;
	}


	public void EachChildT(BiConsumer<T, Integer> action)
    {
		DoEachChild(action,2);
    }
	protected void DoEachChild(BiConsumer<T, Integer> action, int depth)
    {
		List<T> Children=GetChildren();
		for(int i=0;i<Children.size();i++) {
			T a=Children.get(i);
			action.accept(a, depth);
			a.DoEachChild(action, depth + 1);
		}
    }
//    public void EachChild(BiConsumer<Object, Integer> action, int depth)
//    {
//    	BiConsumer<T, Integer> action2 = (a, b) ->
//        {
//            action.accept(a, b);
//        };
//        this.DoEachChild(action2,depth);
//    }
    @Override
    public void EachChild(BiConsumer<ITreeListItem, Integer> action)
    {
    	BiConsumer<T, Integer> action2 = (a, b) ->
        {
            action.accept(a, b);
        };
        this.DoEachChild(action2,2);
    }

	/**
	 * 深度优先递归
	 * @param action 参数:T子项,int深度,T父节点
	 */
    public void EachChildT(SGAction<T, Integer, TreeListItemT<T>> action)
    {
    	DoEachChild(action,2);
    }
    @Override
    public  void EachChild(SGAction<ITreeListItem, Integer, ITreeListItem> action)
    {
    	EachChild(action,2);	
    }
    @Override
    public  void EachChild(SGAction<ITreeListItem, Integer, ITreeListItem> action, int depth)
    {
		List<T> Children=GetChildren();

		for(int i=0;i<Children.size();i++) {
			T a=Children.get(i);
			action.go(a, depth, this);
			a.EachChild(action, depth + 1);
		}		
    }
    protected void DoEachChild(SGAction<T, Integer, TreeListItemT<T>> action, int depth )
    {
		List<T> Children=GetChildren();

		for(int i=0;i<Children.size();i++) {
			T a=Children.get(i);
			action.go(a, depth, this);
			a.DoEachChild(action, depth + 1);
		}		
    }

    /// <summary>
    /// 深度优先递归
    /// </summary>
    public void EachChild(Consumer<T> action)
    {
		List<T> Children=GetChildren();

		for(int i=0;i<Children.size();i++) {
			T a=Children.get(i);
			action.accept(a);
			a.EachChild(action);
		}	
    }

    /// <summary>
    /// 遍历末级叶节点
    /// </summary>
    /// <param name="action"></param>
    public void EachLeaf(Consumer<T> action)
    {
		List<T> Children=GetChildren();

		for(int i=0;i<Children.size();i++) {
			T a=Children.get(i);

            if (a.GetChildren().size()>0)
            {
                a.EachLeaf(action);
            }
            else
            {
            	action.accept(a);
            }
		}			
    }
    /// <summary>
    /// 第一个叶节点
    /// </summary>
    public T FirstLeaf(Function<T, Boolean> condition)
    {
		List<T> Children=GetChildren();
//        if (Children.size() < 1) { return condition.apply((T)this) ? (T)this : null; }
        if (Children.size() < 1) { 
    		T thisT=SGDataHelper.ObjectAs(this);
        	return condition.apply(thisT) ? thisT : null; 
        	}
        T result;
        for (T i : Children)
        {
            result = i.FirstLeaf(condition);
            if (result != null) { return result; }
        }
        return null;
    }


//    //#region 便于过滤所需要的变量,请不要在外部使用
//    //protected TreeListItemT<T> _parent = null;
//    @Deprecated
//    protected ITreeListItem _parent = null;
//    @Deprecated
//    protected Boolean _fitFilter = false;
//    @SuppressWarnings("unchecked")
//	@Deprecated
//    private void SetParent()
//    {
//        EachChild((child, depth, parent) ->
//        {
//            ((TreeListItemT<T>)child)._parent = parent;
//            ((TreeListItemT<T>)child)._fitFilter = false;
//        });
//    }
//    @SuppressWarnings("unchecked")
//	@Deprecated
//    protected void SetFit()
//    {
//        this._fitFilter = true;
//        if (this._parent != null) { ((TreeListItemT<T>)_parent).SetFit(); }
//    }
//    @Deprecated
//    protected void RemoveNotFitOld()
//    {
//		List<T> Children=GetChildren();
//		
//        for (int i = Children.size() - 1; i >= 0; i--)
//        {
//        	T ChildrenI=Children.get(i);
//            if (ChildrenI._fitFilter)
//            {
//            	ChildrenI.RemoveNotFitOld();
//            }
//            else
//            {
//                Children.remove(ChildrenI);
//            }
//        }
//    }
//    //#endregion
//
//    /**
//     * 根据叶节点来过滤
//     * 
//     * 此方法要用临时成员_parent和_fitFilter,感觉很麻烦,尝试用新方法FilterByLeaf2来取代此方法
//     * @param condition
//     * @return
//     */
//    @Deprecated
//    public TreeListItemT<T> FilterByLeafOld(Function<T, Boolean> condition)
//    {
//        SetParent();
//        EachLeaf((a) -> {
//            if (condition.apply(a))
//            {
//                a.SetFit();
//            }
//        });
//        RemoveNotFitOld();
//        return this;
//    }
    protected boolean isLeaf() {
    	return this.Children==null||this.Children.size()<1;
    }
    /**
     * 根据叶节点来过滤
     * @param condition
     * @return
     */
    protected boolean RemoveNotFit(Function<T, Boolean> condition)
    {
		List<T> Children=GetChildren();
		boolean hasFit=false;
        for (int i = Children.size() - 1; i >= 0; i--)
        {
        	T ChildrenI=Children.get(i);
            if (ChildrenI.isLeaf())
            {
            	if(condition.apply(ChildrenI)) {
                	hasFit=true;	
            	}
            }else {
        		boolean childHasFit=ChildrenI.RemoveNotFit(condition);
        		if(childHasFit) {   
                	hasFit=true;         			
        		}else {
                	Children.remove(ChildrenI);
        		}
            }
        }
        return hasFit;
    }
    public TreeListItemT<T> FilterByLeaf(Function<T, Boolean> condition)
    {
    	RemoveNotFit(condition);
        return this;
    }

    /// <summary>
    /// 获得最大深度(最小为1)
    /// </summary>
    /// <returns></returns>
    public int GetDepth()
    {
         int max = 1;
         int[] arr = new int[]{max};
        EachChild((a, b) -> { 
        	//if (b > max) { max =b.intValue(); }//报错:Local variable max defined in an enclosing scope must be final or effectively final
        	if (b > arr[0]) { arr[0] =b.intValue(); }
        	});
        max=arr[0];
        return max;
    }


    /// <summary>
    ///     获得所有children的数量,递归查找
    /// </summary>
    /// <returns></returns>
    public int GetAllChildrenCount()
    {
        int total = 0;
        int[] arr = new int[]{total};
        EachChild(a -> arr[0]++);
        total=arr[0];
        return total;
    }

    /// <summary>
    /// 获得所有末级叶Child的数量
    /// </summary>
    /// <returns></returns>
    public int GetAllLeafCount()
    {
        int total = 0;
        int[] arr = new int[]{total};
        EachLeaf(a -> arr[0]++);
        total=arr[0];
        return total;
    }
    /// <summary>
    /// 获得所有末级叶Child的数量
    /// </summary>
    /// <returns></returns>
    public int GetAllLeafCount(Function<T, Boolean> condition)
    {
        int total = 0;
        int[] arr = new int[]{total};
        EachLeaf(a -> { if (condition == null || condition.apply(a)) { arr[0]++; } });
        total=arr[0];
        return total;
    }
}

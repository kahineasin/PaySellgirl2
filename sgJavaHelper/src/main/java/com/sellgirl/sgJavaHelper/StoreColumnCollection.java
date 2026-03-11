package com.sellgirl.sgJavaHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/**
 * 
 * @author Administrator
 *
 */
public class StoreColumnCollection extends ArrayList<StoreColumn>
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4809536007649879394L;
	protected PFModelConfigCollection _modelConfig;
    public StoreColumnCollection() { }
    public StoreColumnCollection(String modelConfig)
    {
        _modelConfig = SGDataHelper.GetMultiModelConfig(modelConfig);
    }
    //public StoreColumn FirstLeaf(Func<StoreColumn, bool> predicate)
    //{
    //    return (new StoreColumn { Children = this }).FirstLeaf(predicate);
    //}
    public void Add(String data, Consumer<StoreColumn> action)//, bool setWidthByHeaderWord = true)
    {
    	PFModelConfig config = _modelConfig == null ? null : _modelConfig.Get(data);
    	StoreColumn c = new StoreColumn(data, config);

        if (action != null) { action.accept(c); }//action里有可能改title,所以此句一定在GetWordsWidth之前--wxj20180906

        //if (//setWidthByHeaderWord&&
        //    PFDataHelper.StringIsNullOrWhiteSpace(c.width))//如果xml配置中有宽度,就不用计算字长了
        //{
        //    var w = PFDataHelper.GetWordsWidth(c.title ?? c.data);
        //    if (!PFDataHelper.StringIsNullOrWhiteSpace(w)) { c.width = w; }
        //}

        add(c);
    }
    public void Add(String data)//, bool setWidthByHeaderWord = true)
    {
    	Add(data,null);
    }
    public void Add(PFDataColumn column, Consumer<StoreColumn> action)//, bool setWidthByHeaderWord = true)
    {
    	PFModelConfig config = _modelConfig == null ? null : _modelConfig.Get(column.getKey());
    	StoreColumn c = new StoreColumn(column, config);

        if (action != null) { action.accept(c); }//action里有可能改title,所以此句一定在GetWordsWidth之前--wxj20180906

        //if (//setWidthByHeaderWord&&
        //    PFDataHelper.StringIsNullOrWhiteSpace(c.width))//如果xml配置中有宽度,就不用计算字长了
        //{
        //    var w = PFDataHelper.GetWordsWidth(c.title ?? c.data);
        //    if (!PFDataHelper.StringIsNullOrWhiteSpace(w)) { c.width = w; }
        //}

        add(c);
    }
    /// <summary>
    /// 树型转二维数组(现在只有导出excel时用到,所以默认过滤了visible==false的数据
    /// </summary>
    /// <returns></returns>
    public static void StoreColumnTo2DArray(
    		//SGRef< List<List<StoreColumn>>> target,
    		SGRef<ArrayList<ArrayList<StoreColumn>>> target,
    		StoreColumnCollection columns, SGRef<Integer> maxDepth)
    {
        //var result = new List<List<StoreColumn>>();
    	ArrayList<StoreColumn> floor = new ArrayList<StoreColumn>();
    	StoreColumnCollection next = new StoreColumnCollection();
        int currentDepth = target.GetValue().size() + 1;
        int rowSpan = maxDepth.GetValue() - currentDepth + 1;
        columns.forEach(a ->
        {
            if (a.get_visible())
            {
            	List<StoreColumn> children =  a.GetChildren().stream().filter(b -> b.get_visible()).collect(Collectors.toList());
                //if (a.Children.Any(b => b.visible))
                if (children != null && (!children.isEmpty()))
                {
                    next.addAll(children);
                    a.set_colspan(a.GetAllLeafCount(b -> b.get_visible()));
                }
                else
                {
                    a.set_rowspan(rowSpan);
                }
                //a.Children = null;
                floor.add(a);
            }
        });
        target.GetValue().add(floor);
        //if (next.Any())
       if (!next.isEmpty())
        {
            StoreColumnTo2DArray( target, next,  maxDepth);
        }
    }
}
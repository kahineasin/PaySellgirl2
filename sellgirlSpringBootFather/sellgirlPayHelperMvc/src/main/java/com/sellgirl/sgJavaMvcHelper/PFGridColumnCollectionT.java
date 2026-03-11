package com.sellgirl.sgJavaMvcHelper;

import java.lang.reflect.ParameterizedType;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class PFGridColumnCollectionT<TModel> extends PFGridColumnCollection//List<PFGridColumn>
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public PFGridColumnCollectionT()
    {
//		  Type genType = getClass().getGenericSuperclass();  
//		  Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
//		  Class<TModel> entityClass =   (Class) params[0];   
//		  Class<TModel> tClass = (Class<TModel>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		  Class<TModel> tClass =SGDataHelper.<Class<TModel>>ObjectAs (((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        //var modelType = typeof(TModel);
        SetModelConfig(tClass.getSimpleName(), tClass.getName());
    }
//	public PFGridColumnCollectionT(Class<?> cls)
//    {
//        //var modelType = typeof(TModel);
//        SetModelConfig(cls.getSimpleName(),cls.getName());
//    }
    /// <summary>
    /// 增加列
    /// </summary>
    /// <typeparam name="TProperty"></typeparam>
    /// <param name="exp">字段表达式</param>
    /// <param name="text">表头文字</param>
    /// <returns></returns>
//    public PFGridColumn Add<TProperty>(Expression<Func<TModel, TProperty>> exp, String text)
//    {
//        var col = Add(exp);
//        col.Text = text;
//        return col;
//    }
//    public PFGridColumn Add<TProperty>(Expression<Func<TModel, TProperty>> exp)
//    {
//        var col = Add(ExpressionHelper.GetExpressionText(exp));
//        //字典类型的话,直接拿Key调用另一个重载就好了
//        //var sExp = ExpressionHelper.GetExpressionText(exp);
//        ////if (typeof(TModel) is IDictionary)//当是字典类型时,sExp型如: [xx]
//        //if(typeof(TModel).Equals(typeof(Dictionary<String, object>)))
//        //{
//        //    if (sExp[0] == '[') { sExp = sExp.SubString(1); }
//        //    if (sExp[sExp.Length-1] == ']') { sExp = sExp.SubString(0, sExp.Length - 1); }
//        //}
//        //var col = Add(sExp);
//        return col;
//
//    }
//
//    /// <summary>
//    /// 增加列
//    /// </summary>
//    /// <param name="text">表头文字</param>
//    /// <param name="render">渲染方法 参数:列,行,值;返回:显示值</param>
//    /// <returns></returns>
//    public PFGridColumn Add<TProperty>(Expression<Func<TModel, TProperty>> exp, Func<PFGridColumn, object, object, String> render)
//    {
//        var col = Add(exp);
//        col.Render = render;
//        return col;
//    }

}

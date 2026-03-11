package com.sellgirl.sgJavaHelper;

import java.util.List;

/// <summary>
/// 树型矩阵
/// </summary>
public class TreeMatrix
{
    /// <summary>
    /// 节点坐标矩阵
    /// </summary>
    Boolean[][] _node;
    /// <summary>
    /// 每一层的最后一个节点坐标矩阵
    /// </summary>
    Boolean[][] _lastChild;
    /// <summary>
    /// 线条网矩阵
    /// </summary>
    TreeMatrixNet[][] _net;

    /// <summary>
    /// 指net的x最大值，node的x要比net的多1
    /// </summary>
    int _xMax;
    /// <summary>
    /// net和node的y相等
    /// </summary>
    int _yMax;

    public TreeMatrix(List<ITreeListItem> rows)
    {
        int x = GetDepth(rows);
        int y = GetAllChildrenCount(rows);
        _node = new Boolean[x][y];
        _lastChild = new Boolean[x][y];
        _net = new TreeMatrixNet[x - 1][ y];
        
        //C# 里不需要这样，因为C#的enum可以
        for(int i=0;i<x-1;i++) {
        	for(int j=0;j<y;j++) {
        		//_net[i][j]=new TreeMatrixNet();
        		_net[i][j]=TreeMatrixNet.None;
        	}
        }
        
        _xMax = x - 2;//注意:这里不是_net的x-1,是因为_xMax是最大索引号,而x-1指的是数量
        _yMax = y - 1;

        for(int i=0;i<=x-1;i++) {
        	for(int j=0;j<=y-1;j++) {
        		_node[i][j]=false;
        	}
        }

        int i = 0;
        for (ITreeListItem row : rows)
        {
            SetNode(0, i);
            if (rows.size() - 1 == i) { SetLastChild(0, i); }
            i++;
            int[] iArr=new int[] {i};
            row.EachChild((child, depth, parent) ->
            {
                SetNode(depth - 1, iArr[0]);
                if (parent.GetChildren().get(parent.GetChildren().size()-1) == child) { SetLastChild(depth - 1, iArr[0]); }
//                i++;
                iArr[0]++;
            });
            i=iArr[0];
        }
        SetNetByNode();
    }
    /// <summary>
    ///     获得深度
    /// </summary>
    /// <returns></returns>
    private int GetDepth(List<ITreeListItem> rows)
    {
        int max = 0;
        //foreach (var i in rows)
        for (ITreeListItem row : rows)
        {
            //var row = (TreeListItem)i;
            int d = row.GetDepth();
            if (d > max) max = d;
        }
        return max;
    }

    /// <summary>
    ///     获得所有children的数量,递归查找
    /// </summary>
    /// <returns></returns>
    private int GetAllChildrenCount(List<ITreeListItem> rows)
    {
        int total = 0;
        //foreach (var i in rows)
        for (ITreeListItem row : rows)
        {
            total += 1;
            //var row = (TreeListItem)i;
            total += row.GetAllChildrenCount();
        }
        return total;
    }
    public void SetMatrix(Boolean[][] matrix, int x, int y)
    {
        matrix[x][y] = true;
    }
    /// <summary>
    /// x即是treecolumn的缩进等级lv;y即是row的行号
    /// </summary>
    /// <param name="x"></param>
    /// <param name="y"></param>
    public void SetNode(int x, int y)
    {
        SetMatrix(_node, x, y);//把node阵的xy行赋值，因为setNetByNode时要用到
    }
    /// <summary>
    /// x即是treecolumn的缩进等级lv;y即是row的行号
    /// </summary>
    /// <param name="x"></param>
    /// <param name="y"></param>
    public void SetLastChild(int x, int y)
    {

        SetMatrix(_lastChild, x, y);//把lastChild阵的xy行赋值，因为setNetByNode时要用到
    }
    /// <summary>
    /// 根据所有节点生成连线网
    /// </summary>
    public void SetNetByNode()
    {
        for (int x = 0; x <= _xMax; x++)
        {
            for (int y = 1; y <= _yMax; y++)
            {//注意网线是从序号1开始的
                if (_node[x + 1][ y]) { 
                	//_net[x][y].Or(TreeMatrixNet.Right); 
                	_net[x][y]=_net[x][y].Or(TreeMatrixNet.Right); 
                	}
                if (
                     (_net[x][ y - 1].HasFlag(TreeMatrixNet.Down))
                     || (_node[x][ y - 1])//上格有下方向线或者是节点时，本格加上线
                   )
                {
                    _net[x][ y].Or(TreeMatrixNet.Up);
                }
                if (
                    !((!_net[x][ y].HasFlag( TreeMatrixNet.Up)) 
                    		|| _lastChild[x + 1][ y])//上格有下方向线或者是节点时，本格加上线
                   )
                {
                    //_net[x, y] |= TreeMatrixNet.Down;
                    _net[x][ y].Or(TreeMatrixNet.Down);
                }

            }
        }
    }
    /// <summary>
    /// 查询连线网某个点的线条形状
    /// </summary>
    /// <param name="x"></param>
    /// <param name="y"></param>
    /// <returns></returns>
    public TreeMatrixNet GetNetLine(int x, int y)
    {
        return _net[x][ y];
    }
    public String GetNetLineString(int x, int y)
    {
    	TreeMatrixNet net = _net[x][ y];
        String urd = "┝", //(上右下)
            ud = "│", //(上下)
            ur = "┕";//(上右)

        if (net.HasFlag(TreeMatrixNet.Up) && net.HasFlag( TreeMatrixNet.Right) && net.HasFlag( TreeMatrixNet.Down))
        {
            return urd;// "linearea-urd";
        }
        if (net.HasFlag( TreeMatrixNet.Up) && net.HasFlag( TreeMatrixNet.Down))
        {
            return ud;// "linearea -ud";
        }
        if (net.HasFlag( TreeMatrixNet.Up) && net.HasFlag( TreeMatrixNet.Right))
        {
            //return "tree-tr-linearea-ur";
            return ur;// "linearea-ur";
        }
        return "  ";
    }
}
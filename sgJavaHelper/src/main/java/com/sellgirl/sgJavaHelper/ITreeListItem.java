package com.sellgirl.sgJavaHelper;

import java.util.List;
import java.util.function.BiConsumer;

public interface ITreeListItem {

    Object GetData();
    void SetData(Object data);
    List<?> GetChildren();
    //void EachChild(PFAction<Object, Integer,Object> action, Integer depth);
    //void EachChild(BiConsumer<Object, Integer> action);
    //void EachChild(BiConsumer<Object, Integer> action, int depth);
    //void EachChild(BiConsumer<Object, Integer> action);
    void EachChild(BiConsumer<ITreeListItem, Integer> action);
    void EachChild(SGAction<ITreeListItem, Integer,ITreeListItem> action);
    void EachChild(SGAction<ITreeListItem, Integer,ITreeListItem> action, int depth);

    int GetDepth();
    int GetAllChildrenCount();
}

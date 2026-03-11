package com.sellgirl.sgGameHelper.tabUi;

/**
 * 地图上不同的节点之间移动光标
 * tab切换的地图(多用于手柄控制菜单光标移动)
 * 此接口和 SGTabUpDownMap、SGTabUDLRMap 等继承此接口的类均可以用于相似的应用场境
 */
public interface ISGTabMap {
    /**
     *
     * @return true:切换成功 false:到尽头了
     */
     boolean up();
    boolean down();
    boolean left();
    boolean right();
    Object getCurrent();
}

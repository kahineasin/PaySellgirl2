package com.sellgirl.sgJavaSpringHelper;

/**
 * 有向图的节点类型
 * @author Administrator
 *
 */
public enum DirectNodeType {
	/**
	 * 开始节点,特殊节点
	 */
	StartNode,
	/**
	 * 结束节点,特殊节点
	 */
	EndNode,
	/**
	 * 任务节点
	 */
	TaskNode,
	/**
	 * 打包节点,特殊节点
	 * (此种节虚是为了便于在addNext时当成一个节点来处理,本节点无意义的,只计算子节点,当前版本只计算第一层next),
	 */
	PackEmptyNode
}

package com.sellgirl.sgJavaHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

/**
 * 有向图的节点
 * 
 * @author Administrator
 *
 */
public class DirectNode {
	protected String hashId = null;
	protected DirectNodeType nodeType=DirectNodeType.TaskNode;
	/**
	 * 完成度 0~100 (便于显示任务进度)
	 * 一般可以在action执行之中设置此值
	 */
	protected int finishedPercent = 0;
	/**
	 * 对于无法评估总量的,由于无法计算finishedPercent,用此值表示进度
	 * (如迁移表时,不知道总行数,可以用finishNum表示已经迁移的行数)
	 */
	protected int finishedNum=0;
	/**
	 * 此属性便于在其它线程内判断是否已经执行完(但并不表示成功),最好保留本属性
	 */
	public boolean finish = false;
	public boolean success = true;
	// public AtomicBoolean started=false;
	/**
	 * 此属性的其中一个作用是,保证当某node的parent有多个时,不会重复执行多次.
	 */
	public AtomicBoolean started = new AtomicBoolean(false);
	public List<DirectNode> prev = null;
	public List<DirectNode> next = null;
	public Object data;
	//public Consumer<Object> action;
	//public Consumer<DirectNode> action;
	public PFFunc3<DirectNode, Object, Object,Boolean> action;

	public DirectNode() {
		
	}
	private DirectNode(DirectNodeType nt) {
		this.nodeType=nt;
	}
	public static  DirectNode StartNode() {
		return new DirectNode(DirectNodeType.StartNode);
	}
	public static  DirectNode EndNode() {
		return new DirectNode(DirectNodeType.EndNode);
	}
	public static  DirectNode PackEmptyNode() {
		return new DirectNode(DirectNodeType.PackEmptyNode);
	}
	public DirectNode(String hashId) {
		this.hashId=hashId;
	}
	public String getHashId() {
		return hashId;
	}

	public void setHashId(String hashId) {
		this.hashId = hashId;
	}

	public int getFinishedPercent() {
		return finishedPercent;
	}

	public void setFinishedPercent(int finishedPercent) {
		this.finishedPercent = finishedPercent;
	}
	public int getFinishedNum() {
		return finishedNum;
	}
	public void setFinishedNum(int finishedNum) {
		this.finishedNum = finishedNum;
	}

	/**
	 * 开始执行(异步)
	 * 以后尽量用GoSync
	 */
	@Deprecated
	public void Go() {
//		if(started) {return;}
//		started=true;

		if (started.compareAndSet(false, true)) {

			try {
				while (prev != null && prev.size() > 0 && SGDataHelper.ListAny(prev, a -> !a.finish)) {
				//while (prev != null && prev.size() > 0 && PFDataHelper.ListAny(prev, a -> (!a.finish)&&DirectNodeType.PackEmptyNode!=a.nodeType)) {
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (action != null) {
				if (prev != null && SGDataHelper.ListAny(prev, a -> !a.success)) {// 如果上一步有失败的，就不执行action了
					success = false;
				} else {
					try {
						// 没有上一步 或者 前面所有都是success才招待action
						//action.accept(data);
						success=action.go(this,null,null)&&success;//以便兼容以前版本
					} catch (Exception e) {
						success = false;
						SGDataHelper.WriteError(new Throwable(),e);
					}
				}
			}
			finish = true;
			this.finishedPercent=100;
			if (next == null) {
				return;
			}
			for (DirectNode i : next) {

				Thread thread = new Thread() {// 线程操作
					public void run() {
						i.Go();
					}
				};
				thread.start();
			}
		}

	}

	/**
	 * 开始执行(同步)
	 */
	public boolean GoSync(Consumer<DirectNodeProgressItem> progress) {
		try {
			DoGoSync(progress);
			DirectNodeProgress p=getAllNodeFinishedPercent();
			return p.isAllSuccess();
		}catch(Exception e) {
			//PFDataHelper.WriteError(new Throwable(),e);
			SGDataHelper.WriteError(e);
			return false;
		}
	}
	public void DoGoSync(Consumer<DirectNodeProgressItem> progress) {
		if (started.compareAndSet(false, true)) {

			try {
				while (prev != null && prev.size() > 0 && SGDataHelper.ListAny(prev, a -> !a.finish)) {
				//while (prev != null && prev.size() > 0 && PFDataHelper.ListAny(prev, a -> (!a.finish)&&DirectNodeType.PackEmptyNode!=a.nodeType)) {
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//if (action != null) {
				if (prev != null && SGDataHelper.ListAny(prev, a -> !a.success)) {// 如果上一步有失败的，就不执行action了
					success = false;
				} else {
					if (action != null) {
					try {
						// 没有上一步 或者 前面所有都是success才招待action
						//action.accept(data);
						//action.accept(this);
						//success=action.go(this,null,null)&&success;//以便兼容以前版本
						finishedPercent=1;//表示开始
						success=action.go(this,null,null);//以便兼容以前版本
//						//progress.accept(this);				
//						if(progress!=null) {
//							progress.accept(new DirectNodeProgressItem(this));
//						}
					} catch (Exception e) {
						success = false;
//						//PFDataHelper.WriteError(new Throwable(),e);
//						PFDataHelper.WriteError(new Throwable(),new Exception(
//								PFDataHelper.FormatString("DirectNode[{0}]执行失败", getHashId()),
//								e));
						SGDataHelper.WriteErrors(Arrays.asList(e,new Exception(
								SGDataHelper.FormatString("DirectNode[{0}]执行失败", getHashId()))) );
					}
					}
				}
			//}
			finish = true;
			if(success&&this.finishedPercent<100) {
				this.finishedPercent=100;		
			}
			
			if(progress!=null) {
				progress.accept(new DirectNodeProgressItem(this));
			}
			
			if (next == null) {
				return;
			}
			List<Thread> ts=new ArrayList<Thread>(); 
			for (DirectNode i : next) {

				Thread thread = new Thread() {// 线程操作
					public void run() {
						i.DoGoSync(progress);
					}
				};
				thread.start();
				ts.add(thread);
			}
			for (Thread i : ts) {
				try {
					i.join();
				} catch (InterruptedException e) {
					//PFDataHelper.WriteError(new Throwable(),e);
					SGDataHelper.WriteError(e);
				}
			}
		}

	}


	public DirectNodeProgress getAllNodeFinishedPercent() {
		DirectNodeProgress result= new DirectNodeProgress();
		int qty=0;
		int finishedQty=0;
		int successQty=0;
		//Map<String, Integer> r = new HashMap<String, Integer>();
		//r.put(getHashId(), finishedPercent);
		List<DirectNodeProgressItem> r = new ArrayList<DirectNodeProgressItem>();
		//r.add(new DirectNodeProgressItem(this) );
		List<DirectNode> nextNodes = getAllNextNode();
		for (DirectNode i : nextNodes) {
			if(!SGDataHelper.StringIsNullOrWhiteSpace( i.hashId)) {//开始结束节点等没有hashId的就不计算了
				qty+=1;
				if(i.finish) {
					finishedQty+=1;
				}
				if(i.success) {
					successQty+=1;
				}
				//r.put(i.getHashId(), i.getFinishedPercent());
				r.add(new DirectNodeProgressItem(i));
			}
		}
		result.setNodeQty(qty);
		result.setFinishedNodeQty(finishedQty);
		result.setSuccessNodeQty(successQty);
		//result.setFinishedPercent(r);
		result.setProgressItem(r);
		return result;
	}

	/**
	 * 获得所有下级节点(含本身)
	 * @return
	 */
	protected List<DirectNode> getAllNextNode() {
		List<DirectNode> list = new ArrayList<DirectNode>();
		list.add(this);
		if (next != null && next.size() > 0) {
			for (DirectNode i : next) {
				list.addAll(i.getAllNextNode());
			}
		}
		list = list.stream().distinct().collect(Collectors.toList());// 有向图的所历next会有重叠
		return list;
	}
	/**
	 * 获得所有末级节点(如果本身是末级,返回自身)
	 * @return
	 */
	protected List<DirectNode> getAllNextLeafNode() {
		List<DirectNode> list = new ArrayList<DirectNode>();
		if (next != null && next.size() > 0) {
			for (DirectNode i : next) {
				list.addAll(i.getAllNextLeafNode());
			}
		}else {
			list.add(this);
		}
		list = list.stream().distinct().collect(Collectors.toList());// 有向图的所历next会有重叠
		return list;
	}

	public void addPrev(DirectNode... n) {
		if (prev == null) {
			prev = new ArrayList<DirectNode>();
		}
		for (DirectNode i : n) {
			if(DirectNodeType.PackEmptyNode==i.nodeType) {
				List<DirectNode> leaf=i.getAllNextLeafNode();
				for(DirectNode j : leaf) {
					prev.add(j);
					j.doAddNext(this);
				}
			}else {
				prev.add(i);
				i.doAddNext(this);
			}
		}
	}

	public void addNext(DirectNode... n) {
		if (next == null) {
			next = new ArrayList<DirectNode>();
		}
		for (DirectNode i : n) {
			if(DirectNodeType.PackEmptyNode==i.nodeType) {
				for(DirectNode j : i.next) {
					next.add(j);
					if(DirectNodeType.PackEmptyNode!=nodeType) {//不要把PackEmptyNode加到prev,否则Go的时候都要跳过PackEmptyNode来判断,很容易出错
						j.doAddPrev(this);
					}
				}
			}else {
				next.add(i);
				if(DirectNodeType.PackEmptyNode!=nodeType) {
					i.doAddPrev(this);
				}
			}
		}
	}

	protected void doAddPrev(DirectNode... n) {
		if (prev == null) {
			prev = new ArrayList<DirectNode>();
		}
		for (DirectNode i : n) {
			prev.add(i);
		}
	}

	protected void doAddNext(DirectNode... n) {
		if (next == null) {
			next = new ArrayList<DirectNode>();
		}
		for (DirectNode i : n) {
			next.add(i);
		}
	}
}

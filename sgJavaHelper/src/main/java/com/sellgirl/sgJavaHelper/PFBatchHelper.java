package com.sellgirl.sgJavaHelper;

/**
 * 便于批量操作的判断
 * 其实这样似乎不太好用,因为在while的情况下,hasUnDo可能不是必需的
 *
 * 用法1:
 PFBatchHelper batchHelper=new PFBatchHelper();
 batchHelper.batchSize = this.insertOpt.getProcessBatch();
 batchHelper.batchCnt = 0;
 for (int i = 0; i <cnt; i++) {
     stmt.addBatch(sql.get(i));
     batchHelper.hasNext=cnt>i+1;
     batchHelper.batchCnt++;
     if (batchHelper.ifDo()) {
         stmt.executeBatch();
         conn.commit();
         batchHelper.hasDone();
     }
     //if (!batchHelper.hasNext) {break;}  当外层是用while true循环时,可能需要这句来保证跳出
 }
 */
public class PFBatchHelper {
    public int batchSize=0;
    /***
     * 当前累计了多少
     */
    public int batchCnt=0;
    /***
     * 是否有下一条数据
     */
    public boolean hasNext=false;

//    /**
//     * 好像重复,可以用batchCnt>0代替
//     */
//    @Deprecated
//    public boolean hasUnDo=false;

    /**
     * 是否执行
     * @return
     */
    public boolean ifDo(){
//        //if (batchCnt >= batchSize || ((!hasNext) && hasUnDo)) {
//        if (batchCnt >= batchSize || ((!hasNext) && batchCnt>0)) {
////            stmt.executeBatch();
////            conn.commit();
//            //batchCnt = 0;
//            return true;
//        }
//        return false;
        return batchCnt >= batchSize || ((!hasNext) && batchCnt>0);
    }

    /**
     * 刚执行完,更新helper的状态
     */
    public void hasDone(){
        batchCnt = 0;
        //hasUnDo = false;
    }
}

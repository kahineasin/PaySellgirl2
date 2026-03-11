package test.sellgirl.sellgirlPayMq.model;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Flink消息. */
public class FlinkMessage {

    @JsonProperty private JSONObject before;
    @JsonProperty private JSONObject after;
    @JsonProperty private FlinkMessageSource source;
    @JsonProperty private String op;
    @JsonProperty private Long ts_ms;
    @JsonProperty private String transaction;

    public JSONObject getBefore() {
        return before;
    }

    public void setBefore(JSONObject before) {
        this.before = before;
    }

    public JSONObject getAfter() {
        return after;
    }

    public void setAfter(JSONObject after) {
        this.after = after;
    }

    public FlinkMessageSource getSource() {
        return source;
    }

    public void setSource(FlinkMessageSource source) {
        this.source = source;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public Long getTs_ms() {
        return ts_ms;
    }

    public void setTs_ms(Long ts_ms) {
        this.ts_ms = ts_ms;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }
}

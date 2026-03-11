package test.java.pfHelper.model;

public abstract class TestSerialAbsBase {
	/**
	 * 序列化
	 */
    public String bb="bb";
    /**
     * 不序列化
     */
    protected String ee="ee";
    protected String ff="ff";
	public String getFf() {
		return ff;
	}
	public void setFf(String ff) {
		this.ff = ff;
	}
}

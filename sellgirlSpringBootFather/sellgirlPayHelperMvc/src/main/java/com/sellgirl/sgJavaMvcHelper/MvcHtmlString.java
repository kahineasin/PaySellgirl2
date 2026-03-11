package com.sellgirl.sgJavaMvcHelper;

public class MvcHtmlString implements 
java.io.Serializable, Comparable<String>, CharSequence {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _html;
	private MvcHtmlString(String html) {
		_html=html;
	}
	public static MvcHtmlString Create(String html) {
		return new MvcHtmlString(html);
	}
	@Override
	public String toString() {
		return _html;
	}
	@Override
	public int length() {
		return _html==null?0:_html.length();
	}
	@Override
	public char charAt(int index) {
		return _html==null?0:_html.charAt(index);
	}
	@Override
	public CharSequence subSequence(int start, int end) {
		// TODO Auto-generated method stub
		return  _html==null?null:_html.subSequence(start,end);
	}
	@Override
	public int compareTo(String o) {
		return _html==null?0:_html.compareTo(o);
	}
}

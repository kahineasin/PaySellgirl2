package pf.java.pfHelper.expression;

import com.sellgirl.sgJavaHelper.SGRef;

public class WordQuery implements Query_base  {
//	friend class Query;
	
//	WordQuery(const std::string &s):query_word(s){}
	public WordQuery(SGRef<String> s) {
		query_word=s.GetValue();
	}
	
//	std::set<line_no> eval(const TextQuery &t) const 
//	{return t.run_query(query_word);}
//	std::ostream& display(std::ostream &os) const 
//	{return os <<query_word;}
	String query_word;
}

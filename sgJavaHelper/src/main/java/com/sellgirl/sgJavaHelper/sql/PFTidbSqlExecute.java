package com.sellgirl.sgJavaHelper.sql;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
//import java.math.RoundingMode;
//import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import com.sellgirl.sgJavaHelper.SGAction;
import com.sellgirl.sgJavaHelper.PFCmonth;
import com.sellgirl.sgJavaHelper.SGDataTable;
import com.sellgirl.sgJavaHelper.SGFunc;
import com.sellgirl.sgJavaHelper.SGSqlCommandString;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class PFTidbSqlExecute extends SGMySqlExecute {
	public PFTidbSqlExecute(ISGJdbc jdbc) throws Exception {
		super(jdbc);
	}

//	@Override
//	protected BatchInsertOption DefaultSqlUpdateOption() {
//		BatchInsertOption o = new BatchInsertOption();
//		o.setProcessBatch(10000);
//		return o;
//	}
//	@Override
//	public <T> boolean HugeUpdateList(PFSqlUpdateCollection update, List<T> list, String tableName,
//									  // Action<BatchInsertOption> insertOptionAction,
////		        Func<MySqlUpdateCollection, T, bool> rowAction,//考虑这个是否必要
////		        Action<int> sqlRowsUpdatedAction = null
//									  PFFunc<BaseSqlUpdateCollection, T, Object, Boolean> rowAction, Consumer<Integer> sqlRowsUpdatedAction,
//									  Predicate<Boolean> stopAction) {
////		if(PFSqlType.Tidb==_jdbc.GetSqlType()) {
////			CloseConn();
////			this.connConfigSettingSql=new PFSqlCommandString(
////					//"SET tidb_multi_statement_mode='ON'",
////					"set tidb_batch_insert = 1",
////					"update mysql.tidb set variable_value='24h' where variable_name='tikv_gc_life_time'",
////					"update mysql.tidb set variable_value='30m' where variable_name='tikv_gc_life_time'");
////			//this.SetInsertOption(a->a.setProcessBatch(500));
////			//this.SetInsertOption(a->a.setProcessBatch(1));
////		}
//		return super.HugeUpdateList(update, list, tableName, rowAction, sqlRowsUpdatedAction, stopAction);
//	}
//	@Override
//	protected PreparedStatement insertIntoCachedRowSet(PreparedStatement crs, ResultSetMetaData md,
//			PFSqlInsertCollection dstInsert
//// ,Map<String,Integer> srcColumnType
//// ,Map<String,Integer> dstColumnType
//	) throws SQLException {
//		//try {
//			return super.insertIntoCachedRowSet(crs, md, dstInsert);
////		}catch(SQLException e) {
////			String err=e.toString();
////			if(err!=null&&err.indexOf("No value specified for parameter")>-1) {
////				PFDataHelper.WriteError(new Throwable(), new Exception(
////						PFDataHelper.FormatString("tidb的addBatch不支持忽略没执行set的值,当前dstInsert为:{0}", JSON.toJSONString(dstInsert))
////						));
////			}
////			throw e;
////		}
//	}
	@Override
	@Deprecated
	public PreparedStatement GetPs(String tableName) {
		throw new RuntimeException(
				"tidb禁止使用此方法获得所有字段的插入方法,因为tidb不支持某个字段不调用set方法,必报错:No value specified for parameter xx");
	}

//	@Override
//	public PreparedStatement insertIntoCachedRowSet(PreparedStatement crs, ResultSetMetaData md,
//			PFSqlInsertCollection dstInsert
//	) throws SQLException {
//		List<String> nullCols = new ArrayList<String>();
//		for (int i = 0; i < md.getColumnCount(); i++) {
//			int mdIdx = i + 1;
//			String colName = md.getColumnLabel(mdIdx);
//
//			if (dstInsert.containsKey(colName)) {
//				int dataT = md.getColumnType(mdIdx);
//
//				if (java.sql.Types.DECIMAL == dataT) {// 如果直接updateObject,云徒订单的Totalmoney字段会有100倍溢出的问题--benjamin20210112
//					Object v = dstInsert.get(colName).Value;
//					if (v != null) {
//						BigDecimal vD = PFDataHelper.<BigDecimal>ObjectAs(
//								PFDataHelper.ConvertObjectToSqlTypeByPFType(v, dstInsert.get(colName).getSrcDataPFType(), dataT));
//						vD = vD.setScale(md.getScale(mdIdx), RoundingMode.HALF_UP);
//
//						crs.setBigDecimal(mdIdx, vD);
//						if (vD == null) {
//							nullCols.add(colName);
//						}
//					} else {
//						nullCols.add(colName);
//					}
//				} else if (java.sql.Types.TIMESTAMP == dataT) {
//
//					String dataTName = md.getColumnTypeName(mdIdx);
//					Timestamp t = PFDataHelper.ObjectAs(PFDataHelper.ConvertObjectToSqlTypeByPFType(
//							dstInsert.get(colName).Value, dstInsert.get(colName).getSrcDataPFType(), dataT, dataTName));
//					crs.setTimestamp(mdIdx, t);
//					if (t == null) {
//						nullCols.add(colName);
//					}
//				}
//
//				else {
//					Object v = PFDataHelper.ConvertObjectToSqlTypeByPFType(dstInsert.get(colName).Value,
//							dstInsert.get(colName).getSrcDataPFType(), dataT);
//					crs.setObject(mdIdx, v);
//					if (v == null) {
//						// String aa="aa";
//						nullCols.add(colName);
//					}
//				}
//
//			} else {
//				nullCols.add(colName);
//			}
//		}
//
//		try {
//			crs.addBatch();
//		} catch (SQLException e) {
//			String err = e.toString();
//			if (err != null && err.indexOf("No value specified for parameter") > -1) {
//
//				PFDataHelper.WriteErrors(Arrays.asList(e,new Exception(PFDataHelper.FormatString(
//						"tidb的addBatch不支持忽略没执行set的值,当前dstInsert为:{0},\r\n转换后的值为null的列有:{1}",
//						JSON.toJSONString(nullCols), JSON.toJSONString(nullCols))))
//						);
//			}
//			throw e;
//		}
//		return crs;
//	}
	private boolean isMapEqual(LinkedHashMap<String, Object> i, LinkedHashMap<String, Object> j, String[] primaryKeys) {

		boolean isMatch = true;
		for (String k : primaryKeys) {
			if (!k.equals("create_date")) {

				if (!SGDataHelper.IsObjectEquals(i.get(k), j.get(k))) {
					isMatch = false;
					break;
				}
			}
		}
		return isMatch;
	}

	/**
	 * 不知为何,在C#用update tidb就很快,但在java中update
	 * tidb就非常慢,效率还没有10分之1,所以把基类方法复写为先delete再新增
	 * tidb_monthly_province_statistics表用到,以后看看能不能不用此方法
	 * 
	 * @param tableName
	 * @param cmonth
	 * @param primaryKeys
	 * @param valuefields
	 */
	public boolean UpdateYearOnYearField(String tableName, String cmonth, String[] primaryKeys, String[] valuefields) {
		// var cmonth = transfer.ViewData["cmonth"].ToString();
		String last_cmonth = SGDataHelper.CMonthAddMonths(cmonth, -1);
		String last_year = SGDataHelper.CMonthAddYears(cmonth, -1);

		PFCmonth pfCmonth = new PFCmonth();
		pfCmonth.setCmonth(cmonth);
		// var sqlExec = new MySqlExecute(transfer.DstConn);
		SGDataTable thisMonthDt = GetDataTable(SGDataHelper.FormatString(
				"select * from {0} where create_date= STR_TO_DATE('{1}.01','%Y.%m.%d')", tableName, cmonth));
		// var thisMonthList = thisMonthDt == null ? new List<Dictionary<string,
		// object>>() : thisMonthDt.ToDictList(false);
		ArrayList<LinkedHashMap<String, Object>> thisMonthList = thisMonthDt == null
				? new ArrayList<LinkedHashMap<String, Object>>()
				: thisMonthDt.ToDictList();

		SGDataTable lastMonthDt = GetDataTable(SGDataHelper.FormatString(
				"select * from {0} where create_date= STR_TO_DATE('{1}.01','%Y.%m.%d')", tableName, last_cmonth));
		ArrayList<LinkedHashMap<String, Object>> lastMonthList = lastMonthDt == null
				? new ArrayList<LinkedHashMap<String, Object>>()
				: lastMonthDt.ToDictList();

		SGDataTable lastYearDt = GetDataTable(SGDataHelper.FormatString(
				"select * from {0} where create_date= STR_TO_DATE('{1}.01','%Y.%m.%d')", tableName, last_year));
		ArrayList<LinkedHashMap<String, Object>> lastYearList = lastYearDt == null
				? new ArrayList<LinkedHashMap<String, Object>>()
				: lastYearDt.ToDictList();

		ArrayList<LinkedHashMap<String, Object>> updateList = new ArrayList<LinkedHashMap<String, Object>>();
		ArrayList<LinkedHashMap<String, Object>> insertList = new ArrayList<LinkedHashMap<String, Object>>();
		LinkedHashMap<String, Object> valueFieldZero = new LinkedHashMap<String, Object>();// 值列最好是不要有null值,否则BI里相减会得null,很不方便,其实所有decimal列都要

		SGAction<ArrayList<LinkedHashMap<String, Object>>, Object, Object> findZero = (list, c, d) -> {
			if (!list.isEmpty()) {
//            foreach (var i in list[0])
//            {
//                if ((!primaryKeys.Contains(i.Key)) && (!valueFieldZero.ContainsKey(i.Key)) && i.Value != null)
//                {
//                    var typeString = PFDataHelper.GetStringByType(i.Value.GetType());
//                    if (typeString == "decimal")
//                    {
//                        valueFieldZero.Add(i.Key, new decimal(0));
//                    }
//                    else if (new string[] { "int", "long" }.Contains(typeString))
//                    {
//                        valueFieldZero.Add(i.Key, 0);
//                    }
//                }
//            }

				LinkedHashMap<String, Object> r0 = list.get(0);
				Iterator<Entry<String, Object>> iter = r0.entrySet().iterator();
				while (iter.hasNext()) {
					Entry<String, Object> key = iter.next();

					if ((!Arrays.asList(primaryKeys).contains(key.getKey()))
							&& (!valueFieldZero.containsKey(key.getKey())) && key.getValue() != null) {
						String typeString = SGDataHelper.GetStringByType(key.getValue().getClass());
						if ("decimal".equals(typeString)) {
							valueFieldZero.put(key.getKey(), new BigDecimal(0));
						} else if (Arrays.asList(new String[] { "int", "long" }).contains(typeString)) {
							valueFieldZero.put(key.getKey(), 0);
						}
					}
				}
			}
		};
		findZero.go(thisMonthList, null, null);
		findZero.go(lastMonthList, null, null);
		findZero.go(lastYearList, null, null);

		SGFunc<LinkedHashMap<String, Object>, String, Object, LinkedHashMap<String, Object>> newRow = (srcRow, cmonth1,
				d) -> {
			LinkedHashMap<String, Object> r = new LinkedHashMap<String, Object>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					// put("create_date",PFDataHelper.CMonthToMySqlDate(cmonth));
					put("create_date", pfCmonth.ToDateTime());
				}
			};
			for (String k : primaryKeys) {
				if (!k.equals("create_date")) {
					r.put(k, srcRow.get(k));
				}
			}
//        foreach (var k in valueFieldZero)
//        {
//            r.Add(k.Key, k.Value);
//        }
			Iterator<Entry<String, Object>> iter = valueFieldZero.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Object> key = iter.next();
				r.put(key.getKey(), key.getValue());
			}
			return r;
		};

		for (LinkedHashMap<String, Object> j : lastMonthList) {
			LinkedHashMap<String, Object> item = null;
			if (!thisMonthList.isEmpty()) {
				for (LinkedHashMap<String, Object> i : thisMonthList) {
					boolean isMatch = true;
					for (String k : primaryKeys) {
						if (!k.equals("create_date")) {

							if (!SGDataHelper.IsObjectEquals(i.get(k), j.get(k))) {
								isMatch = false;
								break;
							}
						}
					}
					if (isMatch) {
						item = i;
						break;
					}
				}
			}
			// var item = thisMonthList.Any()? thisMonthList.FirstOrDefault(a =>
			// a["province_name"] == j["province_name"] && a["hpos"] == j["hpos"] &&
			// a["qpos"] == j["qpos"] && a["is_new_hy"] == j["is_new_hy"])
			// :null;
			// bool isAdd = false;
			if (item == null) {
				// isAdd = true;
				item = newRow.go(j, cmonth, null);

				insertList.add(item);
			} else {
				updateList.add(item);
			}
			for (String k : valuefields) {
//            item["lm_" + k] = j[k];
				item.put("lm_" + k, j.get(k));
			}

		}
		for (LinkedHashMap<String, Object> j : lastYearList) {
			LinkedHashMap<String, Object> item = null;
			if (!thisMonthList.isEmpty()) {
				for (LinkedHashMap<String, Object> i : thisMonthList) {
					boolean isMatch = true;
					for (String k : primaryKeys) {
						if (!k.equals("create_date")) {
							if (!SGDataHelper.IsObjectEquals(i.get(k), j.get(k))) {
								isMatch = false;
								break;
							}
						}
					}
					if (isMatch) {
						item = i;
						break;
					}
				}
			}
			// var item = thisMonthList.FirstOrDefault(a => a.province_name ==
			// j.province_name && a.hpos == j.hpos && a.qpos == j.qpos && a.is_new_hy ==
			// j.is_new_hy);
			// bool isAdd = false;
			if (item == null) {
				// isAdd = true;
				if (!insertList.isEmpty()) {
					for (LinkedHashMap<String, Object> i : insertList) {
						boolean isMatch = true;
						for (String k : primaryKeys) {
							if (!k.equals("create_date")) {
								if (!SGDataHelper.IsObjectEquals(i.get(k), j.get(k))) {
									isMatch = false;
									break;
								}
							}
						}
						if (isMatch) {
							item = i;
							break;
						}
					}
				}
				// item = insertList.FirstOrDefault(a => a.province_name == j.province_name &&
				// a.hpos == j.hpos && a.qpos == j.qpos && a.is_new_hy == j.is_new_hy);
				if (item == null) {
					item = newRow.go(j, cmonth, null);

					insertList.add(item);
				}
			} else {
				LinkedHashMap<String, Object> updateItem = null;
				if (!updateList.isEmpty()) {
					for (LinkedHashMap<String, Object> i : updateList) {
						boolean isMatch = true;
						for (String k : primaryKeys) {
							if (k != "create_date") {
								if (!SGDataHelper.IsObjectEquals(i.get(k), j.get(k))) {
									isMatch = false;
									break;
								}
							}
						}
						if (isMatch) {
							updateItem = i;
							break;
						}
					}
				}

				// var updateItem = updateList.FirstOrDefault(a => a.province_name ==
				// j.province_name && a.hpos == j.hpos && a.qpos == j.qpos && a.is_new_hy ==
				// j.is_new_hy);
				if (updateItem == null) {
					updateList.add(item);
				} else {
					item = updateItem;
				}
			}
			for (String k : valuefields) {
//            item["ly_" + k] = j[k];
				item.put("ly_" + k, j.get(k));
			}
		}

		boolean b = true;
		// 当单独执行AfterTransferAction时,还是先把同比环比清空比较稳,否则会有上次残留结果
		// var valuefields = new string[] { "new_hy_qty", "valid_hy_qty",
		// "valid_hy_hpos_above_5_qty", "valid_hy_hpos_0_qty" };
		b = ExecuteSql(new SGSqlCommandString(
				SGDataHelper.FormatString("update {0} set {1},{2} where create_date= STR_TO_DATE('{3}.01','%Y.%m.%d')",
						tableName, String.join(",", SGDataHelper.ListSelect(Arrays.asList(valuefields),
								a -> SGDataHelper.FormatString(" ly_{0}=0 ", a))),
//string.Join(",", valuefields.Select(a => string.Format(" lm_{0}=0 ", a))),
						String.join(",", SGDataHelper.ListSelect(Arrays.asList(valuefields),
								a -> SGDataHelper.FormatString(" lm_{0}=0 ", a))),
						cmonth)));

		List<LinkedHashMap<String, Object>> thisMonthNotUpdateList = SGDataHelper.ListWhere(thisMonthList, i -> {
			return !SGDataHelper.ListAny(updateList, j -> {
				return isMapEqual(i, j, primaryKeys);
			});
		});

		HugeDelete(tableName, a -> a.Add("create_date", SGDataHelper.CMonthToDate(cmonth)));
		if (!thisMonthNotUpdateList.isEmpty()) {
			// var insert = new MySqlInsertCollection(insertList.First());
			b = HugeBulkList(null, thisMonthNotUpdateList, tableName, null, null, null);
		}
		if (!updateList.isEmpty()) {
//			List<String> srcFieldNames=new ArrayList<String>( updateList.get(0).keySet());
//			ResultSetMetaData dstMd =GetMetaData(tableName,srcFieldNames);
//			SqlUpdateCollection update = getUpdateCollection(dstMd);
//			
//			List<String> list1=PFDataHelper.ListSelect(Arrays.asList(valuefields), a -> "lm_" + a);
//			List<String> list2=PFDataHelper.ListSelect(Arrays.asList(valuefields), a -> "ly_" + a);
//			List<String> mList = PFDataHelper.MergeList(
//					new ArrayList<List<String>>() {
//						/**
//						 * 
//						 */
//						private static final long serialVersionUID = 1L;
//
//						{
//							add(list1);
//							add(list2);
//						}
//					});
//			
//			String[] mArray = mList.toArray(new String[mList.size()]);
//			update.UpdateFields(mArray);
//			update.PrimaryKeyFields(false, primaryKeys);
//			b = HugeUpdateList(update, updateList, tableName, null, null, null);
			b = HugeBulkList(null, updateList, tableName, null, null, null);
		}
		if (!insertList.isEmpty()) {
			// var insert = new MySqlInsertCollection(insertList.First());
			b = HugeBulkList(null, insertList, tableName, null, null, null);
		}
		return b;
	}
}

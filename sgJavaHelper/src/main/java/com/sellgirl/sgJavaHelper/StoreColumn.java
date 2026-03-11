package com.sellgirl.sgJavaHelper;

import com.sellgirl.sgJavaHelper.config.*;

//[JsonObject(MemberSerialization.OptOut)]
	    public class StoreColumn extends TreeListItemT<StoreColumn>
	    {
	        private transient int _rowspan = 1;
			private transient int _colspan = 1;
	        private Boolean _visible = true;
			private Boolean _hasSummary = false;
			private SummaryType _summaryType = SummaryType.None;
			public String data ;
	        public String title ;
	        //[DefaultValue(null)]
	        //[JsonProperty(DefaultValueHandling = DefaultValueHandling.Ignore)]
	        public String width ;//需然FieldSets里面不写px单位,但设到这里时补上
	        //[DefaultValue(null)]
	        //[JsonProperty(DefaultValueHandling = DefaultValueHandling.Ignore)]
	        //public int? precision ;
//	        [DefaultValue(null)]
//	        [JsonProperty(DefaultValueHandling = DefaultValueHandling.Ignore)]
	        public String dataType ;

	        //[DefaultValue("")]
	        //[JsonProperty(NullValueHandling = NullValueHandling.Ignore, DefaultValueHandling = DefaultValueHandling.Ignore)]
	        public Object summary ;

	        /// <summary>
	        /// 日期显示格式
	        /// </summary>
	        //[DefaultValue("")]
	       // [JsonProperty(NullValueHandling = NullValueHandling.Ignore, DefaultValueHandling = DefaultValueHandling.Ignore)]
	        public String dateFormat ;
	        public int get_rowspan() {
				return _rowspan;
			}
			public void set_rowspan(int _rowspan) {
				this._rowspan = _rowspan;
			}
	        //[DefaultValue(1)]
	        //[JsonProperty(DefaultValueHandling = DefaultValueHandling.Ignore)]
			public int get_colspan() {
				return _colspan;
			}
			public void set_colspan(int _colspan) {
				this._colspan = _colspan;
			}
	        //[DefaultValue(true)]
	        //[JsonProperty(NullValueHandling = NullValueHandling.Ignore, DefaultValueHandling = DefaultValueHandling.Ignore)]
	        public Boolean get_visible() {
				return _visible;
			}
			public void set_visible(Boolean _visible) {
				this._visible = _visible;
			}
	        //[JsonIgnore]
	        public Boolean get_hasSummary() {
				return _hasSummary;
			}
			public void set_hasSummary(Boolean hasSummary) {
				if (hasSummary) { _summaryType = SummaryType.Sum; } _hasSummary = hasSummary; 
			}
	        //[JsonIgnore]
	        public SummaryType get_summaryType() {
				return _summaryType;
			}
			public void set_summaryType(SummaryType summaryType) {
				if (summaryType != SummaryType.None) { _hasSummary = true; } _summaryType = summaryType; 
			}
	        //[JsonIgnore]//现在的excel是后端导出的，前端应该用不着这个属性--benjamin20190704
	        public Double getExcelWidth() {
	        	return SGDataHelper.WebWidthToExcel(width);
	        }
	        public void setExcelWidth(Double value)
	        {
                if (value == null) { width = null; }
                else
                {
                    width = SGDataHelper.ExcelWidthToWeb(value);
                }
	        }//需然FieldSets里面不写px单位,但设到这里时补上

	        public StoreColumn() { }
	        public StoreColumn(String fieldName)
	        {
	            title = data = fieldName;
	        }
	        public StoreColumn(PFDataColumn c)
	            //: this(c.ColumnName)
	        {
	        	this(c.getKey());
	            //dataType = PFDataHelper.GetStringByType(c.getDataType());
	            dataType = SGDataHelper.GetStringByType(c.getDataType());
	            if (c.ExtendedProperties.containsKey("summaryType"))
	            {
	            	SummaryType tmp=SGDataHelper.ObjectToEnum(SummaryType.class,c.ExtendedProperties.get("summaryType"));
	            	set_summaryType(tmp==null ?SummaryType.None:tmp);
	            }
	        }
	        public StoreColumn(PFDataColumn c, PFModelConfig config)
	            //: this(c.ColumnName)
	            //: this(c)
	        {
	        	this(c);
	            //dataType = PFDataHelper.GetStringByType(c.DataType);
	            SetPropertyByModelConfig(config);
	        }
	        public StoreColumn(String fieldName, PFModelConfig config)
	           // : this(fieldName)
	        {
	        	this(fieldName);
	            SetPropertyByModelConfig(config);
	        }
	        public void SetPropertyByModelConfig(PFModelConfig config)
	        {
	            if (config != null)
	            {
	                //data = config.FieldName;
	                title = config.FieldText;
	                if (!SGDataHelper.StringIsNullOrWhiteSpace(config.FieldWidth)) { width = config.FieldWidth; }
	                if (config.FieldType != null) { dataType = SGDataHelper.GetStringByType(config.FieldType); }
	                set_visible(config.getVisible());
	                //if (config.Precision != null) { precision = config.Precision; }
	            }
	        }

	        public void SetWidthByTitleWords()
	        {
	            if (SGDataHelper.StringIsNullOrWhiteSpace(title)) { return; }
	            width = SGDataHelper.GetWordsWidth(title, null, null, null, "bold");
	        }
	    }
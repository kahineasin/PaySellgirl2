//放在原来 common.js 之后引入

function mapResourceToDto(i,resourceType){
	i.id=i.resource_id;
	i.title=i.resource_name;
	if('comic'==resourceType||'image'==resourceType){
		i.imageCount=i.duration;
		delete i.duration;
	}else{
		i.duration=(i.duration/60).toFixed(0);//second->minute
	}
	i.size=(i.size/1024).toFixed(1);//KB->MB
	if(i.cover){
		let coverArr=i.cover.split(',');
		if(0<coverArr.length){
			//i.previewImg='/resourceImg/'+resourceType+'/'+i.resource_id+'/'+coverArr[0];
			i.previewImg='/resourceImg/'+resourceType+'60/'+i.resource_id+'/'+coverArr[0];
			i.images=[];
			coverArr.forEach(j=>{
				i.images.push('/resourceImg/'+resourceType+'/'+i.resource_id+'/'+j);
			});
		}
	}
	return i;
}

function formatMB(MB){
	if(MB>1024){
		return ''+(MB/1024).toFixed(1)+'GB';
	}
	return ''+MB+'MB';
}
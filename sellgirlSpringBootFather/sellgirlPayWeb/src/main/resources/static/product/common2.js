//放在原来 common.js 之后引入

function signIn(callback) {
	if(null!=callback&&undefined!=callback){
	
		  $.post("/PostSign",
		  {
		    name:"Donald Duck",
		    city:"Duckburg"
		  },
		  function(data,status){
		    //alert("Data: " + data + "\nStatus: " + status);
			callback({success:data.success,message:data.msg,signDays:data.data},status);
		  })
		  .success(function() { /*alert("second success");*/ })//grid进入
		  .error(function() { alert("error"); alert(JSON.stringify(arguments))})//tree进入
		  .complete(function(XMLHttpRequest, textStatus){})
		  ;
		  
		  return;
	}
    const user = getCurrentUser();
    if (!user) return { success: false, message: '请先登录' };

    const users = getUsers();
    const index = users.findIndex(u => u.username === user.username);
    if (index === -1) return { success: false, message: '用户不存在' };

    const today = getTodayStr();
    const lastSign = users[index].lastSignDate || '';
    let signDays = users[index].signDays || 0;

    if (lastSign === today) {
        return { success: false, signedToday: true, signDays, message: '今日已签到' };
    }

    const yesterday = new Date();
    yesterday.setDate(yesterday.getDate() - 1);
    const yesterdayStr = `${yesterday.getFullYear()}-${String(yesterday.getMonth()+1).padStart(2,'0')}-${String(yesterday.getDate()).padStart(2,'0')}`;

    if (lastSign === yesterdayStr) {
        signDays += 1;
    } else {
        signDays = 1;
    }

    users[index].lastSignDate = today;
    users[index].signDays = signDays;
    saveUsers(users);
    setCurrentUser(users[index]);

    return { success: true, signedToday: true, signDays, message: '签到成功' };
}


function addFavorite(item) {
    const user = getCurrentUser();
    if (!user) return false;
    const users = getUsers();
    let index = users.findIndex(u => u.username === user.username);
    // if (index === -1) return false;
    if (index === -1) {
        users.push(user);
        index=users.findIndex(u => u.username === user.username);
    }
    if (!users[index].favorites) users[index].favorites = [];
    if (!users[index].favorites.some(f => f.type === item.type && f.id === item.id)) {
        users[index].favorites.push(item);
        saveUsers(users);
        setCurrentUser(users[index]);
    }
    return true;
}
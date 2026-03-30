//放在原来 common.js 之后引入, 因为调用或复写了它的方法
//by hyaweh

//新版本前端以curUser为主
function setCurrentUser(user) {
    localStorage.setItem('currentUser', JSON.stringify(user));
    
    const users = getUsers();
    let index = users.findIndex(u => u.username === user.username);
    // if (index === -1) return false;
    if (index === -1) {
        users.push(user);
        //index=users.findIndex(u => u.username === user.username);
    }else{
    	users[index]=user;
    }
    saveUsers(users);
}

function getTodayStr() {
    const d = new Date();
    return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')}`;
}

//签到
function signIn(
//username,
user,
callback) {
	if(null!=callback&&undefined!=callback){
	
		  $.post("/PostSign",
		  {
		    username:user.username
		  },
		  function(data,status){
		    //alert("Data: " + data + "\nStatus: " + status);		    
            user.lastSignDate=getTodayStr();
            user.signDays=data.data.signDay;
            
    user.consecutiveDays = data.data.signDay;//新版本改了个名
    user.points = data.data.point;
    
            setCurrentUser(user);
			callback({
			success:data.success,message:data.msg,
			signDays:data.data.signDay,
			point:data.data.point,
			pointsEarned:data.data.pointsEarned
			},status);
		  })
		  .success(function() { /*alert("second success");*/ })//grid进入
		  .error(function() { alert("error"); alert(JSON.stringify(arguments))})//tree进入
		  .complete(function(XMLHttpRequest, textStatus){})
		  ;
		  
		  return;
	}
    //const user = getCurrentUser();
    //if (!user) return { success: false, message: '请先登录' };
    //
    //const users = getUsers();
    //const index = users.findIndex(u => u.username === user.username);
    //if (index === -1) return { success: false, message: '用户不存在' };
    //
    //const today = getTodayStr();
    //const lastSign = users[index].lastSignDate || '';
    //let signDays = users[index].signDays || 0;
    //
    //if (lastSign === today) {
    //    return { success: false, signedToday: true, signDays, message: '今日已签到' };
    //}
    //
    //const yesterday = new Date();
    //yesterday.setDate(yesterday.getDate() - 1);
    //const yesterdayStr = `${yesterday.getFullYear()}-${String(yesterday.getMonth()+1).padStart(2,'0')}-${String(yesterday.getDate()).padStart(2,'0')}`;
    //
    //if (lastSign === yesterdayStr) {
    //    signDays += 1;
    //} else {
    //    signDays = 1;
    //}
    //
    //users[index].lastSignDate = today;
    //users[index].signDays = signDays;
    //saveUsers(users);
    //setCurrentUser(users[index]);
    //
    //return { success: true, signedToday: true, signDays, message: '签到成功' };
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
        //saveUsers(users);
        //setCurrentUser(users[index]);
    }
    saveUsers(users);
    setCurrentUser(users[index]);
    return true;
}

function removeFavorite(type, id) {
    const user = getCurrentUser();
    if (!user) return false;
    const users = getUsers();
    const index = users.findIndex(u => u.username === user.username);
    //if (index === -1) return false;    
    if (index === -1) {
        users.push(user);
        index=users.findIndex(u => u.username === user.username);
    }
    if (users[index].favorites) {
        users[index].favorites = users[index].favorites.filter(f => !(f.type === type && f.id == id));  //id有些页面是string
        //saveUsers(users);
        //setCurrentUser(users[index]);
    }
    saveUsers(users);
    setCurrentUser(users[index]);
    return true;
}

function getSignStatus() {
    const user = getCurrentUser();
    //if (!user) return { signedToday: false, signDays: 0 };
    //const users = getUsers();
    //const found = users.find(u => u.username === user.username);
    //if (!found) return { signedToday: false, signDays: 0 };
    const today = getTodayStr();
    return {
        signedToday: user.lastSignDate === today,
        signDays: user.signDays || 0,
        consecutiveDays: user.signDays || 0,
        points: user.points || 0
    };
}

const UNLOGIN=2001;
// ==================== 升级为资源用户 ====================
function upgradeToResource(inviteCode,callback) {

	if(null!=callback&&undefined!=callback){	
		  $.post("/api/UpgradeToResource",
		  {
		    inviteCode:inviteCode
		  },
		  function(data,status){
		    //alert("Data: " + data + "\nStatus: " + status);
		    if(data.success){
    			const user = getCurrentUser();
    			user.role = 'resource';
            	setCurrentUser(user);
		    }else if(!data.success&&UNLOGIN==data.code){
                window.location.href='/login.html';
                return;
		    }		    
			callback({success:data.success,message:data.msg},status);
		  })
		  .success(function() { /*alert("second success");*/ })//grid进入
		  .error(function() { alert("error"); alert(JSON.stringify(arguments))})//tree进入
		  .complete(function(XMLHttpRequest, textStatus){})
		  ;
		  
		  return;
	}
	
    const SECRET_INVITE_CODE = '9527';
    if (inviteCode !== SECRET_INVITE_CODE) {
        return { success: false, message: '邀请码错误' };
    }
    const user = getCurrentUser();
    if (!user) return { success: false, message: '未登录' };
    if (user.role === 'resource') {
        return { success: false, message: '您已经是资源用户' };
    }
    const users = getUsers();
    const index = users.findIndex(u => u.username === user.username);
    if (index === -1) return { success: false, message: '用户不存在' };
    users[index].role = 'resource';
    saveUsers(users);
    setCurrentUser(users[index]);
    return { success: true, message: '升级成功！页面即将刷新...' };
}

// ==================== 积分系统 ====================
function getUserPoints() {
    const user = getCurrentUser();
    if (!user) return 0;
    return user.points || 0;
}

// ==================== 资源站解锁 ====================
function unlockResource(resourceId,resourceType,
callback) {

		  $.post("/api/resource/unlock",
		  {
		    resourceType:resourceType,
		    resourceId:resourceId
		  },
		  function(data,status){
		  	if(data.success){
			    const user = getCurrentUser();
			    if(user){
				    if (!user.unlockedResources) user.unlockedResources = [];
				    if (!user.unlockedResources.includes(resourceId)) {
				        user.points =data.data;
				        user.unlockedResources.push(resourceId);
				        setCurrentUser(user);
				    }
			    }
    		}
			callback({
			success:data.success,message:data.msg,
			point:data.data
			},status);
		  })
		  .success(function() { /*alert("second success");*/ })//grid进入
		  .error(function() { alert("error"); alert(JSON.stringify(arguments))})//tree进入
		  .complete(function(XMLHttpRequest, textStatus){})
		  ;
		  
}

//如果需要前端判断,那就还需要resourceType
//function isResourceUnlocked(resourceId) {
//    const user = getCurrentUser();
//    if (!user) return false;
//    return (user.unlockedResources || []).includes(resourceId);
//}

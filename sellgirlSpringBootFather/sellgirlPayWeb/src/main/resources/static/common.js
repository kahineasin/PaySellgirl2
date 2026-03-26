// common.js
function getCurrentUser() {
    const user = localStorage.getItem('currentUser');
    return user ? JSON.parse(user) : null;
}

function setCurrentUser(user) {
    localStorage.setItem('currentUser', JSON.stringify(user));
}

function logout() {
    localStorage.removeItem('currentUser');
    window.location.href = 'index.html';
}

function getUsers() {
    return JSON.parse(localStorage.getItem('users')) || [];
}

function saveUsers(users) {
    localStorage.setItem('users', JSON.stringify(users));
}

function getUserFavorites() {
    const user = getCurrentUser();
    if (!user) return [];
    const users = getUsers();
    const found = users.find(u => u.username === user.username);
    return (found && found.favorites) ? found.favorites : [];
}

function addFavorite(item) {
    const user = getCurrentUser();
    if (!user) return false;
    const users = getUsers();
    const index = users.findIndex(u => u.username === user.username);
    if (index === -1) return false;
    if (!users[index].favorites) users[index].favorites = [];
    if (!users[index].favorites.some(f => f.type === item.type && f.id === item.id)) {
        users[index].favorites.push(item);
        saveUsers(users);
        setCurrentUser(users[index]);
    }
    return true;
}

function removeFavorite(type, id) {
    const user = getCurrentUser();
    if (!user) return false;
    const users = getUsers();
    const index = users.findIndex(u => u.username === user.username);
    if (index === -1) return false;
    if (users[index].favorites) {
        users[index].favorites = users[index].favorites.filter(f => !(f.type === type && f.id === id));
        saveUsers(users);
        setCurrentUser(users[index]);
    }
    return true;
}

function isFavorite(type, id) {
    const user = getCurrentUser();
    if (!user) return false;
    const users = getUsers();
    const found = users.find(u => u.username === user.username);
    return found ? (found.favorites || []).some(f => f.type === type && f.id === id) : false;
}

function requireResourceUser() {
    const user = getCurrentUser();
    if (!user || user.role !== 'resource') {
        window.location.href = 'index.html';
    }
}

// ==================== 积分系统 ====================
function getUserPoints() {
    const user = getCurrentUser();
    if (!user) return 0;
    const users = getUsers();
    const found = users.find(u => u.username === user.username);
    return found ? (found.points || 0) : 0;
}

function addPoints(amount) {
    const user = getCurrentUser();
    if (!user) return false;
    const users = getUsers();
    const index = users.findIndex(u => u.username === user.username);
    if (index === -1) return false;
    users[index].points = (users[index].points || 0) + amount;
    saveUsers(users);
    setCurrentUser(users[index]);
    return true;
}

function consumePoints() {
    const user = getCurrentUser();
    if (!user) return false;
    const users = getUsers();
    const index = users.findIndex(u => u.username === user.username);
    if (index === -1) return false;
    const currentPoints = users[index].points || 0;
    if (currentPoints < 1) return false;
    users[index].points = currentPoints - 1;
    saveUsers(users);
    setCurrentUser(users[index]);
    return true;
}

// ==================== 资源站解锁 ====================
function unlockResource(resourceId) {
    const user = getCurrentUser();
    if (!user) return false;
    const users = getUsers();
    const index = users.findIndex(u => u.username === user.username);
    if (index === -1) return false;
    if (!users[index].unlockedResources) users[index].unlockedResources = [];
    if (!users[index].unlockedResources.includes(resourceId)) {
        // 消耗 1 积分（电子书与资源站统一）
        if ((users[index].points || 0) < 1) return false;
        users[index].points -= 1;
        users[index].unlockedResources.push(resourceId);
        saveUsers(users);
        setCurrentUser(users[index]);
        return true;
    }
    return true; // 已解锁
}

function isResourceUnlocked(resourceId) {
    const user = getCurrentUser();
    if (!user) return false;
    const users = getUsers();
    const found = users.find(u => u.username === user.username);
    return found ? (found.unlockedResources || []).includes(resourceId) : false;
}

// ==================== 电子书解锁 ====================
function unlockBook(bookId) {
    const user = getCurrentUser();
    if (!user) return false;
    const users = getUsers();
    const index = users.findIndex(u => u.username === user.username);
    if (index === -1) return false;
    if (!users[index].unlockedBooks) users[index].unlockedBooks = [];
    if (!users[index].unlockedBooks.includes(bookId)) {
        if ((users[index].points || 0) < 1) return false;
        users[index].points -= 1;
        users[index].unlockedBooks.push(bookId);
        saveUsers(users);
        setCurrentUser(users[index]);
        return true;
    }
    return true; // 已解锁
}

function isBookUnlocked(bookId) {
    const user = getCurrentUser();
    if (!user) return false;
    const users = getUsers();
    const found = users.find(u => u.username === user.username);
    return found ? (found.unlockedBooks || []).includes(bookId) : false;
}

// ==================== 签到 ====================
function doSign() {
    const user = getCurrentUser();
    if (!user) return { success: false, message: '请先登录' };
    const users = getUsers();
    const index = users.findIndex(u => u.username === user.username);
    if (index === -1) return { success: false, message: '用户不存在' };

    const today = new Date().toDateString();
    const lastSign = users[index].lastSignDate || '';
    let consecutiveDays = users[index].consecutiveDays || 0;

    if (lastSign === today) {
        return { success: false, message: '今日已签到' };
    }

    const yesterday = new Date();
    yesterday.setDate(yesterday.getDate() - 1);
    const yesterdayStr = yesterday.toDateString();

    if (lastSign === yesterdayStr) {
        consecutiveDays += 1;
    } else {
        consecutiveDays = 1;
    }

    let pointsEarned = 0;
    if (consecutiveDays >= 10) {
        pointsEarned = 1;
        consecutiveDays = 0;
    }

    users[index].lastSignDate = today;
    users[index].consecutiveDays = consecutiveDays;
    users[index].points = (users[index].points || 0) + pointsEarned;
    saveUsers(users);
    setCurrentUser(users[index]);

    return {
        success: true,
        pointsEarned,
        consecutiveDays,
        totalPoints: users[index].points
    };
}

function getSignStatus() {
    const user = getCurrentUser();
    if (!user) return { signedToday: false, consecutiveDays: 0, points: 0 };
    const users = getUsers();
    const found = users.find(u => u.username === user.username);
    if (!found) return { signedToday: false, consecutiveDays: 0, points: 0 };
    const today = new Date().toDateString();
    return {
        signedToday: found.lastSignDate === today,
        consecutiveDays: found.consecutiveDays || 0,
        points: found.points || 0
    };
}

// ==================== 升级为资源用户 ====================
function upgradeToResource(inviteCode) {
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
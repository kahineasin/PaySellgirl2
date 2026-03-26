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

function getTodayStr() {
    const d = new Date();
    return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')}`;
}

function signIn() {
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

function getSignStatus() {
    const user = getCurrentUser();
    if (!user) return { signedToday: false, signDays: 0 };
    const users = getUsers();
    const found = users.find(u => u.username === user.username);
    if (!found) return { signedToday: false, signDays: 0 };
    const today = getTodayStr();
    return {
        signedToday: found.lastSignDate === today,
        signDays: found.signDays || 0
    };
}
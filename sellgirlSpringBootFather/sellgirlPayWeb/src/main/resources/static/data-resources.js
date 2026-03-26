// data-resources.js
const boards = [
    // ==================== 视频区 ====================
    {
        id: 'movie',
        name: '🎬 视频区',
        threads: [
            {
                id: 'm1',
                title: '盗梦空间',
                previewImg: 'assets/images/resources/1015.jpg',
                images: ['assets/images/resources/1015.jpg'],
                description: '诺兰巅峰之作，多层梦境',
                netdisk: 'https://pan.baidu.com/s/1movie1015',
                extractCode: '1234',
                unlockPassword: 'abc123',
                uploadTime: 1700000000,
                duration: 148,
                size: 2100
            },
            {
                id: 'm2',
                title: '肖申克的救赎',
                previewImg: 'assets/images/resources/1043.jpg',
                images: ['assets/images/resources/1043.jpg'],
                description: '希望与自由',
                netdisk: 'https://pan.baidu.com/s/1movie1043',
                extractCode: '5678',
                unlockPassword: 'def456',
                uploadTime: 1700500000,
                duration: 142,
                size: 1950
            },
            {
                id: 'm3',
                title: '星际穿越',
                previewImg: 'assets/images/resources/104.jpg',
                images: ['assets/images/resources/104.jpg'],
                description: '穿越虫洞',
                netdisk: 'https://pan.baidu.com/s/1movie104',
                extractCode: '9101',
                unlockPassword: 'ghi789',
                uploadTime: 1701000000,
                duration: 169,
                size: 2450
            },
            {
                id: 'm4',
                title: '阿凡达',
                previewImg: 'assets/images/resources/105.jpg',
                images: ['assets/images/resources/105.jpg'],
                description: '潘多拉星球',
                netdisk: 'https://pan.baidu.com/s/1movie105',
                extractCode: '1121',
                unlockPassword: 'jkl012',
                uploadTime: 1701500000,
                duration: 162,
                size: 3200
            },
            {
                id: 'm5',
                title: '泰坦尼克号',
                previewImg: 'assets/images/resources/106.jpg',
                images: ['assets/images/resources/106.jpg'],
                description: '经典爱情',
                netdisk: '#',
                extractCode: '',
                unlockPassword: '',
                uploadTime: 1702000000,
                duration: 194,
                size: 3800
            },
            {
                id: 'm6',
                title: '教父',
                previewImg: 'assets/images/resources/107.jpg',
                images: ['assets/images/resources/107.jpg'],
                description: '黑帮史诗',
                netdisk: '#',
                extractCode: '',
                unlockPassword: '',
                uploadTime: 1702500000,
                duration: 175,
                size: 2200
            },
            {
                id: 'm7',
                title: '这个杀手不太冷',
                previewImg: 'assets/images/resources/108.jpg',
                images: ['assets/images/resources/108.jpg'],
                description: '杀手与女孩',
                netdisk: '#',
                extractCode: '',
                unlockPassword: '',
                uploadTime: 1703000000,
                duration: 110,
                size: 1800
            },
            {
                id: 'm8',
                title: '楚门的世界',
                previewImg: 'assets/images/resources/109.jpg',
                images: ['assets/images/resources/109.jpg'],
                description: '虚假人生',
                netdisk: '#',
                extractCode: '',
                unlockPassword: '',
                uploadTime: 1703500000,
                duration: 103,
                size: 1650
            },
            {
                id: 'm9',
                title: '海上钢琴师',
                previewImg: 'assets/images/resources/110.jpg',
                images: ['assets/images/resources/110.jpg'],
                description: '音乐传奇',
                netdisk: '#',
                extractCode: '',
                unlockPassword: '',
                uploadTime: 1704000000,
                duration: 125,
                size: 2100
            },
            {
                id: 'm10',
                title: '放牛班的春天',
                previewImg: 'assets/images/resources/111.jpg',
                images: ['assets/images/resources/111.jpg'],
                description: '童声合唱',
                netdisk: '#',
                extractCode: '',
                unlockPassword: '',
                uploadTime: 1704500000,
                duration: 96,
                size: 1500
            },
            {
                id: 'm11',
                title: '阿甘正传',
                previewImg: 'assets/images/resources/112.jpg',
                images: ['assets/images/resources/112.jpg'],
                description: '人生如巧克力',
                netdisk: '#',
                extractCode: '',
                unlockPassword: '',
                uploadTime: 1705000000,
                duration: 142,
                size: 2100
            },
            {
                id: 'm12',
                title: '美丽人生',
                previewImg: 'assets/images/resources/113.jpg',
                images: ['assets/images/resources/113.jpg'],
                description: '父爱伟大',
                netdisk: '#',
                extractCode: '',
                unlockPassword: '',
                uploadTime: 1705500000,
                duration: 116,
                size: 1700
            }
        ]
    },
    // ==================== 图片区 ====================
    {
        id: 'image',
        name: '🖼️ 图片区',
        threads: [
            {
                id: 'i1',
                title: '极光摄影',
                previewImg: 'assets/images/resources/15.jpg',
                images: ['assets/images/resources/15.jpg'],
                description: '北极光精选',
                netdisk: 'https://pan.baidu.com/s/1image15',
                extractCode: 'abcd',
                unlockPassword: 'pass123',
                uploadTime: 1702000000,
                imageCount: 24
            },
            {
                id: 'i2',
                title: '城市夜景',
                previewImg: 'assets/images/resources/16.jpg',
                images: ['assets/images/resources/16.jpg'],
                description: '霓虹都市',
                netdisk: 'https://pan.baidu.com/s/1image16',
                extractCode: 'efgh',
                unlockPassword: 'pass456',
                uploadTime: 1702500000,
                imageCount: 36
            },
            {
                id: 'i3',
                title: '微观世界',
                previewImg: 'assets/images/resources/17.jpg',
                images: ['assets/images/resources/17.jpg'],
                description: '昆虫微距',
                netdisk: 'https://pan.baidu.com/s/1image17',
                extractCode: 'ijkl',
                unlockPassword: 'pass789',
                uploadTime: 1703000000,
                imageCount: 18
            },
            {
                id: 'i4',
                title: '人像精选',
                previewImg: 'assets/images/resources/18.jpg',
                images: ['assets/images/resources/18.jpg'],
                description: '情绪人像',
                netdisk: 'https://pan.baidu.com/s/1image18',
                extractCode: 'mnop',
                unlockPassword: 'pass000',
                uploadTime: 1703500000,
                imageCount: 42
            }
        ]
    },
    // ==================== 漫画区 ====================
    {
        id: 'comic',
        name: '📚 漫画区',
        threads: [
            {
                id: 'c1',
                title: '进击的巨人',
                previewImg: 'assets/images/resources/20.jpg',
                images: ['assets/images/resources/20.jpg'],
                description: '最终季漫画',
                netdisk: 'https://pan.baidu.com/s/1comic20',
                extractCode: 'qrst',
                unlockPassword: 'comic123',
                uploadTime: 1704000000,
                imageCount: 128
            },
            {
                id: 'c2',
                title: '海贼王',
                previewImg: 'assets/images/resources/21.jpg',
                images: ['assets/images/resources/21.jpg'],
                description: '和之国篇',
                netdisk: 'https://pan.baidu.com/s/1comic21',
                extractCode: 'uvwx',
                unlockPassword: 'comic456',
                uploadTime: 1704500000,
                imageCount: 256
            },
            {
                id: 'c3',
                title: '咒术回战',
                previewImg: 'assets/images/resources/22.jpg',
                images: ['assets/images/resources/22.jpg'],
                description: '涩谷事变',
                netdisk: 'https://pan.baidu.com/s/1comic22',
                extractCode: 'yzab',
                unlockPassword: 'comic789',
                uploadTime: 1705000000,
                imageCount: 96
            },
            {
                id: 'c4',
                title: '间谍过家家',
                previewImg: 'assets/images/resources/23.jpg',
                images: ['assets/images/resources/23.jpg'],
                description: '温馨日常',
                netdisk: 'https://pan.baidu.com/s/1comic23',
                extractCode: 'cdef',
                unlockPassword: 'comic000',
                uploadTime: 1705500000,
                imageCount: 64
            }
        ]
    }
];
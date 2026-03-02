# 乡村旅游智慧平台 - 用户端 API 文档

## 文档概述

- **平台名称**：乡村旅游智慧平台
- **版本**：1.0.0
- **客户端类型**：用户端（User Client）
- **后端框架**：Spring Boot
- **数据分析**：Python
- **前端框架**：Vue.js
- **API 协议**：RESTful
- **数据格式**：JSON
- **基础 URL**：`http://api.rural-tourism.com/api/v1`

---

## 目录

1. [通用规范](#通用规范)
2. [身份认证](#身份认证)
3. [用户管理](#用户管理)
4. [景点景区](#景点景区)
5. [民宿住宿](#民宿住宿)
6. [美食餐饮](#美食餐饮)
7. [活动体验](#活动体验)
8. [路线推荐](#路线推荐)
9. [订单管理](#订单管理)
10. [支付服务](#支付服务)
11. [评价评论](#评价评论)
12. [收藏夹](#收藏夹)
13. [消息通知](#消息通知)
14. [搜索推荐](#搜索推荐)
15. [数据分析](#数据分析)
16. [错误代码](#错误代码)

---

## 通用规范

### 请求格式

所有请求必须满足以下格式：

```json
{
  "method": "GET|POST|PUT|DELETE",
  "headers": {
    "Content-Type": "application/json",
    "Authorization": "Bearer {token}",
    "User-Agent": "rural-tourism-app/1.0"
  }
}
```

### 响应格式

所有响应遵循统一的 JSON 结构：

**成功响应**
```json
{
  "code": 200,
  "message": "success",
  "timestamp": "2026-03-01T10:30:00Z",
  "data": {},
  "requestId": "uuid"
}
```

**失败响应**
```json
{
  "code": 400,
  "message": "error description",
  "timestamp": "2026-03-01T10:30:00Z",
  "data": null,
  "requestId": "uuid"
}
```

### 分页参数

所有列表接口均支持分页：

| 参数名 | 类型 | 必需 | 说明 |
|--------|------|------|------|
| pageNo | Integer | 否 | 页码，默认 1 |
| pageSize | Integer | 否 | 每���数量，默认 10，最大 100 |
| sort | String | 否 | 排序字段 |
| order | String | 否 | 排序顺序 (asc/desc) |

### 时间格式

所有时间戳采用 ISO 8601 格式：`YYYY-MM-DDTHH:mm:ssZ`

---

## 身份认证

### 1. 用户注册

**端点**
```
POST /auth/register
```

**请求体**
```json
{
  "phone": "13800138000",
  "password": "securePassword123",
  "name": "张三",
  "idCard": "110101199003071234",
  "verifyCode": "123456"
}
```

**响应**
```json
{
  "code": 201,
  "message": "register success",
  "data": {
    "userId": "uuid",
    "phone": "13800138000",
    "name": "张三",
    "token": "eyJhbGc...",
    "refreshToken": "eyJhbGc...",
    "expiresIn": 7200
  }
}
```

**状态码**
- `201` - 注册成功
- `400` - 参数不合法
- `409` - 用户已存在

---

### 2. 用户登录

**端点**
```
POST /auth/login
```

**请求体**
```json
{
  "phone": "13800138000",
  "password": "securePassword123",
  "deviceId": "device-uuid",
  "loginType": "phone"
}
```

**响应**
```json
{
  "code": 200,
  "message": "login success",
  "data": {
    "userId": "uuid",
    "phone": "13800138000",
    "name": "张三",
    "avatar": "https://...",
    "token": "eyJhbGc...",
    "refreshToken": "eyJhbGc...",
    "expiresIn": 7200,
    "loginTime": "2026-03-01T10:30:00Z"
  }
}
```

**状态码**
- `200` - 登录成功
- `401` - 用户名或密码错误
- `404` - 用户不存在

---

### 3. 发送验证码

**端点**
```
POST /auth/send-verify-code
```

**请求体**
```json
{
  "phone": "13800138000",
  "type": "register",
  "countryCode": "+86"
}
```

**参数说明**
- `type`: register(注册), login(登录), reset(重置密码), bind(绑定)

**响应**
```json
{
  "code": 200,
  "message": "verify code sent",
  "data": {
    "requestId": "uuid",
    "nextSendTime": 60
  }
}
```

---

### 4. 刷新 Token

**端点**
```
POST /auth/refresh-token
```

**请求体**
```json
{
  "refreshToken": "eyJhbGc..."
}
```

**响应**
```json
{
  "code": 200,
  "message": "token refreshed",
  "data": {
    "token": "eyJhbGc...",
    "refreshToken": "eyJhbGc...",
    "expiresIn": 7200
  }
}
```

---

### 5. 用户登出

**端点**
```
POST /auth/logout
```

**请求头**
```
Authorization: Bearer {token}
```

**响应**
```json
{
  "code": 200,
  "message": "logout success"
}
```

---

## 用户管理

### 1. 获取用户信息

**端点**
```
GET /users/profile
```

**请求头**
```
Authorization: Bearer {token}
```

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "userId": "uuid",
    "phone": "13800138000",
    "name": "张三",
    "avatar": "https://...",
    "gender": "male",
    "idCard": "110101199003071234",
    "birthDate": "1990-03-07",
    "age": 36,
    "region": "北京市朝阳区",
    "bio": "旅游爱好者",
    "memberLevel": "gold",
    "totalScore": 1500,
    "createdAt": "2025-01-15T08:30:00Z",
    "updatedAt": "2026-03-01T10:30:00Z"
  }
}
```

---

### 2. 更新用户信息

**端点**
```
PUT /users/profile
```

**请求体**
```json
{
  "name": "张三",
  "avatar": "https://...",
  "gender": "male",
  "birthDate": "1990-03-07",
  "region": "北京市朝阳区",
  "bio": "旅游爱好者"
}
```

**响应**
```json
{
  "code": 200,
  "message": "profile updated successfully",
  "data": {
    "userId": "uuid",
    "name": "张三",
    "avatar": "https://...",
    "updatedAt": "2026-03-01T10:35:00Z"
  }
}
```

---

### 3. 修改密码

**端点**
```
PUT /users/password
```

**请求体**
```json
{
  "oldPassword": "oldPassword123",
  "newPassword": "newPassword456",
  "confirmPassword": "newPassword456"
}
```

**响应**
```json
{
  "code": 200,
  "message": "password changed successfully"
}
```

---

### 4. 重置密码

**端点**
```
POST /users/reset-password
```

**请求体**
```json
{
  "phone": "13800138000",
  "verifyCode": "123456",
  "newPassword": "newPassword456",
  "confirmPassword": "newPassword456"
}
```

**响应**
```json
{
  "code": 200,
  "message": "password reset successfully"
}
```

---

### 5. 上传头像

**端点**
```
POST /users/avatar
```

**请求类型**
```
Content-Type: multipart/form-data
```

**请求参数**
- `file` (File) - 图片文件，最大 5MB

**响应**
```json
{
  "code": 200,
  "message": "avatar uploaded successfully",
  "data": {
    "avatarUrl": "https://oss.rural-tourism.com/avatars/user-uuid.jpg",
    "uploadTime": "2026-03-01T10:40:00Z"
  }
}
```

---

### 6. 获取用户积分

**端点**
```
GET /users/points
```

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalPoints": 1500,
    "availablePoints": 1200,
    "usedPoints": 300,
    "pointsExpireDate": "2027-03-01",
    "pointsHistory": [
      {
        "id": "uuid",
        "type": "order",
        "points": 50,
        "description": "订单消费获得",
        "createdAt": "2026-03-01T10:30:00Z"
      }
    ]
  }
}
```

---

### 7. 用户地址簿

#### 获取地址列表
**端点**
```
GET /users/addresses
```

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "addressId": "uuid",
      "name": "家里",
      "province": "北京市",
      "city": "北京市",
      "district": "朝阳区",
      "address": "某街道1号",
      "postalCode": "100000",
      "phone": "13800138000",
      "isDefault": true,
      "createdAt": "2026-03-01T10:30:00Z"
    }
  ]
}
```

#### 添加地址
**端点**
```
POST /users/addresses
```

**请求体**
```json
{
  "name": "家里",
  "province": "北京市",
  "city": "北京市",
  "district": "朝阳区",
  "address": "某街道1号",
  "postalCode": "100000",
  "phone": "13800138000",
  "isDefault": false
}
```

#### 更新地址
**端点**
```
PUT /users/addresses/{addressId}
```

#### 删除地址
**端点**
```
DELETE /users/addresses/{addressId}
```

---

## 景点景区

### 1. 获取景点列表

**端点**
```
GET /attractions
```

**查询参数**
```
pageNo=1&pageSize=10&region=北京&rating=4&sort=rating&order=desc
```

| 参数 | 类型 | 说明 |
|------|------|------|
| pageNo | Integer | 页码 |
| pageSize | Integer | 每页数量 |
| region | String | 地区搜索 |
| category | String | 分类 (natural/cultural/theme) |
| rating | Float | 最低评分 |
| priceMin | Float | 最低价格 |
| priceMax | Float | 最高价格 |
| sort | String | 排序字段 |
| order | String | 排序顺序 |

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "pageNo": 1,
    "pageSize": 10,
    "list": [
      {
        "attractionId": "uuid",
        "name": "故宫博物院",
        "category": "cultural",
        "province": "北京市",
        "city": "北京市",
        "district": "东城区",
        "address": "景山前街4号",
        "latitude": 39.9163,
        "longitude": 116.3972,
        "description": "中国古代宫殿建筑",
        "mainImage": "https://...",
        "images": ["https://...", "https://..."],
        "rating": 4.8,
        "reviewCount": 5000,
        "price": 60,
        "openTime": "08:30",
        "closeTime": "17:00",
        "visitDuration": "3-4小时",
        "tags": ["世界遗产", "5A景区", "历史"],
        "facilities": ["停车场", "餐厅", "卫生间"],
        "isCollected": false,
        "isFavorite": false
      }
    ]
  }
}
```

---

### 2. 获取景点详情

**端点**
```
GET /attractions/{attractionId}
```

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "attractionId": "uuid",
    "name": "故宫博物院",
    "category": "cultural",
    "province": "北京市",
    "city": "北京市",
    "district": "东城区",
    "address": "景山前街4号",
    "latitude": 39.9163,
    "longitude": 116.3972,
    "description": "中国古代宫殿建筑",
    "detailedDescription": "故宫是中国明清两代的皇家宫殿...",
    "mainImage": "https://...",
    "images": ["https://...", "https://..."],
    "rating": 4.8,
    "reviewCount": 5000,
    "price": 60,
    "openTime": "08:30",
    "closeTime": "17:00",
    "visitDuration": "3-4小时",
    "tags": ["世界遗产", "5A景区"],
    "facilities": ["停车场", "餐厅", "卫生间"],
    "bestVisitTime": "3-5月，9-11月",
    "trafficInfo": "地铁S2线",
    "servicePhone": "010-8500-0321",
    "website": "https://www.dpm.org.cn",
    "ticketInfo": {
      "adultPrice": 60,
      "childPrice": 30,
      "studentPrice": 30,
      "seniorPrice": 0,
      "bookingUrl": "https://..."
    },
    "relatedAttractions": [
      {
        "attractionId": "uuid",
        "name": "景山公园",
        "distance": "0.5km"
      }
    ],
    "isCollected": false,
    "isFavorite": false
  }
}
```

---

### 3. 获取景点评价

**端点**
```
GET /attractions/{attractionId}/reviews
```

**查询参数**
```
pageNo=1&pageSize=10&rating=5&sort=helpful
```

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 500,
    "averageRating": 4.7,
    "ratingDistribution": {
      "5": 350,
      "4": 100,
      "3": 30,
      "2": 10,
      "1": 10
    },
    "list": [
      {
        "reviewId": "uuid",
        "userId": "uuid",
        "userName": "张三",
        "avatar": "https://...",
        "rating": 5,
        "title": "非常棒的体验",
        "content": "故宫值得一去，建筑宏伟...",
        "images": ["https://..."],
        "visitDate": "2026-02-28",
        "helpful": 150,
        "unhelpful": 10,
        "createdAt": "2026-03-01T10:30:00Z"
      }
    ]
  }
}
```

---

### 4. 搜索景点

**端点**
```
GET /attractions/search
```

**查询参数**
```
keyword=故宫&region=北京&limit=20
```

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "attractionId": "uuid",
      "name": "故宫博物院",
      "region": "北京市",
      "rating": 4.8,
      "image": "https://..."
    }
  ]
}
```

---

## 民宿住宿

### 1. 获取民宿列表

**端点**
```
GET /homestays
```

**查询参数**
```
pageNo=1&pageSize=10&region=丽江&checkIn=2026-03-15&checkOut=2026-03-17&guests=2&minPrice=100&maxPrice=500
```

| 参数 | 类型 | 说明 |
|------|------|------|
| region | String | 地区 |
| checkIn | Date | 入住日期 (YYYY-MM-DD) |
| checkOut | Date | 退房日期 (YYYY-MM-DD) |
| guests | Integer | 入住人数 |
| minPrice | Float | 最低价格 |
| maxPrice | Float | 最高价格 |
| type | String | 类型 (house/apartment/room) |
| rating | Float | 最低评分 |

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 200,
    "pageNo": 1,
    "pageSize": 10,
    "list": [
      {
        "homestayId": "uuid",
        "name": "古城边的小院",
        "type": "house",
        "region": "云南丽江",
        "address": "丽江古城区南门街",
        "latitude": 26.8741,
        "longitude": 100.2299,
        "mainImage": "https://...",
        "images": ["https://...", "https://..."],
        "description": "纳西民居风格的传统小院",
        "rating": 4.7,
        "reviewCount": 300,
        "price": 280,
        "originalPrice": 350,
        "discount": 20,
        "bedrooms": 3,
        "bathrooms": 2,
        "guests": 6,
        "amenities": ["WiFi", "厨房", "洗衣机", "空调"],
        "checkInTime": "15:00",
        "checkOutTime": "11:00",
        "cancellationPolicy": "free_before_7days",
        "hostName": "李四",
        "hostRating": 4.8,
        "isCollected": false,
        "availability": {
          "checkIn": "2026-03-15",
          "checkOut": "2026-03-17",
          "available": true
        }
      }
    ]
  }
}
```

---

### 2. 获取民宿详情

**端点**
```
GET /homestays/{homestayId}
```

**查询参数**
```
checkIn=2026-03-15&checkOut=2026-03-17
```

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "homestayId": "uuid",
    "name": "古城边的小院",
    "type": "house",
    "region": "云南丽江",
    "address": "丽江古城区南门街",
    "latitude": 26.8741,
    "longitude": 100.2299,
    "mainImage": "https://...",
    "images": ["https://...", "https://..."],
    "description": "纳西民居风格的传统小院",
    "detailedDescription": "这是一个保留了传统纳西风格的小院...",
    "rating": 4.7,
    "reviewCount": 300,
    "basePrice": 280,
    "originalPrice": 350,
    "discount": 20,
    "currency": "CNY",
    "bedrooms": 3,
    "bathrooms": 2,
    "livingRooms": 1,
    "guests": 6,
    "squareMeter": 120,
    "amenities": ["WiFi", "厨房", "洗衣机", "空调", "电视", "冰箱"],
    "facilities": ["停车场", "花园", "露台"],
    "checkInTime": "15:00",
    "checkOutTime": "11:00",
    "cancellationPolicy": "free_before_7days",
    "minNights": 1,
    "maxNights": 365,
    "host": {
      "hostId": "uuid",
      "name": "李四",
      "avatar": "https://...",
      "rating": 4.8,
      "responseRate": 95,
      "responseTime": "1小时",
      "responseLanguage": ["中文", "英文"]
    },
    "pricing": {
      "checkIn": "2026-03-15",
      "checkOut": "2026-03-17",
      "nights": 2,
      "pricePerNight": 280,
      "subtotal": 560,
      "cleaningFee": 50,
      "serviceFee": 56,
      "total": 666
    },
    "rules": [
      "禁止吸烟",
      "不允许宠物",
      "没有聚会或活动"
    ],
    "nearbyAttractions": [
      {
        "name": "丽江古城",
        "distance": "0.5km",
        "type": "attraction"
      }
    ],
    "isCollected": false
  }
}
```

---

### 3. 获取民宿评价

**端点**
```
GET /homestays/{homestayId}/reviews
```

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 300,
    "averageRating": 4.7,
    "ratingDistribution": {
      "5": 200,
      "4": 80,
      "3": 15,
      "2": 3,
      "1": 2
    },
    "list": [
      {
        "reviewId": "uuid",
        "userId": "uuid",
        "userName": "王五",
        "avatar": "https://...",
        "rating": 5,
        "title": "古城的一处静谧之地",
        "content": "民宿很干净，老板热情好客...",
        "images": ["https://..."],
        "checkInDate": "2026-02-28",
        "nights": 2,
        "cleanliness": 5,
        "communication": 5,
        "location": 5,
        "value": 5,
        "helpful": 50,
        "unhelpful": 2,
        "createdAt": "2026-03-01T10:30:00Z"
      }
    ]
  }
}
```

---

## 美食餐饮

### 1. 获取餐厅列表

**端点**
```
GET /restaurants
```

**查询参数**
```
pageNo=1&pageSize=10&region=丽江&cuisine=云南菜&rating=4&sort=rating&order=desc
```

| 参数 | 类型 | 说明 |
|------|------|------|
| region | String | 地区 |
| cuisine | String | 菜系 |
| priceMin | Float | 最低价格 |
| priceMax | Float | 最高价格 |
| rating | Float | 最低评分 |
| sort | String | 排序字段 |

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 150,
    "pageNo": 1,
    "pageSize": 10,
    "list": [
      {
        "restaurantId": "uuid",
        "name": "老四川火锅",
        "cuisine": "川菜",
        "region": "云南丽江",
        "address": "丽江古城区文明街",
        "latitude": 26.8741,
        "longitude": 100.2299,
        "mainImage": "https://...",
        "images": ["https://...", "https://..."],
        "description": "正宗四川火锅，麻辣鲜香",
        "rating": 4.6,
        "reviewCount": 800,
        "pricePerPerson": 120,
        "openTime": "11:00",
        "closeTime": "23:00",
        "phone": "0888-5188888",
        "tags": ["火锅", "川菜", "人气高"],
        "dishes": [
          {
            "dishId": "uuid",
            "name": "毛肚",
            "price": 25,
            "image": "https://..."
          }
        ],
        "isCollected": false
      }
    ]
  }
}
```

---

### 2. 获取餐厅详情

**端点**
```
GET /restaurants/{restaurantId}
```

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "restaurantId": "uuid",
    "name": "老四川火锅",
    "cuisine": "川菜",
    "region": "云南丽江",
    "address": "丽江古城区文明街",
    "latitude": 26.8741,
    "longitude": 100.2299,
    "mainImage": "https://...",
    "images": ["https://...", "https://..."],
    "description": "正宗四川火锅",
    "detailedDescription": "我们的火锅选材讲究...",
    "rating": 4.6,
    "reviewCount": 800,
    "pricePerPerson": 120,
    "openTime": "11:00",
    "closeTime": "23:00",
    "phone": "0888-5188888",
    "website": "https://...",
    "seats": 80,
    "tags": ["火锅", "川菜", "人气高"],
    "services": ["外卖", "堂食", "订座"],
    "payment": ["微信", "支付宝", "现金", "银行卡"],
    "menu": [
      {
        "categoryId": "uuid",
        "categoryName": "素菜",
        "dishes": [
          {
            "dishId": "uuid",
            "name": "冬瓜",
            "description": "清汤冬瓜",
            "price": 15,
            "image": "https://...",
            "spicy": 0,
            "popular": false
          }
        ]
      }
    ],
    "reservationRequired": false,
    "parkingAvailable": true,
    "atmosphere": 4.5,
    "service": 4.7,
    "taste": 4.8,
    "isCollected": false
  }
}
```

---

### 3. 获取菜品详情

**端点**
```
GET /restaurants/{restaurantId}/dishes/{dishId}
```

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "dishId": "uuid",
    "name": "毛肚",
    "description": "精选毛肚，嫩滑可口",
    "price": 25,
    "image": "https://...",
    "images": ["https://..."],
    "cuisine": "川菜",
    "spicyLevel": 3,
    "ingredients": ["毛肚", "辣椒油", "花椒"],
    "nutritionInfo": "热量: 150 kcal",
    "allergens": ["花椒"],
    "rating": 4.8,
    "reviewCount": 200,
    "popular": true,
    "soldToday": 150
  }
}
```

---

### 4. 餐厅评价

**端点**
```
GET /restaurants/{restaurantId}/reviews
```

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 800,
    "averageRating": 4.6,
    "ratingDistribution": {
      "5": 500,
      "4": 200,
      "3": 80,
      "2": 15,
      "1": 5
    },
    "list": [
      {
        "reviewId": "uuid",
        "userId": "uuid",
        "userName": "赵六",
        "avatar": "https://...",
        "rating": 5,
        "title": "正宗四川火锅",
        "content": "麻辣鲜香，食材新鲜...",
        "images": ["https://..."],
        "visitDate": "2026-02-28",
        "taste": 5,
        "service": 4,
        "environment": 4,
        "price": 4,
        "helpful": 30,
        "unhelpful": 1,
        "createdAt": "2026-03-01T10:30:00Z"
      }
    ]
  }
}
```

---

## 活动体验

### 1. 获取活动列表

**端点**
```
GET /activities
```

**查询参数**
```
pageNo=1&pageSize=10&region=丽江&category=hiking&startDate=2026-03-15&minPrice=100&maxPrice=500
```

| 参数 | 类型 | 说明 |
|------|------|------|
| region | String | 地区 |
| category | String | 分类 (hiking/cooking/cultural/water-sports) |
| startDate | Date | 开始日期 |
| endDate | Date | 结束日期 |
| minPrice | Float | 最低价格 |
| maxPrice | Float | 最高价格 |
| difficulty | String | 难度 (easy/medium/hard) |

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 80,
    "pageNo": 1,
    "pageSize": 10,
    "list": [
      {
        "activityId": "uuid",
        "name": "古城文化漫步之旅",
        "category": "cultural",
        "region": "云南丽江",
        "location": "丽江古城",
        "mainImage": "https://...",
        "images": ["https://...", "https://..."],
        "description": "跟随本地导游探索丽江古城的文化秘密",
        "rating": 4.8,
        "reviewCount": 200,
        "price": 198,
        "originalPrice": 280,
        "duration": "3小时",
        "difficulty": "easy",
        "groupSize": "8-15人",
        "startTime": "09:00",
        "availableDates": ["2026-03-15", "2026-03-16", "2026-03-17"],
        "meetingPoint": "丽江古城南门广场",
        "highlights": ["古城讲解", "特色美食", "文化体验"],
        "included": ["导游费", "饮用水"],
        "notIncluded": ["餐饮", "门票"],
        "language": "中文",
        "instructor": {
          "instructorId": "uuid",
          "name": "王导游",
          "avatar": "https://...",
          "rating": 4.9,
          "experience": "8年"
        },
        "maxParticipants": 15,
        "currentParticipants": 8,
        "isCollected": false
      }
    ]
  }
}
```

---

### 2. 获取活动详情

**端点**
```
GET /activities/{activityId}
```

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "activityId": "uuid",
    "name": "古城文化漫步之旅",
    "category": "cultural",
    "region": "云南丽江",
    "location": "丽江古城",
    "mainImage": "https://...",
    "images": ["https://...", "https://..."],
    "description": "跟随本地导游探索丽江古城的文化秘密",
    "detailedDescription": "这是一个深度了解丽江文化的行程...",
    "rating": 4.8,
    "reviewCount": 200,
    "price": 198,
    "originalPrice": 280,
    "discount": 30,
    "currency": "CNY",
    "duration": "3小时",
    "difficulty": "easy",
    "groupSize": "8-15人",
    "scheduleTime": "09:00-12:00",
    "meetingPoint": "丽江古城南门广场",
    "endPoint": "丽江古城南门广场",
    "availableDates": ["2026-03-15", "2026-03-16"],
    "highlights": ["古城讲解", "特色美食品尝", "文化体验"],
    "itinerary": [
      {
        "order": 1,
        "time": "09:00",
        "location": "南门广场",
        "activity": "集合点名"
      },
      {
        "order": 2,
        "time": "09:15",
        "location": "古城街道",
        "activity": "古城讲解"
      }
    ],
    "included": ["专业导游", "饮用水"],
    "notIncluded": ["餐饮", "门票"],
    "languages": ["中文", "英文"],
    "minAge": 0,
    "maxAge": 120,
    "physicalRequirement": "无特殊要求",
    "instructor": {
      "instructorId": "uuid",
      "name": "王导游",
      "avatar": "https://...",
      "rating": 4.9,
      "reviewCount": 500,
      "experience": "8年",
      "languages": ["中文", "英文"],
      "bio": "资深旅游导游"
    },
    "maxParticipants": 15,
    "currentParticipants": 8,
    "cancellationPolicy": "free_before_24hours",
    "weather": "晴天最佳",
    "whatToBring": ["舒适的鞋子", "防晒霜", "相机"],
    "notes": ["请提前15分钟到达"],
    "isCollected": false
  }
}
```

---

### 3. 预订活动

**端点**
```
POST /activities/{activityId}/bookings
```

**请求体**
```json
{
  "selectedDate": "2026-03-15",
  "selectedTime": "09:00",
  "participants": [
    {
      "name": "张三",
      "phone": "13800138000",
      "age": 30,
      "relation": "self"
    }
  ],
  "totalParticipants": 1,
  "specialRequirements": "需要轮椅无障碍路线",
  "notificationPreference": "email"
}
```

**响应**
```json
{
  "code": 201,
  "message": "booking created successfully",
  "data": {
    "bookingId": "uuid",
    "activityId": "uuid",
    "activityName": "古城文化漫步之旅",
    "selectedDate": "2026-03-15",
    "selectedTime": "09:00",
    "participants": 1,
    "totalPrice": 198,
    "status": "pending_payment",
    "createdAt": "2026-03-01T10:30:00Z"
  }
}
```

---

## 路线推荐

### 1. 获取推荐路线列表

**端点**
```
GET /routes
```

**查询参数**
```
pageNo=1&pageSize=10&region=云南&duration=3-5days&budget=5000-10000
```

| 参数 | 类型 | 说明 |
|------|------|------|
| region | String | 地区 |
| duration | String | 时长 (1day/2-3days/3-5days/more) |
| budget | String | 预算范围 |
| theme | String | 主题 (nature/culture/adventure) |

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 50,
    "list": [
      {
        "routeId": "uuid",
        "name": "云南七彩之旅",
        "region": "云南",
        "theme": "nature",
        "duration": "5天4晚",
        "mainImage": "https://...",
        "images": ["https://...", "https://..."],
        "description": "昆明-大理-丽江-香格里拉全景之旅",
        "rating": 4.8,
        "reviewCount": 500,
        "estimatedBudget": 8000,
        "budget_breakdown": "住宿2000+餐饮1500+门票1500+交通1500+购物1500",
        "attractionsCount": 15,
        "highlights": ["大理洱海", "丽江古城", "虎跳峡"],
        "bestSeason": "4-10月",
        "difficulty": "easy",
        "isCollected": false
      }
    ]
  }
}
```

---

### 2. 获取路线详情

**端点**
```
GET /routes/{routeId}
```

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "routeId": "uuid",
    "name": "云南七彩之旅",
    "region": "云南",
    "theme": "nature",
    "duration": "5天4晚",
    "mainImage": "https://...",
    "images": ["https://...", "https://..."],
    "description": "昆明-大理-丽江-香格里拉全景之旅",
    "detailedDescription": "这是一条集山水、人文、美食于一体的黄金线路...",
    "rating": 4.8,
    "reviewCount": 500,
    "estimatedBudget": 8000,
    "budgetBreakdown": {
      "accommodation": 2000,
      "food": 1500,
      "attraction_tickets": 1500,
      "transportation": 1500,
      "shopping": 1500
    },
    "bestSeason": "4-10月",
    "difficulty": "easy",
    "physicalRequirement": "低体力要求",
    "itinerary": [
      {
        "day": 1,
        "title": "抵达昆明",
        "attractions": [
          {
            "attractionId": "uuid",
            "name": "昆明市区游览",
            "description": "翠湖公园、民族博物馆"
          }
        ],
        "accommodation": {
          "homestayId": "uuid",
          "name": "昆明市中心酒店",
          "price": 400
        },
        "meals": ["dinner"],
        "summary": "抵达昆明后，自由活动或参加城市游览"
      }
    ],
    "attractions": [
      {
        "attractionId": "uuid",
        "name": "洱海",
        "city": "大理"
      }
    ],
    "restaurants": [
      {
        "restaurantId": "uuid",
        "name": "云南特色菜馆",
        "city": "大理"
      }
    ],
    "homestays": [
      {
        "homestayId": "uuid",
        "name": "民族风格小院",
        "city": "丽江"
      }
    ],
    "transportation": {
      "type": "self-drive",
      "totalDistance": "1500km",
      "totalDrivingTime": "18小时",
      "notes": "需国际驾照或国内驾照翻译件"
    },
    "packingList": ["防晒霜", "舒适的鞋子", "相机"],
    "tips": ["高原反应预防", "季节特点"],
    "contacts": [
      {
        "name": "路线规划师",
        "phone": "010-8888-8888",
        "email": "planner@example.com"
      }
    ],
    "isCollected": false
  }
}
```

---

### 3. 自定义路线

**端点**
```
POST /routes/customize
```

**请求体**
```json
{
  "name": "我的丽江之旅",
  "region": "云南丽江",
  "duration": "3天2晚",
  "budget": 5000,
  "theme": ["nature", "culture"],
  "startDate": "2026-03-15",
  "endDate": "2026-03-17",
  "interests": ["hiking", "photography", "local-cuisine"],
  "attractions": ["uuid1", "uuid2"],
  "homestays": ["uuid1"],
  "restaurants": ["uuid1", "uuid2"],
  "activities": ["uuid1"],
  "notes": "希望远离人群，体验原汁原味的丽江"
}
```

**响应**
```json
{
  "code": 201,
  "message": "custom route created successfully",
  "data": {
    "routeId": "uuid",
    "name": "我的丽江之旅",
    "status": "draft",
    "createdAt": "2026-03-01T10:30:00Z"
  }
}
```

---

## 订单管理

### 1. 获取订单列表

**端点**
```
GET /orders
```

**查询参数**
```
pageNo=1&pageSize=10&status=completed&sort=createdAt&order=desc
```

| 参数 | 类型 | 说明 |
|------|------|------|
| status | String | 状态 (pending/confirmed/cancelled/completed) |
| type | String | 类型 (attraction/homestay/restaurant/activity) |
| sort | String | 排序字段 |
| order | String | 排序顺序 |

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 50,
    "pageNo": 1,
    "pageSize": 10,
    "list": [
      {
        "orderId": "uuid",
        "orderNo": "ORD20260301001",
        "type": "homestay",
        "itemName": "古城边的小院",
        "itemImage": "https://...",
        "price": 666,
        "discount": 0,
        "totalPrice": 666,
        "quantity": 1,
        "status": "completed",
        "paymentStatus": "paid",
        "paymentMethod": "wechat",
        "createdAt": "2026-02-15T10:30:00Z",
        "updatedAt": "2026-03-01T10:30:00Z",
        "checkIn": "2026-02-28",
        "checkOut": "2026-03-01",
        "rating": {
          "ratingId": "uuid",
          "rating": 5,
          "hasReview": true
        }
      }
    ]
  }
}
```

---

### 2. 获取订单详情

**端点**
```
GET /orders/{orderId}
```

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "orderId": "uuid",
    "orderNo": "ORD20260301001",
    "type": "homestay",
    "itemId": "uuid",
    "itemName": "古城边的小院",
    "itemImage": "https://...",
    "itemDetails": {
      "homestayId": "uuid",
      "region": "云南丽江",
      "address": "丽江古城区南门街"
    },
    "checkIn": "2026-02-28",
    "checkOut": "2026-03-01",
    "nights": 2,
    "guests": 2,
    "pricePerNight": 280,
    "subtotal": 560,
    "cleaningFee": 50,
    "serviceFee": 56,
    "discount": 0,
    "totalPrice": 666,
    "paymentStatus": "paid",
    "paymentMethod": "wechat",
    "paymentTime": "2026-02-15T10:35:00Z",
    "orderstatus": "completed",
    "statusHistory": [
      {
        "status": "pending",
        "timestamp": "2026-02-15T10:30:00Z"
      },
      {
        "status": "confirmed",
        "timestamp": "2026-02-15T10:35:00Z"
      }
    ],
    "hostInfo": {
      "hostId": "uuid",
      "name": "李四",
      "avatar": "https://...",
      "phone": "13900139000",
      "email": "host@example.com"
    },
    "guestInfo": {
      "name": "张三",
      "phone": "13800138000",
      "email": "guest@example.com"
    },
    "cancellationPolicy": "free_before_7days",
    "specialRequests": "需要接送服务",
    "notes": "已确认房间",
    "createdAt": "2026-02-15T10:30:00Z",
    "updatedAt": "2026-03-01T10:30:00Z"
  }
}
```

---

### 3. 取消订单

**端点**
```
POST /orders/{orderId}/cancel
```

**请求体**
```json
{
  "reason": "行程变更",
  "description": "由于工作原因无法按时出行"
}
```

**响应**
```json
{
  "code": 200,
  "message": "order cancelled successfully",
  "data": {
    "orderId": "uuid",
    "status": "cancelled",
    "refund": {
      "originalPrice": 666,
      "refundAmount": 500,
      "refundReason": "取消费用",
      "refundStatus": "processing",
      "estimatedRefundDate": "2026-03-05"
    }
  }
}
```

---

### 4. 修改订单

**端点**
```
PUT /orders/{orderId}
```

**请求体**
```json
{
  "checkIn": "2026-03-15",
  "checkOut": "2026-03-17",
  "guests": 2,
  "specialRequests": "需要婴儿床"
}
```

**响应**
```json
{
  "code": 200,
  "message": "order updated successfully",
  "data": {
    "orderId": "uuid",
    "newPrice": 700,
    "priceDifference": 34,
    "paymentRequired": true
  }
}
```

---

### 5. 下载订单凭证

**端点**
```
GET /orders/{orderId}/voucher
```

**响应**
```
Content-Type: application/pdf
Content-Disposition: attachment; filename="ORD20260301001.pdf"

[PDF 二进制数据]
```

---

## 支付服务

### 1. 创建支付

**端点**
```
POST /payments/create
```

**请求体**
```json
{
  "orderId": "uuid",
  "amount": 666,
  "currency": "CNY",
  "paymentMethod": "wechat",
  "description": "古城边的小院民宿预订",
  "returnUrl": "https://app.rural-tourism.com/order/success"
}
```

**响应**
```json
{
  "code": 200,
  "message": "payment created",
  "data": {
    "paymentId": "uuid",
    "orderId": "uuid",
    "amount": 666,
    "paymentMethod": "wechat",
    "status": "pending",
    "qrCode": "https://...",
    "paymentUrl": "https://...",
    "expiresIn": 900,
    "createdAt": "2026-03-01T10:30:00Z"
  }
}
```

---

### 2. 支付宝支付

**端点**
```
POST /payments/alipay
```

**请求体**
```json
{
  "orderId": "uuid",
  "amount": 666,
  "currency": "CNY",
  "returnUrl": "https://app.rural-tourism.com/order/success"
}
```

**响应**
```json
{
  "code": 200,
  "message": "payment created",
  "data": {
    "paymentId": "uuid",
    "paymentUrl": "https://openapi.alipay.com/gateway.do?biz_content=...",
    "tradeNo": "2026030122001234567890",
    "expiresIn": 900
  }
}
```

---

### 3. 获取支付状态

**端点**
```
GET /payments/{paymentId}/status
```

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "paymentId": "uuid",
    "orderId": "uuid",
    "status": "completed",
    "amount": 666,
    "transactionId": "20260301001234567890",
    "paymentTime": "2026-03-01T10:35:00Z"
  }
}
```

---

### 4. 申请退款

**端点**
```
POST /payments/{paymentId}/refund
```

**请求体**
```json
{
  "amount": 500,
  "reason": "订单取消",
  "description": "用户取消订单，退款全部金额的75%"
}
```

**响应**
```json
{
  "code": 200,
  "message": "refund initiated",
  "data": {
    "refundId": "uuid",
    "paymentId": "uuid",
    "amount": 500,
    "status": "processing",
    "estimatedRefundDate": "2026-03-05",
    "createdAt": "2026-03-01T10:30:00Z"
  }
}
```

---

## 评价评论

### 1. 提交评价

**端点**
```
POST /reviews
```

**请求体**
```json
{
  "orderId": "uuid",
  "type": "homestay",
  "itemId": "uuid",
  "rating": 5,
  "title": "古城的一处静谧之地",
  "content": "民宿很干净，老板热情好客，强烈推荐！",
  "images": ["https://...", "https://..."],
  "tags": ["clean", "friendly", "convenient"],
  "ratingDetails": {
    "cleanliness": 5,
    "communication": 5,
    "location": 5,
    "value": 5
  }
}
```

**响应**
```json
{
  "code": 201,
  "message": "review submitted successfully",
  "data": {
    "reviewId": "uuid",
    "orderId": "uuid",
    "createdAt": "2026-03-01T10:30:00Z"
  }
}
```

---

### 2. 修改评价

**端点**
```
PUT /reviews/{reviewId}
```

**请求体**
```json
{
  "rating": 4,
  "title": "不错的住宿体验",
  "content": "修改后的评价内容..."
}
```

**响应**
```json
{
  "code": 200,
  "message": "review updated successfully",
  "data": {
    "reviewId": "uuid",
    "updatedAt": "2026-03-01T10:40:00Z"
  }
}
```

---

### 3. 删除评价

**端点**
```
DELETE /reviews/{reviewId}
```

**响应**
```json
{
  "code": 200,
  "message": "review deleted successfully"
}
```

---

### 4. 获取用户评价列表

**端点**
```
GET /users/reviews
```

**查询参数**
```
pageNo=1&pageSize=10&sort=createdAt&order=desc
```

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 20,
    "list": [
      {
        "reviewId": "uuid",
        "type": "homestay",
        "itemName": "古城边的小院",
        "itemImage": "https://...",
        "rating": 5,
        "title": "古城的一处静谧之地",
        "content": "民宿很干净，老板热情好客，强烈推荐！",
        "helpful": 50,
        "unhelpful": 2,
        "createdAt": "2026-03-01T10:30:00Z"
      }
    ]
  }
}
```

---

### 5. 评价有用/无用

**端点**
```
POST /reviews/{reviewId}/helpful
```

**请求体**
```json
{
  "type": "helpful"
}
```

**参数说明**
- `type`: helpful(有用), unhelpful(无用)

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "reviewId": "uuid",
    "helpful": 51,
    "unhelpful": 2
  }
}
```

---

## 收藏夹

### 1. 获取收藏列表

**端点**
```
GET /collections
```

**查询参数**
```
pageNo=1&pageSize=10&type=attraction
```

| 参数 | 类型 | 说明 |
|------|------|------|
| type | String | 类型 (all/attraction/homestay/restaurant/activity) |

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 30,
    "pageNo": 1,
    "pageSize": 10,
    "list": [
      {
        "collectionId": "uuid",
        "type": "attraction",
        "itemId": "uuid",
        "itemName": "故宫博物院",
        "itemImage": "https://...",
        "rating": 4.8,
        "price": 60,
        "location": "北京市朝阳区",
        "collectedAt": "2026-02-28T10:30:00Z"
      }
    ]
  }
}
```

---

### 2. 添加收藏

**端点**
```
POST /collections
```

**请求体**
```json
{
  "type": "attraction",
  "itemId": "uuid"
}
```

**响应**
```json
{
  "code": 201,
  "message": "added to collection",
  "data": {
    "collectionId": "uuid"
  }
}
```

---

### 3. 删除收藏

**端点**
```
DELETE /collections/{collectionId}
```

**响应**
```json
{
  "code": 200,
  "message": "removed from collection"
}
```

---

### 4. 批量操作

**端点**
```
POST /collections/batch
```

**请求体**
```json
{
  "action": "delete",
  "collectionIds": ["uuid1", "uuid2", "uuid3"]
}
```

**响应**
```json
{
  "code": 200,
  "message": "batch operation completed",
  "data": {
    "successCount": 3,
    "failedCount": 0
  }
}
```

---

## 消息通知

### 1. 获取通知列表

**端点**
```
GET /notifications
```

**查询参数**
```
pageNo=1&pageSize=20&type=order&read=false
```

| 参数 | 类型 | 说明 |
|------|------|------|
| type | String | 类型 (all/order/system/activity/message) |
| read | Boolean | 是否已读 |

**响应**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 15,
    "unreadCount": 5,
    "list": [
      {
        "notificationId": "uuid",
        "type": "order",
        "title": "订单确认",
        "content": "您的订单已确认，房主已收到您的预订",
        "image": "https://...",
        "read": false,
        "actionUrl": "/orders/uuid",
        "createdAt": "2026-03-01T10:30:00Z"
      }
    ]
  }
}
```

---

### 2. 标记已读

**端点**
```
PUT /notifications/{notificationId}/read
```

**响应**
```json
{
  "code": 200,
  "message": "marked as read"
}
```

---

### 3. 批量标记已读

**端点**
```
POST /notifications/batch-read
```

**请求体**
```json
{
  "notificationIds": ["uuid1", "uuid2", "uuid3"]
}
```

**响应**
```json
{
  "code": 200,
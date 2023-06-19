# Class_Expense_Android 班费记录程序安卓前端

Class_Expense_Backend 班费记录程序后端地址：https://github.com/MrNeoJeep/Class_Expense_Backend


> 任务描述：开发一个班费日常使用记录的APP，记账员记录班费的收支情况，每笔支出需要包含日期、金额、购买的实物照片、购物小票（如果有）、实物的验收人。<br>
> 班级成员可以查看和查询班费的开支情况，可以在1周内（讨论期）对当笔开支提出质疑，班委成员回复质疑，所有质疑和应答全员可见。<br>
> 班级成员半数以上确认且所有质疑经发起人确认可以close的记录，可以标记为已确认状态，之后任何人无权再修改。超过讨论期未close的质疑自动close。
> 
本次开发采用前后端分离开发，后端使用的开发工具是IntelliJ IDEA 2022.2.1，前端采用的开发工具是Android Studio，使用Postman接口调试与测试工具，版本控制工具选择git，采用GitHub进行代码远程托管。<br>
> 注意！！！ 由于时间的限制、疫情等不可抗力因素，本项目部分功能未完成，在质疑部分、多班级管理、管理员权限分配上仍存在部分问题。

该项目分为前端和后端两个部分。前端采用的是Android进行开发，后端采用的是基于SpringBoot，MyBatisPlus开发的后台处理系统。前端是用户能够直接接触到的所有视觉内容，也是本次项目的设计重点。当前端需要数据时，向后端发起请求，Java后端返回统一格式的Json字符串，前端接收这个Json字符串即可获取数据。<br>
本项目采用三级权限管理，分别是老师、记账员、学生，接下来详细介绍一些这三个角色拥有什么功能。
- 学生
学生是本软件中权限最小的，可以查看个人信息，班费信息，班费记录列表，对班费支出提出疑问
- 记账员
记账员除了拥有学生的所有权限之外，可以对班费记录进行修改和添加，可以回复同学的质疑，可以修改班费。
- 老师
老师拥有记账员的所有权限，老师可以任免记账员。

## 后端
后端使用到的主要技术栈有：Springboot、MyBatisPlus、Redis、Shiro。<br>
-	Springboot  该框架使用了特定的方式来进行配置，从而使开发人员不再需要定义样板化的配置。因此比较适合本项目的快速开发环境。<br>
-	MyBatisPlus 数据持久层框架，是mybatis的一款增强工具包，目的是简化开发，提高效率<br>
-	Redis  是key-value 存储系统，跨平台的非关系型数据库。因为存储在内存中，所以读写速度很快，一般用于对常用数据的缓存。
-	Shiro Apache Shiro 是一个强大灵活的开源安全框架，可以完全处理身份验证、授权、加密和会话管理。本项目使用Shiro进行登录身份验证。在Android端采用SharedPreferencesUtils保存和管理由后端传递的token信息，用户登录一次token信息可以保存一定时间，不用频繁登录。<br>
![image](https://github.com/MrNeoJeep/Class_Expense_Android/assets/76543465/d772a84f-2d47-4691-b023-bdc4b7610cb4)

## 前端
Android前端框架主要采用切片的思想，本软件在登录后进入主页面，主页面首先包含底部导航栏，默认加载的是记录列表页，当点击导航栏的个人中心按钮，页面跳转至个人中心，这里使用到的列表页面、个人中心页面均属于Fragment，由导航栏（NavActivity）进行统一管理。其次记录列表页面使用RecyclerView实现，RecyclerView具有良好的可扩展性，并且在设计上满足高内聚低耦合的原则。在进入具体的记录详情页面，整体布局采用线性布局，其中在实物照片和小票部分采用嵌套绝对布局，在最下面是质疑回复部分，这里选择的方式是嵌套RecyclerView的方式，当有人提出质疑，即向RecyclerView中加入一个列表项。<br>

![image](https://github.com/MrNeoJeep/Class_Expense_Android/assets/76543465/3cf13aa3-a232-4c62-b6a4-75b173d8091b)


## UI界面
- 登录

![image](https://github.com/MrNeoJeep/Class_Expense_Android/assets/76543465/9428b7c4-d4ff-4c57-b14f-8255ca96fa26)
- 注册

![image](https://github.com/MrNeoJeep/Class_Expense_Android/assets/76543465/477fe6fc-54ff-4d94-a2f8-0a731a3b44cf)

- 个人中心模块

![image](https://github.com/MrNeoJeep/Class_Expense_Android/assets/76543465/c6f2c764-8000-4323-9a50-b5e3fc52bb84)

- 班费记录模块

![image](https://github.com/MrNeoJeep/Class_Expense_Android/assets/76543465/620e4886-9acc-4fe7-b6e1-9b77c1395107)
![image](https://github.com/MrNeoJeep/Class_Expense_Android/assets/76543465/346e7d84-ae7c-4b46-aade-03e1b23c21e1)
![image](https://github.com/MrNeoJeep/Class_Expense_Android/assets/76543465/7490f517-b231-45aa-adf7-1f5025aa58dc)
![image](https://github.com/MrNeoJeep/Class_Expense_Android/assets/76543465/cda702dc-d74b-42f8-90b2-73e87b66db41)




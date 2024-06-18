基于规则实现动态访问控制，基于JsonFilter实现动态视图
1. 实现功能权限的校验, Assertion(user.id == 1, subject.id == 2, operate == 'add')  Jackson JsonPointer 
2. 实现数据权限的校验，Query
3. 控制数据显示的逻辑，JsonFilter


功能权限的策略定义：
user, subject, operate


Assertion(user.id == 1, subject.type == "query")


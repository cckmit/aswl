iface Service
=============
###模块
-asms
    |-modules
        |-iface-service-parent
            |-iface-service
            |-iface-service-api



### 接口服务说明

1.接口版本
如：http://localhost:8080/v1/device/{id}
v1为接口版本号

2.响应体统一格式
com.aswl.as.common.core.model.ResponseBean

3.登录认证 (/v1/auth/{name}/{password})
采用jwt认证，接口用户通过用户、密码登录，获得token(有效期8小时)

4.业务接口访问认证
在请求头Header带认证参数AUTH-TOKEN:${token}

5.分页请求格式
如：http://localhost:8080/v1/device/deviceList/{pageNo}/{pageSize}


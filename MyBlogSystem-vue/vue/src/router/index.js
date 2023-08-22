// 这个文件用来创建整个应用的路由器
import VueRouter from 'vue-router'

import Login from "@/pages/Login/Login.vue";
import Reg from "@/pages/Reg/Reg.vue";
import Home from "@/pages/Home/Home.vue";
import Create from "@/pages/Create/Create.vue";
import Detail from "@/pages/Detail/Detail.vue";
import Center from "@/pages/Center/Center.vue";


// 创建一个路由器
export default new VueRouter({
    routes: [
        {
          path: "/home", // 路径
          component: Home,  // 组件名
            children: [
                {
                    path: "detail",
                    component: Detail,
                    // props 传递参数
                    props($route) {
                        return {id: $route.query.id }
                    }
                },
                {
                    path: "create",
                    component: Create,
                    // props 传递参数
                    props($route) {
                        return {
                                user_id: $route.query.user_id,
                                edit_title: $route.query.edit_title,
                                edit_content: $route.query.edit_content,
                                isEdit: $route.query.isEdit
                        }
                    }
                }
            ]
        },
        {
            path: '/center',
            component: Center
        },
        {
            path: '/login',
            component: Login
        },
        {
            path: '/reg',
            component: Reg
        }
    ]
})
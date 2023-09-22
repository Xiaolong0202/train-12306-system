import {createRouter, createWebHistory} from 'vue-router'
import store from "@/store";
import {openNotificationWithIcon} from "@/util/info";


const routes = [
    {
        path: '/',
        name: 'home',
        component: () => import("../views/MainView.vue"),
        meta: {
            requiredAuth: true
        }
    },
    {
        path: '/login',
        name: 'login',
        component: () => import("../views/LoginView.vue")
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

router.beforeEach((to, from, next) => {
    if (to.meta.requiredAuth) {
        if (!store.state.member.token) {
            console.log("登录过期或非法")
            openNotificationWithIcon("error", "未登录或登录超时，跳转到登录页面")
            next('/login')
        } else {
            next()
        }
    } else {
        next()
    }
})

export default router

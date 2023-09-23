import {createRouter, createWebHistory} from 'vue-router'
import store from "@/store";
import {openNotificationWithIcon} from "@/util/info";
import WelcomeView from "@/views/main/WelcomeView.vue";


const routes = [
    {
        path: '/main',
        name: 'home',
        redirect : '/main/welcome',
        component: () => import("../views/MainView.vue"),
        meta: {
            requiredAuth: true
        },
        children:[
            {
                path: 'welcome',
                component: WelcomeView
            },
        ]
    },
    {
        path: '/login',
        name: 'login',
        component: () => import("../views/LoginView.vue")
    },
    {
        path: '',
        alias: '/',
        redirect : '/main/welcome'
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

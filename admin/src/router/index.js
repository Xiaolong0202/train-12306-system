import {createRouter, createWebHistory} from 'vue-router'


const routes = [
    {
        path: '/main',
        name: 'main',
        redirect: '/main/welcome',
        component: () => import("../views/MainView.vue"),
        meta: {
            requiredAuth: true
        },
        children: [
            {
                path: 'welcome',
                component: () => import('../views/main/WelcomeView.vue')
            },
            {
                path: 'about',
                component: () => import('../views/main/AboutView.vue')
            },
            {
                path: 'station',
                component: ()=> import('../views/main/StationView.vue')
            },
            {
                path: 'train',
                component: () => import('../views/main/TrainView.vue')
            },
            {
                path: 'trainStation',
                component: () => import('../views/main/TrainStation.vue')
            }
        ]
    },
    {
        path: '',
        alias: '/',
        redirect: '/main/welcome'
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})


export default router

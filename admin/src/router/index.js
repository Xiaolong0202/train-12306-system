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
                path: 'base',
                children:[
                    {
                        path: 'station',
                        component: ()=> import('../views/main/StationView.vue')
                    },
                    {
                        path: 'train',
                        component: () => import('../views/main/TrainView.vue')
                    },
                ]
            },
            {
                path: 'batch',
                children :[
                    {
                        path: 'job',
                        component: () => import('../views/batch/jobView.vue')
                    }
                ]
            },
            {
                path: 'daily',
                children:[
                    {
                        path:'Dtrain',
                        component: () => import('@/views/daily/DailyTrainView.vue')
                    }
                ]
            },
        ]
    },
    {
        path: '',
        alias: '/',
        redirect: '/main/welcome'
    },
    {
        path: '/trainEdit/:trainId',
        component: () => import('../views/TrainEditView.vue'),
        children: [
            {
                path: 'trainCarriage/:trainId',
                component: ()=> import('../views/trainEdit/TrainCarriageView.vue')
            },
            {
                path: 'trainSeat/:trainId',
                component: ()=> import('../views/trainEdit/TrainSeatView.vue')
            },
            {
                path: 'trainStation/:trainId',
                component: ()=> import('../views/trainEdit/TrainStation.vue')
            },
        ]
    },
    {
        path: '/dailyTrainEdit/:dailyTrainId',
        component: ()=>import('@/views/DailyTrainEditView.vue'),
        children: [
            {
                path: 'dailyTrainStation/:dailyTrainId',
                component: ()=> import('@/views/dailyTrainEdit/DailyTrainStation.vue')
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})


export default router

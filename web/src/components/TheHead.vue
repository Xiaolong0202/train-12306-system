<template>
    <a-layout-header class="header">
        <div class="logo"/>
        <div style="float: right; color: white;font-size: large">
            您好: {{ member.mobile }} &nbsp;&nbsp;
            <router-link to="/login" style="text-decoration-color: dodgerblue;font-size: small">
                退出登录
            </router-link>
        </div>
        <a-menu
                v-model:selectedKeys="selectedKeys1"
                theme="dark"
                mode="horizontal"
                :style="{ lineHeight: '64px' }"
                :items="items"
                @click="toPage"
        >
        </a-menu>

    </a-layout-header>
</template>


<script setup>
import {reactive, ref} from "vue";
import store from "@/store";
import {useRouter} from "vue-router";//该方法只能用于setUp当中

const member = store.state.member
const selectedKeys1 = ref(['2'])
const router = useRouter()

/**
 * 快速构建一个Item对象,在该项目当中我将key设置为路径
 * @param label
 * @param key
 * @param icon
 * @param children
 * @param type
 * @returns {{children, icon, label, type, key}}
 */
function getItem(label, key, icon, children, type) {
    return {
        key,
        icon,
        children,
        label,
        type,
    };
}

const items = reactive([
    getItem('12312', '/main/welcome', null,null, null),
])

const toPage = ({keyPath}) => {
    let finalPath = '';
    for (let i = 0; i < keyPath.length; i++) {
        finalPath += keyPath[i]
    }
    router.push(finalPath)
}

</script>

<style scoped>
#components-layout-demo-top-side-2 .logo {
    float: left;
    width: 120px;
    height: 31px;
    margin: 16px 24px 16px 0;
    background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #components-layout-demo-top-side-2 .logo {
    float: right;
    margin: 16px 0 16px 24px;
}
</style>
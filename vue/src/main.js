import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import '@/assets/css/global.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'

const app = createApp(App)

app.use(router)
//全局引入element-plus,中文
app.use(ElementPlus, { locale: zhCn })
app.mount('#app')

import * as ElementPlusIconsVue from '@element-plus/icons-vue'
//引入注册了图标组件
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

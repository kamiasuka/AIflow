import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import AppShell from './AppShell.vue'

const app = createApp(AppShell)
app.use(ElementPlus)
app.mount('#app')

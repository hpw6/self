<template>
    <div>
        <el-menu
                default-active="path"
                class="el-menu-vertical-demo"
                @open="handleOpen"
                @close="handleClose"
                router
                style="width: 200px; min-height: calc(100vh - 50px)">
            <el-submenu index="1" v-if="user.role === 1">
                <template #title>
                    <i class="el-icon-location"></i>
                    <span>系统管理</span>
                </template>
                <el-menu-item-group>
                    <el-menu-item index="/user">用户管理</el-menu-item>
                </el-menu-item-group>
            </el-submenu>
            <el-menu-item index="/book">书籍管理</el-menu-item>
            <el-menu-item index="/news">新闻管理</el-menu-item>
        </el-menu>
    </div>
</template>

<script>
    import request from "../utils/request";

    export default {
        name: "Aside",
        data(){
          return {
              user:{},
              path: this.$router.path      //default-active="path"  设置默认高亮的菜单
          }
        },
        created() {
            let userStr = sessionStorage.getItem("user") || "{}"
            this.user = JSON.parse(userStr)

            request.get("/user/" + this.user.id).then(res => {
                if(res.code == '0'){
                    this.user = res.data
                }
            })
            console.log(this.$router.path)
        },
        methods: {
            handleOpen(key, keyPath) {
                console.log(key, keyPath);
            },
            handleClose(key, keyPath) {
                console.log(key, keyPath);
            }
        }
    }
</script>

<style scoped>

</style>

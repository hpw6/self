<template>
    <div style="width: 100%; height: 100vh; background-color: darkturquoise; overflow: hidden">
        <div style="width: 400px;margin: 150px auto">
            <div style="color: #cccccc; font-size: 30px; text-align: center; padding: 30px 0" >欢迎登录</div>

            <el-form ref="form" :model="form" size="normal" :rules="rules">
                <el-form-item prop="username">
                    <el-input v-model="form.username" prefix-icon="el-icon-user-solid"></el-input>
                </el-form-item>

                <el-form-item prop="password">
                    <el-input v-model="form.password" prefix-icon="el-icon-lock" show-password></el-input>
                </el-form-item>

                <el-form-item>
                    <el-button v-model="form.username" style="width: 100%" @click="login">登录</el-button>
                </el-form-item>
            </el-form>
        </div>

    </div>
</template>

<script>
    import request from "../utils/request";

    export default {
        name: "Login",
        data(){
            return {
                form:{},
                rules: {
                    username: [
                        { required: true, message: '请输入用户名', trigger: 'blur' },
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' },
                    ],
                }
            }
        },
        created() {
          sessionStorage.removeItem("user")
        },
        methods:{
            login(){
                this.$refs['form'].validate((valid) => {
                    if(valid){
                        request.post('/user/login', this.form).then(res => {
                            console.log(res)
                            if(res.code === '0') {
                                this.$message({
                                    type: "success",
                                    message: "登录成功"
                                })
                                sessionStorage.setItem("user",JSON.stringify(res.data))
                                this.$router.push("/")      //登录成功后进行页面跳转，跳转到主页
                            }else {
                                this.$message({
                                    type: "error",
                                    message: res.msg
                                })
                            }
                        })
                    }
                })
            }
        },

    }
</script>

<style scoped>

</style>

<template>
    <div style="width: 100%; height: 100vh; background-color: darkturquoise; overflow: hidden">
        <div style="width: 400px;margin: 150px auto">
            <div style="color: #cccccc; font-size: 30px; text-align: center; padding: 30px 0" >欢迎注册</div>

            <el-form ref="form" :model="form" size="normal" :rules="rules">
<!--                用户名-->
                <el-form-item prop="username">
                    <el-input v-model="form.username" prefix-icon="el-icon-user-solid"></el-input>
                </el-form-item>

<!--                密码-->
                <el-form-item prop="password">
                    <el-input v-model="form.password" prefix-icon="el-icon-lock" show-password></el-input>
                </el-form-item>

<!--                确认密码-->
                <el-form-item prop="confirm">
                    <el-input v-model="form.confirm" prefix-icon="el-icon-lock" show-password></el-input>
                </el-form-item>


                <el-form-item>
                    <el-button style="width: 100%" @click="register">注册</el-button>
                </el-form-item>
            </el-form>
        </div>

    </div>
</template>

<script>
    import request from "../utils/request";

    export default {
        name: "Register",
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
                    confirm: [
                        { required: true, message: '请确认密码', trigger: 'blur' },
                    ],
                }
            }

        },
        methods:{
            register(){
                if(this.form.password !== this.form.confirm){
                    this.$message({
                        type: "error",
                        message: "两次密码不一致！"
                    })
                    return
                }
                this.$refs['form'].validate((valid) => {
                    if (valid) {
                        request.post('/user', this.form).then(res => {
                            console.log(res)
                            if (res.code === '0') {
                                this.$message({
                                    type: "success",
                                    message: "注册成功"
                                })
                                this.$router.push("/login")      //登录成功后进行页面跳转，跳转到主页
                            } else {
                                this.$message({
                                    type: "error",
                                    message: res.msg
                                })
                            }
                        })
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>
